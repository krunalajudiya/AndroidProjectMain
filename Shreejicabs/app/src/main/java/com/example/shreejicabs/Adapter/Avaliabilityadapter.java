package com.example.shreejicabs.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shreejicabs.BuildConfig;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Fragment.MyAvaliability;
import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Avaliabilityadapter extends RecyclerView.Adapter<Avaliabilityadapter.ViewHolder> {

    ArrayList<Avaliabilitymodel> avaliabilitymodelArrayList=new ArrayList<>();
    Activity context;
    public OnItemClickListener onItemClickListener;
    public  String status="0",id;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;





    public  class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView from,to,cartype,date;
        public final ToggleButton toggleButton;
        public final ImageView whatsapp;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            from=(TextView)view.findViewById(R.id.from);
            to=(TextView)view.findViewById(R.id.to);
            cartype=(TextView)view.findViewById(R.id.cartype);
            date=(TextView)view.findViewById(R.id.date);
            toggleButton=(ToggleButton)view.findViewById(R.id.status_btn);
            whatsapp=(ImageView)view.findViewById(R.id.whatapp);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });



        }
    }

    public Avaliabilityadapter(Activity context, ArrayList<Avaliabilitymodel> avaliabilitymodelArrayList) {

        this.context=context;
        this.avaliabilitymodelArrayList=avaliabilitymodelArrayList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.availability_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.from.setText(avaliabilitymodelArrayList.get(position).getFrom());
        holder.to.setText(avaliabilitymodelArrayList.get(position).getTo());
        holder.cartype.setText(avaliabilitymodelArrayList.get(position).getCartype());
        holder.date.setText(avaliabilitymodelArrayList.get(position).getDate());
        id=avaliabilitymodelArrayList.get(position).getId();
        if (TextUtils.equals(avaliabilitymodelArrayList.get(position).getStatus(),"1"))
        {
            holder.toggleButton.setChecked(true);
        }
        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    status="1";
                    new Update_status().execute();
                }else{
                    status="0";
                    new Update_status().execute();
                }
            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String shareMessage;
                shareMessage="Taxi Available: "+avaliabilitymodelArrayList.get(position).getFrom()+ " To "+avaliabilitymodelArrayList.get(position).getTo()+
                        "\nDate : "+avaliabilitymodelArrayList.get(position).getDate()+","+avaliabilitymodelArrayList.get(position).getTime()+
                        "\nVehicle : "+avaliabilitymodelArrayList.get(position).getCartype()+"\nComment : "+avaliabilitymodelArrayList.get(position).getComment()+
                        "\n\nPut Your Taxi Availability on TaxiTrip App and Share,absolutely free."+
                        "\n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                intent.setType("text/plain");
                context.startActivity(intent);
            }
        });
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
    }
    class Update_status extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "availablity_status"));
                params.add(new BasicNameValuePair("id",id));
                params.add(new BasicNameValuePair("status", status));

                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));

                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (success==200) {
                Toast.makeText(context,"Status Change SuccessFully",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"Status  Not Change",Toast.LENGTH_LONG).show();
            }

        }
    }



}
