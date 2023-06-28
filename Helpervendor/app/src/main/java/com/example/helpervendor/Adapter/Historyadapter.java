package com.example.helpervendor.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.helpervendor.Constant.Constant;
import com.example.helpervendor.Model.Historymodel;
import com.example.helpervendor.Model.Ongoingmodel;
import com.example.helpervendor.R;

import java.util.ArrayList;
import java.util.List;

public class Historyadapter extends RecyclerView.Adapter<Historyadapter.ViewHolder> {

    List<Historymodel.History> historyArrayList=new ArrayList<>();
    Activity context;
    public Historyadapter.OnItemClickListener onItemClickListener;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView sub_cat_name,date_and_time,address,detail,bill;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            sub_cat_name=(TextView) view.findViewById(R.id.sub_cat_name);
            date_and_time=(TextView)view.findViewById(R.id.date_and_time);
            address=(TextView)view.findViewById(R.id.user_address);
            detail=(TextView)view.findViewById(R.id.details);
            bill=(TextView)view.findViewById(R.id.bill);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onItemClick(getAdapterPosition(), v);
//                }
//            });



        }
    }

    public Historyadapter(Activity context, List<Historymodel.History> historyArrayList) {

        this.context=context;
        this.historyArrayList=historyArrayList;

    }

    @Override
    public Historyadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_order_item, parent, false);

        return new Historyadapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final Historyadapter.ViewHolder holder, final int position) {

        holder.sub_cat_name.setText(historyArrayList.get(position).getSub_name());
        holder.date_and_time.setText(historyArrayList.get(position).getTime()+" on "+historyArrayList.get(position).getDate() );
        holder.address.setText(historyArrayList.get(position).getAddress());
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.IMAGE_URL+historyArrayList.get(position).getBill_pdf()));
                context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    public void setOnItemClickListener(Historyadapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }



}
