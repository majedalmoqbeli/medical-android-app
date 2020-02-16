package com.majedalmoqbeli.medicalnewapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.majedalmoqbeli.medicalnewapp.R;
import com.majedalmoqbeli.medicalnewapp.ui.activities.SingleFragmentActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DepartmentFragment extends Fragment {

    ArrayList<DepartmentData> departmentData;
    Map<String, String> map = new HashMap<String, String>();

    RecyclerView recyclerView;
    AdapterDepartmentPage adapter;

    public static DepartmentFragment newInstance() {
        return new DepartmentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        departmentData = new ArrayList<>();
        getAllDepartment();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);
        MyProgressDialog.show(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);
        return view;
    }

    public void setAdapter(){
        adapter = new AdapterDepartmentPage(departmentData);
        Tools tools = new Tools();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacesItemDecoration(2, tools.dpToPx(getActivity(), 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.hasFixedSize();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    public class AdapterDepartmentPage extends RecyclerView.Adapter<ViewHolderPage>{

        ArrayList<DepartmentData> departmentData;

        public AdapterDepartmentPage(ArrayList<DepartmentData> departmentData) {
            this.departmentData = departmentData;
        }

        @NonNull
        @Override
        public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new  ViewHolderPage(LayoutInflater.from(getActivity()).inflate(R.layout.custom_department_page, parent, false));

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
            holder.bind(departmentData.get(position));
        }

        @Override
        public int getItemCount() {
            return departmentData.size();
        }
    }

    public class ViewHolderPage extends RecyclerView.ViewHolder{
        ImageView dept_img;
        public TextView title;
        public View lyt_parent;

        public ViewHolderPage(View itemView) {
            super(itemView);
            dept_img = itemView.findViewById(R.id.dept_img);
            title =  itemView.findViewById(R.id.title);
            lyt_parent =  itemView.findViewById(R.id.lyt_parent);
            //dept_img.setBackground(R.drawable.placeholder_thumb);

        }

        public void bind(final DepartmentData dept){
            Picasso.with(getActivity())
                    .load(dept.getImg_url())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(dept_img);

            title.setText(dept.getDepNameEn());

            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }
    }

    public void getAllDepartment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SaveSetting.ServerURL + "General",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response :", response.toString());
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

                            setAdapter();
                            MyProgressDialog.hide();
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), R.string.errorLogin, Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage().toString());
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
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


    /*** Style ***/

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int i, int space, boolean b) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
        }
    }

    public class Tools {
        public int dpToPx(Context c, int dp) {
            Resources r = c.getResources();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
        }
    }

    /*** End Style **/


}
