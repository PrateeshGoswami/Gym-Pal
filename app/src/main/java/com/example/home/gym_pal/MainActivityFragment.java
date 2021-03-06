package com.example.home.gym_pal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.gym_pal.Activities.RunningActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SensorEventListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private int stepsFromSensor1 = 0;
    private int stepsFromSensor2 = 0;
    private int countForLaunch = 0;

    private SensorManager sensorManager;
    boolean activityRunning;

    @BindView(R.id.step_count)
    public TextView mText_count;
    @BindView(R.id.step_count_today)
    public TextView mText_count_today;
    @BindView(R.id.avg_steps)
    public TextView mText_avg;
    @BindView(R.id.last_visit)
    public TextView mText_last;
    @BindView(R.id.week_visit)
    public TextView mText_week;

    private static final int CURSOR_LOADER_ID = 0;

    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Cursor c = getActivity().getContentResolver().query(GymProvider.Attendance.CONTENT_URI,
                null, null, null, null);
        Log.i(LOG_TAG, "cursor count: " + c.getCount());
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);


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
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            stepsFromSensor2 = (int) event.values[0];
            long elapsedTime = SystemClock.elapsedRealtime();
            int seconds = (int) (elapsedTime / 1000) % 60;
            int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
            int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);
            int days = (int) ((elapsedTime / (1000 * 60 * 60 * 24)));
            float avg = (event.values[0] / elapsedTime) * 1000 * 60 * 60 * 24;
            mText_count.setText(getString(R.string.steps_count) + ": " + String.valueOf(Math.floor(event.values[0])));
            mText_count_today.setText(getString(R.string.steps_count_midNight) + ": " + days + " days " + hours + " hrs " + minutes + " mins " + seconds + " secs ");
            mText_avg.setText(getString(R.string.steps_avg) + ": " + avg);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("testing", "Loader created " + GymProvider.Attendance.CONTENT_URI);
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                GymProvider.Attendance.CONTENT_URI,
                null,
                null,
                null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        String text = null;
        long text2 = 0;
        while (!data.isAfterLast()) {
            text = data.getString(1);
            text2 = Long.parseLong(data.getString(2));
            data.moveToNext();
        }
        long yourmilliseconds = text2;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);
        mText_last.setText(getString(R.string.last_visit) + ": " + sdf.format(resultdate));
        mText_week.setText(getString(R.string.week_visit) + ": " + text);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
