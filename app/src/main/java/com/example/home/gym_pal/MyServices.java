package com.example.home.gym_pal;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/9/2016
 */
public class MyServices extends Service implements Loader.OnLoadCompleteListener<Cursor> {


    private static final int LOADER_ID_NETWORK = 0;
    private int wentToGym;
    private String gymWiFi;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    when user enters gyms wifi and presses the done button
//    this service starts and keeps running in the back ground
//    it keeps track if user connects to a particular wifi


    @Override
    public void onCreate() {
        CursorLoader mCursorLoader = new CursorLoader(getApplicationContext(), GymProvider.Attendance.CONTENT_URI, null, null, null, null);
        mCursorLoader.registerListener(LOADER_ID_NETWORK, this);
        mCursorLoader.startLoading();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BroadcastReceiver broadcastReceiver = new WifiBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        int num = 0;
        while (!data.isAfterLast()) {
            num = data.getInt(1);

            data.moveToNext();
        }
        wentToGym = num;
    }


    private class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
                SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                if (SupplicantState.isValidState(state)
                        && state == SupplicantState.COMPLETED) {

                    boolean connect = checkConnectedToDesiredWifi();
                    if (connect == true) {


//                     every time user visits gym increase the counter and save it

                        wentToGym++;
                        ContentResolver contentResolver = getContentResolver();
                        ContentValues values = new ContentValues();
                        values.put(GymColumns.COUNT, wentToGym);
                        values.put(GymColumns.DATETIME, System.currentTimeMillis());
                        contentResolver.insert(GymProvider.Attendance.CONTENT_URI, values);


                    }
                }
            }
        }

        /**
         * Detect you are connected to a specific network.
         */
        private boolean checkConnectedToDesiredWifi() {
            boolean connected = false;
            SharedPreferences mPrefs = getSharedPreferences("wifidata", 0);
            String restoredText = mPrefs.getString("wifi", null);
            if (restoredText != null) {
                gymWiFi = mPrefs.getString("wifi", "No wifi set");
            }

            WifiManager wifiManager =
                    (WifiManager) getSystemService(Context.WIFI_SERVICE);

            WifiInfo wifi = wifiManager.getConnectionInfo();
            if (wifi != null) {

                // get current router Mac address
                String bssid = wifi.getSSID();
                if (bssid.contains(gymWiFi)) {
                    connected = true;
                }

            }
//
            return connected;
        }
    }
}
