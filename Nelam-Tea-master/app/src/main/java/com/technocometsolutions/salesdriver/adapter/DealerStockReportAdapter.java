package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.AddOrderActivity;
import com.technocometsolutions.salesdriver.model.CategoryItemModel;
import com.technocometsolutions.salesdriver.model.DealerStockReportItemModel;

import java.util.ArrayList;
import java.util.List;

public class DealerStockReportAdapter extends RecyclerView.Adapter<DealerStockReportAdapter.MyViewHolder> implements Filterable {
    public Context mContext;
    public List<DealerStockReportItemModel> categoryItemModelList;
    public List<DealerStockReportItemModel> categoryItemModelListFilterable;
    public AddOrderAdapterListener listener;
    public DealerStockReportAdapter(Context mContext, List<DealerStockReportItemModel> categoryItemModelList)
    {
        this.mContext=mContext;
        this.categoryItemModelList=categoryItemModelList;
        this.categoryItemModelListFilterable=categoryItemModelList;
        this.listener=listener;

    }

    @NonNull
    @Override
    public DealerStockReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealerStockReportAdapter.MyViewHolder holder, int position) {
        final DealerStockReportItemModel categoryItemModel = categoryItemModelListFilterable.get(position);
        holder.heading.setText(categoryItemModel.getCategory());
        holder.lv_layout.removeAllViews();

        if (categoryItemModelListFilterable.get(position).getAflag()==0)
        {
            holder.lv_layout.setVisibility(View.GONE);
            holder.expandable_icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
        }
        else
        {
            // ic_remove_circle_outline_black_24dp
            holder.lv_layout.setVisibility(View.VISIBLE);
            holder.expandable_icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_remove_circle_outline_black_24dp));
        }

        holder.lv_haderr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryItemModelListFilterable.get(position).getAflag()==0)
                {
                    holder.lv_layout.setVisibility(View.VISIBLE);
                    holder.expandable_icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_remove_circle_outline_black_24dp));
                    categoryItemModelListFilterable.get(position).setAflag(1);

                }
                else
                {
                    holder.lv_layout.setVisibility(View.GONE);
                    holder.expandable_icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
                    categoryItemModelListFilterable.get(position).setAflag(0);
                    // ic_remove_circle_outline_black_24dp

                }
            }
        });



        for(int i=0;i<categoryItemModelListFilterable.get(position).getProduct().size();i++)
        {

            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_dealer_stock,null);
            TextView tvProduct,tvDealerStock,tvRetailerStock,tvDifferent,tvdealer_bag;
            tvProduct=itemView.findViewById(R.id.tvProduct);
            tvDealerStock=itemView.findViewById(R.id.tvDealerStock);
            tvRetailerStock=itemView.findViewById(R.id.tvRetailerStock);
            tvDifferent=itemView.findViewById(R.id.tvDifferent);
            tvdealer_bag=itemView.findViewById(R.id.tvdealer_bag);
             tvProduct.setText(""+categoryItemModelListFilterable.get(position).getProduct().get(i).getProduct());
             tvDealerStock.setText(""+categoryItemModelListFilterable.get(position).getProduct().get(i).getDealerStock());
             tvRetailerStock.setText(""+categoryItemModelListFilterable.get(position).getProduct().get(i).getRetailerStock());
             tvDifferent.setText(""+categoryItemModelListFilterable.get(position).getProduct().get(i).getDifferent());
            tvdealer_bag.setText(""+categoryItemModelListFilterable.get(position).getProduct().get(i).getTotal_dealer_bag());

            /*TextView tv_product_name=view.findViewById(R.id.tv_product_name);
            EditText et_qty=view.findViewById(R.id.integer_number);
            TextView tv_wait=view.findViewById(R.id.tv_wait);
            TextView tv_price=view.findViewById(R.id.price);
            tv_product_name.setText(categoryItemModelListFilterable.get(position).getProducts().get(i).getProduct());
            tv_wait.setText(categoryItemModelListFilterable.get(position).getProducts().get(i).getWeight()+" KG");
            tv_price.setText("₹"+categoryItemModelListFilterable.get(position).getProducts().get(i).getPrice());

            int finalI = i;*/


            holder.lv_layout.addView(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return categoryItemModelListFilterable.size();
    }

    @Override
    public Filter getFilter() {
      //  return null;

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    categoryItemModelListFilterable = categoryItemModelList;
                } else {
                    categoryItemModelListFilterable.clear();
                    List<DealerStockReportItemModel> filteredList = new ArrayList<>();
                    for (DealerStockReportItemModel row : filteredList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCategory().toString().contains(charString.toLowerCase()) || row.getId().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    categoryItemModelListFilterable = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = categoryItemModelListFilterable;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categoryItemModelListFilterable = (List<DealerStockReportItemModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;
        public ImageView expandable_icon;
        public LinearLayout lv_layout,lv_haderr;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            lv_layout = itemView.findViewById(R.id.lv_layout);
            lv_haderr = itemView.findViewById(R.id.lv_haderr);
            expandable_icon = itemView.findViewById(R.id.expandable_icon);
        }
    }

    public interface AddOrderAdapterListener {
        void onContactSelected(CategoryItemModel contact);
    }
}
