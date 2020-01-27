package com.majedalmoqbeli.medicalnewapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.ClassControl.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.ClassControl.SaveSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ImageView bgapp, clover;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;
    Map<String, String> map = new HashMap<String, String>();
    String url = "";
    EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        bgapp = findViewById(R.id.bgapp);
        clover = findViewById(R.id.clover);
        textsplash = findViewById(R.id.textsplash);
        texthome = findViewById(R.id.texthome);
        menus = findViewById(R.id.menus);

        bgapp.animate().translationY(-2200).setDuration(800).setStartDelay(400);
        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

        // this is for email and password EditText
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }
    public void create() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyProgressDialog.hide();
                        Log.i("Response :", response.toString());
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject p =  array.getJSONObject(0);
                            SaveSetting.USERID = p.getString("user_id");
                            SaveSetting.USERNAME = p.getString("user_name");
                            SaveSetting.USERTYPE = p.getString("type");
                            SaveSetting sv = new SaveSetting(LoginActivity.this);
                            sv.SaveData();
                            if (SaveSetting.USERTYPE.equals("1")) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            } else if (SaveSetting.USERTYPE.equals("2")) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }

                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.toString());
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.toString());
                Toast.makeText(LoginActivity.this, getString(R.string.internetError), Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    public void loginClicked(View view) {
        if (!email.getText().toString().isEmpty() & !password.getText().toString().isEmpty()) {
            url = SaveSetting.ServerURL + "login.php";
            map.put("email_username", email.getText().toString());
            map.put("password", password.getText().toString());
            MyProgressDialog.show(LoginActivity.this);
            create();
        } else Toast.makeText(this, getString(R.string.inputAllData), Toast.LENGTH_SHORT).show();
    }
}
