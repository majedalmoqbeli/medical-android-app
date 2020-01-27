package com.majedalmoqbeli.medicalnewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.majedalmoqbeli.medicalnewapp.ClassControl.SaveSetting;

public class BootingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    SaveSetting sv = new SaveSetting(BootingActivity.this);
                    sv.LoadData();
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
