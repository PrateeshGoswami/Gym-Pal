package com.example.home.gym_pal;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Implementation of App Widget functionality.
 */
public class Gym_Pal_Widget extends AppWidgetProvider {
    private static final String Log_Tag = Gym_Pal_Widget.class.getSimpleName();


    private static final String ACTION_CLICK = "ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.w(Log_Tag, "update method called");


        ComponentName thisWidget = new ComponentName(context, Gym_Pal_Widget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        Intent intent = new Intent(context.getApplicationContext(),
                UpdateWidgetService.class);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        context.startService(intent);
//        }
    }
}

