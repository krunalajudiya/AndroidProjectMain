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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.R;

import java.util.ArrayList;

public class AvailabletaxiAdapter extends RecyclerView.Adapter<AvailabletaxiAdapter.ViewHolder> {

    public Activity context;
    public ArrayList<Avaliabilitymodel> avaliabilitymodelArrayList;
    private final int REQUEST_PERMISSION = 1001;

    public AvailabletaxiAdapter(Activity context, ArrayList<Avaliabilitymodel> avaliabilitymodelArrayList) {
        this.context = context;
        this.avaliabilitymodelArrayList = avaliabilitymodelArrayList;
    }

    public void updatedata(ArrayList<Avaliabilitymodel> avaliabilitymodelArrayListfilter,AvailabletaxiAdapter availabletaxiAdapter){
        this.avaliabilitymodelArrayList=avaliabilitymodelArrayListfilter;
        Log.d("notify", String.valueOf(avaliabilitymodelArrayListfilter.size()));
        availabletaxiAdapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.avalibletaxi_user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (avaliabilitymodelArrayList.get(position).getDrivernumber()!=null){
            holder.name.setText(avaliabilitymodelArrayList.get(position).getDrivername());
        }
        if (avaliabilitymodelArrayList.get(position).getTime()!=null){
            holder.time.setText(avaliabilitymodelArrayList.get(position).getTime());
        }
        if (avaliabilitymodelArrayList.get(position).getFrom()!=null){
            holder.from.setText(avaliabilitymodelArrayList.get(position).getFrom());
        }
        if (avaliabilitymodelArrayList.get(position).getTo()!=null){
            holder.to.setText(avaliabilitymodelArrayList.get(position).getTo());
        }
        if (avaliabilitymodelArrayList.get(position).getCartype()!=null){
            String cartyp=avaliabilitymodelArrayList.get(position).getCartype();
            holder.cartype.setText(cartyp);
            if (TextUtils.equals(cartyp,"SUV")){
                holder.car_image.setImageResource(R.drawable.suv);
            }
            if (TextUtils.equals(cartyp,"Sedan")){
                holder.car_image.setImageResource(R.drawable.sedan);
            }
            if (TextUtils.equals(cartyp,"Hatchback")){
                holder.car_image.setImageResource(R.drawable.hatchback);
            }
        }
        if (avaliabilitymodelArrayList.get(position).getDate()!=null){
            holder.date.setText(avaliabilitymodelArrayList.get(position).getDate());
        }

        if (TextUtils.equals(avaliabilitymodelArrayList.get(position).getCommunication(),"Chat")){

            holder.whatsapp.setVisibility(View.VISIBLE);
            holder.phone.setVisibility(View.GONE);
            holder.whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(context, Chat.class);
//                    intent.putExtra("receiver",avaliabilitymodelArrayList.get(position).getDriverid());
//                    intent.putExtra("receivername",avaliabilitymodelArrayList.get(position).getDrivername());
//                    context.startActivity(intent);
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
                        intent.setData(Uri.parse("https://wa.me/"+"+91"+avaliabilitymodelArrayList.get(position).getDrivernumber()+"?text="+""));
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context,"whatsapp not install in you phone",Toast.LENGTH_LONG).show();
                    }

                }
            });

        }else if (TextUtils.equals(avaliabilitymodelArrayList.get(position).getCommunication(),"Call")){

            holder.phone.setVisibility(View.VISIBLE);
            holder.whatsapp.setVisibility(View.GONE);
            holder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION);
                    }else {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + avaliabilitymodelArrayList.get(position).getDrivernumber()));
                        context.startActivity(intent);
                    }
                }
            });
        }
//        if (avaliabilitymodelArrayList.size()==position){
//            holder.layoutmain.setBottom(40);
//        }
        //avaliabilitymodelArrayList.get(position).getDate()

    }

    @Override
    public int getItemCount() {
        return avaliabilitymodelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,time,from,to,cartype,date;
        CardView layoutmain;
        ImageView car_image,whatsapp,phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.driver_name);
            time=itemView.findViewById(R.id.time);
            from=itemView.findViewById(R.id.fromcity);
            to=itemView.findViewById(R.id.tocity);
            cartype=itemView.findViewById(R.id.car_type);
            date=itemView.findViewById(R.id.date);
            layoutmain=itemView.findViewById(R.id.layoutmain);
            car_image=itemView.findViewById(R.id.car_image);
            whatsapp=itemView.findViewById(R.id.whatsapp);
            phone=itemView.findViewById(R.id.call);

        }
    }
}
