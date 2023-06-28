package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Activity.Chat;
import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.R;

import java.util.ArrayList;

public class SearchAvaliabilityadapter extends RecyclerView.Adapter<SearchAvaliabilityadapter.ViewHolder> {

    ArrayList<Avaliabilitymodel> avaliabilitymodelArrayList=new ArrayList<>();
    Activity context;
    public OnItemClickListener onItemClickListener;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView from,to,cartype;
        public final ImageView call,chat;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            from=(TextView)view.findViewById(R.id.from);
            to=(TextView)view.findViewById(R.id.to);
            cartype=(TextView)view.findViewById(R.id.cartype);
            call=(ImageView)view.findViewById(R.id.call);
            chat=(ImageView)view.findViewById(R.id.chat);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public SearchAvaliabilityadapter(Activity context, ArrayList<Avaliabilitymodel> avaliabilitymodelArrayList) {

        this.context=context;
        this.avaliabilitymodelArrayList=avaliabilitymodelArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_avaliability_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.from.setText(avaliabilitymodelArrayList.get(position).getFrom());
        holder.to.setText(avaliabilitymodelArrayList.get(position).getTo());
        holder.cartype.setText(avaliabilitymodelArrayList.get(position).getCartype());
        if (TextUtils.equals(avaliabilitymodelArrayList.get(position).getCommunication(),"Call"))
        {
            holder.chat.setVisibility(View.GONE);
            holder.call.setVisibility(View.VISIBLE);
            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }else if (TextUtils.equals(avaliabilitymodelArrayList.get(position).getCommunication(),"Chat"))
        {
            holder.chat.setVisibility(View.VISIBLE);
            holder.call.setVisibility(View.GONE);
            holder.chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Chat.class);
                    intent.putExtra("receiver",avaliabilitymodelArrayList.get(position).getDriverid());
                    intent.putExtra("receivername",avaliabilitymodelArrayList.get(position).getDrivername());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return avaliabilitymodelArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

}