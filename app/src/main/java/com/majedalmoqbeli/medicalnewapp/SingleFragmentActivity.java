package com.majedalmoqbeli.medicalnewapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.majedalmoqbeli.medicalnewapp.ClassControl.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.Fragment.DepartmentFragment;
import com.majedalmoqbeli.medicalnewapp.Fragment.DoctorFragment;
import com.majedalmoqbeli.medicalnewapp.Fragment.SendFragment;


public class SingleFragmentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        if(getIntent().getExtras().getString("to").equals("send")){
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.send_con, SendFragment.newInstance()).commit();
        } else if (getIntent().getExtras().getString("to").equals("dep")){
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.send_con, DepartmentFragment.newInstance()).commit();
        }else if (getIntent().getExtras().getString("to").equals("doc")){
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.send_con, DoctorFragment.newInstance()).commit();
        }

    }
}
