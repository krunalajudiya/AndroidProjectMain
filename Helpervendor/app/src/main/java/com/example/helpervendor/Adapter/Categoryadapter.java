package com.example.helpervendor.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.helpervendor.Model.Categorymodel;
import com.example.helpervendor.Model.Ongoingmodel;
import com.example.helpervendor.R;

import java.util.ArrayList;
import java.util.List;

public class Categoryadapter extends RecyclerView.Adapter<Categoryadapter.ViewHolder> {

    List<Categorymodel.Category> categoryArrayList=new ArrayList<>();
    Activity context;
    public Categoryadapter.OnItemClickListener onItemClickListener;
    TextView user_name,user_number;
    ImageView close;
    int selectedItem = 0;


    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView cat_name;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            cat_name=(TextView) view.findViewById(R.id.cat_name);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItem = getAdapterPosition();
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                    notifyDataSetChanged();
                }
            });



        }
    }

    public Categoryadapter(Activity context, List<Categorymodel.Category> categoryArrayList) {

        this.context=context;
        this.categoryArrayList=categoryArrayList;

    }

    @Override
    public Categoryadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        return new Categoryadapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final Categoryadapter.ViewHolder holder, final int position) {
        if (position==selectedItem) {
            holder.cat_name.setBackgroundResource(R.drawable.active_cat_item);
            holder.cat_name.setText(categoryArrayList.get(position).getC_name());
            holder.cat_name.setTextColor(Color.WHITE);

        }
        else{
            holder.cat_name.setBackgroundResource(R.drawable.notactive_cat_item);
            holder.cat_name.setText(categoryArrayList.get(position).getC_name());
            holder.cat_name.setTextColor(Color.BLACK);
        }


    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public void setOnItemClickListener(Categoryadapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }


}