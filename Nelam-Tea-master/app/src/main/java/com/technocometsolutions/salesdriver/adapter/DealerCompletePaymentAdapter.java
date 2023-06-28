package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.fregment.PendingPaymentFragment;
import com.technocometsolutions.salesdriver.model.CompletePaymentItemModel;
import com.technocometsolutions.salesdriver.model.DealerPaymentPandingItemModel;

import java.util.List;

public class DealerCompletePaymentAdapter extends RecyclerView.Adapter<DealerCompletePaymentAdapter.MyViewHolder> {
    Context mContext;
    public List<CompletePaymentItemModel> completePaymentItemModelList;
    public DealerCompletePaymentAdapter(Context mContext, List<CompletePaymentItemModel> completePaymentItemModels)
    {
        this.mContext=mContext;
        this.completePaymentItemModelList=completePaymentItemModels;
    }

    @NonNull
    @Override
    public DealerCompletePaymentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_row_complete_payment, parent, false);

        // Return a new holder instance
        MyViewHolder viewHolder = new DealerCompletePaymentAdapter.MyViewHolder(contactView);
        return viewHolder;

       // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DealerCompletePaymentAdapter.MyViewHolder holder, int position) {
        holder.tvDate.setText(""+completePaymentItemModelList.get(position).getDate());
        holder.tvEmployee.setText(""+completePaymentItemModelList.get(position).getEmploye());
        holder.tvDealer.setText(""+completePaymentItemModelList.get(position).getDealer());
        holder.tvOrderNo.setText(""+completePaymentItemModelList.get(position).getOrder_no());
        holder.tvAmount.setText(""+completePaymentItemModelList.get(position).getAmount());
        holder.tvSr.setText(completePaymentItemModelList.get(position).getNo());

    }

    @Override
    public int getItemCount() {
        return completePaymentItemModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate,tvEmployee,tvDealer,tvOrderNo,tvAmount,tvSr;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvEmployee=itemView.findViewById(R.id.tvEmployee);
            tvDealer=itemView.findViewById(R.id.tvDealerName);
            tvOrderNo=itemView.findViewById(R.id.tvOrderNo);
            tvAmount=itemView.findViewById(R.id.tvAmount);
            tvSr=itemView.findViewById(R.id.tvsr);

        }
    }
}
