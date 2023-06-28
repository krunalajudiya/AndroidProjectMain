package com.technocometsolutions.salesdriver.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.CatalougeDetailsModel;

import java.util.List;

public class CatalougeAdapter extends RecyclerView.Adapter<CatalougeAdapter.ViewHolder> {

    Activity context;
    List<CatalougeDetailsModel> catalougeDetailsModelList;
    public OnItemClickListener onItemClickListener;

    public CatalougeAdapter(Activity context, List<CatalougeDetailsModel> catalougeDetailsModelList) {
        this.context = context;
        this.catalougeDetailsModelList = catalougeDetailsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.catalouge_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.catalougename.setText(catalougeDetailsModelList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return catalougeDetailsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout catalougelayout;
        TextView catalougename;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catalougelayout=itemView.findViewById(R.id.layoutcatalouge);
            catalougename=itemView.findViewById(R.id.catalougename);

            catalougelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(),v);
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
