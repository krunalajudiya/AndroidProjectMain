package com.example.shreejicabs.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Activity.Chat;
import com.example.shreejicabs.Model.Packagesmodel;
import com.example.shreejicabs.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackagelistAdapter extends RecyclerView.Adapter<PackagelistAdapter.ViewHolder>  implements Filterable {

    ArrayList<Packagesmodel> packagesmodelArrayList=new ArrayList<>();
    ArrayList<Packagesmodel> packagesmodelArrayListFiltered=new ArrayList<>();
    Activity context;
    public OnItemClickListener onItemClickListener;
    private static final int REQUEST_PERMISSION = 1001;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView city,rate,cartype,comment,vendername;
        public final ImageView call,chat,car_type_img;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            city=(TextView)view.findViewById(R.id.city);
            rate=(TextView)view.findViewById(R.id.rate);
            cartype=(TextView)view.findViewById(R.id.cartype);
            call=(ImageView)view.findViewById(R.id.call);
            chat=(ImageView)view.findViewById(R.id.chat);
            comment=(TextView) view.findViewById(R.id.comment);
            car_type_img=(ImageView)view.findViewById(R.id.car_type_img);
            vendername=(TextView)view.findViewById(R.id.vendername);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
            vendername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemClickListener.onVendornameClick(getAdapterPosition());
                }
            });

        }
    }


    public PackagelistAdapter(Activity context, ArrayList<Packagesmodel> packagesmodelArrayList) {

        this.context=context;
        this.packagesmodelArrayList=packagesmodelArrayList;
        this.packagesmodelArrayListFiltered=packagesmodelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_package_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (TextUtils.equals(packagesmodelArrayListFiltered.get(position).getCar_type(),"SUV"))
        {
            holder.car_type_img.setImageDrawable(context.getResources().getDrawable(R.drawable.suv));
        }
        if (TextUtils.equals(packagesmodelArrayListFiltered.get(position).getCar_type(),"Sedan"))
        {
            holder.car_type_img.setImageDrawable(context.getResources().getDrawable(R.drawable.sedan));
        }
        if (TextUtils.equals(packagesmodelArrayListFiltered.get(position).getCar_type(),"HatchBack"))
        {
            holder.car_type_img.setImageDrawable(context.getResources().getDrawable(R.drawable.hatchback));
        }
        holder.cartype.setText(packagesmodelArrayListFiltered.get(position).getCar_type());
        holder.city.setText(packagesmodelArrayListFiltered.get(position).getCity());
        holder.rate.setText("\u20A8 "+packagesmodelArrayListFiltered.get(position).getRate());
        holder.comment.setText(packagesmodelArrayListFiltered.get(position).getComment());
        holder.vendername.setText(packagesmodelArrayListFiltered.get(position).getDrivername());
//        Log.d("vendor name",);
        if (TextUtils.equals(packagesmodelArrayListFiltered.get(position).getService(),"One way"))
        {
            holder.city.setVisibility(View.GONE);
        }

        if (TextUtils.equals(packagesmodelArrayListFiltered.get(position).getCommunication(),"Call"))
        {
            holder.chat.setVisibility(View.GONE);
            holder.call.setVisibility(View.VISIBLE);
            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION);
                    }else {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + packagesmodelArrayListFiltered.get(position).getDrivernumber()));
                        context.startActivity(intent);
                    }
                }
            });

        }else if (TextUtils.equals(packagesmodelArrayListFiltered.get(position).getCommunication(),"Chat"))
        {
            holder.chat.setVisibility(View.VISIBLE);
            holder.call.setVisibility(View.GONE);
            holder.chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Chat.class);
                    intent.putExtra("receiver",packagesmodelArrayListFiltered.get(position).getDriverid());
                    intent.putExtra("receivername",packagesmodelArrayListFiltered.get(position).getDrivername());
                    context.startActivity(intent);
                }
            });
        }
        holder.vendername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ff","click");
            }
        });

    }


    @Override
    public int getItemCount() {
        return packagesmodelArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    packagesmodelArrayListFiltered = packagesmodelArrayList;
                } else {
                    ArrayList<Packagesmodel> filteredList = new ArrayList<>();
                    for (Packagesmodel row : packagesmodelArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        List<String> city= Arrays.asList(row.getCity().toLowerCase().split(","));
                        Log.d("stringsize", charString);
                        if (row.getCity().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                            Log.d("string",charString);
                        }
                    }

                    packagesmodelArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = packagesmodelArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                packagesmodelArrayListFiltered = (ArrayList<Packagesmodel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
        void onVendornameClick(int pos);
    }

}
