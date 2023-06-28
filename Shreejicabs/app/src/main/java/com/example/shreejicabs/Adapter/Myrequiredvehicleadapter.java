package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.BuildConfig;
import com.example.shreejicabs.Model.Requirementmodel;
import com.example.shreejicabs.R;

import java.util.ArrayList;

public class Myrequiredvehicleadapter extends RecyclerView.Adapter<Myrequiredvehicleadapter.ViewHolder> {

    ArrayList<Requirementmodel> requirementmodelArrayList=new ArrayList<>();
    Activity context;
    public OnItemClickListener onItemClickListener;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView from,to,cartype,date;
        public final ImageView whatsapp;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            from=(TextView)view.findViewById(R.id.from);
            to=(TextView)view.findViewById(R.id.to);
            cartype=(TextView)view.findViewById(R.id.cartype);
            date=(TextView)view.findViewById(R.id.date);
            whatsapp=(ImageView)view.findViewById(R.id.whatapp);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public Myrequiredvehicleadapter(Activity context, ArrayList<Requirementmodel> requirementmodelArrayList) {

        this.context=context;
        this.requirementmodelArrayList=requirementmodelArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_required_vehicle_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.from.setText(requirementmodelArrayList.get(position).getFrom());
        holder.to.setText(requirementmodelArrayList.get(position).getTo());
        holder.cartype.setText(requirementmodelArrayList.get(position).getCartype());
        holder.date.setText(requirementmodelArrayList.get(position).getDate());
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String shareMessage;
                shareMessage="Required Taxi : "+requirementmodelArrayList.get(position).getFrom()+ " To "+requirementmodelArrayList.get(position).getTo()+
                        "\nDate : "+requirementmodelArrayList.get(position).getDate()+","+requirementmodelArrayList.get(position).getTime()+
                        "\nVehicle : "+requirementmodelArrayList.get(position).getCartype()+"\nComment : "+requirementmodelArrayList.get(position).getComment()+
                        "\n\nPut Your Taxi Availability on TaxiTrip App and Share,absolutely free."+
                        "\n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                intent.setType("text/plain");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requirementmodelArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

}