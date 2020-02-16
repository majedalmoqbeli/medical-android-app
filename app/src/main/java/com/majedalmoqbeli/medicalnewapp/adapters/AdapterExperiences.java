package com.majedalmoqbeli.medicalnewapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.majedalmoqbeli.medicalnewapp.models.ExperiencesData;
import com.majedalmoqbeli.medicalnewapp.R;

import java.util.ArrayList;

public class AdapterExperiences extends RecyclerView.Adapter<AdapterExperiences.ViewHolder> {

    ArrayList<ExperiencesData> experiencesData;
    Context ctx;

    public AdapterExperiences(ArrayList<ExperiencesData> experiencesData, Context ctx) {
        this.experiencesData = experiencesData;
        this.ctx = ctx;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(experiencesData.get(position));
    }

    @Override
    public int getItemCount() {
        return experiencesData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        public ViewHolder(View itemView) {
            super(itemView);
        txt = itemView.findViewById(R.id.txtName);
        }
        public  void bind(ExperiencesData expData){
            txt.setText(expData.getExpName());
        }
    }
}
