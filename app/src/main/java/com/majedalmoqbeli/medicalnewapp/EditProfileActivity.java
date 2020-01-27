package com.majedalmoqbeli.medicalnewapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
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
import com.majedalmoqbeli.medicalnewapp.ClassControl.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.ClassControl.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.ClassData.UserDoctorData;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    EditText username, user_email, address, about, phone, consprice;
    Button btn_edit;
    private String URLline = SaveSetting.ServerURL + "editProfileDoctor.php";
    CircularImageView image;
    UserDoctorData userDoctorData;
    String imgBase64 = "0";
    private static int GALLERY_REQUEST_CODE = 1;
    Bitmap myImageBitmap;
AppBarLayout app_bar_layout;
    CollapsingToolbarLayout collapsing_toolbar;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (SaveSetting.USERID.equals("0")) finish();
        else {
            if (getIntent().getExtras() != null) {
                userDoctorData = (UserDoctorData) getIntent().getExtras().getSerializable("userDoctorData");
            } else finish();
        }
        app_bar_layout = findViewById(R.id.app_bar_layout);
     collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        username = findViewById(R.id.edit_username);
        user_email = findViewById(R.id.edit_email);
        address = findViewById(R.id.edit_address);
        about = findViewById(R.id.edit_about);
        phone = findViewById(R.id.edit_phone);
        consprice = findViewById(R.id.edit_consprice);
        image = findViewById(R.id.image);
        btn_edit = findViewById(R.id.btn_edit);
    initComponent2();
        setDataForDoctor();

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() ||
                        user_email.getText().toString().isEmpty() ||
                        address.getText().toString().isEmpty() ||
                        about.getText().toString().isEmpty() ||
                        phone.getText().toString().isEmpty() ||
                        consprice.getText().toString().isEmpty()
                        ) {
                    Toast.makeText(EditProfileActivity.this, getString(R.string.inputAllData), Toast.LENGTH_SHORT).show();
                } else {
                    MyProgressDialog.show(EditProfileActivity.this);
                    connectToServer();
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });


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


    public void backClicked(View view) {
        finish();
    }

    public void setDataForDoctor() {
        username.setText(userDoctorData.getUser_name());
        user_email.setText(userDoctorData.getEmail());
        consprice.setText(userDoctorData.getCons_price());
        about.setText(userDoctorData.getAbout());
        address.setText(userDoctorData.getAddress());
        phone.setText(userDoctorData.getPhone());
        Picasso.with(this).load(SaveSetting.ServerURL + "user_image/" + userDoctorData.getA_content()).into(image);

    }


    public void connectToServer() {

        RequestQueue queue = Volley.newRequestQueue(EditProfileActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyProgressDialog.hide();
                        SaveSetting.USERNAME=username.getText().toString();
                        SaveSetting sv = new SaveSetting(EditProfileActivity.this);
                        sv.SaveData();
                        Toast.makeText(EditProfileActivity.this, getString(R.string.updataDone), Toast.LENGTH_SHORT).show();
                        Log.i("MySuccess", "" + response);
                        finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Toast.makeText(EditProfileActivity.this, getString(R.string.errorNetwork) , Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", SaveSetting.USERID);
                params.put("username", username.getText().toString().trim());
                params.put("user_email", user_email.getText().toString().trim());
                params.put("address", address.getText().toString().trim());
                params.put("phone", phone.getText().toString());
                params.put("about", about.getText().toString());
                params.put("consprice", consprice.getText().toString());
                params.put("imageuser", imgBase64);
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

        if(data!=null){
            try {
                Uri selectedImage = data.getData();
                myImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                if (myImageBitmap.getWidth() >= 1000 || myImageBitmap.getHeight() >= 1000)
                    myImageBitmap = Bitmap.createScaledBitmap(myImageBitmap, (int) (myImageBitmap.getWidth() * 0.4), (int) (myImageBitmap.getHeight() * 0.4), true);
                image.setImageBitmap(myImageBitmap);
                imgBase64 = encodeImage(myImageBitmap);
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.errorPickImage), Toast.LENGTH_SHORT).show();
            }
        }




    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }


}


