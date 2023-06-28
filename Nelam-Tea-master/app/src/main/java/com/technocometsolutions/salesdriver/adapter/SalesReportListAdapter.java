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
import com.technocometsolutions.salesdriver.model.SalesReportItemModel;

import java.util.List;

public class SalesReportListAdapter extends RecyclerView.Adapter<SalesReportListAdapter.MyViewHolder> {
    public List<SalesReportItemModel> salesReportItemModelList;
    public Context mContext;



    public SalesReportListAdapter(Context mContext, List<SalesReportItemModel> salesReportItemModels) {
        this.mContext = mContext;
        this.salesReportItemModelList= salesReportItemModels;
    }

    @NonNull
    @Override
    public SalesReportListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_sales_report, parent, false);

        return new MyViewHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReportListAdapter.MyViewHolder holder, int position) {
        final SalesReportItemModel salesReportItemModel = salesReportItemModelList.get(position);

        holder.tvDealerName.setText(salesReportItemModel.getDealer_name());
        holder.tvDate.setText(salesReportItemModel.getDate());
        holder.tvEmployeeName.setText(salesReportItemModel.getEmpploye_name());
        holder.tvTotalWeight.setText(salesReportItemModel.getTotal_weight());
        holder.tvTotalPrice.setText(salesReportItemModel.getTotal_price());




        for (int i = 0; i < salesReportItemModelList.get(position).getItems().size(); i++) {
        TextView tvProduct,tvPrice,tvWeight,tvQuantity,tvTotalPrice,tvTotalWeight;
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_product_sales_list, null);
            tvProduct=itemView.findViewById(R.id.tvProduct);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvWeight=itemView.findViewById(R.id.tvWeight);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice=itemView.findViewById(R.id.tvTotalPrice);
            tvTotalWeight=itemView.findViewById(R.id.tvTotalWeight);

            tvProduct.setText(salesReportItemModelList.get(position).getItems().get(i).getProduct());
            tvPrice.setText(salesReportItemModelList.get(position).getItems().get(i).getPrice());
            tvWeight.setText(salesReportItemModelList.get(position).getItems().get(i).getWeight());
            tvQuantity.setText(salesReportItemModelList.get(position).getItems().get(i).getQuantity());
            tvTotalPrice.setText(salesReportItemModelList.get(position).getItems().get(i).getTotal_price());
            tvTotalWeight.setText(salesReportItemModelList.get(position).getItems().get(i).getTotal_weight());

            holder.linearLayout.addView(itemView);
        }

    }

    @Override
    public int getItemCount() {
        return salesReportItemModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
    LinearLayout linearLayout;
    TextView tvDate,tvEmployeeName,tvDealerName,tvTotalPrice,tvTotalWeight;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.salesReport);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvEmployeeName=itemView.findViewById(R.id.tvEmployeeName);
            tvDealerName=itemView.findViewById(R.id.tvDealerName);
            tvTotalPrice=itemView.findViewById(R.id.tvTotalPrice);
            tvTotalWeight=itemView.findViewById(R.id.tvTotalWeight);

        }
    }
}
