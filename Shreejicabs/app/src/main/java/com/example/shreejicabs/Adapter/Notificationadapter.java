package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Model.Notificationmodel;
import com.example.shreejicabs.R;

import java.util.List;

public class Notificationadapter extends RecyclerView.Adapter<Notificationadapter.ViewHolder> {

    Activity context;
    List<Notificationmodel> notificationmodelList;

    public Notificationadapter(Activity context, List<Notificationmodel> notificationmodelList) {
        this.context = context;
        this.notificationmodelList = notificationmodelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(notificationmodelList.get(position).getDate());
        holder.title.setText(notificationmodelList.get(position).getTitle());
        holder.message.setText(notificationmodelList.get(position).getMessage());

        //Log.d("date",notificationmodelList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return notificationmodelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,title,message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.date);
            title=itemView.findViewById(R.id.title);
            message=itemView.findViewById(R.id.message);
        }
    }
}
