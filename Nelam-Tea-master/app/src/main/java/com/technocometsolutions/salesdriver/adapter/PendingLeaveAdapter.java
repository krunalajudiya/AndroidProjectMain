package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.GetDelalerListModel;
import com.technocometsolutions.salesdriver.model.PendingLeaveItemModel;

import java.util.List;

public class PendingLeaveAdapter extends RecyclerView.Adapter<PendingLeaveAdapter.MyViewHolder> {
    public List<PendingLeaveItemModel> pendingLeaveItemModels;
    public Context mContext;



    public PendingLeaveAdapter(Context mContext, List<PendingLeaveItemModel> productModelList) {
        this.mContext = mContext;
        this.pendingLeaveItemModels= productModelList;
    }

    @NonNull
    @Override
    public PendingLeaveAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pending_leave_item, parent, false);

        return new MyViewHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PendingLeaveAdapter.MyViewHolder holder, int position) {
        final PendingLeaveItemModel pendingLeaveItemModelss= pendingLeaveItemModels.get(position);


        holder.tvempName.setText(pendingLeaveItemModelss.getEmp_name());
        holder.tvstartDate.setText(pendingLeaveItemModelss.getStart_date());
        holder.tvendDate.setText(pendingLeaveItemModelss.getEnd_date());
        holder.tvReason.setText(pendingLeaveItemModelss.getReason());




    }

    @Override
    public int getItemCount() {
        return pendingLeaveItemModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tvempName,tvstartDate,tvendDate,tvReason;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvempName=itemView.findViewById(R.id.tvempName);
            tvstartDate=itemView.findViewById(R.id.tvstartDate);
            tvendDate=itemView.findViewById(R.id.tvendDate);
            tvReason=itemView.findViewById(R.id.tvReason);

        }
    }
}
