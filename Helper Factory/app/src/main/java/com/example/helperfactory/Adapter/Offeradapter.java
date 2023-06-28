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
import com.example.helperfactory.Model.Categorymodel;
import com.example.helperfactory.Model.Offermodel;
import com.example.helperfactory.R;

import java.util.ArrayList;
import java.util.List;

public class Offeradapter extends RecyclerView.Adapter<Offeradapter.ViewHolder> {

    List<Offermodel.Offer> offermodelArrayList=new ArrayList<>();
    Activity context;
    public Offeradapter.OnItemClickListener onItemClickListener;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView o_name,o_desc;
        public final ImageView o_img;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            o_img=(ImageView)view.findViewById(R.id.o_img);
            o_name=(TextView)view.findViewById(R.id.o_name);
            o_desc=(TextView)view.findViewById(R.id.o_desc);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public Offeradapter(Activity context, List<Offermodel.Offer> offermodelArrayList) {

        this.context=context;
        this.offermodelArrayList=offermodelArrayList;

    }

    @Override
    public Offeradapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);

        return new Offeradapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final Offeradapter.ViewHolder holder, final int position) {

        holder.o_name.setText(offermodelArrayList.get(position).getO_name());
        holder.o_desc.setText(offermodelArrayList.get(position).getO_desc());
        Glide.with(context).load(Constant.IMAGE_URL+offermodelArrayList.get(position).getO_img()).into(holder.o_img);

    }

    @Override
    public int getItemCount() {
        return offermodelArrayList.size();
    }

    public void setOnItemClickListener(Offeradapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

}