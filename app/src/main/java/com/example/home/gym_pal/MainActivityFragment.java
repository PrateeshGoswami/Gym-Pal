package com.example.home.gym_pal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SensorEventListener {

    private int stepsFromSensor1 = 0;
    private int stepsFromSensor2 = 0;
    private int stepsSinceMidnight = 0;
    private int countForLaunch = 0;

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;

    @Bind(R.id.step_count)
    public TextView mText_count;
    @Bind(R.id.step_count_today)
    public TextView mText_count_today;
    @Bind(R.id.avg_steps)
    public TextView mText_avg;
    @Bind(R.id.button_start_runningActivity)
    public Button mbutton_start_run;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        SharedPreferences mPrefs = getActivity().getSharedPreferences("countLaunchData", 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        countForLaunch++;
        prefsEditor.putInt("numrun", countForLaunch);
        prefsEditor.commit();


        return view;
    }

    @OnClick(R.id.button_start_runningActivity)
    public void setMbutton_start_run() {
        Intent otherIntent = new Intent(getActivity(), RunningActivity.class);
        startActivity(otherIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getActivity(), R.string.sensor_not_Available, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        activityRunning = false;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {


            stepsFromSensor2 = (int) event.values[0];
            stepsSinceMidnight = stepsFromSensor2 - stepsFromSensor1;
            Log.d("test","stepsfromsensor2 =" + stepsFromSensor2);
            long elapsedTime = SystemClock.elapsedRealtime();
            Log.d("test","elapsed time =" + elapsedTime);
            int seconds = (int) (elapsedTime / 1000) % 60 ;
            int minutes = (int) ((elapsedTime / (1000*60)) % 60);
            int hours   = (int) ((elapsedTime / (1000*60*60)) % 24);
            int days = (int) ((elapsedTime /(1000*60*60*24)));

            Log.d("test","seconds:"+seconds);
            Log.d("test","minutes:"+minutes);
            Log.d("test","hours:"+hours);

            float avg = (event.values[0]/elapsedTime)*1000*60*60*24;



            mText_count.setText(getString(R.string.steps_count) + ": " + String.valueOf(Math.floor(event.values[0])));
            mText_count_today.setText(getString(R.string.steps_count_midNight) + ": " +"\n" + days + " days "+  hours +" hrs " + minutes +" mins " + seconds + " secs " );
            mText_avg.setText(getString(R.string.steps_avg) +": "+ avg );

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
