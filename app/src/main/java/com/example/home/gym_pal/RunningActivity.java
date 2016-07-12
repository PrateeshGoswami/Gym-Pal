package com.example.home.gym_pal;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RunningActivity extends AppCompatActivity implements OnMapReadyCallback {

    GPSTracker gps;
    private GoogleMap mMap;


    @Bind(R.id.text_location)
    public TextView mText_location;

    @Bind(R.id.button_find_location)
    public Button mbuttonFindLoc;

    @Bind(R.id.toolbar)
    public Toolbar mToolbar;

    @Bind(R.id.adView)
    public AdView mAdview;

    private LocationManager locationManager;
    private LocationListener locationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
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

        mbuttonFindLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(RunningActivity.this);

                if (gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    mText_location.append("\n" + latitude +"\n" + longitude);

                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);



    }

    @OnClick(R.id.fab)
    public void setfabclick() {
        Snackbar.make(getCurrentFocus(), "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        float lat =(float)Math.round(gpsTracker.getLatitude());
        float lon = (float)Math.round(gpsTracker.getLongitude());

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
