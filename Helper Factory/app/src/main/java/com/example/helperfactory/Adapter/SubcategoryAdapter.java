package com.example.helperfactory.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.Model.Subcategorymodel;
import com.example.helperfactory.R;

import java.util.ArrayList;
import java.util.List;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

    List<Subcategorymodel.SubCategory> subCategoryArrayList=new ArrayList<>();
    Activity context;
    public SubcategoryAdapter.OnItemClickListener onItemClickListener;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView sub_cat_name;
        public final ImageView sub_cat_img;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            sub_cat_img=(ImageView)view.findViewById(R.id.sub_cat_img);
            sub_cat_name=(TextView)view.findViewById(R.id.sub_cat_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public SubcategoryAdapter(Activity context, List<Subcategorymodel.SubCategory> subCategoryArrayList) {

        this.context=context;
        this.subCategoryArrayList=subCategoryArrayList;

    }

    @Override
    public SubcategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_item, parent, false);

        return new SubcategoryAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final SubcategoryAdapter.ViewHolder holder, final int position) {

        holder.sub_cat_name.setText(subCategoryArrayList.get(position).getSub_name());
        Glide.with(context).load(Constant.IMAGE_URL+subCategoryArrayList.get(position).getSub_img()).into(holder.sub_cat_img);



    }

    @Override
    public int getItemCount() {
        return subCategoryArrayList.size();
    }

    public void setOnItemClickListener(SubcategoryAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

}
