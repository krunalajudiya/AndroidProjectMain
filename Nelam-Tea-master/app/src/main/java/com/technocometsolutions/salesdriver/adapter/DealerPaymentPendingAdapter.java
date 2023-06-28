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
import com.technocometsolutions.salesdriver.model.DealerPaymentPandingItemModel;

import java.util.List;

public class DealerPaymentPendingAdapter extends RecyclerView.Adapter<DealerPaymentPendingAdapter.MyViewHolder> {
    Context mContext;
    public List<DealerPaymentPandingItemModel> dealerPaymentPandingItemModelArrayList;
    public DealerPaymentPendingAdapter(Context mContext, List<DealerPaymentPandingItemModel> dealerPaymentPandingItemModelArrayList)
    {
        this.mContext=mContext;
        this.dealerPaymentPandingItemModelArrayList=dealerPaymentPandingItemModelArrayList;
    }

    @NonNull
    @Override
    public DealerPaymentPendingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_row_payment, parent, false);

        // Return a new holder instance
        MyViewHolder viewHolder = new DealerPaymentPendingAdapter.MyViewHolder(contactView);
        return viewHolder;

       // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DealerPaymentPendingAdapter.MyViewHolder holder, int position) {
        holder.tv_sr_no.setText(""+dealerPaymentPandingItemModelArrayList.get(position).getNo());
        holder.tv_date.setText(""+dealerPaymentPandingItemModelArrayList.get(position).getDate());
        holder.tv_order_no.setText(""+dealerPaymentPandingItemModelArrayList.get(position).getOrderNo());
        holder.tv_amout.setText(""+dealerPaymentPandingItemModelArrayList.get(position).getAmount());


        holder.cb_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                {

                    PendingPaymentFragment.getInstance().dealerPaymentPandingItemModelArrayList1.add(dealerPaymentPandingItemModelArrayList.get(position));
                    //PendingPaymentFragment.getInstance().et_amount.setVisibility(View.GONE);
                }
                else
                {
                    PendingPaymentFragment.getInstance().dealerPaymentPandingItemModelArrayList1.remove(dealerPaymentPandingItemModelArrayList.get(position));

                    //PendingPaymentFragment.getInstance().et_amount.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dealerPaymentPandingItemModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_sr_no,tv_date,tv_order_no,tv_amout,tv_pending;
        public CheckBox cb_checkbox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sr_no=itemView.findViewById(R.id.tv_sr_no);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_order_no=itemView.findViewById(R.id.tv_order_no);
            tv_amout=itemView.findViewById(R.id.tv_amout);
            cb_checkbox=itemView.findViewById(R.id.cb_checkbox);

        }
    }
}
