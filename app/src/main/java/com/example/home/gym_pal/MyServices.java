package com.example.home.gym_pal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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
        Toast.makeText(this,"Service Started",Toast.LENGTH_LONG).show();
        Log.d("test","Start service");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service Stopped",Toast.LENGTH_LONG).show();
        Log.d("test","Start service");

    }
}
