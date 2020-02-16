package com.majedalmoqbeli.medicalnewapp.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.helpers.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.control.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.models.DepartmentData;
import com.majedalmoqbeli.medicalnewapp.models.DoctorData;
import com.majedalmoqbeli.medicalnewapp.ui.activities.CreateAccountActivity;
import com.majedalmoqbeli.medicalnewapp.ui.activities.LoginActivity;
import com.majedalmoqbeli.medicalnewapp.ui.activities.MainActivity;
import com.majedalmoqbeli.medicalnewapp.ui.activities.ProfileDocActivity;
import com.majedalmoqbeli.medicalnewapp.R;
import com.majedalmoqbeli.medicalnewapp.ui.activities.SingleFragmentActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class DoctorFragment extends Fragment {

    ArrayList<DoctorData> doctorsList;
    ArrayList<DepartmentData> departmentData;
    Map<String, String> map = new HashMap<String, String>();

    RecyclerView recyclerView;
    AdapterDoctorPage adapter;
    Button search_btn;
    String dept_id = "0";

    public String URL = SaveSetting.ServerURLD+"General/get_all_doctors";

    public static DoctorFragment newInstance() {
        return new DoctorFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        doctorsList = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor, container, false);
//        search_btn = view.findViewById(R.id.search_btn);
        MyProgressDialog.show(getActivity());
        recyclerView = view.findViewById(R.id.contentRec);
        departmentData = new ArrayList<>();

        Intent intent = getActivity().getIntent();
        dept_id = intent.getStringExtra("dept_id") != null ? intent.getStringExtra("dept_id") : "0";
        getAllDoctor();

        SearchView search_txt = view.findViewById(R.id.search_txt);

        search_txt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!query.isEmpty()) {
                    final ArrayList<DoctorData> newDoc = new ArrayList<>();
                    for (DoctorData item : doctorsList) {
                        String match = ("(.*)"+query+"(.*)");
                        if (item.getUser_name().matches(match)
                                || item.getCons_price().matches(match)) {
                            newDoc.add(item);
                        }
                    }

                    setAdapter(newDoc);


                } else
                    Toast.makeText(getActivity(), R.string.noDataInsert, Toast.LENGTH_LONG).show();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

    public void setAdapter(ArrayList<DoctorData> data){
        adapter = new AdapterDoctorPage(data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    class AdapterDoctorPage extends RecyclerView.Adapter<ViewHolder>{

        ArrayList<DoctorData> doctorsList;

        public AdapterDoctorPage(ArrayList<DoctorData> doctorsList) {
            this.doctorsList = doctorsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new  ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_contact, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(doctorsList.get(position));
        }

        @Override
        public int getItemCount() {
            return doctorsList.size();
        }


    }
    class ViewHolder extends RecyclerView.ViewHolder{
        DoctorData data;
        TextView vName , vPhone ,text,price;
        ImageView photo;
        FrameLayout frameLayout;
        Button send_con_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            vName = itemView.findViewById(R.id.tvName);
            vPhone = itemView.findViewById(R.id.tvPhon);
            photo = itemView.findViewById(R.id.cusImage);
            price = itemView.findViewById(R.id.price);
            text=itemView.findViewById(R.id.text);
            frameLayout= itemView.findViewById(R.id.temp_framlayout);
            photo.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            send_con_btn = itemView.findViewById(R.id.send_con_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProfileDocActivity.class);
                    intent.putExtra("ID",data.getU_id_doc());
                    startActivity(intent);
                }
            });

        }

        public void bind(final DoctorData data){
            this.data = data;

            vName.setText(data.getUser_name());
            vPhone.setText("القسم : "+data.getDepart_name());
            price.setText("سعرالاستشارة : "+data.getCons_price()+" $ ");
            photo.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            Picasso.with(getActivity())
                    .load(SaveSetting.ServerURL + "user_image/" +data.getImg_url())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .into(photo);

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog  alertDialog = new AlertDialog.Builder(getActivity()).create();
                    View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.click_show_templet, null);
                    final CircleImageView img_doctor = view1.findViewById(R.id.img_doctor);
                    Picasso.with(getActivity()).load(SaveSetting.ServerURL + "user_image/user_"+data.getU_id_doc()+".jpg").into(img_doctor);
                    final TextView doc_name = view1.findViewById(R.id.doc_name);
                    doc_name.setText(data.getUser_name());
                    final Button btnShowProfile = view1.findViewById(R.id.btnShowProfile);
                    final Button btnSendCon = view1.findViewById(R.id.btnSendCon);
                    if (SaveSetting.USERTYPE.equals("1"))btnSendCon.setVisibility(View.GONE);
                    btnShowProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ProfileDocActivity.class);
                            intent.putExtra("ID",data.getU_id_doc());
                            startActivity(intent);
                        }
                    });
                    btnSendCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (SaveSetting.USERID.equals("0"))
                                alertToGetLogin();
                            else {
                                Intent i = new Intent(getActivity(), SingleFragmentActivity.class);
                                i.putExtra("doc_id",data.getU_id_doc());
                                i.putExtra("dep_id",dept_id);
                                i.putExtra("to","send");
                                startActivity(i);
                            }
                        }
                    });

                    alertDialog.setView(view1);

                    alertDialog.show();

                }
            });

            if (SaveSetting.USERTYPE.equals("1"))send_con_btn.setVisibility(View.GONE);
            send_con_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SaveSetting.USERID.equals("0")){
                        alertToGetLogin();
                    }
                    else{
                        Intent i = new Intent(getActivity(), SingleFragmentActivity.class);
                        i.putExtra("doc_id",data.getU_id_doc());
                        i.putExtra("dep_id",dept_id);
                        i.putExtra("to","send");
                        startActivity(i);
                    }
                }
            });

        }
    }
    public void getAllDoctor() {
        doctorsList = new ArrayList<>();
        URL += "?depart_id="+dept_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ResponseSUMAIA :", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("doctors");
                            int i = 0;
                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);
                                doctorsList.add(new DoctorData(
                                        p.getString("u_id_doc"),
                                        p.getString("about"),
                                        p.getString("depart_id"),
                                        p.getString("address"),
                                        p.getString("cons_price"),
                                        p.getString("user_name"),
                                        p.getString("email"),
                                        "",
                                        "",
                                        "",
                                        p.getString("img_url"),
                                        p.getString("depart_name"),
                                        p.getString("depart_en_name"),
                                        p.getString("is_active")));
                                i++;
                            }
                            MyProgressDialog.hide();
                            setAdapter(doctorsList);
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), R.string.noData, Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage());
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
//                Log.i("VolleyError :", error.getMessage().toString());
                Toast.makeText(getActivity(), R.string.internetError, Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return null;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }


    private void alertToGetLogin() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle("لم تقم بتسجيل الدخول");
        builder1.setIcon(R.drawable.ic_exit);
        builder1.setMessage("أنت لم تقم بتسجيل الدخول بعد ، يرجى تسجيل الدخول أولاً ثم اعد المحاولة !!");
        builder1.setCancelable(true);
        builder1.setPositiveButton("تسجيل الدخول",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        MainActivity.activity.finish();
                    }
                });
        builder1.setNegativeButton("إنشاء حساب",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getActivity(),CreateAccountActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
