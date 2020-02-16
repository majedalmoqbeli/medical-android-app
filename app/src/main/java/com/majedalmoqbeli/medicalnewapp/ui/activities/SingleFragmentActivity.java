package com.majedalmoqbeli.medicalnewapp.ui.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.majedalmoqbeli.medicalnewapp.R;
import com.majedalmoqbeli.medicalnewapp.ui.fragments.DepartmentFragment;
import com.majedalmoqbeli.medicalnewapp.ui.fragments.DoctorFragment;
import com.majedalmoqbeli.medicalnewapp.ui.fragments.SendFragment;


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
