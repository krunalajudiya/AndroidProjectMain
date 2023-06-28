package com.example.helperfactory.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.Model.Historymodel;
import com.example.helperfactory.Model.Ongoingmodel;
import com.example.helperfactory.R;

import java.util.ArrayList;
import java.util.List;

public class Historyadapter extends RecyclerView.Adapter<Historyadapter.ViewHolder> {

    List<Historymodel.History_data> history_dataArrayList=new ArrayList<>();
    Activity context;
    public Historyadapter.OnItemClickListener onItemClickListener;

    TextView user_name,user_number;
    ImageView close;


    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView sub_cat_name,date_and_time,bill,details,booking_msg_txt,review;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            sub_cat_name=(TextView) view.findViewById(R.id.sub_cat_name);
            date_and_time=(TextView)view.findViewById(R.id.date_and_time);
            bill=(TextView)view.findViewById(R.id.bill);
            details=(TextView)view.findViewById(R.id.detail);
            booking_msg_txt=(TextView)view.findViewById(R.id.booking_msg_txt);
            review=(TextView)view.findViewById(R.id.review);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
            review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.ReviewClick(getAdapterPosition());
                }
            });


        }
    }

    public Historyadapter(Activity context, List<Historymodel.History_data> history_dataArrayList) {

        this.context=context;
        this.history_dataArrayList=history_dataArrayList;

    }

    @Override
    public Historyadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);

        return new Historyadapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final Historyadapter.ViewHolder holder, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = context.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.detail_dialog, viewGroup, false);
        user_name=dialogView.findViewById(R.id.user_name);
        user_number=dialogView.findViewById(R.id.user_number);
        close=dialogView.findViewById(R.id.close);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        holder.sub_cat_name.setText(history_dataArrayList.get(position).getSub_cat_name());
        holder.date_and_time.setText(history_dataArrayList.get(position).getTime()+" on "+history_dataArrayList.get(position).getDate() );
        holder.bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.IMAGE_URL+history_dataArrayList.get(position).getBill_pdf()));
                context.startActivity(browserIntent);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Historymodel.Vendor_detail vendor_detail=history_dataArrayList.get(position).getVendor_detail().get(0);
        user_name.setText(vendor_detail.getVendor_name());
        user_number.setText(vendor_detail.getMobile());

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    alertDialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return history_dataArrayList.size();
    }

    public void setOnItemClickListener(Historyadapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
        void ReviewClick(int pos);
    }



}