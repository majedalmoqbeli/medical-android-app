package com.majedalmoqbeli.medicalnewapp.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.majedalmoqbeli.medicalnewapp.models.DepartmentData;

import java.util.ArrayList;



public class DeptSpinAdapter extends ArrayAdapter<DepartmentData> {

    private ArrayList<DepartmentData> departmentData;
    private Context context;

    public DeptSpinAdapter(@NonNull Context context, int resource, ArrayList<DepartmentData> data) {
        super(context, resource);
        this.departmentData = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return departmentData.size();
    }

    @Nullable
    @Override
    public DepartmentData getItem(int position) {
        return departmentData.get(0);
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
        label.setText(departmentData.get(position).getDepNameEn());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(departmentData.get(position).getDepNameEn());

        return label;
    }
}
