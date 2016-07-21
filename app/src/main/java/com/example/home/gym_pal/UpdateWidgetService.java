package com.example.home.gym_pal;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/20/2016
 */
public class UpdateWidgetService extends RemoteViewsService implements Loader.OnLoadCompleteListener<Cursor> {
    private static final String Log_Tag = UpdateWidgetService.class.getSimpleName();
    private static final int LOADER_ID_NETWORK = 0;

    public int numOfGymVisits;


    @Override
    public void onCreate() {
        CursorLoader mCursorLoader = new CursorLoader(getApplicationContext(),
                GymProvider.Attendance.CONTENT_URI, null, null, null, null);
        mCursorLoader.registerListener(LOADER_ID_NETWORK, this);
        mCursorLoader.startLoading();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }

    public UpdateWidgetService() {
        super();
    }


    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        int num = 0;
        while (!data.isAfterLast()) {
            num = data.getInt(1);

            data.moveToNext();
        }
        numOfGymVisits = num;
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                Gym_Pal_Widget.class));
        // Add the data to the RemoteViews
        int layoutId = R.layout.gym__pal__widget;
        RemoteViews views = new RemoteViews(getPackageName(), layoutId);
        views.setTextViewText(R.id.update, String.valueOf(numOfGymVisits));
        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetIds, views);

    }
}
