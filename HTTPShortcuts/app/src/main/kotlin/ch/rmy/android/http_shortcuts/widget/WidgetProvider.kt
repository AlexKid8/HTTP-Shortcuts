package ch.rmy.android.http_shortcuts.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.util.Log
import ch.rmy.android.http_shortcuts.data.Controller

class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, widgetIds: IntArray) {
        if (widgetIds.isEmpty()) {
            return
        }
        Controller().use { controller ->
            widgetIds.forEach { widgetId ->

                val shortcut = controller.getShortcuts().random()

                WidgetManager.updateWidget(context, shortcut, widgetId)
            }
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int, newOptions: Bundle) {
        Log.i("WidgetProvider", "Sizes: "+newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)+"x"+newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)+" "+newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)+"x"+newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT))


        /*Controller().use { controller ->
            RemoteViews(context.packageName, R.layout.widget).also { views ->

                val shortcut = controller.getShortcuts().random()

                views.setOnClickPendingIntent(R.id.widget_base, ExecuteActivity.IntentBuilder(context, shortcut.id)
                    .build()
                    .let { intent ->
                        PendingIntent.getActivity(context, 0, intent, 0)
                    })
                views.setTextViewText(R.id.widget_label, shortcut.name)
                views.setImageViewIcon(R.id.widget_icon, IconUtil.getIcon(context, shortcut.iconName))

                views.

                appWidgetManager
                    .updateAppWidget(widgetId, views)
            }
        }*/

    }

}