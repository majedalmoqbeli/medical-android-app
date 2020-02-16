package com.majedalmoqbeli.medicalnewapp.ui.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.R;
import com.majedalmoqbeli.medicalnewapp.helpers.ViewAnimation;
import com.majedalmoqbeli.medicalnewapp.adapters.AdapterExperiences;
import com.majedalmoqbeli.medicalnewapp.helpers.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.control.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.models.ExperiencesData;
import com.majedalmoqbeli.medicalnewapp.models.UserDoctorData;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileDocActivity extends AppCompatActivity {

    Map<String, String> map = new HashMap<String, String>();
    String url = "";
    TextView user_profile_name, user_emil, txtCost, moreInfo, address, txtDep, user_phone, created_date;
    AdapterExperiences adapter;
    ArrayList<ExperiencesData> experiences;
    RecyclerView listView;
    AppBarLayout app_bar_layout;
    CollapsingToolbarLayout collapsing_toolbar;
    CircularImageView image;
    CoordinatorLayout coordinatorID;
    ArrayList<UserDoctorData> userDoctorData;
   // ImageView edit_profile, image2;
    private ImageButton bt_toggle_reviews, bt_toggle_description, bt_toggle_description1;
    private View lyt_expand_reviews, lyt_expand_description1, lyt_expand_description;
    private NestedScrollView nested_scroll_view;

    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dectoer_profile);
        MyProgressDialog.show(this);

        if (getIntent().getExtras() != null) {
            url = SaveSetting.ServerURL + "getDoctorByID.php";
            map.put("userID", getIntent().getExtras().getString("ID"));
        } else finish();


        coordinatorID = findViewById(R.id.coordinatorID);
        user_profile_name = findViewById(R.id.user_profile_name);
        user_emil = findViewById(R.id.user_emil);
        txtCost = findViewById(R.id.txtCost);
        moreInfo = findViewById(R.id.moreInfo);
        address = findViewById(R.id.address);
        txtDep = findViewById(R.id.txtDep);
        created_date = findViewById(R.id.created_date);
        user_phone = findViewById(R.id.user_phone);

        listView = findViewById(R.id.recyclerView);
        app_bar_layout = findViewById(R.id.app_bar_layout);
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        bt_toggle_description = (ImageButton) findViewById(R.id.bt_toggle_description);
        lyt_expand_description = (View) findViewById(R.id.lyt_expand_description);

        bt_toggle_description1 = (ImageButton) findViewById(R.id.bt_toggle_description3);
        lyt_expand_description1 = (View) findViewById(R.id.lyt_expand_description3);
        // section reviews
        // bt_toggle_reviews = (ImageButton) myView.findViewById(R.id.bt_toggle_reviews);
        lyt_expand_reviews = (View) findViewById(R.id.lyt_expand_reviews);

        image = (CircularImageView) findViewById(R.id.image);


        getProfileData();
        getExperiences();
        initComponent();
        initComponent2();

    }

    public void getProfileData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyProgressDialog.hide();
                        Log.i("Response :", response.toString());
                        try {
                            userDoctorData = new ArrayList<>();
                            JSONArray array = new JSONArray(response);
                            JSONObject p = array.getJSONObject(0);
                            userDoctorData.add(new UserDoctorData(p.getString("user_name"),
                                    p.getString("email"),
                                    p.getString("address"),
                                    p.getString("balance"),
                                    p.getString("phone"),
                                    p.getString("active"),
                                    p.getString("created_date"),
                                    p.getString("a_content"),
                                    p.getString("about"),
                                    p.getString("dep_name"),
                                    p.getString("cons_price")));
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage().toString());
                        }
                        if (userDoctorData.size() > 0) {
                            setDataForDoctor();
                        } else {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
                builder1.setTitle("خطأ في الأتصال بالأنترنت");
                builder1.setIcon(R.drawable.ic_cancel);
                builder1.setMessage("تأكد من الأتصال بالانترنت ، ثم اعد المحاولة !");
                builder1.setCancelable(true);
                builder1.setPositiveButton("أعادة المحاولة",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MyProgressDialog.show(getApplicationContext());
                                getProfileData();
                                getExperiences();
                            }
                        });
                builder1.setNegativeButton("خروج",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                Log.i("VolleyError :", error.toString());

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getExperiences() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SaveSetting.ServerURL + "getExperiencesByID.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyProgressDialog.hide();
                        Log.i("Response :", response.toString());
                        try {
                            experiences = new ArrayList<>();
                            JSONArray array = new JSONArray(response);
                            int i = 0;
                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);
                                experiences.add(new ExperiencesData(p.getString("id"),
                                        p.getString("place") + " لمدة " + p.getString("period") + " سنوات "));
                                i++;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage().toString());
                        }

                        listView.setHasFixedSize(true);
                        listView.setLayoutManager(new LinearLayoutManager(ProfileDocActivity.this));
                        adapter = new AdapterExperiences(experiences, ProfileDocActivity.this);
                        listView.setAdapter(adapter);
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.toString());
                Toast.makeText(ProfileDocActivity.this, getString(R.string.internetError), Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileDocActivity.this);
        requestQueue.add(stringRequest);
    }

    public void setDataForDoctor() {
        user_profile_name.setText(userDoctorData.get(0).getUser_name());
        user_emil.setText(userDoctorData.get(0).getEmail());
        txtCost.setText(userDoctorData.get(0).getCons_price() + " $");
        moreInfo.setText(userDoctorData.get(0).getAbout());
        address.setText(userDoctorData.get(0).getAddress());
        txtDep.setText(userDoctorData.get(0).getDep_name());
        user_phone.setText(userDoctorData.get(0).getPhone());
        created_date.setText(userDoctorData.get(0).getCreated_date());
        Picasso.with(ProfileDocActivity.this).load(SaveSetting.ServerURL + "user_image/" + userDoctorData.get(0).getA_content()).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
        if (userDoctorData.get(0).getActive().equals("1")) {
            user_profile_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_active, 0);
        } else {
            user_profile_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cancel, 0);
        }
        coordinatorID.setVisibility(View.VISIBLE);
    }

    private void initComponent2() {

        (app_bar_layout).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
                image.setScaleX(scale >= 0 ? scale : 0);
                image.setScaleY(scale >= 0 ? scale : 0);

            }
        });
    }

    private void initComponent() {
        // nested scrollview


        // section exparin

        bt_toggle_description1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description1);
            }
        });

        // section description

        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description);
            }
        });

    }

    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    nestedScrollTo(nested_scroll_view, lyt);
                }
            });
        } else {
            ViewAnimation.collapse(lyt);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public void backClicked(View view) {
        finish();
    }

}
