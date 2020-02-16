package com.majedalmoqbeli.medicalnewapp.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.majedalmoqbeli.medicalnewapp.models.DoctorData;

import java.util.ArrayList;



public class DoctorSpinerAdapter extends ArrayAdapter<DoctorData> {
    private ArrayList<DoctorData> doctorData;
    private Context context;

    public DoctorSpinerAdapter(@NonNull Context context, int resource, ArrayList<DoctorData> data) {
        super(context, resource);
        this.doctorData = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return doctorData.size();
    }

    @Nullable
    @Override
    public DoctorData getItem(int position) {
        return doctorData.get(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(doctorData.get(position).getUser_name());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(doctorData.get(position).getUser_name());

        return label;
    }
}
