package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.Model.Packagesmodel;
import com.example.shreejicabs.R;

import java.util.ArrayList;

public class Mypackageadapter extends RecyclerView.Adapter<Mypackageadapter.ViewHolder> {

    ArrayList<Packagesmodel> packagesmodelArrayList=new ArrayList<>();
    Activity context;
    public OnItemClickListener onItemClickListener;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView city,rate,cartype,service;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            city=(TextView)view.findViewById(R.id.city);
            rate=(TextView)view.findViewById(R.id.rate);
            cartype=(TextView)view.findViewById(R.id.cartype);
            service=(TextView)view.findViewById(R.id.service);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public Mypackageadapter(Activity context, ArrayList<Packagesmodel> avaliabilitymodelArrayList) {

        this.context=context;
        this.packagesmodelArrayList=avaliabilitymodelArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypackage_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.cartype.setText(packagesmodelArrayList.get(position).getCar_type());
        holder.city.setText(packagesmodelArrayList.get(position).getCity());
        holder.rate.setText("\u20A8"+packagesmodelArrayList.get(position).getRate());
        holder.service.setText(packagesmodelArrayList.get(position).getService());


    }

    @Override
    public int getItemCount() {
        return packagesmodelArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

}
