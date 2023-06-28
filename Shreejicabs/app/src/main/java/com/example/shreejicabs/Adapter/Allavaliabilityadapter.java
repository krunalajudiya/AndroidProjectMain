package com.example.shreejicabs.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.Activity.Chat;
import com.example.shreejicabs.BuildConfig;
import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Allavaliabilityadapter extends RecyclerView.Adapter<Allavaliabilityadapter.ViewHolder> {

    ArrayList<Avaliabilitymodel> avaliabilitymodelArrayList=new ArrayList<>();
    Activity context;
    public OnItemClickListener onItemClickListener;
    private static final int REQUEST_PERMISSION = 1001;



    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView from,to,cartype,comment,date,driver_name,posttime;
        public final ImageButton call,chat,share;
        public final LinearLayout commentlayout;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            from=(TextView)view.findViewById(R.id.from);
            to=(TextView)view.findViewById(R.id.to);
            cartype=(TextView)view.findViewById(R.id.car_type);
            comment=(TextView)view.findViewById(R.id.comment);
            date=(TextView)view.findViewById(R.id.dateandtime);
            driver_name=(TextView)view.findViewById(R.id.driver_name);
            call=(ImageButton) view.findViewById(R.id.call);
            chat=(ImageButton) view.findViewById(R.id.chat);
            share=(ImageButton) view.findViewById(R.id.share);
            commentlayout=(LinearLayout)view.findViewById(R.id.commentlayout);
            posttime=(TextView)view.findViewById(R.id.posttime);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
            driver_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onDriverNameClick(getAdapterPosition());
                }
            });
            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onChatClick(getAdapterPosition());
                }
            });
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onCallClick(getAdapterPosition());
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onShareClick(getAdapterPosition());
                }
            });




        }
    }

    public Allavaliabilityadapter(Activity context, ArrayList<Avaliabilitymodel> avaliabilitymodelArrayList) {

        this.context=context;
        this.avaliabilitymodelArrayList=avaliabilitymodelArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_availability_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        Calendar cl=Calendar.getInstance();
//        int hour=cl.get(Calendar.HOUR_OF_DAY);
//        int min=cl.get(Calendar.MINUTE);
//        String currenttime=timeset(hour,min);
//        String date=avaliabilitymodelArrayList.get(position).getDate();
//
//        if (currenttime.compareTo(date)!=0){

            holder.from.setText(avaliabilitymodelArrayList.get(position).getFrom());
            holder.to.setText(avaliabilitymodelArrayList.get(position).getTo());
            holder.cartype.setText(avaliabilitymodelArrayList.get(position).getCartype());
            holder.comment.setText(avaliabilitymodelArrayList.get(position).getComment());
            if (TextUtils.isEmpty(avaliabilitymodelArrayList.get(position).getComment()))
            {
                holder.commentlayout.setVisibility(View.GONE);
            }
            holder.driver_name.setText(avaliabilitymodelArrayList.get(position).getDrivername());
            holder.date.setText(avaliabilitymodelArrayList.get(position).getDate()+","+avaliabilitymodelArrayList.get(position).getTime());
            if (TextUtils.equals(avaliabilitymodelArrayList.get(position).getCommunication(),"Call"))
            {
                holder.chat.setVisibility(View.GONE);
                holder.call.setVisibility(View.VISIBLE);

//                holder.call.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
//                        {
//                            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION);
//                        }else {
//                            Intent intent = new Intent(Intent.ACTION_DIAL);
//                            intent.setData(Uri.parse("tel:" + avaliabilitymodelArrayList.get(position).getDrivernumber()));
//                            context.startActivity(intent);
//                        }
//
//                    }
//                });

            }else if (TextUtils.equals(avaliabilitymodelArrayList.get(position).getCommunication(),"Chat"))
            {
                holder.chat.setVisibility(View.VISIBLE);
                holder.call.setVisibility(View.GONE);
//                holder.chat.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                    Intent intent=new Intent(context, Chat.class);
////                    intent.putExtra("receiver",avaliabilitymodelArrayList.get(position).getDriverid());
////                    intent.putExtra("receivername",avaliabilitymodelArrayList.get(position).getDrivername());
////                    context.startActivity(intent);
//                        PackageManager packageManager=context.getPackageManager();
//                        boolean app_installed=false;
//                        String url = null;
//                        Boolean first=true;
//                        if (first){
//                            try{
//                                packageManager.getPackageInfo("com.whatsapp.w4b",PackageManager.GET_PERMISSIONS);
//                                app_installed=true;
//                                url="com.whatsapp.w4b";
//                                first=false;
//                            } catch (PackageManager.NameNotFoundException e) {
//                                e.printStackTrace();
//                                app_installed=false;
//                            }
//                        }
//                        if (first){
//                            try{
//                                packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_PERMISSIONS);
//                                app_installed=true;
//                                url="com.whatsapp";
//                                first=false;
//                            } catch (PackageManager.NameNotFoundException e) {
//                                e.printStackTrace();
//                                app_installed=false;
//                                first=false;
//                            }
//                        }
//
//
//                        if (app_installed){
//                            Intent intent=new Intent(Intent.ACTION_VIEW);
//                            intent.setPackage(url);
//                            intent.setData(Uri.parse("https://wa.me/"+"+91"+avaliabilitymodelArrayList.get(position).getDrivernumber()+"?text="+""));
//                            context.startActivity(intent);
//                        }else {
//                            Toast.makeText(context,"whatsapp not install in you phone",Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                });
            }
//            holder.share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    String shareMessage;
//                    shareMessage="Taxi Available: "+avaliabilitymodelArrayList.get(position).getFrom()+ " To "+avaliabilitymodelArrayList.get(position).getTo()+
//                            "\nDate : "+avaliabilitymodelArrayList.get(position).getDate()+","+avaliabilitymodelArrayList.get(position).getTime()+
//                            "\nVehicle : "+avaliabilitymodelArrayList.get(position).getCartype()+"\nComment : "+avaliabilitymodelArrayList.get(position).getComment()+
//                            "\n\nPut Your Taxi Availability on TaxiTrip App and Share,absolutely free."+
//                            "\n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
//                    intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
//                    intent.setType("text/plain");
//                    context.startActivity(intent);
//                }
//            });
            PrettyTime p = new PrettyTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Log.d("post time",avaliabilitymodelArrayList.get(position).getCreate_at());
            Date mDate = null;
            try {
                mDate = sdf.parse(avaliabilitymodelArrayList.get(position).getCreate_at());


            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.posttime.setText(p.format(mDate));
            //String posttime=avaliabilitymodelArrayList.get(position).getCreate_at();
            //Log.d("fftime", String.valueOf(posttime));
//        }

    }

    @Override
    public int getItemCount() {
        return avaliabilitymodelArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
        void onDriverNameClick(int pos);
        void onShareClick(int pos);
        void onCallClick(int pos);
        void onChatClick(int pos);
    }

    public String timeset(int hour,int minute){
        String strhr="";
        String strmn="";
        String ampm="";
        int hr=hour;
        int mn=minute;
        if(hr<12){
            ampm="AM";
        }
        else {
            ampm="PM";
        }
        if (hr==0){

            strhr="12";

        }else {
            if (hr>12){
                hr-=12;
                if (hr>=10){
                    strhr=Integer.toString(hr);
                }
                else {
                    strhr="0"+Integer.toString(hr);
                }
                Log.d("ffhr", String.valueOf(hr));
            }else {
                if (hr>=10){
                    strhr=Integer.toString(hr);
                }
                else {
                    strhr="0"+Integer.toString(hr);
                }
            }
        }

        if (mn>=10){
            strmn=Integer.toString(mn);
        }
        else {
            strmn="0"+Integer.toString(mn);
        }

        Log.d("fftime",strhr+":"+strmn+" "+ampm);
        return strhr+":"+strmn+" "+ampm;
    }

//    String PostTime(String givenDateString){
//
//        if (givenDateString.equalsIgnoreCase("")) {
//            return "";
//        }
//
//        long timeInMilliseconds=0;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        try {
//
//            Date mDate = sdf.parse(givenDateString);
//            timeInMilliseconds = mDate.getTime();
//            System.out.println("Date in milli :: " + timeInMilliseconds);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        String result = "now";
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//
//        String todayDate = formatter.format(new Date());
//        Calendar calendar = Calendar.getInstance();
//
//        long dayagolong =  timeInMilliseconds;
//        calendar.setTimeInMillis(dayagolong);
//        String agoformater = formatter.format(calendar.getTime());
//
//        Date CurrentDate = null;
//        Date CreateDate = null;
//
//        try {
//            CurrentDate = formatter.parse(todayDate);
//            CreateDate = formatter.parse(agoformater);
//
//            long different = Math.abs(CurrentDate.getTime() - CreateDate.getTime());
//
//            long secondsInMilli = 1000;
//            long minutesInMilli = secondsInMilli * 60;
//            long hoursInMilli = minutesInMilli * 60;
//            long daysInMilli = hoursInMilli * 24;
//
//            long elapsedDays = different / daysInMilli;
//            different = different % daysInMilli;
//
//            long elapsedHours = different / hoursInMilli;
//            different = different % hoursInMilli;
//
//            long elapsedMinutes = different / minutesInMilli;
//            different = different % minutesInMilli;
//
//            long elapsedSeconds = different / secondsInMilli;
//
//            different = different % secondsInMilli;
//            if (elapsedDays == 0) {
//                if (elapsedHours == 0) {
//                    if (elapsedMinutes == 0) {
//                        if (elapsedSeconds < 0) {
//                            return "0" + " s";
//                        } else {
//                            if (elapsedDays > 0 && elapsedSeconds < 59) {
//                                return "now";
//                            }
//                        }
//                    } else {
//                        return String.valueOf(elapsedMinutes) + "mins ago";
//                    }
//                } else {
//                    return String.valueOf(elapsedHours) + "hr ago";
//                }
//
//            } else {
//                if (elapsedDays <= 29) {
//                    return String.valueOf(elapsedDays) + "d ago";
//
//                }
//                else if (elapsedDays > 29 && elapsedDays <= 58) {
//                    return "1Mth ago";
//                }
//                if (elapsedDays > 58 && elapsedDays <= 87) {
//                    return "2Mth ago";
//                }
//                if (elapsedDays > 87 && elapsedDays <= 116) {
//                    return "3Mth ago";
//                }
//                if (elapsedDays > 116 && elapsedDays <= 145) {
//                    return "4Mth ago";
//                }
//                if (elapsedDays > 145 && elapsedDays <= 174) {
//                    return "5Mth ago";
//                }
//                if (elapsedDays > 174 && elapsedDays <= 203) {
//                    return "6Mth ago";
//                }
//                if (elapsedDays > 203 && elapsedDays <= 232) {
//                    return "7Mth ago";
//                }
//                if (elapsedDays > 232 && elapsedDays <= 261) {
//                    return "8Mth ago";
//                }
//                if (elapsedDays > 261 && elapsedDays <= 290) {
//                    return "9Mth ago";
//                }
//                if (elapsedDays > 290 && elapsedDays <= 319) {
//                    return "10Mth ago";
//                }
//                if (elapsedDays > 319 && elapsedDays <= 348) {
//                    return "11Mth ago";
//                }
//                if (elapsedDays > 348 && elapsedDays <= 360) {
//                    return "12Mth ago";
//                }
//
//                if (elapsedDays > 360 && elapsedDays <= 720) {
//                    return "1 year ago";
//                }
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

}
