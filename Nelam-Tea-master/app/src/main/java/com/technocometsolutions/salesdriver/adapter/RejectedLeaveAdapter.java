package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.PendingLeaveItemModel;
import com.technocometsolutions.salesdriver.model.RejectedLeaveItemModel;

import java.util.List;

public class RejectedLeaveAdapter extends RecyclerView.Adapter<RejectedLeaveAdapter.MyViewHolder> {
    public List<RejectedLeaveItemModel> rejectedLeaveItemModelList;
    public Context mContext;



    public RejectedLeaveAdapter(Context mContext, List<RejectedLeaveItemModel> rejectedLeaveItemModels) {
        this.mContext = mContext;
        this.rejectedLeaveItemModelList= rejectedLeaveItemModels;
    }

    @NonNull
    @Override
    public RejectedLeaveAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pending_leave_item, parent, false);

        return new MyViewHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RejectedLeaveAdapter.MyViewHolder holder, int position) {
        final RejectedLeaveItemModel rejectedLeaveItemModel= rejectedLeaveItemModelList.get(position);


        holder.tvempName.setText(rejectedLeaveItemModel.getEmp_name());
        holder.tvstartDate.setText(rejectedLeaveItemModel.getStart_date());
        holder.tvendDate.setText(rejectedLeaveItemModel.getEnd_date());
        holder.tvReason.setText(rejectedLeaveItemModel.getReason());




    }

    @Override
    public int getItemCount() {
        return rejectedLeaveItemModelList.size();
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
