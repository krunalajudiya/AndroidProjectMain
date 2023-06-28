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

import com.bumptech.glide.Glide;
import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.Model.Categorymodel;
import com.example.helperfactory.Model.Ongoingmodel;
import com.example.helperfactory.R;

import java.util.ArrayList;
import java.util.List;

public class Ongoingadapter extends RecyclerView.Adapter<Ongoingadapter.ViewHolder> {

    List<Ongoingmodel.Ongoing_data> ongoing_dataArrayList=new ArrayList<>();
    Activity context;
    public Ongoingadapter.OnItemClickListener onItemClickListener;
    public OnCancelClickListener onCancelClickListener;
    public OnPaymentClickListener onPaymentClickListener;
    TextView user_name,user_number;
    ImageView close;


    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView sub_cat_name,date_and_time,cancel,details,booking_msg_txt,payment;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            sub_cat_name=(TextView) view.findViewById(R.id.sub_cat_name);
            date_and_time=(TextView)view.findViewById(R.id.date_and_time);
            cancel=(TextView)view.findViewById(R.id.cancel);
            details=(TextView)view.findViewById(R.id.detail);
            booking_msg_txt=(TextView)view.findViewById(R.id.booking_msg_txt);
            payment=(TextView)view.findViewById(R.id.payment);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public Ongoingadapter(Activity context, List<Ongoingmodel.Ongoing_data> ongoing_dataArrayList) {

        this.context=context;
        this.ongoing_dataArrayList=ongoing_dataArrayList;

    }

    @Override
    public Ongoingadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);

        return new Ongoingadapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final Ongoingadapter.ViewHolder holder, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = context.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.detail_dialog, viewGroup, false);
        user_name=dialogView.findViewById(R.id.user_name);
        user_number=dialogView.findViewById(R.id.user_number);
        close=dialogView.findViewById(R.id.close);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        holder.sub_cat_name.setText(ongoing_dataArrayList.get(position).getSub_cat_name());
        holder.date_and_time.setText(ongoing_dataArrayList.get(position).getTime()+" on "+ongoing_dataArrayList.get(position).getDate() );
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClickListener.onCancelClick(position);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (Integer.parseInt(ongoing_dataArrayList.get(position).getStatus())==1)
        {
            holder.details.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.VISIBLE);
            holder.booking_msg_txt.setText("Booking Pending");
            holder.booking_msg_txt.setBackgroundResource(R.drawable.booking_pending_box);
        }else if (Integer.parseInt(ongoing_dataArrayList.get(position).getStatus())==2)
        {
            holder.details.setVisibility(View.VISIBLE);
            holder.cancel.setVisibility(View.GONE);
            holder.booking_msg_txt.setText("Booking Accepted");
            holder.booking_msg_txt.setBackgroundResource(R.drawable.booking_accept_box);
        }else if (Integer.parseInt(ongoing_dataArrayList.get(position).getStatus())==3)
        {
            holder.details.setVisibility(View.VISIBLE);
            holder.cancel.setVisibility(View.GONE);
            holder.booking_msg_txt.setText("Bill");
            holder.booking_msg_txt.setBackgroundResource(R.drawable.booking_accept_box);
            holder.booking_msg_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.IMAGE_URL+ongoing_dataArrayList.get(position).getBill_pdf().get(0).getBill_pdf()));
                    context.startActivity(browserIntent);
                }
            });
            holder.payment.setVisibility(View.VISIBLE);
            holder.payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPaymentClickListener.onPaymentClick(position);
                }
            });
        }

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(ongoing_dataArrayList.get(position).getStatus())==2)
                {
                    Ongoingmodel.Vendor_detail vendor_detail=ongoing_dataArrayList.get(position).getVendor_detail().get(0);
                    user_name.setText(vendor_detail.getVendor_name());
                    user_number.setText(vendor_detail.getMobile());
                    alertDialog.show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return ongoing_dataArrayList.size();
    }

    public void setOnItemClickListener(Ongoingadapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }
    public interface OnCancelClickListener {
        void onCancelClick(int pos);
    }
    public void setOnPaymentClickListener(OnPaymentClickListener onPaymentClickListener) {
        this.onPaymentClickListener = onPaymentClickListener;
    }
    public interface OnPaymentClickListener {
        void onPaymentClick(int pos);
    }


}