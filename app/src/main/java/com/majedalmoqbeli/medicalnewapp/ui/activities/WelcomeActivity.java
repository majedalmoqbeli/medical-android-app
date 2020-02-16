package com.majedalmoqbeli.medicalnewapp.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.majedalmoqbeli.medicalnewapp.adapters.IntroViewPagerAdapter;
import com.majedalmoqbeli.medicalnewapp.R;
import com.majedalmoqbeli.medicalnewapp.models.ScreenItem;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by LENEVO on 4/17/2019.
 */

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext,btnGetStarted;
    int position = 0;
    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(restorePrefData()){
            Intent homeActivity = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(homeActivity);
            finish();
        }
        setContentView(R.layout.activity_one);

        //getSupportActionBar().hide();

        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tabLayout);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);


        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("هل لديك سؤال طبي ؟","يقدم التطبيق استشارات طبية من اطباء معتمدين منتقين بعناية و ذوو خبرات علمية ممتازة ،مرخصون و على درجة عالية من الكفاءة في اختصاصهم .",R.drawable.doctor1));
        mList.add(new ScreenItem("دردش مع طبيبك","خدماتنا متوفره طوال اليوم حيث يمكنك وصف استشارتك بوضوح وباسهل طريقة ممكنة .",R.drawable.patient1));
        mList.add(new ScreenItem("استشارات أمنة و سرية","أدخل في استشارة خاصة وسرية مع طبيبك بحيث لا يصل لها أحد سواك .",R.drawable.patient2));

        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        tabIndicator.setupWithViewPager(screenPager);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()){
                    position++;
                    screenPager.setCurrentItem(position);
                }
                if (position == mList.size()-1){
                    loadLastScreen();
                }
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(homeActivity);

                savePrefsData();
                finish();
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("introPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("introPrefs",MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();
    }

    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        btnGetStarted.setAnimation(btnAnim);
    }
}

