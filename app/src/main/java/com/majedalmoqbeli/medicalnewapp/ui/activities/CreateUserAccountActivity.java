package com.majedalmoqbeli.medicalnewapp.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.R;
import com.majedalmoqbeli.medicalnewapp.models.DepartmentData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateUserAccountActivity extends AppCompatActivity {
    Map<String, String> map = new HashMap<String, String>();
    ArrayList<DepartmentData> departmentData;
    ArrayList<String> adapter_name = new ArrayList<String>();
    private static final String URLline = "http://www.sooqazal.com/Doctor/creat_userAcount.php";
    public static String ServerURLD = "http://www.sooqazal.com/medcare/";
    //public static String ServerURLD = "http://www.sooqazal.com/Doctor/creat_userAcount.php";
    public static String KEY = "acb8f67925ab60e2dff0ab535a56cfe8a26f565e";
    EditText username, user_email, address, about, phone, password, confirmPassword;
    Button btn_edit;


    CircleImageView image;
    String ecnodview, dept_id, typeAcount;
    String imgBase64 = "0";
    private static int GALLERY_REQUEST_CODE = 1;
    Bitmap myImageBitmap;
    String emailInput;
    public static int type = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_account);
        username = findViewById(R.id.username);
        user_email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        image = findViewById(R.id.image);
        confirmPassword = findViewById(R.id.passwordconf);
        btn_edit = findViewById(R.id.savebtn);


        btn_edit.setOnClickListener(new View.OnClickListener() {
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


                } else if (phone.getText().toString().isEmpty()) {
                    phone.setError("");
                } else if (phone.getText().toString().length() < 9) {
                    phone.setError("رقم الهاتف قصير");
                } else if (phone.getText().toString().length() > 15) {
                    phone.setError("The number phone is more");
                } else if (address.getText().toString().isEmpty()) {
                    address.setError("ادخل عنوانك");
                } else if (password.getText().toString().length() < 4) {
                    password.setError("كلمة السر قصيرة");


                } else if (!password.getText().toString().equals(confirmPassword.getText().toString().toString())) {
                    Toast.makeText(CreateUserAccountActivity.this, "كلمة السر غير متطابقتان", Toast.LENGTH_SHORT).show();
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
                        password.getText().toString().equals(confirmPassword.getText().toString().toString())

                        ) {

                    connectToServer();

                }


            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 100);


                } catch (Exception x) {

                }


            }
        });


    }


    public void connectToServer() {

        RequestQueue queue = Volley.newRequestQueue(CreateUserAccountActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CreateUserAccountActivity.this, "Acount Created Succesfully" + response, Toast.LENGTH_SHORT).show();
                        Log.i("MySuccess", "" + response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CreateUserAccountActivity.this, "my error :" + error.toString(), Toast.LENGTH_LONG).show();
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
                params.put("imageuser", imgBase64);
                params.put("password", password.getText().toString().trim());
                return params;
            }
        };
        queue.add(request);
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


}