package com.example.helpervendor.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.helpervendor.Model.Newordermodel;
import com.example.helpervendor.Model.Ongoingmodel;
import com.example.helpervendor.R;

import java.util.ArrayList;
import java.util.List;

public class Neworderadapter extends RecyclerView.Adapter<Neworderadapter.ViewHolder> {

    List<Newordermodel.Neworder> ongoing_dataArrayList=new ArrayList<>();
    Activity context;
    public Neworderadapter.OnItemClickListener onItemClickListener;
    public Neworderadapter.OnConfirmationClickListener onConfirmationClickListener;
    public OnRejectClickListener onRejectClickListener;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView sub_cat_name,date_and_time,address,confirm_order,reject;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            sub_cat_name=(TextView) view.findViewById(R.id.sub_cat_name);
            date_and_time=(TextView)view.findViewById(R.id.date_and_time);
            address=(TextView)view.findViewById(R.id.user_address);
            confirm_order=(TextView)view.findViewById(R.id.confirm_btn);
            reject=(TextView)view.findViewById(R.id.reject_btn);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public Neworderadapter(Activity context, List<Newordermodel.Neworder> ongoing_dataArrayList) {

        this.context=context;
        this.ongoing_dataArrayList=ongoing_dataArrayList;

    }

    @Override
    public Neworderadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_item, parent, false);

        return new Neworderadapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final Neworderadapter.ViewHolder holder, final int position) {

        holder.sub_cat_name.setText(ongoing_dataArrayList.get(position).getSub_name());
        holder.date_and_time.setText(ongoing_dataArrayList.get(position).getTime()+" on "+ongoing_dataArrayList.get(position).getDate() );
        holder.address.setText(ongoing_dataArrayList.get(position).getAddress());
        holder.confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmationClickListener.onConfirmationClick(position);
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRejectClickListener.onRejectClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ongoing_dataArrayList.size();
    }

    public void setOnItemClickListener(Neworderadapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

    public void setOnConfirmationClickListener(Neworderadapter.OnConfirmationClickListener onConfirmationClickListener) {
        this.onConfirmationClickListener = onConfirmationClickListener;
    }
    public interface OnConfirmationClickListener {
        void onConfirmationClick(int pos);
    }
    public void setOnRejectClickListener(OnRejectClickListener onRejectClickListener) {
        this.onRejectClickListener = onRejectClickListener;
    }
    public interface OnRejectClickListener {
        void onRejectClick(int pos);
    }



}
