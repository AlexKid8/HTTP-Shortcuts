package ch.rmy.android.http_shortcuts.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.activities.ExecuteActivity
import ch.rmy.android.http_shortcuts.data.models.Shortcut
import ch.rmy.android.http_shortcuts.utils.IconUtil

object WidgetManager {

    fun updateWidget(context: Context, shortcut: Shortcut, widgetId: Int) {
        RemoteViews(context.packageName, R.layout.widget).also { views ->

            views.setOnClickPendingIntent(R.id.widget_base, ExecuteActivity.IntentBuilder(context, shortcut.id)
                .build()
                .let { intent ->
                    PendingIntent.getActivity(context, 0, intent, 0)
                })
            views.setTextViewText(R.id.widget_label, shortcut.name)
            views.setImageViewIcon(R.id.widget_icon, IconUtil.getIcon(context, shortcut.iconName))

            AppWidgetManager.getInstance(context)
                .updateAppWidget(widgetId, views)
        }
    }

    fun getIntent(widgetId: Int) =
        Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }

    fun getWidgetIdFromIntent(intent: Intent): Int =
        intent.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

}