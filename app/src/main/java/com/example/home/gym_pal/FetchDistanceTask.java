package com.example.home.gym_pal;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/14/2016
 */
public class FetchDistanceTask  extends AsyncTask<Double, Integer, StringBuilder> {

    private static final String TAG = FetchDistanceTask.class.getSimpleName();


    public double startLatitude = 0;
    public double startLongitude = 0;
    public double endLatitude = 0;
    public double endLongitude = 0;

    private static final String DIRECTIONS_API_BASE = "https://maps.googleapis.com/maps/api/distancematrix/json?";

    private static final String OUT_JSON = "/json";

    // API KEY of the project Google Map Api For work
    private static final String API_KEY = "AIzaSyBRIEw6T832UzbrjB665qMivXCTnF6ycpM";

    @Override
    protected StringBuilder doInBackground(Double... params) {
        startLatitude = params[0];
        startLongitude = params[1];
        endLatitude = params[2];
        endLongitude = params[3];
        // If there's no info, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        Log.i(TAG, "doInBackground of ApiDirectionsAsyncTask");

        HttpURLConnection mUrlConnection = null;
        StringBuilder mJsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(DIRECTIONS_API_BASE );
            sb.append("units=imperial");
            sb.append("&origins="+startLatitude + "," + startLongitude);
            sb.append("&destinations="+endLatitude + "," + endLongitude);
            sb.append("&key=" + API_KEY);

            URL url = new URL(sb.toString());
            mUrlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(mUrlConnection.getInputStream());
            Log.i(TAG, "doInBackground of ApiDirectionsAsyncTask"+ url);
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1){
                mJsonResults.append(buff, 0, read);
                Log.i(TAG, "doInBackground of ApiDirectionsAsyncTask" + mJsonResults);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Distance Matrix API URL");
            return null;

        } catch (IOException e) {
            System.out.println("Error connecting to Distance Matrix");
            return null;
        } finally {
            if (mUrlConnection != null) {
                mUrlConnection.disconnect();
            }
        }

        return mJsonResults;
    }

}