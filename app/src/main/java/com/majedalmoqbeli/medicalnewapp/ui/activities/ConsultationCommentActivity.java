package com.majedalmoqbeli.medicalnewapp.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.majedalmoqbeli.medicalnewapp.adapters.AdapterComment;
import com.majedalmoqbeli.medicalnewapp.helpers.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.control.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.models.CommentData;
import com.majedalmoqbeli.medicalnewapp.models.ConsultaionData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultationCommentActivity extends AppCompatActivity {
    ConsultaionData conData;
    LinearLayout addCommentLinear, linearShowMore , doc_nameLinear;
    TextView userName, cons_title, cons_content, cons_date_time, doc_name, birthday, gender, weight;
    ImageView is_public;
    CircleImageView cons_img;
    RecyclerView recyclerView;
    ArrayList<CommentData> commentData;
    AdapterComment adapter;
    Map<String, String> map = new HashMap<String, String>();
    Map<String, String> mapComment = new HashMap<String, String>();
    ImageView imageView, imageSend;
    Button commentBtn, moreDetilesBtn;
    String url = "";
    String urlComment = "";
    EditText myComment;
    String has_img = "0";
    String imgBase64 = "0";
    private static int GALLERY_REQUEST_CODE = 1;
    Bitmap myImageBitmap;

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_comment);
        fa = this;
        if (!(getIntent().getExtras().isEmpty())) {
                conData = (ConsultaionData) getIntent().getExtras().getSerializable("ConData");
            } else finish();

        addCommentLinear = findViewById(R.id.addCommentLinear);
        if (conData.getU_id_doc().equals(SaveSetting.USERID) || conData.getUser_id().equals(SaveSetting.USERID)) {
            addCommentLinear.setVisibility(View.VISIBLE);
        }
        recyclerView = findViewById(R.id.recyclerComment);
        myComment = findViewById(R.id.myComment);
        cons_title = findViewById(R.id.cons_title);
        cons_img = findViewById(R.id.cons_img);
        cons_content = findViewById(R.id.cons_content);
        is_public = findViewById(R.id.is_public);
        userName = findViewById(R.id.userName);
        cons_date_time = findViewById(R.id.cons_date_time);
        doc_name = findViewById(R.id.doc_name);
        imageView = findViewById(R.id.imageView);
        imageSend = findViewById(R.id.imageSend);
        birthday = findViewById(R.id.birthday);
        gender = findViewById(R.id.gender);
        weight = findViewById(R.id.weight);
        linearShowMore = findViewById(R.id.linearShowMore);
        doc_nameLinear = findViewById(R.id.doc_nameLinear);


        doc_nameLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog  alertDialog = new AlertDialog.Builder(ConsultationCommentActivity.this).create();
                View view1 = LayoutInflater.from(ConsultationCommentActivity.this).inflate(R.layout.click_show_templet, null);
                final CircleImageView img_doctor = view1.findViewById(R.id.img_doctor);
                Picasso.with(ConsultationCommentActivity.this).load(SaveSetting.ServerURL + "user_image/user_"+conData.getU_id_doc()+".jpg").into(img_doctor);
                final TextView doc_name = view1.findViewById(R.id.doc_name);
                doc_name.setText(conData.getDoctor_name());
                final Button btnShowProfile = view1.findViewById(R.id.btnShowProfile);
                final Button btnSendCon = view1.findViewById(R.id.btnSendCon);

                if (SaveSetting.USERTYPE.equals("1"))btnSendCon.setVisibility(View.GONE);
                btnShowProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ConsultationCommentActivity.this, ProfileDocActivity.class);
                        intent.putExtra("ID",conData.getU_id_doc());
                        startActivity(intent);
                    }
                });

                btnSendCon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SaveSetting.USERID.equals("0"))
                            alertToGetLogin();
                        else {
                            Intent intent = new Intent(getApplicationContext(),SingleFragmentActivity.class);
                            intent.putExtra("to","send");
                            startActivity(intent);                        }
                    }
                });
                alertDialog.setView(view1);

                alertDialog.show();

            }
        });


        url = SaveSetting.ServerURL + "getCommentByConsID.php";
        map.put("cons_id", conData.getCons_id());
        commentBtn = findViewById(R.id.commnetBtn);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView.getVisibility() == View.GONE) {
                    getComment();
                    MyProgressDialog.show(ConsultationCommentActivity.this);
                    commentBtn.setBackgroundColor(Color.parseColor("#0495c6"));
                } else {
                    commentBtn.setBackgroundColor(Color.parseColor("#00BFFF"));
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        moreDetilesBtn = findViewById(R.id.moreDetilesBtn);
        moreDetilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearShowMore.getVisibility() == View.GONE) {
                    linearShowMore.setVisibility(View.VISIBLE);
                    moreDetilesBtn.setBackgroundColor(Color.parseColor("#0495c6"));
                } else {
                    moreDetilesBtn.setBackgroundColor(Color.parseColor("#00BFFF"));
                    linearShowMore.setVisibility(View.GONE);
                }
            }
        });

        bindConsData(conData);
    }

    public void backClicked(View view) {
        finish();
    }

    public void bindConsData(ConsultaionData consData) {
        Picasso.with(this).load(SaveSetting.ServerURL + "user_image/" + consData.getCo_a_content()).into(cons_img);
        userName.setText(consData.getUser_name());
        cons_title.setText(consData.getCons_title());
        cons_content.setText(consData.getCons_content());
        doc_name.setText(consData.getDoctor_name());
        if (consData.getIs_public().equals("1")) {
            is_public.setImageResource(R.drawable.ic_public);
        } else is_public.setImageResource(R.drawable.ic_privet);
        cons_date_time.setText(consData.getCons_date_time());
        birthday.setText(consData.getBirthday());
        if (consData.getGender().equals("1")) gender.setText("ذكر");
        else gender.setText("أنثى");
        weight.setText(consData.getWeight()+" كجم ");
    }

    public void getComment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response :", response.toString());
                        MyProgressDialog.hide();
                        try {
                            commentData = new ArrayList<>();
                            JSONArray array = new JSONArray(response);
                            int i = 0;
                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);
                                commentData.add(new CommentData(
                                        p.getString("comment_id"),
                                        p.getString("cons_id"),
                                        p.getString("user_id"),
                                        p.getString("comment"),
                                        p.getString("comment_data"),
                                        p.getString("has_img"),
                                        p.getString("user_name"),
                                        p.getString("a_content")
                                ));
                                i++;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ConsultationCommentActivity.this, getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage().toString());
                        }
                        if (commentData.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ConsultationCommentActivity.this));
                            adapter = new AdapterComment(commentData, ConsultationCommentActivity.this, conData);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(ConsultationCommentActivity.this, getString(R.string.noComments), Toast.LENGTH_LONG).show();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.toString());
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ConsultationCommentActivity.this);
                builder1.setTitle("خطأ في الأتصال بالأنترنت");
                builder1.setIcon(R.drawable.ic_cancel);
                builder1.setMessage("تأكد من الأتصال بالانترنت ، ثم اعد المحاولة !");
                builder1.setCancelable(true);
                builder1.setPositiveButton("أعادة المحاولة",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getComment();
                            }
                        });
                builder1.setNegativeButton("خروج",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                MainActivity.activity.finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ConsultationCommentActivity.this);
        requestQueue.add(stringRequest);
    }

    public void evaluationClicked(View view) {
        Toast.makeText(this, "لقد قمت بتقييم الدكتور", Toast.LENGTH_SHORT).show();
    }

    public void picImageClicked(View view) {
        pickFromGallery();
    }

    public void sendComment() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlComment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject p = new JSONObject(response);
                            if (p.getString("isAdd").equals("1")) {
                                Toast.makeText(ConsultationCommentActivity.this, getString(R.string.sendCommentDone), Toast.LENGTH_LONG).show();
                                myComment.setText("");
                                myComment.clearFocus();
                                myComment.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.GONE);
                                imgBase64 = "0";
                                has_img = "0";
                                getComment();
                                MyProgressDialog.hide();
                            }

                        } catch (JSONException e) {
                            MyProgressDialog.hide();
                            Log.i("JSONException :", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.toString());
                Toast.makeText(ConsultationCommentActivity.this, getString(R.string.internetError), Toast.LENGTH_LONG).show();

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mapComment;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ConsultationCommentActivity.this);
        requestQueue.add(stringRequest);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void sendClick(View view) {

        if (!imgBase64.equals("0")) {
            makeCommentToSend("any");
        } else if (!myComment.getText().toString().isEmpty()) {
            makeCommentToSend(myComment.getText().toString());
            hideSoftKeyboard(ConsultationCommentActivity.this);
        } else Toast.makeText(this, "Error Data", Toast.LENGTH_SHORT).show();

    }

    private void makeCommentToSend(String comment) {

        urlComment = SaveSetting.ServerURL + "sendComment.php";
        mapComment.put("cons_id", conData.getCons_id());
        mapComment.put("user_id", SaveSetting.USERID);
        mapComment.put("myComment", comment);
        mapComment.put("has_img", has_img);
        mapComment.put("imagecomment", imgBase64);
        MyProgressDialog.show(this);
        sendComment();


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

        if (data != null) {
            try {
                Uri selectedImage = data.getData();
                myImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                if (myImageBitmap.getWidth() >= 1000 || myImageBitmap.getHeight() >= 1000)
                    myImageBitmap = Bitmap.createScaledBitmap(myImageBitmap, (int) (myImageBitmap.getWidth() * 0.3), (int) (myImageBitmap.getHeight() * 0.3), true);

                myComment.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(myImageBitmap);
                imgBase64 = encodeImage(myImageBitmap);
                has_img = "1";

            } catch (IOException e) {

                Toast.makeText(this, getString(R.string.errorPickImage), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
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
                        Intent intent = new Intent(ConsultationCommentActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        MainActivity.activity.finish();
                    }
                });
        builder1.setNegativeButton("إنشاء حساب",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getBaseContext(),CreateAccountActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
