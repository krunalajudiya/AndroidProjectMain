package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.AttendanceManagementModel;

import java.util.List;

public class AddAttendanceAdapter extends RecyclerView.Adapter<AddAttendanceAdapter.MyHolder> {
    List<AttendanceManagementModel.Item> attendanceMangementModelList;
    Context mContext;

    public AddAttendanceAdapter(Context context, List<AttendanceManagementModel.Item> attendanceMangementModelList)
    {
        this.mContext=context;
        this.attendanceMangementModelList=attendanceMangementModelList;
    }

    @NonNull
    @Override
    public AddAttendanceAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_addattendance_in_out, parent, false);

        // Return a new holder instance
        MyHolder viewHolder = new MyHolder(contactView);
        return viewHolder;


        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AddAttendanceAdapter.MyHolder holder, int position) {
        if (position==0)
        {
            holder.iv_in_out.setBackgroundResource(R.drawable.cerclebackgroundgreen);
            holder.tv_in_out.setText("Punch In:"+attendanceMangementModelList.get(position).getPunchInDate()+" "+attendanceMangementModelList.get(position).getPunchInTime());
        }
        else
        {
            holder.iv_in_out.setBackgroundResource(R.drawable.cerclebackgroundred);
            holder.tv_in_out.setText("Punch Out:"+attendanceMangementModelList.get(position).getPunchOutDate()+" "+attendanceMangementModelList.get(position).getPunchOutTime());
        }



    }

    @Override
    public int getItemCount() {
        return attendanceMangementModelList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public ImageView iv_in_out;
        public TextView tv_in_out;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            iv_in_out=(ImageView)itemView.findViewById(R.id.iv_in_out);
            tv_in_out=(TextView) itemView.findViewById(R.id.tv_in_out);
        }
    }
}
