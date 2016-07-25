package com.example.home.gym_pal.Activities;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.gym_pal.AsyncResponse;
import com.example.home.gym_pal.FetchDistanceTask;
import com.example.home.gym_pal.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RunningActivity extends AppCompatActivity implements LocationListener,
        OnMapReadyCallback,AsyncResponse {
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;
    private TextView latitude, longitude;

    public double startLatitude = 0.0;
    public double startLongitude = 0.0;
    public double endLatitude = 0.0;
    public double endLongitude = 0.0;
    public double fusedLatitude = 0.0;
    public double fusedLongitude = 0.0;
    private GoogleMap mMap;
    Marker currLocationMarker;
    private LocationManager locationManager;
    private LocationListener locationListener;
    @BindView(R.id.adView)
    public AdView mAdview;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.start_run)
    public Button mButton_start;
    @BindView(R.id.stop_run)
    public Button mButton_stop;
    @BindView(R.id.calc_dist)
    public Button mButton_dist;
    @BindView(R.id.textview_distance)
    public TextView mText_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        initializeViews();
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        //        Adbanner
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdview.loadAd(adRequest);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (checkPlayServices()) {
            startFusedLocation();
            registerRequestUpdate(this);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        Log.d("test", "latitude is " + startLatitude + "longitude is :" + startLongitude);


    }

    private void initializeViews() {
        latitude = (TextView) findViewById(R.id.textview_latitude);
        longitude = (TextView) findViewById(R.id.textview_longitude);
    }

    @Override
    protected void onStop() {
        stopFusedLocation();
        super.onStop();
    }


    // check if google play services is installed on the device
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(),
                        R.string.device_is_supported, Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.device_is_not_supported, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    public void startFusedLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnectionSuspended(int cause) {
                        }

                        @Override
                        public void onConnected(Bundle connectionHint) {

                        }
                    }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {

                        @Override
                        public void onConnectionFailed(ConnectionResult result) {

                        }
                    }).build();
            mGoogleApiClient.connect();
        } else {
            mGoogleApiClient.connect();
        }
    }

    public void stopFusedLocation() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }


    public void registerRequestUpdate(final LocationListener listener) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // every second

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, listener);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!isGoogleApiClientConnected()) {
                        mGoogleApiClient.connect();
                    }
                    registerRequestUpdate(listener);
                }
            }
        }, 1000);
    }

    public boolean isGoogleApiClientConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

    @Override
    public void onLocationChanged(final Location location) {
        setFusedLatitude(location.getLatitude());
        setFusedLongitude(location.getLongitude());
        latitude.setText(getString(R.string.latitude_string) + " " + getFusedLatitude());
        longitude.setText(getString(R.string.longitude_string) + " " + getFusedLongitude());
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        currLocationMarker = mMap.addMarker(markerOptions);

        mButton_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLatitude = location.getLatitude();
                startLongitude = location.getLongitude();
                Log.d("test", "latitude is " + startLatitude + "longitude is :" + startLongitude);


            }
        });
        mButton_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endLatitude = location.getLatitude();
                endLongitude = location.getLongitude();
                Log.d("test", " end latitude is " + endLatitude + " end longitude is :" + endLongitude);

            }
        });

        mButton_dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDistanceRan(startLatitude,startLongitude,endLatitude,endLongitude);
//                fetchDistanceTask = new FetchDistanceTask();
//                fetchDistanceTask.execute(startLatitude,startLongitude,endLatitude,endLongitude);
            }
        });
    }
    public void getDistanceRan(Double stlat,Double stlon,Double enlat,Double enlon){
        FetchDistanceTask distanceTask = new FetchDistanceTask(RunningActivity.this);
        distanceTask.setOnResponce(this);
        distanceTask.execute(stlat,stlon,enlat,enlon);

    }

    public void setFusedLatitude(double lat) {
        fusedLatitude = lat;
    }

    public void setFusedLongitude(double lon) {
        fusedLongitude = lon;
    }

    public double getFusedLatitude() {
        return fusedLatitude;
    }

    public double getFusedLongitude() {
        return fusedLongitude;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    @Override
    public void processFinish(String output) {
        if (output !=null) {
            int stop = output.indexOf(",");
            int start = output.indexOf(":");
            String result = output.substring(start, stop);
            mText_distance.setText(getString(R.string.distance) + result);
        }
    }
}

