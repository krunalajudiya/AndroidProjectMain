package com.example.shreejicabs.User.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Activity.Chat;
import com.example.shreejicabs.Model.Packagesmodel;
import com.example.shreejicabs.R;

import java.util.ArrayList;

public class AllServiceAdapter extends RecyclerView.Adapter<AllServiceAdapter.ViewHolder> {

    Activity context;
    ArrayList<Packagesmodel> packagesmodelArrayList;
    ArrayList<Packagesmodel> packagesmodelArrayListfilter;
    private final int REQUEST_PERMISSION = 1001;

    public AllServiceAdapter(Activity context, ArrayList<Packagesmodel> packagesmodelArrayList) {
        this.context = context;
        this.packagesmodelArrayList = packagesmodelArrayList;
        packagesmodelArrayListfilter=packagesmodelArrayList;
    }

    public void filterdata(ArrayList<Packagesmodel> packagesmodelArrayListfilter){
        this.packagesmodelArrayListfilter=packagesmodelArrayListfilter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.allservice_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (packagesmodelArrayListfilter.get(position).getService()!=null){
            holder.servicetype.setText(packagesmodelArrayListfilter.get(position).getService());
        }
        if (packagesmodelArrayListfilter.get(position).getCar_type()!=null){
            String cartyp=packagesmodelArrayListfilter.get(position).getCar_type();
            holder.cartype.setText(cartyp);
            if (TextUtils.equals(cartyp,"SUV")){
                holder.car_image.setImageResource(R.drawable.suv);
            }
            if (TextUtils.equals(cartyp,"Sedan"))
            {
                holder.car_image.setImageResource(R.drawable.sedan);
            }
            if (TextUtils.equals(cartyp,"HatchBack"))
            {
                holder.car_image.setImageResource(R.drawable.hatchback);
            }
        }
        if (packagesmodelArrayListfilter.get(position).getCity()!=null){
            holder.city.setText(packagesmodelArrayListfilter.get(position).getCity());
        }
        if (packagesmodelArrayListfilter.get(position).getRate()!=null){
            holder.rate.setText(packagesmodelArrayListfilter.get(position).getRate());
        }
        if (packagesmodelArrayListfilter.get(position).getDrivername()!=null){
            holder.name.setText(packagesmodelArrayListfilter.get(position).getDrivername());
        }
        if (packagesmodelArrayListfilter.get(position).getCommunication()!=null){
            if (TextUtils.equals(packagesmodelArrayListfilter.get(position).getCommunication(),"Call")){
                holder.whatsapp.setVisibility(View.GONE);
                holder.phone.setVisibility(View.VISIBLE);
                holder.phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION);
                        }else {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + packagesmodelArrayListfilter.get(position).getDrivernumber()));
                            context.startActivity(intent);
                        }
                    }
                });
            }else if (TextUtils.equals(packagesmodelArrayListfilter.get(position).getCommunication(),"Chat"))
            {
                holder.whatsapp.setVisibility(View.VISIBLE);
                holder.phone.setVisibility(View.GONE);
                holder.whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PackageManager packageManager=context.getPackageManager();
                        boolean app_installed=false;
                        String url = null;
                        Boolean first=true;
                        if (first){
                            try{
                                packageManager.getPackageInfo("com.whatsapp.w4b",PackageManager.GET_PERMISSIONS);
                                app_installed=true;
                                url="com.whatsapp.w4b";
                                first=false;
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                                app_installed=false;
                            }
                        }
                        if (first){
                            try{
                                packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_PERMISSIONS);
                                app_installed=true;
                                url="com.whatsapp";
                                first=false;
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                                app_installed=false;
                                first=false;
                            }
                        }


                        if (app_installed){
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setPackage(url);
                            intent.setData(Uri.parse("https://wa.me/"+"+91"+packagesmodelArrayListfilter.get(position).getDrivernumber()+"?text="+""));
                            context.startActivity(intent);
                        }else {
                            Toast.makeText(context,"whatsapp not install in you phone",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else {
                holder.whatsapp.setVisibility(View.GONE);
                holder.phone.setVisibility(View.GONE);
                Log.d("enter wrong","wronge");
                Log.d("communicaiton falut",packagesmodelArrayListfilter.get(position).getCommunication());
            }
        }


    }

    @Override
    public int getItemCount() {
        return packagesmodelArrayListfilter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView servicetype,cartype,city,rate,name;
        ImageView car_image,whatsapp,phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            servicetype=itemView.findViewById(R.id.servicetype);
            cartype=itemView.findViewById(R.id.cartype);
            city=itemView.findViewById(R.id.city);
            rate=itemView.findViewById(R.id.rate);
            name=itemView.findViewById(R.id.name);
            car_image=itemView.findViewById(R.id.car_image);
            whatsapp=itemView.findViewById(R.id.whatsapp);
            phone=itemView.findViewById(R.id.call);

        }
    }
}
