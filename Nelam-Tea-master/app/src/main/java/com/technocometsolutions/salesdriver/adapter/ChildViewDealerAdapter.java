package com.technocometsolutions.salesdriver.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.AddOrderActivity;
import com.technocometsolutions.salesdriver.activity.RetailerActivity;
import com.technocometsolutions.salesdriver.model.ProductCatModel1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChildViewDealerAdapter extends RecyclerView.Adapter<ChildViewDealerAdapter.ViewHolder> {

    Activity context;
    public List<ProductCatModel1> categoryItemModelList;
    public List<ProductCatModel1> productCatModel1ListFilterable;
    ChildViewDealerAdapter childViewDealerAdapter;
    List<ProductCatModel1> productCatModel1s;
    ArrayList quntity=new ArrayList();

    public ChildViewDealerAdapter(Activity context, List<ProductCatModel1> categoryItemModelList) {
        this.context = context;
        this.categoryItemModelList = categoryItemModelList;
        this.productCatModel1ListFilterable = categoryItemModelList;
    }



    public void filterList(List<ProductCatModel1> filterllist,ChildViewDealerAdapter childViewDealerAdapter) {

        // below line is to add our filtered
        // list in our course array list.
        childViewDealerAdapter.productCatModel1ListFilterable= filterllist;
        Log.d("catagory", String.valueOf(productCatModel1ListFilterable.get(0).getProduct()));
        this.childViewDealerAdapter=childViewDealerAdapter;
        this.productCatModel1s=productCatModel1s;
        // below line is to notify our adapter
        // as change in recycler view data.
        childViewDealerAdapter.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.child_row,parent,false);
        ViewHolder ph = new ViewHolder(view, new CustomEtListener());
        return ph;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d("childpos", String.valueOf(position));
        holder.tv_product_name.setText(productCatModel1ListFilterable.get(position).getProduct());
        holder.tv_wait.setText(productCatModel1ListFilterable.get(position).getSize());
        //tv_price.setText("₹"+categoryItemModelListFilterable.get(position).getProducts().get(i).getPrice());
        Glide.with(context).load(context.getString(R.string.image_url) + productCatModel1ListFilterable.get(position).getProduct_photo()).into(holder.p_image);
        //holder.et_qty.setText(String.valueOf(productCatModel1ListFilterable.get(position).getTotal_qty()));
        holder.myCustomEditTextListener.updatePosition(position);



//        holder.et_qty.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return productCatModel1ListFilterable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_product_name,tv_wait,tv_price;
        EditText et_qty;
        ImageView p_image;
        public CustomEtListener myCustomEditTextListener;

        public ViewHolder(@NonNull View itemView,CustomEtListener myLis) {
            super(itemView);

            tv_product_name=itemView.findViewById(R.id.tv_product_name);
            et_qty=itemView.findViewById(R.id.integer_number);
            tv_wait=itemView.findViewById(R.id.tv_wait);
            tv_price=itemView.findViewById(R.id.price);
            p_image=itemView.findViewById(R.id.product_img);

            myCustomEditTextListener = myLis;
            et_qty.addTextChangedListener(myCustomEditTextListener);

        }
    }
    private class CustomEtListener implements TextWatcher {
        private int position;

        /**
         * Updates the position according to onBindViewHolder
         *
         * @param position - position of the focused item
         */
        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // Change the value of array according to the position


            Log.d("outer","out");
            if (charSequence.toString().trim().length() != 0) {

                Log.d("enterif", String.valueOf(position));
//                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                productCatModel1ListFilterable.get(position).setTotal_qty(Integer.parseInt(""+charSequence.toString()));

//                for (int j=0;j<productCatModel1s.size();j++){
//                    if (productCatModel1s.get(j).getId()==productCatModel1ListFilterable.get(position).getId()){
//                        productCatModel1s.get(j).setTotal_qty(Integer.parseInt(""+charSequence.toString()));
//                    }
//                }
                //RetailerOrderAdapter.productCatModel1s.get(position).setTotal_qty(Integer.parseInt(""+charSequence.toString()));
                //childViewDealerAdapter.productCatModel1ListFilterable.get(position).setTotal_qty(Integer.parseInt(""+charSequence.toString()));
                RetailerActivity.getRetailerActivity().getProgucteed();

            } else {

                Log.d("else","else");
                //holder.et_qty.setText("0");
                RetailerActivity.getRetailerActivity().getProgucteed();

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}

