package com.majedalmoqbeli.medicalnewapp.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.TooltipCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.majedalmoqbeli.medicalnewapp.control.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.R;
import com.majedalmoqbeli.medicalnewapp.ui.fragments.AllConsultationFragment;
import com.majedalmoqbeli.medicalnewapp.ui.fragments.MyConsultationFargment;
import com.majedalmoqbeli.medicalnewapp.ui.fragments.ProfileFragment;
import com.majedalmoqbeli.medicalnewapp.ui.fragments.UserProfileFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtUserName;
    CircleImageView imageUser;
    FloatingActionButton floatingActionBtn;

  public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        floatingActionBtn = findViewById(R.id.floatingActionBtn);
        TooltipCompat.setTooltipText(floatingActionBtn, getString(R.string.floatingActionBtn));
        if (SaveSetting.USERTYPE.equals("1")) floatingActionBtn.setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (SaveSetting.USERID.equals("0"))
            navigationView.inflateMenu(R.menu.menu_guest);
        else if (SaveSetting.USERTYPE.equals("1")) navigationView.inflateMenu(R.menu.menu_doctor);
        else navigationView.inflateMenu(R.menu.menu_user);

        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        txtUserName = view.findViewById(R.id.userName);
        if (SaveSetting.USERID.equals("0"))
            txtUserName.setText(getString(R.string.noLogin));
        else {
            txtUserName.setText(SaveSetting.USERNAME);
        }
        imageUser = view.findViewById(R.id.imageView);
        Picasso.with(this).load(SaveSetting.ServerURL + "user_image/user_" + SaveSetting.USERID + ".jpg").memoryPolicy(MemoryPolicy.NO_CACHE).into(imageUser);
        loadFragment(new AllConsultationFragment());


        floatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SaveSetting.USERID.equals("0")){
                    alertToGetLogin();
                }else {
                    Intent intent = new Intent(getApplicationContext(),SingleFragmentActivity.class);
                    intent.putExtra("to","send");
                    startActivity(intent);
                }
            }
        });


        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new AllConsultationFragment();
                        break;

                    case R.id.navigation_dashboard: {
                        if (SaveSetting.USERID.equals("0"))
                            alertToGetLogin();
                        else
                            fragment = new MyConsultationFargment();
                        break;
                    }

                    case R.id.navigation_profile:
                        if (SaveSetting.USERID.equals("0"))
                            alertToGetLogin();
                        else {
                            if(SaveSetting.USERTYPE.equals("1"))
                                fragment = new ProfileFragment();
                            else fragment = new UserProfileFragment();
                        }

                        break;

                }
                return loadFragment(fragment);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            if (SaveSetting.USERID.equals("0")){
                alertToGetLogin();
            }else {
                Intent intent = new Intent(getApplicationContext(),SingleFragmentActivity.class);
                intent.putExtra("to","send");
                startActivity(intent);
            }
        } else if (id == R.id.nav_department) {
            Intent intent = new Intent(getApplicationContext(),SingleFragmentActivity.class);
            intent.putExtra("to","dep");
            startActivity(intent);
        } else if (id == R.id.nav_doctor) {

            Intent intent = new Intent(this, SingleFragmentActivity.class);
           // intent.putExtra("dept_id","0");
            intent.putExtra("to","doc");
            startActivity(intent);

        } else if (id == R.id.nav_sign_out) {
            SaveSetting.USERTYPE = "0";
            SaveSetting.USERID = "0";
            SaveSetting.USERNAME = "0";
            SaveSetting sv = new SaveSetting(MainActivity.this);
            sv.SaveData();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_create_account) {

            Intent intent = new Intent(getApplicationContext(),CreateAccountActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void alertToGetLogin() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("لم تقم بتسجيل الدخول");
        builder1.setIcon(R.drawable.ic_exit);
        builder1.setMessage("أنت لم تقم بتسجيل الدخول بعد ، يرجى تسجيل الدخول أولاً ثم اعد المحاولة !!");
        builder1.setCancelable(true);
        builder1.setPositiveButton("تسجيل الدخول",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder1.setNegativeButton("إنشاء حساب",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getApplicationContext(),CreateAccountActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
