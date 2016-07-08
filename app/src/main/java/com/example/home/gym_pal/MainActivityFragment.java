package com.example.home.gym_pal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SensorEventListener{

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_main, container, false);

        count = (TextView) view.findViewById(R.id.count);
        count.setText(String.valueOf(0));

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
        }else {
            Toast.makeText(getActivity(),"count sensor not available",Toast.LENGTH_LONG).show();
        }
        }

    @Override
    public void onPause() {
        super.onPause();
        activityRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning){

            count.setText(String.valueOf(Math.floor(event.values[0])));

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
