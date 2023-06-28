package com.example.helpervendor.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.helpervendor.Constant.Constant;
import com.example.helpervendor.Model.Ongoingmodel;
import com.example.helpervendor.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class Ongoingadapter extends RecyclerView.Adapter<Ongoingadapter.ViewHolder> {

    List<Ongoingmodel.Ongoing> ongoing_dataArrayList=new ArrayList<>();
    Activity context;
    public OnItemClickListener onItemClickListener;
    public OnBillClickListener onBillClickListener;
    TextView user_name,user_number,user_address;
    PhotoView user_image;
    ImageView close;


    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView sub_cat_name,date_and_time,address,detail,bill_generate;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            sub_cat_name=(TextView) view.findViewById(R.id.sub_cat_name);
            date_and_time=(TextView)view.findViewById(R.id.date_and_time);
            address=(TextView)view.findViewById(R.id.user_address);
            detail=(TextView)view.findViewById(R.id.details);
            bill_generate=(TextView)view.findViewById(R.id.bill_generate);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public Ongoingadapter(Activity context, List<Ongoingmodel.Ongoing> ongoing_dataArrayList) {

        this.context=context;
        this.ongoing_dataArrayList=ongoing_dataArrayList;

    }

    @Override
    public Ongoingadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_order_item, parent, false);

        return new Ongoingadapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final Ongoingadapter.ViewHolder holder, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = context.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.detail_dialog, viewGroup, false);
        user_name=dialogView.findViewById(R.id.user_name);
        user_number=dialogView.findViewById(R.id.address_customer);
        user_image=dialogView.findViewById(R.id.image_customer);
        user_address=dialogView.findViewById(R.id.address_detail);
        close=dialogView.findViewById(R.id.close);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        holder.sub_cat_name.setText(ongoing_dataArrayList.get(position).getSub_name());
        holder.date_and_time.setText(ongoing_dataArrayList.get(position).getTime()+" on "+ongoing_dataArrayList.get(position).getDate() );
        holder.address.setText(ongoing_dataArrayList.get(position).getAddress());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        user_name.setText(ongoing_dataArrayList.get(position).getClient_name());
        user_number.setText(ongoing_dataArrayList.get(position).getMobile());
        user_address.setText(ongoing_dataArrayList.get(position).getAddress());
        if (ongoing_dataArrayList.get(position).getIssue_photo()!=null){
            Picasso.get().load(Constant.IMAGE_URL+ongoing_dataArrayList.get(position).getIssue_photo()).into(user_image);
        }else {
        }
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(ongoing_dataArrayList.get(position).getIssue_photo())!=null){
                    Log.d("ff",String.valueOf(ongoing_dataArrayList.get(position).getIssue_photo()));
                }else {
                    Log.d("ff","not image");
                }


                alertDialog.show();
            }
        });
        if (Integer.parseInt(ongoing_dataArrayList.get(position).getStatus())==2) {
            holder.bill_generate.setText("Bill Calculate");
            holder.bill_generate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBillClickListener.onBillClick(position);
                }
            });
        }else if (Integer.parseInt(ongoing_dataArrayList.get(position).getStatus())==3)
        {
            holder.bill_generate.setText("Bill");
            holder.bill_generate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.IMAGE_URL+ongoing_dataArrayList.get(position).getBill_pdf().get(0).getBill_pdf()));
                    context.startActivity(browserIntent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return ongoing_dataArrayList.size();
    }

    public void setOnItemClickListener(Ongoingadapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnBillClickListener(Ongoingadapter.OnBillClickListener onBillClickListener) {
        this.onBillClickListener = onBillClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
    public interface OnBillClickListener {
        void onBillClick(int pos);
    }




}