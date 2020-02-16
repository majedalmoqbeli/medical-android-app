package com.majedalmoqbeli.medicalnewapp.ui.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.adapters.AdapterConsultation;
import com.majedalmoqbeli.medicalnewapp.models.ConsultaionData;

import com.majedalmoqbeli.medicalnewapp.helpers.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.R;
import com.majedalmoqbeli.medicalnewapp.control.SaveSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyConsultationFargment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ConsultaionData> consultaionData;
    AdapterConsultation adapter;
    Map<String, String> map = new HashMap<String, String>();
    String url = "";
    public MyConsultationFargment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyProgressDialog.show(getActivity());
        url = SaveSetting.ServerURL + "getConsultaionByDoctorID.php";
      if(SaveSetting.USERTYPE.equals("1"))
        map.put("userDoc", SaveSetting.USERID);
        else map.put("userID", SaveSetting.USERID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_my_consultation_fargment, container, false);
        recyclerView = myView.findViewById(R.id.recyclerView);
        getConsultation();
        return myView;
    }

    public void getConsultation() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyProgressDialog.hide();
                        Log.i("Response :", response.toString());
                        try {
                            consultaionData = new ArrayList<>();
                            JSONArray array = new JSONArray(response);
                            int i = 0;
                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);
                                consultaionData.add(
                                        new ConsultaionData(
                                                p.getString("cons_id"),
                                                p.getString("user_id"),
                                                p.getString("cons_title"),
                                                p.getString("cons_content"),
                                                p.getString("is_public"),
                                                p.getString("cons_date_time"),
                                                p.getString("birthday"),
                                                p.getString("gender"),
                                                p.getString("weight"),
                                                p.getString("co_a_content"),
                                                p.getString("user_name"),
                                                p.getString("u_id_doc"),
                                                p.getString("doctor_name")

                                        ));
                                i++;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage().toString());
                        }

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new AdapterConsultation(consultaionData, getActivity(),1);
                        recyclerView.setAdapter(adapter);
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.toString());
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("خطأ في الأتصال بالأنترنت");
                builder1.setIcon(R.drawable.ic_cancel);
                builder1.setMessage("تأكد من الأتصال بالانترنت ، ثم اعد المحاولة !");
                builder1.setCancelable(true);
                builder1.setPositiveButton("أعادة المحاولة",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MyProgressDialog.show(getActivity());
                                getConsultation();
                            }
                        });
                builder1.setNegativeButton("خروج",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                getActivity().finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
