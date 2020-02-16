package com.majedalmoqbeli.medicalnewapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.majedalmoqbeli.medicalnewapp.control.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.helpers.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Map<String, String> map = new HashMap<String, String>();
    String url = "";
    EditText email, password;
    TextView create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        create_account = findViewById(R.id.create_account);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    public void create() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyProgressDialog.hide();
                        Log.i("Response :", response);
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject p = array.getJSONObject(0);
                            SaveSetting.USER_ID = p.getString("user_id");
                            SaveSetting.USER_NAME = p.getString("user_name");
                            SaveSetting.USER_TYPE = p.getString("type");
                            SaveSetting sv = new SaveSetting(LoginActivity.this);
                            sv.saveData();

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
