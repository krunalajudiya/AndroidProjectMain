package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Addvehiclemodel;
import com.example.shreejicabs.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Cardetailsshowadapter extends RecyclerView.Adapter<Cardetailsshowadapter.ViewHolder> {

    private final Activity context;
    private final ArrayList<Addvehiclemodel> vehicle_details;

    public Cardetailsshowadapter(Activity context, ArrayList<Addvehiclemodel> vehicle_details) {
        this.context = context;
        this.vehicle_details = vehicle_details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.car_details_show_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.modelname.setText(vehicle_details.get(position).getCar_name());
        holder.fuel.setText("Fule: "+vehicle_details.get(position).getFuel());
        holder.passingyear.setText("Passing: "+vehicle_details.get(position).getModel_year());
        holder.color.setText("Color: "+vehicle_details.get(position).getColor());
        Picasso.get().load(Constants.IMAGE_URL+vehicle_details.get(position).getPhoto()).into(holder.car_image);
    }

    @Override
    public int getItemCount() {
        return vehicle_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView car_image;
        TextView modelname,passingyear,fuel,color;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            car_image=itemView.findViewById(R.id.carimage);
            modelname=itemView.findViewById(R.id.modelname_);
            passingyear=itemView.findViewById(R.id.passingyear_);
            fuel=itemView.findViewById(R.id.fuel_);
            color=itemView.findViewById(R.id.color_);

        }
    }
}
