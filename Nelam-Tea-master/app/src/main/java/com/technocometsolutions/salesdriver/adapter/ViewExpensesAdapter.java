package com.technocometsolutions.salesdriver.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.ExpensesDetail;
import com.technocometsolutions.salesdriver.model.ExpensesModel;

import java.util.ArrayList;

public class ViewExpensesAdapter extends RecyclerView.Adapter<ViewExpensesAdapter.ViewHolder> {

    Activity context;
    ArrayList<ExpensesModel> ExpensesModelArrayList;
    public OnItemClickListener onItemClickListener;


    public ViewExpensesAdapter(Activity context, ArrayList<ExpensesModel> ExpensesModelArrayList) {
        this.context = context;
        this.ExpensesModelArrayList = ExpensesModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.view_expenses_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.from.setText(ExpensesModelArrayList.get(position).getE_from());
        holder.to.setText(ExpensesModelArrayList.get(position).getE_to());
        holder.date.setText(ExpensesModelArrayList.get(position).getE_date());
        holder.distance.setText(ExpensesModelArrayList.get(position).getDistance());
        Log.d("fffxxff",ExpensesModelArrayList.get(0).getE_to());

    }

    @Override
    public int getItemCount() {
        return ExpensesModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView from,to,date,distance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            from=itemView.findViewById(R.id.from);
            to=itemView.findViewById(R.id.to);
            date=itemView.findViewById(R.id.date);
            distance=itemView.findViewById(R.id.distance);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getLayoutPosition(),itemView);
                }
            });
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}
