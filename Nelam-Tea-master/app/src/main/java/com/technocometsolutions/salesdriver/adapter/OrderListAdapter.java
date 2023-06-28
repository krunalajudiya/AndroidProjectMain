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
import com.technocometsolutions.salesdriver.model.ProductModel;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    public List<ProductModel> productModelList;
    public Context mContext;
    int minteger = 1;
    int new_price = 1000;
    int new_price1;
    public OrderListAdapter(Context mContext,List<ProductModel> productModelList) {
        this.mContext = mContext;
        this.productModelList = productModelList;
    }

    @NonNull
    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);

        return new MyViewHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.MyViewHolder holder, int position) {

        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minteger>1)
                {
                    minteger = minteger - 1;
                    holder.integer_number.setText(""+minteger);
                    new_price1 = new_price * minteger;
                    holder.price.setText("$"+new_price1);
                }
                else
                {
                    holder.integer_number.setText(""+1);
                    holder.price.setText("$"+new_price);
                }


            }
        });
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    minteger = minteger + 1;
                    holder.integer_number.setText(""+minteger);
                    new_price1=new_price * minteger;
                //new_price=new_price * minteger;
                    holder.price.setText("$"+new_price1);

            }
        });

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView decrease,increase;
        public TextView integer_number,price;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            decrease=itemView.findViewById(R.id.decrease);
            increase=itemView.findViewById(R.id.increase);
            integer_number=itemView.findViewById(R.id.integer_number);
            price=itemView.findViewById(R.id.price);
        }
    }
}
