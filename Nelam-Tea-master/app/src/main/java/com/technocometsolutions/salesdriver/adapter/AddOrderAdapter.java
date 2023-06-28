package com.technocometsolutions.salesdriver.adapter;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.AddOrderActivity;
import com.technocometsolutions.salesdriver.activity.OrderScNewActivity;
import com.technocometsolutions.salesdriver.model.CategoryItemModel;
import com.technocometsolutions.salesdriver.model.CategoryItemModel1;
import com.technocometsolutions.salesdriver.model.ProductCatModel;
import com.technocometsolutions.salesdriver.model.ProductCatModel1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddOrderAdapter extends RecyclerView.Adapter<AddOrderAdapter.MyViewHolder> implements Filterable {
    public Context mContext;
    public List<CategoryItemModel1> categoryItemModelList;
    public List<CategoryItemModel1> categoryItemModelListFilterable;
    public AddOrderAdapterListener listener;

    LinearLayoutManager linearLayoutManager;
    List<ProductCatModel1> productCatModel1s;
    ChildViewAdapter childViewAdapter;
    List<ChildViewAdapter> childViewAdapterList=new ArrayList<>();


    public AddOrderAdapter(Context mContext, List<CategoryItemModel1> categoryItemModelList)
    {
        this.mContext=mContext;
        this.categoryItemModelList=categoryItemModelList;
        this.categoryItemModelListFilterable=categoryItemModelList;
        this.listener=listener;


    }

    @NonNull
    @Override
    public AddOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddOrderAdapter.MyViewHolder holder, int position) {
        final CategoryItemModel1 categoryItemModel = categoryItemModelListFilterable.get(position);
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
            holder.lv_layout.removeAllViews();
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



//        for(int i=0;i<categoryItemModelListFilterable.get(position).getProducts().size();i++)
//        {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.child_row,null);
//            TextView tv_product_name=view.findViewById(R.id.tv_product_name);
//            EditText et_qty=view.findViewById(R.id.integer_number);
//            TextView tv_wait=view.findViewById(R.id.tv_wait);
//            TextView tv_price=view.findViewById(R.id.price);
//            ImageView p_image=view.findViewById(R.id.product_img);
//            tv_product_name.setText(categoryItemModelListFilterable.get(position).getProducts().get(i).getProduct());
//            tv_wait.setText(categoryItemModelListFilterable.get(position).getProducts().get(i).getSize());
//            //tv_price.setText("₹"+categoryItemModelListFilterable.get(position).getProducts().get(i).getPrice());
//            Glide.with(mContext).load(mContext.getString(R.string.image_url)+categoryItemModelListFilterable.get(position).getProducts().get(i).getProduct_photo()).into(p_image);
//            /*tv_wait.setText("0 KG");
//            tv_price.setText("₹ 0.00");*/
//            int finalI = i;
//            et_qty.addTextChangedListener(new TextWatcher() {
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
//                if (s.toString().trim().length()!=0)
//                {
//                    DecimalFormat decimalFormat=new DecimalFormat("0.00");
//                        //double total_price = Double.parseDouble(categoryItemModelListFilterable.get(position).getProducts().get(finalI).getPrice()) * Double.parseDouble(s.toString());
//                        //double total_weight = Double.parseDouble(categoryItemModelListFilterable.get(position).getProducts().get(finalI).getWeight()) * Double.parseDouble(s.toString());
//                        //categoryItemModelListFilterable.get(position).getProducts().get(finalI).setTotal_price(total_price);
//                        categoryItemModelListFilterable.get(position).getProducts().get(finalI).setTotal_qty(Double.parseDouble("" + s.toString()));
//                        //categoryItemModelListFilterable.get(position).getProducts().get(finalI).setTotal_kg(total_weight);
//                        // integer_number.setText(s.toString());
//                        //tv_price.setText("₹" + decimalFormat.format(total_price));
//                        //tv_wait.setText("" + decimalFormat.format(total_weight) + " KG");
//                        AddOrderActivity.getAddOrderActivity().getProgucteed();
//                }
//                else
//                {
//                    /*double total_price = Double.parseDouble(categoryItemModelListFilterable.get(position).getProducts().get(finalI).getPrice()) * Double.parseDouble(s.toString());
//                    double total_weight = Double.parseDouble(categoryItemModelListFilterable.get(position).getProducts().get(finalI).getWeight()) * Double.parseDouble(s.toString());
//                    categoryItemModelListFilterable.get(position).getProducts().get(finalI).setTotal_price(total_price);
//                    categoryItemModelListFilterable.get(position).getProducts().get(finalI).setTotal_qty(Double.parseDouble("" + s.toString()));
//                    categoryItemModelListFilterable.get(position).getProducts().get(finalI).setTotal_kg(total_weight);*/
//                    // integer_number.setText(s.toString());
//                    et_qty.setText("0");
//                    //tv_price.setText("₹ 0");
//                    //tv_wait.setText("0 KG");
//                    AddOrderActivity.getAddOrderActivity().getProgucteed();
//                }
//            }
//        });

            View view = LayoutInflater.from(mContext).inflate(R.layout.chlid_view, null);
            EditText searchtext =view.findViewById(R.id.search_child);
            RecyclerView recyclerView = view.findViewById(R.id.child_recyclerview);
            linearLayoutManager = new LinearLayoutManager(mContext);
            productCatModel1s= categoryItemModelList.get(position).getProducts();
            childViewAdapter = new ChildViewAdapter((Activity) mContext, productCatModel1s,position);
            childViewAdapterList.add(childViewAdapter);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(childViewAdapter);
            int cat_position=position;

            searchtext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    filter(s.toString(),childViewAdapterList.get(position),position);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
////                    childViewAdapter.getFilter().filter(query);
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
////                    childViewAdapter.getFilter().filter(newText);
//                    filter(newText,childViewAdapterList.get(position),position);
//
//                    Log.d("textchange",newText);
//                    return false;
//                }
//            });

            holder.lv_layout.addView(view);

//        }
    }
    public  void filter(String text,ChildViewAdapter childViewAdapter,int pos){
        List<ProductCatModel1> categoryItemModel1ArrayList=new ArrayList<>();

        int i=0;
        Log.d("ff", String.valueOf(categoryItemModelList.get(pos).getProducts().size()));
        for (ProductCatModel1 productCatModel1 : categoryItemModelList.get(pos).getProducts()){
            if (productCatModel1.getProduct().toLowerCase().contains(text.toLowerCase())){
                categoryItemModel1ArrayList.add(productCatModel1);
            }else {
                Log.d("elseff",productCatModel1.getProduct());

            }
        }
            if (categoryItemModel1ArrayList.isEmpty()){
                Toast.makeText(mContext,"empty",Toast.LENGTH_LONG).show();
                Log.d("empty","empty");
                childViewAdapter.filterList(categoryItemModelList.get(pos).getProducts(),childViewAdapter);
            }
            else {
                childViewAdapter.filterList(categoryItemModel1ArrayList,childViewAdapter);
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
                    List<CategoryItemModel1> filteredList = new ArrayList<>();
                    for (CategoryItemModel1 row : filteredList) {

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
                categoryItemModelListFilterable = (List<CategoryItemModel1>) filterResults.values;
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
