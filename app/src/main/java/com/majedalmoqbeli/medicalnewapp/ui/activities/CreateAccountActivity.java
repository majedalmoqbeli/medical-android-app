package com.majedalmoqbeli.medicalnewapp.ui.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.majedalmoqbeli.medicalnewapp.R;

public class CreateAccountActivity extends AppCompatActivity {
    LinearLayout doctorAcount,userAcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        doctorAcount=findViewById(R.id.doctorAcount);
        userAcount=findViewById(R.id.userAcount);
        doctorAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this,
                        CreateDoctorAccountActivity.class);
                startActivity(intent);

            }
        });

        userAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this,
                        CreateUserAccountActivity.class);
                startActivity(intent);


            }
        });
    }
}
