package com.example.home.gym_pal;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/20/2016
 */
public class UpdateWidgetService extends RemoteViewsService implements Loader.OnLoadCompleteListener<Cursor> {
    private static final String Log_Tag = UpdateWidgetService.class.getSimpleName();
    private static final int LOADER_ID_NETWORK = 0 ;

    public int numOfGymVisits;


    @Override
    public void onCreate() {
        CursorLoader mCursorLoader = new CursorLoader(getApplicationContext(), GymProvider.Attendance.CONTENT_URI, null, null, null, null);
        mCursorLoader.registerListener(LOADER_ID_NETWORK, this);
        mCursorLoader.startLoading();
        Log.w("test","geting data from database" );

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
//                .getApplicationContext());
//
//        int[] allWidgetsIds = intent
//                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//        for (int widgetId : allWidgetsIds) {
//            int number = (new Random().nextInt(100));
//
//            RemoteViews remoteViews = new RemoteViews(this
//                    .getApplicationContext().getPackageName(),
//                    R.layout.gym__pal__widget);
//            Log.w("WidgetExample", String.valueOf(number));
//            int numb = getNumOfGymVisits();
//            Log.w("yayayyyaya","now to next level getting close " + numb);
//
//            remoteViews.setTextViewText(R.id.update,
//                    "Random: " + String.valueOf(number));
//            Intent clickIntent = new Intent(this.getApplicationContext(), Gym_Pal_Widget.class);
//            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
//                    allWidgetsIds);
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext()
//                    , 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);
//
//            stopSelf();
//            super.onStart(intent, startId);
//
//        }
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
    public int getNumOfGymVisits(){
        return numOfGymVisits;
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        int num = 0;
        while(!data.isAfterLast()){
            num = data.getInt(1);

            data.moveToNext();
        }
        numOfGymVisits = num;
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                Gym_Pal_Widget.class));

Log.w("test","getting data from database dude yayyyyyy" + numOfGymVisits);
        int layoutId = R.layout.gym__pal__widget;
        RemoteViews views = new RemoteViews(getPackageName(),layoutId);
        views.setTextViewText(R.id.update,String.valueOf(numOfGymVisits));
        appWidgetManager.updateAppWidget(appWidgetIds,views);


}
}
