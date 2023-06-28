package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.PunchInListModel;

import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceAdapter extends RecyclerView.Adapter<ViewAttendanceAdapter.MyHolder> {
    public Context mContext;
    public List<PunchInListModel.Item> viewAttendanceModelList=new ArrayList<>();
    public ViewAttendanceAdapter(Context mContext,List<PunchInListModel.Item> viewAttendanceModelList)
    {
        this.mContext=mContext;
        this.viewAttendanceModelList=viewAttendanceModelList;
    }

    @NonNull
    @Override
    public ViewAttendanceAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_view_attendance, parent, false);

        // Return a new holder instance
         MyHolder viewHolder = new  MyHolder(contactView);
        return viewHolder;

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAttendanceAdapter.MyHolder holder, int position) {
        holder.tv_date.setText(viewAttendanceModelList.get(position).getDate());
        holder.tv_in.setText("In:"+viewAttendanceModelList.get(position).getPunchInTime()+" ");
        holder.tv_out.setText("out:"+viewAttendanceModelList.get(position).getPunchOutTime());
        if (viewAttendanceModelList.get(position).getLeaveType().equalsIgnoreCase("0"))
        {
            holder.tv_a_p_w.setText("P");
            holder.lv_back_color.setBackgroundColor(Color.parseColor("#00ff00"));
        }
        else if (viewAttendanceModelList.get(position).getLeaveType().equalsIgnoreCase("2"))
        {
            holder.tv_a_p_w.setText("W/O");
            holder.lv_back_color.setBackgroundColor(Color.parseColor("#7c4cff"));
        }
        else
        {
            holder.lv_back_color.setBackgroundColor(Color.parseColor("#ff0000"));
            holder.tv_a_p_w.setText("A");
        }
    }
    @Override
    public int getItemCount() {
        return viewAttendanceModelList.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_date,tv_in,tv_out,tv_a_p_w;
        public LinearLayout lv_back_color;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_date=(TextView)itemView.findViewById(R.id.tv_date);
            tv_in=(TextView)itemView.findViewById(R.id.tv_in);
            tv_out=(TextView)itemView.findViewById(R.id.tv_out);
            tv_a_p_w=(TextView)itemView.findViewById(R.id.tv_a_p_w);
            lv_back_color=(LinearLayout) itemView.findViewById(R.id.lv_back_color);
        }
    }
}