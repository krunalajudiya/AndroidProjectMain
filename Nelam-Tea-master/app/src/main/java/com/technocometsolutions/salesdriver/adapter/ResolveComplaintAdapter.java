package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.ActiveComplaintItemModel;
import com.technocometsolutions.salesdriver.model.ResolveComplaintItemModel;

import java.util.List;

public class ResolveComplaintAdapter extends RecyclerView.Adapter<ResolveComplaintAdapter.MyViewHolder> {
    Context mContext;
    public List<ResolveComplaintItemModel> resolveComplaintItemModelList;
    public ResolveComplaintAdapter(Context mContext, List<ResolveComplaintItemModel> resolveComplaintItemModelList)
    {
        this.mContext=mContext;
        this.resolveComplaintItemModelList=resolveComplaintItemModelList;
    }

    @NonNull
    @Override
    public ResolveComplaintAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_complaint, parent, false);

        // Return a new holder instance
        MyViewHolder viewHolder = new ResolveComplaintAdapter.MyViewHolder(contactView);
        return viewHolder;

       // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ResolveComplaintAdapter.MyViewHolder holder, int position) {
        holder.tvDate.setText(""+resolveComplaintItemModelList.get(position).getDate());
        holder.tvDealerName.setText(""+resolveComplaintItemModelList.get(position).getDealer_name());
        holder.tvMessage.setText(""+resolveComplaintItemModelList.get(position).getMessage());

        Glide.with(mContext).load(resolveComplaintItemModelList.get(position).getImage()).placeholder(R.drawable.complaint_report).into(holder.ivImage);


    }

    @Override
    public int getItemCount() {
        return resolveComplaintItemModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate,tvDealerName,tvMessage;
        public AppCompatImageView ivImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvDealerName=itemView.findViewById(R.id.tvDealerName);
            tvMessage=itemView.findViewById(R.id.tvMessage);

            ivImage=itemView.findViewById(R.id.iv_image);

        }
    }
}
