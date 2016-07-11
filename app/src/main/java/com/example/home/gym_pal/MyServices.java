package com.example.home.gym_pal;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/9/2016
 */
public class MyServices extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BroadcastReceiver broadcastReceiver = new WifiBroadcastReceiver();
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
//        Log.d("test", "Start service");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
//        Log.d("test", "stop service");

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
//                        Toast.makeText(getApplicationContext(), "connected to me,my home network", Toast.LENGTH_LONG).show();
                        Log.d("test", "connected to wifi");

                    } else {
//                        Toast.makeText(getApplicationContext(), "not connected to me,my home network", Toast.LENGTH_LONG).show();
//                        Log.d("test", " not connected to wifi");


                    }
                }
            }
        }

        /**
         * Detect you are connected to a specific network.
         */
        private boolean checkConnectedToDesiredWifi() {
            boolean connected = false;

            String desiredMacAddress = "Goswami";

            WifiManager wifiManager =
                    (WifiManager) getSystemService(Context.WIFI_SERVICE);

            WifiInfo wifi = wifiManager.getConnectionInfo();
            if (wifi != null) {
//                Toast.makeText(getApplicationContext(), "connected to me", Toast.LENGTH_LONG).show();

                // get current router Mac address
                String bssid = wifi.getSSID();
                if (bssid.contains(desiredMacAddress)){
                    connected = true;
                }
//                connected = desiredMacAddress.equals(bssid);
                Log.d("test", "connected to wifi :" + bssid);
//                Log.d("test", "state of conneted is  :" + connected);

            }
//
            return connected;
        }
    }
}
