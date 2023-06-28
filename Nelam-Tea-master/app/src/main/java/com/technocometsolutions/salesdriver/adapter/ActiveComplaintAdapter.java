package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.fregment.PendingPaymentFragment;
import com.technocometsolutions.salesdriver.model.ActiveComplaintItemModel;
import com.technocometsolutions.salesdriver.model.DealerPaymentPandingItemModel;

import java.util.List;

public class ActiveComplaintAdapter extends RecyclerView.Adapter<ActiveComplaintAdapter.MyViewHolder> {
    Context mContext;
    public List<ActiveComplaintItemModel> activeComplaintItemModelList;
    public ActiveComplaintAdapter(Context mContext, List<ActiveComplaintItemModel> activeComplaintItemModelList)
    {
        this.mContext=mContext;
        this.activeComplaintItemModelList=activeComplaintItemModelList;
    }

    @NonNull
    @Override
    public ActiveComplaintAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_complaint, parent, false);

        // Return a new holder instance
        MyViewHolder viewHolder = new ActiveComplaintAdapter.MyViewHolder(contactView);
        return viewHolder;

       // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveComplaintAdapter.MyViewHolder holder, int position) {
        holder.tvDate.setText(""+activeComplaintItemModelList.get(position).getDate());
        holder.tvDealerName.setText(""+activeComplaintItemModelList.get(position).getDealer_name());
        holder.tvMessage.setText(""+activeComplaintItemModelList.get(position).getMessage());

        Glide.with(mContext).load(activeComplaintItemModelList.get(position).getImage()).placeholder(R.drawable.complaint_report).into(holder.ivImage);


    }

    @Override
    public int getItemCount() {
        return activeComplaintItemModelList.size();
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
