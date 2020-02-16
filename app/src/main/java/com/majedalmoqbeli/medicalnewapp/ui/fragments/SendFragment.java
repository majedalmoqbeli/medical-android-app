package com.majedalmoqbeli.medicalnewapp.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.adapters.DeptSpinAdapter;
import com.majedalmoqbeli.medicalnewapp.adapters.DoctorSpinerAdapter;
import com.majedalmoqbeli.medicalnewapp.helpers.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.control.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.models.DepartmentData;
import com.majedalmoqbeli.medicalnewapp.models.DoctorData;
import com.majedalmoqbeli.medicalnewapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class SendFragment extends Fragment {

    Map<String, String> map = new HashMap<String, String>();
    ArrayList<DepartmentData> departmentData;
    ArrayList<DoctorData> doctorsList;
    String dept_id, doc_id;
    public static int PICKFILE_REQUEST_CODE = 1;
    LinearLayout dep_lyt, doc_layout;

    RadioGroup visibility_radio, gender_radio, allergie_radio, medicine_radio, illness_radio;
    EditText weightED, birthdayED, cons_contentED, allergic_ex, medicine_ex, illness_ex, cons_title;
    List<String> cons_attach;
    Button send_cons, load_files;

    Spinner dept_spinner, doc_spinner;


    public static SendFragment newInstance() {
        return new SendFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        departmentData = new ArrayList<>();
        doctorsList = new ArrayList<>();
        cons_attach = new ArrayList<>();


        getAllDepartment();
        View view = inflater.inflate(R.layout.fragment_send, container, false);

        dep_lyt = view.findViewById(R.id.deplayout);
        doc_layout = view.findViewById(R.id.doclayout);
        Intent intent = getActivity().getIntent();
        doc_id = intent.getStringExtra("doc_id");
        dept_id = intent.getStringExtra("dep_id");

        if (doc_id != null && dept_id != null) {
            dep_lyt.setVisibility(View.GONE);
            doc_layout.setVisibility(View.GONE);
        }

        dept_spinner = view.findViewById(R.id.select_dept);
        doc_spinner = view.findViewById(R.id.select_doctor);

        send_cons = view.findViewById(R.id.send_cons);

        send_cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_consultation();
            }
        });

        visibility_radio = view.findViewById(R.id.visibility_radio);
        gender_radio = view.findViewById(R.id.gender_radio);
        allergie_radio = view.findViewById(R.id.allergie_radio);
        weightED = view.findViewById(R.id.weight);
        birthdayED = view.findViewById(R.id.birthday);
        cons_contentED = view.findViewById(R.id.cons_content);
        medicine_ex = view.findViewById(R.id.medicine_ex);
        allergic_ex = view.findViewById(R.id.allergic_ex);
        illness_ex = view.findViewById(R.id.illness_ex);
        load_files = view.findViewById(R.id.load_files);
        medicine_radio = view.findViewById(R.id.medicine_group);
        illness_radio = view.findViewById(R.id.illness_group);
        allergie_radio = view.findViewById(R.id.allergie_radio);
        cons_title = view.findViewById(R.id.cons_title);

        if (SaveSetting.getData(getActivity(), SaveSetting.IS_CON, SaveSetting.CON_PREFERENCES).equals("true")) {
            weightED.setText(SaveSetting.getData(getActivity(), SaveSetting.weight, SaveSetting.CON_PREFERENCES));
            birthdayED.setText(SaveSetting.getData(getActivity(), SaveSetting.birthday, SaveSetting.CON_PREFERENCES));
            cons_contentED.setText(SaveSetting.getData(getActivity(), SaveSetting.cons_content, SaveSetting.CON_PREFERENCES));
            medicine_ex.setText(SaveSetting.getData(getActivity(), SaveSetting.medicine, SaveSetting.CON_PREFERENCES));
            allergic_ex.setText(SaveSetting.getData(getActivity(), SaveSetting.allergies, SaveSetting.CON_PREFERENCES));
            illness_ex.setText(SaveSetting.getData(getActivity(), SaveSetting.illness, SaveSetting.CON_PREFERENCES));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        medicine_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.medicine_yes) {
                    medicine_ex.setVisibility(View.VISIBLE);
                } else
                    medicine_ex.setVisibility(View.GONE);
            }
        });

        allergie_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.allergie_yes) {
                    allergic_ex.setVisibility(View.VISIBLE);
                } else
                    allergic_ex.setVisibility(View.GONE);
            }
        });

        illness_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.illness_yes) {
                    illness_ex.setVisibility(View.VISIBLE);
                } else
                    illness_ex.setVisibility(View.GONE);
            }
        });

        enable_load();
        return view;
    }

    private void send_consultation() {

        MyProgressDialog.show(getActivity());

        map.put("user_id", SaveSetting.USERID);
        map.put("key", "dedededede");
        map.put("u_id_doc", doc_id);
        map.put("is_public", visibility_radio.getCheckedRadioButtonId() == R.id.con_pubic ? "1" : "0");
        map.put("gender", gender_radio.getCheckedRadioButtonId() == R.id.male ? "1" : "2");
        map.put("weight", weightED.getText().toString());
        map.put("cons_content", cons_contentED.getText().toString());
        map.put("birthday", birthdayED.getText().toString());
        map.put("medicine", medicine_ex.getText().toString());
        map.put("illness", illness_ex.getText().toString());
        map.put("allergies", allergic_ex.getText().toString());
        map.put("cons_title", cons_title.getText().toString());

        int i = 0;
        for (String temp : cons_attach) {
            map.put("cons_attach[" + (i++) + "]", temp);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SaveSetting.ServerURLD + "General/send_consultation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("SUMAIARESPONSE :", response.toString());
                        MyProgressDialog.hide();
                        try {
                            if (response.equals("success")) {

                                Toast.makeText(getActivity(), "رائع !! لقد تم أرسال أستشاراتك بنجاح", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } else {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(response)
                                        .setMessage("Cannot Send Consultation")
                                        .setPositiveButton("OK", null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        } catch (Exception e) {
                            Log.i("JSONException :", e.toString());
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.toString());
                Toast.makeText(getActivity(), R.string.internetError, Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enable_load();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    public void enable_load() {
        load_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, PICKFILE_REQUEST_CODE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK) {

            File file = new File(data.getDataString());
            String base64File = "";
            try (FileInputStream imageInFile = new FileInputStream(file)) {
                // Reading a file from file system
                byte fileData[] = new byte[(int) file.length()];
                imageInFile.read(fileData);
                base64File = Base64.getEncoder().encodeToString(fileData);
            } catch (FileNotFoundException e) {
                System.out.println("File not found" + e);
            } catch (IOException ioe) {
                System.out.println("Exception while reading the file " + ioe);
            }

            cons_attach.add(base64File);

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(extension);
    }

    void setDocAdapter() {
        final DoctorSpinerAdapter doctorSpinerAdapter = new DoctorSpinerAdapter(getActivity(), android.R.layout.simple_spinner_item, doctorsList);
        doc_spinner.setAdapter(doctorSpinerAdapter);

        doc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DoctorData data = doctorSpinerAdapter.getItem(position);
                doc_id = data.getU_id_doc();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void setDeptAdapter() {
        final DeptSpinAdapter deptSpinAdapter = new DeptSpinAdapter(getActivity(), android.R.layout.simple_spinner_item, departmentData);
        dept_spinner.setAdapter(deptSpinAdapter);

        dept_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DepartmentData data = deptSpinAdapter.getItem(position);
                dept_id = departmentData.get(position).getDepID();
                getAllDoctor(dept_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getAllDepartment() {
        MyProgressDialog.show(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SaveSetting.ServerURLD + "General",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response :", response.toString());
                        MyProgressDialog.hide();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("departments");
                            int i = 0;

                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);

                                departmentData.add(new DepartmentData(
                                        p.getString("depart_id"),
                                        p.getString("depart_name"),
                                        p.getString("depart_en_name"),
                                        p.getString("is_active"),
                                        p.getString("img_url")
                                ));

                                i++;
                            }

                            setDeptAdapter();
                        } catch (JSONException e) {
                            Log.i("JSONException :", e.toString());
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                Log.i("VolleyError :", error.toString());
                Toast.makeText(getActivity(), R.string.internetError, Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public void getAllDoctor(String dept_id) {
        MyProgressDialog.show(getActivity());
        doctorsList = new ArrayList<>();
        String URL = SaveSetting.ServerURLD + "General/get_all_doctors?depart_id=" + dept_id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyProgressDialog.hide();
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
                                        "",
                                        p.getString("depart_name"),
                                        p.getString("depart_en_name"),
                                        p.getString("is_active")));
                                i++;
                            }
                            setDocAdapter();
                        } catch (JSONException e) {
                            Log.i("JSONException :", e.toString());
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
               Log.i("VolleyError :", error.toString());
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


}
