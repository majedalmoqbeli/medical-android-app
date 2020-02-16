package com.majedalmoqbeli.medicalnewapp.ui.fragments;


import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.helpers.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.control.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    public final static String U2_URL = "http://www.sooqazal.com/medcare/General/get_user_data?user_id=" + SaveSetting.USERID;
    public final static String U_URL = "http://www.sooqazal.com/medcare/User/UpdateUser?";
    private RequestQueue requestQueue;
    private TextInputLayout nameLayout, emailLayout, phoneLayout, addressLayout;
    private TextInputEditText name, email, phone, address, date, balance;
    Button edit, cancel;
    //CircleImageView userImage;
    InputValidation inputValidation;

    public static Fragment newInstant() {
        return new UserProfileFragment();
    }


    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyProgressDialog.show(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.emailUser);
        phone = view.findViewById(R.id.phone);
        address = view.findViewById(R.id.addressUser);
        date = view.findViewById(R.id.data);
        balance = view.findViewById(R.id.balance);
        // userImage = view.findViewById(R.id.imageUser);

        nameLayout = view.findViewById(R.id.nameLayout);
        emailLayout = view.findViewById(R.id.emailLayout);
        phoneLayout = view.findViewById(R.id.phoneLayout);
        addressLayout = view.findViewById(R.id.addressLayout);

        edit = view.findViewById(R.id.edit);
        cancel = view.findViewById(R.id.myCons);

        requestQueue = Volley.newRequestQueue(getActivity());
        inputValidation = new InputValidation(getContext());

        getUserData();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancel.setEnabled(true);
                String txt = edit.getText().toString();
                if (txt.equals("تغيير")) {
                    edit.setText("تحديث");
                    name.setEnabled(true);
                    phone.setEnabled(true);
                    email.setEnabled(true);
                    address.setEnabled(true);
                }
                else {
                    MyProgressDialog.show(getActivity());
                    if (!inputValidation.isInputEditTextFilled(name, nameLayout, "ادخل الاسم")) {
                        return;
                    }
                    if (!inputValidation.isInputEditTextFilled(phone, phoneLayout, "ادخل الرقم")) {
                        return;
                    }
                    if (!inputValidation.isInputEditTextFilled(address, addressLayout, "ادخل العنوان")) {
                        return;
                    }
                    if (!inputValidation.isInputEditTextEmail(email, emailLayout, "ادخل ايميل صالح  ")) {
                        return;
                    } else {

                        final String userName = name.getText().toString();
                        final String userPhone = phone.getText().toString();
                        final String userEmail = email.getText().toString();
                        final String userAddress = address.getText().toString();


                        StringRequest userInfo = new StringRequest(StringRequest.Method.POST, U_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                MyProgressDialog.hide();
                                SaveSetting.USERNAME=name.getText().toString();
                                SaveSetting sv = new SaveSetting(getActivity());
                                sv.SaveData();
                                Toast.makeText(getActivity(), getString(R.string.updataDone), Toast.LENGTH_SHORT).show();
                                Log.i("MySuccess", "" + response);
                                getActivity().finish();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Something happen wrong", Toast.LENGTH_SHORT).show();

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("id", SaveSetting.USERID);
                                map.put("username", userName);
                                map.put("phone", userPhone);
                                map.put("email", userEmail);
                                map.put("address", userAddress);


                                return map;
                            }
                        };
                        requestQueue.add(userInfo);
                        // repasword.setVisibility(View.GONE);

                        edit.setText("تغيير");
                        name.setEnabled(false);
                        phone.setEnabled(false);
                        email.setEnabled(false);
                        address.setEnabled(false);
                    }

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserData();
                name.setEnabled(false);
                phone.setEnabled(false);
                email.setEnabled(false);
                address.setEnabled(false);
            }
        });

        return view;
    }

    public void getUserData() {
        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.GET, U2_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("url", U2_URL);
                        Log.i("urffl", response.toString());
                        try {
                            MyProgressDialog.hide();
                            JSONObject doctorObject = response.getJSONObject("user_info");
                            name.setText(doctorObject.getString("user_name"));
                            email.setText(doctorObject.getString("email"));
                            address.setText(doctorObject.getString("address"));
                            phone.setText(doctorObject.getString("phone"));
                            date.setText(doctorObject.getString("created_date"));
                            balance.setText(doctorObject.getString("balance"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("error", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }
}
