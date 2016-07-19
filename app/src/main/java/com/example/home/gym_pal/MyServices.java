package com.example.home.gym_pal;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/9/2016
 */
public class MyServices extends Service  {


    private int wentToGym = 0;
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

                        wentToGym++;
//                     every time user visits gym increase the counter and save it
                        SharedPreferences.Editor editor = getSharedPreferences("GymAttendence", MODE_PRIVATE).edit();
                        editor.putInt("wentToGym", wentToGym);
                        editor.commit();

                        ContentResolver contentResolver = getContentResolver();

                        ContentValues values = new ContentValues();
                        values.put(GymColumns.COUNT,wentToGym);
                        values.put(GymColumns.DATETIME,System.currentTimeMillis());
                        contentResolver.insert(GymProvider.Attendance.ATTENDANCE,values);
                        long yourmilliseconds = System.currentTimeMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                        Date resultdate = new Date(yourmilliseconds);
                        System.out.println(sdf.format(resultdate));
                        Log.d("testing data","inserted " + wentToGym +" : "+ System.currentTimeMillis() + resultdate);
                        Log.d("testing data","inserted date " + sdf.format(resultdate));

                        Toast.makeText(getApplicationContext(), "connected to WIFI network provided", Toast.LENGTH_LONG).show();
                        Log.d("test", "connected to wifi");

                    } else {
                        Toast.makeText(getApplicationContext(), "not connected to WIFI network provided ", Toast.LENGTH_LONG).show();
                        Log.d("test", " not connected to wifi");


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
                Toast.makeText(getApplicationContext(), "connected to me", Toast.LENGTH_LONG).show();

                // get current router Mac address
                String bssid = wifi.getSSID();
                if (bssid.contains(gymWiFi)) {
                    connected = true;
                }
//                connected = desiredMacAddress.equals(bssid);
                Log.d("test", "connected to wifi :" + bssid);
                Log.d("test", "state of conneted is  :" + connected);

            }
//
            return connected;
        }
    }
}
