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

import java.util.List;

public class DesignationAdapter extends RecyclerView.Adapter<DesignationAdapter.MyViewHolder> {
    public List<GetDelalerListModel> orderListAdapterList;
    public Context mContext;



    public DesignationAdapter(Context mContext, List<GetDelalerListModel> productModelList) {
        this.mContext = mContext;
        this.orderListAdapterList= productModelList;
    }

    @NonNull
    @Override
    public DesignationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_get_order_list, parent, false);

        return new MyViewHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DesignationAdapter.MyViewHolder holder, int position) {
        final GetDelalerListModel getOrderListModel= orderListAdapterList.get(position);

        holder.tvDealerName.setText(getOrderListModel.getDealer_name());
        holder.tvTotalWeight.setText(getOrderListModel.getTotal_weight());
        holder.tvTotalPrice.setText(getOrderListModel.getTotal_price());




        for (int i = 0; i < orderListAdapterList.get(position).getProductList().size(); i++) {
            TextView tvCategoryName,tvProductName,tvProductQuantity,tvOrginalWeight;
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_product_list, null);
            tvCategoryName=itemView.findViewById(R.id.tvCategoryName);
            tvProductName=itemView.findViewById(R.id.tvProductName);
            tvProductQuantity=itemView.findViewById(R.id.tvProductQuantity);
            tvOrginalWeight=itemView.findViewById(R.id.tvOriginalWeight);

            tvCategoryName.setText(orderListAdapterList.get(position).getProductList().get(i).getCategory());
            tvProductName.setText(orderListAdapterList.get(position).getProductList().get(i).getProduct());
            tvProductQuantity.setText(orderListAdapterList.get(position).getProductList().get(i).getQuantity());
            tvOrginalWeight.setText(orderListAdapterList.get(position).getProductList().get(i).getOriginal_weight());

            holder.linearLayout.addView(itemView);
        }

    }

    @Override
    public int getItemCount() {
        return orderListAdapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
    LinearLayout linearLayout;
    TextView tvDealerName,tvTotalPrice,tvTotalWeight;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.getOrderList);
            tvDealerName=itemView.findViewById(R.id.tvDealerName1);
            tvTotalPrice=itemView.findViewById(R.id.tvDealerTotalPrice);
            tvTotalWeight=itemView.findViewById(R.id.tvDealerTotalWeight);

        }
    }
}
