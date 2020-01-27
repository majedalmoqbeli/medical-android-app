package com.majedalmoqbeli.medicalnewapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.ClassControl.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.ClassData.DepartmentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateDoctorAccountActivity extends AppCompatActivity {
    Map<String, String> map = new HashMap<String, String>();
    ArrayList<DepartmentData> departmentData;
    ArrayList<String> adapter_name = new ArrayList<String>();
    private static final String URLline = "http://www.sooqazal.com/Doctor/creat_doctorAcount.php";
    public static String ServerURL = "http://www.sooqazal.com/Doctor/getDeprtment.php";
    public static String ServerURLD = "http://www.sooqazal.com/Doctor/getDeprtment.php";
    public static String KEY = "acb8f67925ab60e2dff0ab535a56cfe8a26f565e";
    EditText username, user_email, address, about, phone, consprice, password, confirmPassword;
    Button credoctbtn;
    TextView textdpatrment, textabout, textconsprice;
    Spinner departmentSpiner;
    String emailInput;
    public static int type = 1;


    CircleImageView image;

    ImageView image1;
    String ecnodview, dept_id, typeAcount;
    String imgBase64 = "0";
    private static int GALLERY_REQUEST_CODE = 1;
    Bitmap myImageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doctor_account);
        username = findViewById(R.id.username);
        user_email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        address = findViewById(R.id.address);
        about = findViewById(R.id.about);
        phone = findViewById(R.id.phone);
        consprice = findViewById(R.id.consprice);
        image = findViewById(R.id.image);
        credoctbtn = findViewById(R.id.creatdocbtn);
        confirmPassword = findViewById(R.id.passwordconf);


        credoctbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInput = user_email.getText().toString().trim();//this for invalidate email

                if (username.getText().toString().isEmpty()) {

                    username.setError("");
                } else if (user_email.getText().toString().isEmpty()) {
                    user_email.setError("valid can't be empty ");

                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    user_email.setError("Please enter");

                } else if (password.getText().toString().isEmpty()) {
                    password.setError("");

                } else if (confirmPassword.getText().toString().isEmpty()) {
                    confirmPassword.setError("كلمة السر غير متطابقة");

                } else if (phone.getText().toString().isEmpty()) {
                    phone.setError("");


                } else if (about.getText().toString().isEmpty()) {
                    about.setError("ادخل تفاصيل خبراتك");

                } else if (consprice.getText().toString().isEmpty()) {
                    about.setError("ادخل سعر الاستشارة ");


                } else if (address.getText().toString().isEmpty()) {
                    about.setError("ادخل عنوانك ");


                } else if (phone.getText().toString().length() < 9) {
                    phone.setError("The number phone is small");
                } else if (phone.getText().toString().length() > 15) {
                    phone.setError("The number phone is more");
                } else if (phone.getText().toString().length() > 15) {
                    phone.setError("The number phone is more");
                } else if (password.getText().toString().length() < 4) {
                    password.setError("The password  is small");


                } else if (!password.getText().toString().equals(confirmPassword.getText().toString().toString())) {
                    Toast.makeText(CreateDoctorAccountActivity.this, "كلمة السر غير متطابقتان", Toast.LENGTH_SHORT).show();
                    password.setError("");
                    confirmPassword.setError("");

                } else if (!username.getText().toString().isEmpty() &&
                        !user_email.getText().toString().isEmpty() &&
                        !phone.getText().toString().isEmpty() &&
                        !password.getText().toString().isEmpty() &&
                        !confirmPassword.getText().toString().isEmpty() &&
                        Patterns.EMAIL_ADDRESS.matcher(emailInput).matches() &&
                        !(phone.getText().toString().length() < 9) &&
                        !(password.getText().toString().length() < 4) &&
                        !(phone.getText().toString().length() > 15) &&
                        !(address.getText().toString().length() > 15) &&
                        !(consprice.getText().toString().length() > 15) &&
                        !(about.getText().toString().length() > 15) &&
                        password.getText().toString().equals(confirmPassword.getText().toString().toString())

                        ) {

                    connectToServer();

                }


            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);


                // pickFromGallery();

            }
        });

        getAllDepartment();


    }


    public void connectToServer() {

        RequestQueue queue = Volley.newRequestQueue(CreateDoctorAccountActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CreateDoctorAccountActivity.this, "Acount Created Succesfully" + response, Toast.LENGTH_SHORT).show();
                        Log.i("MySuccess", "" + response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CreateDoctorAccountActivity.this, "my error :" + error.toString(), Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("type", String.valueOf(Integer.valueOf(type)));
                params.put("username", username.getText().toString().trim());
                params.put("user_email", user_email.getText().toString().trim());
                params.put("address", address.getText().toString().trim());
                params.put("phone", phone.getText().toString());
                params.put("about", about.getText().toString());
                params.put("consprice", consprice.getText().toString().trim());
                params.put("imageuser", imgBase64);
                params.put("password", password.getText().toString().trim());
                params.put("depart_id", dept_id);
                return params;
            }
        };
        queue.add(request);
    }


    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Uri selectedImage = data.getData();
            myImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            if (myImageBitmap.getWidth() >= 500 || myImageBitmap.getHeight() >= 500)
                myImageBitmap = Bitmap.createScaledBitmap(myImageBitmap, (int) (myImageBitmap.getWidth() * 0.3), (int) (myImageBitmap.getHeight() * 0.3), true);
            image.setImageBitmap(myImageBitmap);
            imgBase64 = encodeImage(myImageBitmap);
        } catch (IOException e) {
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        }


    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    public void getAllDepartment() {
        // MyProgressDialog.show(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MyDep", response);
                        try {
                            JSONArray array = new JSONArray(response);
                            departmentData = new ArrayList<>();
                            int i = 0;
                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);
                                departmentData.add(new DepartmentData(
                                        p.getString("depart_id"),
                                        p.getString("depart_name")
                                ));
                                adapter_name.add(p.getString("depart_name"));

                                i++;
                            }
                            if (departmentData.size() > 0) {
                                departmentSpiner = findViewById(R.id.selectDepart);
                                ArrayAdapter arrayAdapter = new ArrayAdapter(CreateDoctorAccountActivity.this, R.layout.support_simple_spinner_dropdown_item, adapter_name);
                                departmentSpiner.setAdapter(arrayAdapter);
                                departmentSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        dept_id = departmentData.get(position).getDepID();
                                        Toast.makeText(CreateDoctorAccountActivity.this, dept_id.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }

                            // MyProgressDialog.hide();
                        } catch (JSONException e) {
                            Log.i("JSONException :", e.toString());
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
//                Log.i("VolleyError :", error.getMessage().toString());
                Toast.makeText(CreateDoctorAccountActivity.this, "" + error, Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CreateDoctorAccountActivity.this);
        requestQueue.add(stringRequest);

    }


}
