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
import com.technocometsolutions.salesdriver.activity.RetailerActivity;
import com.technocometsolutions.salesdriver.activity.RetailernewActivity;
import com.technocometsolutions.salesdriver.model.CategoryItemModel;

import java.util.ArrayList;
import java.util.List;

public class RetailerOrderNewAdapter extends RecyclerView.Adapter<RetailerOrderNewAdapter.MyViewHolder> implements Filterable {
    public Context mContext;
    public List<CategoryItemModel> categoryItemModelList;
    public List<CategoryItemModel> categoryItemModelListFilterable;
    public AddOrderAdapterListener listener;
    public RetailerOrderNewAdapter(Context mContext, List<CategoryItemModel> categoryItemModelList)
    {
        this.mContext=mContext;
        this.categoryItemModelList=categoryItemModelList;
        this.categoryItemModelListFilterable=categoryItemModelList;
        this.listener=listener;

    }

    @NonNull
    @Override
    public RetailerOrderNewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RetailerOrderNewAdapter.MyViewHolder holder, int position) {
        final CategoryItemModel categoryItemModel = categoryItemModelListFilterable.get(position);
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

        for(int i=0;i<categoryItemModelListFilterable.get(position).getProducts().size();i++)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.child_row,null);
            TextView tv_product_name=view.findViewById(R.id.tv_product_name);
            EditText et_qty=view.findViewById(R.id.integer_number);
            TextView tv_wait=view.findViewById(R.id.tv_wait);
            TextView tv_price=view.findViewById(R.id.price);
            tv_product_name.setText(categoryItemModelListFilterable.get(position).getProducts().get(i).getProduct());
            tv_wait.setText(categoryItemModelListFilterable.get(position).getProducts().get(i).getWeight()+" KG");
            tv_price.setText("₹"+categoryItemModelListFilterable.get(position).getProducts().get(i).getPrice());
            tv_wait.setVisibility(View.GONE);
            tv_price.setVisibility(View.GONE);
            int finalI = i;
            et_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0)
                {

                    double total_price= Double.parseDouble(categoryItemModelListFilterable.get(position).getProducts().get(finalI).getPrice()) * Double.parseDouble(s.toString());
                    double total_weight=Double.parseDouble(s.toString());
                    //  /*Double.parseDouble(categoryItemModelListFilterable.get(position).getProducts().get(finalI).getWeight()) **/ ;


                    categoryItemModelListFilterable.get(position).getProducts().get(finalI).setTotal_price(total_price);
                    categoryItemModelListFilterable.get(position).getProducts().get(finalI).setTotal_qty(Double.parseDouble(""+s.toString()));
                    categoryItemModelListFilterable.get(position).getProducts().get(finalI).setTotal_kg(total_weight);
                    // integer_number.setText(s.toString());
                    tv_price.setText("₹"+total_price);
                    tv_wait.setText(""+total_weight+" KG");
                    RetailernewActivity.getRetailerActivity().getProgucteed();



                }
                else
                {

                    et_qty.setText("0");
                    tv_price.setText("₹ 0");
                    tv_wait.setText("0 KG");
                    RetailernewActivity.getRetailerActivity().getProgucteed();
                }
            }
        });

            holder.lv_layout.addView(view);
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
                    List<CategoryItemModel> filteredList = new ArrayList<>();
                    for (CategoryItemModel row : filteredList) {

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
                categoryItemModelListFilterable = (List<CategoryItemModel>) filterResults.values;
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
