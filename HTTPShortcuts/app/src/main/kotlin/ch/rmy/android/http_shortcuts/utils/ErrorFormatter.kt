package ch.rmy.android.http_shortcuts.utils

import android.content.Context
import android.text.format.Formatter
import androidx.annotation.StringRes
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.exceptions.InvalidContentTypeException
import ch.rmy.android.http_shortcuts.exceptions.InvalidHeaderException
import ch.rmy.android.http_shortcuts.exceptions.InvalidUrlException
import ch.rmy.android.http_shortcuts.exceptions.JavaScriptException
import ch.rmy.android.http_shortcuts.exceptions.UnsupportedFeatureException
import ch.rmy.android.http_shortcuts.exceptions.UserException
import ch.rmy.android.http_shortcuts.extensions.mapIf
import ch.rmy.android.http_shortcuts.extensions.tryOrLog
import ch.rmy.android.http_shortcuts.http.ErrorResponse
import ch.rmy.android.http_shortcuts.http.HttpStatus
import io.reactivex.exceptions.CompositeException
import java.net.ConnectException
import java.net.UnknownHostException

class ErrorFormatter(private val context: Context) {

    fun getPrettyError(error: Throwable, shortcutName: String, includeBody: Boolean): String =
        if (error is ErrorResponse) {
            getHttpErrorMessage(error, shortcutName, includeBody)
        } else if (error is UserException) {
            getErrorMessage(error)
        } else {
            String.format(getString(R.string.error_other), shortcutName, getErrorMessage(error))
        }

    private fun getHttpErrorMessage(error: ErrorResponse, shortcutName: String, includeBody: Boolean): String {
        val builder = StringBuilder()
        builder.append(String.format(
            getString(R.string.error_http),
            shortcutName,
            error.shortcutResponse.statusCode,
            HttpStatus.getMessage(error.shortcutResponse.statusCode)
        ))

        if (includeBody && error.shortcutResponse.bodyAsString.isNotEmpty()) {
            tryOrLog {
                builder.append("\n\n")
                builder.append(error.shortcutResponse.bodyAsString)
            }
        }
        return builder.toString()
    }

    private fun getErrorMessage(error: Throwable): String =
        when (error) {
            is CompositeException -> error.exceptions.joinToString(separator = "\n") { getErrorMessage(it) }
            is InvalidUrlException -> context.getString(R.string.error_invalid_url, error.url)
            is InvalidHeaderException -> context.getString(R.string.error_invalid_header, error.header)
            is InvalidContentTypeException -> context.getString(R.string.error_invalid_content_type, error.contentType)
            is SizeLimitedReader.LimitReachedException -> context.getString(R.string.error_response_too_large, Formatter.formatShortFileSize(context, error.limit))
            is JavaScriptException -> context.getString(R.string.error_js_pattern, error.message)
            is UnsupportedFeatureException -> context.getString(R.string.error_not_supported)
            is ConnectException,
            is UnknownHostException -> error.message!!
            else -> getUnknownErrorMessage(error)
        }

    private fun getUnknownErrorMessage(error: Throwable): String =
        getCauseChain(error)
            .map(::getSingleErrorMessage)
            .distinct()
            .joinToString(separator = "\n")

    private fun getCauseChain(error: Throwable, recursionDepth: Int = 0): List<Throwable> =
        listOf(error)
            .mapIf(error.cause != null && recursionDepth < MAX_RECURSION_DEPTH) {
                it.plus(getCauseChain(error.cause!!, recursionDepth + 1))
            }

    private fun getSingleErrorMessage(error: Throwable): String =
        when {
            error.message != null -> error.message!!
            else -> context.getString(R.string.error_generic)
        }

    private fun getString(@StringRes stringRes: Int): String = context.getString(stringRes)

    companion object {
        private const val MAX_RECURSION_DEPTH = 2
    }

}