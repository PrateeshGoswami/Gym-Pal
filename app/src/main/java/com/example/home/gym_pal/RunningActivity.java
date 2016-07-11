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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RunningActivity extends AppCompatActivity {

    GPSTracker gps;


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

//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                mText_location.append("\n" + location.getLatitude() + "\n" + location.getLongitude());
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//
//            }
//        };
//        mbuttonFindLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(getApplicationContext(),
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                requestPermissions(new String[]{
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                }, 10);
//                return;
//            }
//        }else {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
//        }
//
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 10:
//                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    configureButton();
//        }
//    }
//
//    private void configureButton() {
//       mbuttonFindLoc.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//
//           }
//       });


    }

    @OnClick(R.id.fab)
    public void setfabclick() {
        Snackbar.make(getCurrentFocus(), "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


}
