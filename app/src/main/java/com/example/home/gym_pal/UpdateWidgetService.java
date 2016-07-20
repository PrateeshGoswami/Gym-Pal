package com.example.home.gym_pal;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/20/2016
 */
public class UpdateWidgetService extends Service {
    private static final String Log_Tag = UpdateWidgetService.class.getSimpleName();

    @Override
    public void onStart(Intent intent, int startId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetsIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        for (int widgetId : allWidgetsIds) {
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.gym__pal__widget);
            Log.w("WidgetExample", String.valueOf(number));

            remoteViews.setTextViewText(R.id.update,
                    "Random: " + String.valueOf(number));
            Intent clickIntent = new Intent(this.getApplicationContext(), Gym_Pal_Widget.class);
            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    allWidgetsIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext()
                    , 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

            stopSelf();
            super.onStart(intent, startId);

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
