package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Model.Chatusermodel;
import com.example.shreejicabs.Model.Packagesmodel;
import com.example.shreejicabs.R;

import java.util.ArrayList;

public class Useradapter  extends RecyclerView.Adapter<Useradapter.ViewHolder> {

        ArrayList<Chatusermodel> chatusermodelArrayList=new ArrayList<>();
        Activity context;
public OnItemClickListener onItemClickListener;



public  class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView user_name;


    public ViewHolder(View view) {
        super(view);
        mView = view;
        user_name=(TextView)view.findViewById(R.id.user_name);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(getAdapterPosition(), v);
            }
        });



    }
}

    public Useradapter(Activity context, ArrayList<Chatusermodel> chatusermodelArrayList) {

        this.context=context;
        this.chatusermodelArrayList=chatusermodelArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

           holder.user_name.setText(chatusermodelArrayList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return chatusermodelArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

}
