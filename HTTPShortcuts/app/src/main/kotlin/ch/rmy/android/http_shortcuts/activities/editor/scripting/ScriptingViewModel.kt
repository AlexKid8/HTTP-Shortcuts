package ch.rmy.android.http_shortcuts.activities.editor.scripting

import android.app.Application
import ch.rmy.android.http_shortcuts.activities.editor.BasicShortcutEditorViewModel
import ch.rmy.android.http_shortcuts.data.Transactions
import io.reactivex.Completable

class ScriptingViewModel(application: Application) : BasicShortcutEditorViewModel(application) {

    fun setCode(prepareCode: String, successCode: String, failureCode: String): Completable =
        Transactions.commit { realm ->
            getShortcut(realm)?.let { shortcut ->
                shortcut.codeOnPrepare = prepareCode
                shortcut.codeOnSuccess = successCode
                shortcut.codeOnFailure = failureCode
            }
        }

}