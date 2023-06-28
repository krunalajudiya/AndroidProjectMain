package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.AcceptedLeaveItemModel;
import com.technocometsolutions.salesdriver.model.PendingLeaveItemModel;

import java.util.List;

public class AcceptedLeaveAdapter extends RecyclerView.Adapter<AcceptedLeaveAdapter.MyViewHolder> {
    public List<AcceptedLeaveItemModel> acceptedLeaveItemModelList;
    public Context mContext;



    public AcceptedLeaveAdapter(Context mContext, List<AcceptedLeaveItemModel> acceptedLeaveItemModels) {
        this.mContext = mContext;
        this.acceptedLeaveItemModelList= acceptedLeaveItemModels;
    }

    @NonNull
    @Override
    public AcceptedLeaveAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pending_leave_item, parent, false);

        return new MyViewHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedLeaveAdapter.MyViewHolder holder, int position) {
        final AcceptedLeaveItemModel acceptedLeaveItemModel= acceptedLeaveItemModelList.get(position);


        holder.tvempName.setText(acceptedLeaveItemModel.getEmp_name());
        holder.tvstartDate.setText(acceptedLeaveItemModel.getStart_date());
        holder.tvendDate.setText(acceptedLeaveItemModel.getEnd_date());
        holder.tvReason.setText(acceptedLeaveItemModel.getReason());




    }

    @Override
    public int getItemCount() {
        return acceptedLeaveItemModelList.size();
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
