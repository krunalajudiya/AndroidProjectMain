package com.technocometsolutions.salesdriver.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.AddOrderActivity;
import com.technocometsolutions.salesdriver.model.CategoryItemModel1;
import com.technocometsolutions.salesdriver.model.ProductCatModel;
import com.technocometsolutions.salesdriver.model.ProductCatModel1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChildViewAdapter extends RecyclerView.Adapter<ChildViewAdapter.ViewHolder>{
    Activity context;
    public List<ProductCatModel1> categoryItemModelList;
    public List<ProductCatModel1> productCatModel1ListFilterable;
    int pos;
//    int i=0;



    public ChildViewAdapter(Activity context, List<ProductCatModel1> categoryItemModelList,int pos) {
        this.context = context;
        this.categoryItemModelList = categoryItemModelList;
        this.productCatModel1ListFilterable=categoryItemModelList;
        this.pos=pos;
    }
    public void filterList(List<ProductCatModel1> filterllist,ChildViewAdapter childViewAdapterList) {

        // below line is to add our filtered
        // list in our course array list.
        childViewAdapterList.productCatModel1ListFilterable= filterllist;
        Log.d("catagory", String.valueOf(productCatModel1ListFilterable.get(0).getProduct()));
        // below line is to notify our adapter
        // as change in recycler view data.
        childViewAdapterList.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.child_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


            Log.d("ff", String.valueOf(position));
            holder.tv_product_name.setText(productCatModel1ListFilterable.get(position).getProduct());
            holder.tv_wait.setText(productCatModel1ListFilterable.get(position).getSize());
            //tv_price.setText("₹"+categoryItemModelListFilterable.get(position).getProducts().get(i).getPrice());
            Glide.with(context).load(context.getString(R.string.image_url) + categoryItemModelList.get(position).getProduct_photo()).into(holder.p_image);
            holder.et_qty.setText(String.valueOf(productCatModel1ListFilterable.get(position).getTotal_qty()));


            holder.et_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s.toString().trim().length() != 0) {

                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
//                        if (i<productCatModel1ListFilterable.size()){
                            productCatModel1ListFilterable.get(position).setTotal_qty(Integer.parseInt(""+s.toString()));
//                        }

                        AddOrderActivity.getAddOrderActivity().getProgucteed();

                    } else {

                        holder.et_qty.setText("0");
                        AddOrderActivity.getAddOrderActivity().getProgucteed();

                    }
                }
            });


    }

    @Override
    public int getItemCount() {
        return productCatModel1ListFilterable.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product_name,tv_wait,tv_price;
        EditText et_qty;
        ImageView p_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_product_name=itemView.findViewById(R.id.tv_product_name);
            et_qty=itemView.findViewById(R.id.integer_number);
            tv_wait=itemView.findViewById(R.id.tv_wait);
            tv_price=itemView.findViewById(R.id.price);
            p_image=itemView.findViewById(R.id.product_img);


        }
    }
}
