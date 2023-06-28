package com.technocometsolutions.salesdriver.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.DailyReportModel;
import com.technocometsolutions.salesdriver.model.ExpensesModel;

import java.util.ArrayList;

public class ViewDailyReportAdapter extends RecyclerView.Adapter<ViewDailyReportAdapter.ViewHolder> {

    Activity context;
    ArrayList<DailyReportModel> dailyReportModelArrayList;
    public ViewExpensesAdapter.OnItemClickListener onItemClickListener;

    public ViewDailyReportAdapter(Activity context, ArrayList<DailyReportModel> dailyReportModelArrayList) {
        this.context = context;
        this.dailyReportModelArrayList = dailyReportModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.daily_report_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (dailyReportModelArrayList.get(position).getCity_name()!=null){
            holder.date.setText(dailyReportModelArrayList.get(position).getD_date());
        }
        if (dailyReportModelArrayList.get(position).getShop_name()!=null){
            holder.shop.setText(dailyReportModelArrayList.get(position).getShop_name());
        }
        if (dailyReportModelArrayList.get(position).getCity_name()!=null){
            holder.city.setText(dailyReportModelArrayList.get(position).getCity_name());
        }


    }

    @Override
    public int getItemCount() {
        return dailyReportModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView city,shop,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            city=itemView.findViewById(R.id.city);
            shop=itemView.findViewById(R.id.shop);
            date=itemView.findViewById(R.id.date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getLayoutPosition(),itemView);
                }
            });
        }
    }
    public void setOnItemClickListener(ViewExpensesAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}
