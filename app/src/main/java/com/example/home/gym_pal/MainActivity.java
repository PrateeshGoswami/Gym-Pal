package com.example.home.gym_pal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Adbanner
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        //        check if this is the first run
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isfirstrun", true);
                if (isFirstRun) {
            Toast.makeText(this, "This is first run ", Toast.LENGTH_LONG).show();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.wifi_dialog, null);
            dialogBuilder.setView(dialogView);

            final EditText edtxt = (EditText) dialogView.findViewById(R.id.edit1);

            dialogBuilder.setTitle(R.string.app_name);
            dialogBuilder.setMessage(R.string.enter_WiFI);
            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences mPrefs = getSharedPreferences("wifidata", 0);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.putString("wifi", edtxt.getText().toString());
                    prefsEditor.commit();
                    startService(new Intent(getBaseContext(), MyServices.class));


                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog bDialog = dialogBuilder.create();
            bDialog.show();
//            set isfirstrun boolean to  false as we dont need
//            to ask user again the wifi address
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isfirstrun", false).commit();
        }
    }



}
