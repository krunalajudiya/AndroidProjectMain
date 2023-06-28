package com.example.helperfactory.Adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helperfactory.Constant.Constant;
import com.example.helperfactory.Model.Categorymodel;
import com.example.helperfactory.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<Categorymodel.Category> categorymodelArrayList=new ArrayList<>();
    Activity context;
    public CategoryAdapter.OnItemClickListener onItemClickListener;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView cat_name;
        public final ImageView cat_img;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            cat_img=(ImageView)view.findViewById(R.id.cat_img);
            cat_name=(TextView)view.findViewById(R.id.cat_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public CategoryAdapter(Activity context, List<Categorymodel.Category> avaliabilitymodelArrayList) {

        this.context=context;
        this.categorymodelArrayList=avaliabilitymodelArrayList;

    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        return new CategoryAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final CategoryAdapter.ViewHolder holder, final int position) {

        holder.cat_name.setText(Html.fromHtml(categorymodelArrayList.get(position).getC_name()));

        Glide.with(context).load(Constant.IMAGE_URL+categorymodelArrayList.get(position).getC_img()).into(holder.cat_img);



    }

    @Override
    public int getItemCount() {
        return categorymodelArrayList.size();
    }

    public void setOnItemClickListener(CategoryAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

}
