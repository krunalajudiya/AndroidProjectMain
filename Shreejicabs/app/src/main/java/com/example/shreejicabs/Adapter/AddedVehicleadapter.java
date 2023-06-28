package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Addvehiclemodel;
import com.example.shreejicabs.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddedVehicleadapter extends RecyclerView.Adapter<AddedVehicleadapter.MyViewHolder> {

    Activity context;
    ArrayList<Addvehiclemodel> vehicle_detail;
    OnItemClickListener onItemClickListener;

    public AddedVehicleadapter(Activity context, ArrayList<Addvehiclemodel> vehicle_detail) {
        this.context = context;
        this.vehicle_detail = vehicle_detail;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.addedvehicle_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.model.setText("Model: "+vehicle_detail.get(position).getCar_name());
        holder.fule.setText("Fule: "+vehicle_detail.get(position).getFuel());
        holder.passing.setText("Passing: "+vehicle_detail.get(position).getModel_year());
        holder.color.setText("Color: "+vehicle_detail.get(position).getColor());
        holder.carrier.setText("Carrier: "+vehicle_detail.get(position).getCrrier());
        Picasso.get().load(Constants.IMAGE_URL+vehicle_detail.get(position).getPhoto()).into(holder.vehicle_image);
        Log.d("link_img",Constants.IMAGE_URL+vehicle_detail.get(vehicle_detail.size()-1).getPhoto());

//        holder.remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return vehicle_detail.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button remove;
        TextView model,passing,fule,color,carrier;
        ImageView vehicle_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            model=itemView.findViewById(R.id.model);
            passing=itemView.findViewById(R.id.passing);
            fule=itemView.findViewById(R.id.fule);
            color=itemView.findViewById(R.id.color);
            carrier=itemView.findViewById(R.id.carrier);
            remove=itemView.findViewById(R.id.removevehicle);
            vehicle_image=itemView.findViewById(R.id.vehicleimage);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null){
                        int postion=getAdapterPosition();
                        if (postion != RecyclerView.NO_POSITION){
                            onItemClickListener.onRemoveClick(postion);
                        }
                    }
                }
            });
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onRemoveClick(int pos);
    }
}
