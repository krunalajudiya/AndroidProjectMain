package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.CompletePaymentItemModel;
import com.technocometsolutions.salesdriver.model.DealerStockItemModel;

import java.util.List;

public class StockReportAdapter extends RecyclerView.Adapter<StockReportAdapter.MyViewHolder> {
    Context mContext;
    public List<DealerStockItemModel> dealerStockItemModelList;
    public StockReportAdapter(Context mContext, List<DealerStockItemModel> dealerStockItemModels)
    {
        this.mContext=mContext;
        this.dealerStockItemModelList=dealerStockItemModels;
    }

    @NonNull

    @Override
    public StockReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_dealer_stock, parent, false);

        // Return a new holder instance
        MyViewHolder viewHolder = new StockReportAdapter.MyViewHolder(contactView);
        return viewHolder;

       // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StockReportAdapter.MyViewHolder holder, int position) {
        holder.tvProduct.setText(""+dealerStockItemModelList.get(position).getProduct());
        holder.tvDealerStock.setText(""+dealerStockItemModelList.get(position).getDealerStock());
        holder.tvRetailerStock.setText(""+dealerStockItemModelList.get(position).getRetailerStock());
        holder.tvDifferent.setText(""+dealerStockItemModelList.get(position).getDifferent());

    }

    @Override
    public int getItemCount() {
        return dealerStockItemModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProduct,tvDealerStock,tvRetailerStock,tvDifferent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProduct=itemView.findViewById(R.id.tvProduct);
            tvDealerStock=itemView.findViewById(R.id.tvDealerStock);
            tvRetailerStock=itemView.findViewById(R.id.tvRetailerStock);
            tvDifferent=itemView.findViewById(R.id.tvDifferent);

        }
    }
}
