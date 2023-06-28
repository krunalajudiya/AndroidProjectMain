package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Model.Fraudlistmodel;
import com.example.shreejicabs.R;

import java.util.List;

public class FraudListAdapter extends RecyclerView.Adapter<FraudListAdapter.MyViewHolder> {
    Activity context;
    List<Fraudlistmodel> fraudlistmodellist;

    public FraudListAdapter(Activity context, List<Fraudlistmodel> fraudlistmodellist) {
        this.context = context;
        this.fraudlistmodellist = fraudlistmodellist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.fraudlist_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(fraudlistmodellist.get(position).getName());
        holder.number.setText(fraudlistmodellist.get(position).getMobile());

        Log.d("name",fraudlistmodellist.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return fraudlistmodellist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,number;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.fraudlist_name);
            number=itemView.findViewById(R.id.fraudlist_mobile);

        }
    }
}
