package com.example.shreejicabs.User.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.shreejicabs.Adapter.PackagelistAdapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Packagesmodel;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.User.Adapter.AllServiceAdapter;
import com.example.shreejicabs.User.Utlity.LoadingView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class all_Service extends Fragment {

    SearchableSpinner city_spinner;
    LoadingView loadingView;
    private static final String TAG_SUCCESS = "error";
    JSONParser jParser=new JSONParser();
    ArrayList<String> city;
    int success;
    RecyclerView servicerecyclerview;
    //ArrayList<Packagesmodel> packagesmodelArrayList=new ArrayList<>();
    ArrayList<Packagesmodel> packagesfiltermodelArrayList=new ArrayList<>();
    PackagelistAdapter packagelistAdapter;
    LinearLayoutManager linearLayout;
    String service_type="Airport Transfer";
    AllServiceAdapter allServiceAdapter;
    ArrayList<Packagesmodel> packagesmodelArrayList=new ArrayList<>();
    ImageView filter;
    String currentcity="Ahmedabad";
    CheckBox suv,sedan,hatchback,airporttransfar,roundtrip,local8hr,local4hr,local12hr;
    String suv_str,sedan_str,hatchback_str,airporttransfar_str,roundtrip_str,local8hr_str,local4hr_str,local12hr_str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all__service, container, false);



        city_spinner=view.findViewById(R.id.city_spinner);
        servicerecyclerview=view.findViewById(R.id.service);
        filter=view.findViewById(R.id.filter);
        linearLayout=new LinearLayoutManager(getActivity());
        city=new ArrayList<>();



//        Dialog dialog=new Dialog(getActivity());
//        dialog.setContentView(R.layout.filter_dialogview);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogview=LayoutInflater.from(getActivity()).inflate(R.layout.filter_dialogview,viewGroup,false);
        builder.setView(dialogview);
        final AlertDialog alertDialog=builder.create();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                Button filter=alertDialog.findViewById(R.id.filter);
                suv=alertDialog.findViewById(R.id.suv);
                hatchback=alertDialog.findViewById(R.id.hatchback);
                sedan=alertDialog.findViewById(R.id.sedan);
                airporttransfar=alertDialog.findViewById(R.id.airporttransfer);
                roundtrip=alertDialog.findViewById(R.id.roundtrip);
                local8hr=alertDialog.findViewById(R.id.local8hr);
                local12hr=alertDialog.findViewById(R.id.local12hr);
                local4hr=alertDialog.findViewById(R.id.local4hr);

//                if (suv_str!=null){ suv.setChecked(true); }
//                if (hatchback_str!=null){hatchback.setChecked(true);}
//                if (sedan_str!=null){sedan.setChecked(true);}
//                if (airporttransfar_str!=null){airporttransfar.setChecked(true);}
//                if (roundtrip_str!=null){airporttransfar.setChecked(true);}
//                if (local8hr_str!=null){local8hr.setChecked(true);}
//                if (local12hr_str!=null){local8hr.setChecked(true);}
//                if (local4hr_str!=null){ local4hr.setChecked(true);}

                filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (suv.isChecked()){ suv_str="SUV"; }
                        else { suv_str=null; }

                        if (sedan.isChecked()){ sedan_str="Sedan"; }
                        else{ sedan_str=null; }

                        if (hatchback.isChecked()){ hatchback_str="HatchBack"; }
                        else { hatchback_str=null; }

                        if (airporttransfar.isChecked()){ airporttransfar_str="Airport Transfer"; }
                        else {airporttransfar_str=null;}

                        if (roundtrip.isChecked()){ roundtrip_str="Round Trip"; }
                        else {roundtrip_str=null;}

                        if (local4hr.isChecked()){ local4hr_str="Local 4hr/40km"; }
                        else {local4hr_str=null;}

                        if (local8hr.isChecked()){ local8hr_str="Local 8hr/80km"; }
                        else {local8hr_str=null;}

                        if (local12hr.isChecked()){ local12hr_str="Local 12hr/120km"; }
                        else {local12hr_str=null;}

                        get_filterdata();
                        alertDialog.hide();
                        //alertDialog.dismiss();

                        Log.d("service_checkd",suv_str+" "+sedan_str+" "+hatchback_str+" "+airporttransfar_str+" "+roundtrip_str+" "+local4hr_str+" "+local8hr_str+" "+local12hr_str);
                    }
                });
            }
        });


        new Loadcity().execute();
        new LoadService().execute();


        return view;
    }

    class Loadcity extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingView=new LoadingView(getActivity());
            loadingView.ShowLoadingView();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", "carservicecity"));
            JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try{

                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    JSONArray products = c1.getJSONArray("city");
                    // looping through All Products
                    city.add("Select City");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String city_name = c.getString("city_name");

                        city.add(city_name);

                    }



                } else {


                }


            }catch (Exception e){
                Log.d("exception", String.valueOf(e));
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //loadingView.HideLoadingView();


            if (success==200){
                ArrayAdapter cityadapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,city);

                city_spinner.setAdapter(cityadapter);
                city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position!=0){
                            currentcity=city.get(position);
                            filtercity();
                            get_filterdata();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }



        }
    }

    class LoadService extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            //loadingView.ShowLoadingView();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "serch_servicecity"));
                params.add(new BasicNameValuePair("service_type", service_type));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    JSONArray products = c1.getJSONArray("servicecity");
                    // looping through All Products

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String id    = c.getString("service_id");
                        String packge_name = c.getString("Packge_name");
                        String city = c.getString("city");
                        String rate = c.getString("Rate");
                        JSONObject user_detail=c.getJSONObject("user_detail");
                        String driver_id=user_detail.getString("Reg_id");
                        String driver_name=user_detail.getString("Name");
                        String driver_number=user_detail.getString("Mobile_no");
                        String car_type = c.getString("Car_type");
                        String service = c.getString("Service_type");
                        String comment = c.getString("Comment");
                        String communication = c.getString("Communication");


                        packagesmodelArrayList.add(new Packagesmodel(id,packge_name,service,city,car_type,rate,comment,communication,driver_id,driver_name,driver_number));

                    }



                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            loadingView.HideLoadingView();
            if (success==200) {

                //ArrayList<Packagesmodel> packagesfiltermodelArrayList=new ArrayList<>();

                filtercity();
//                //nopackage.setVisibility(View.GONE);
//                if (getActivity()!=null) {
//                    packagelistAdapter = new PackagelistAdapter(getActivity(), packagesfiltermodelArrayList);
//                    servicerecyclerview.setLayoutManager(linearLayout);
//                    servicerecyclerview.setAdapter(packagelistAdapter);
//                    packagelistAdapter.setOnItemClickListener(new PackagelistAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(int pos, View v) {
//
//                        }
//
//                        @Override
//                        public void onVendornameClick(int pos) {
//
//                        }
//                    });
//                }

                allServiceAdapter=new AllServiceAdapter(getActivity(),packagesfiltermodelArrayList);
                servicerecyclerview.setLayoutManager(linearLayout);
                servicerecyclerview.setAdapter(allServiceAdapter);


            }
            else{
                //nopackage.setVisibility(View.VISIBLE);
            }

        }
    }

    public void filtercity(){
        packagesfiltermodelArrayList.clear();
        for (int i=0;i<packagesmodelArrayList.size();i++)
        {
            List<String> city_ar= Arrays.asList(packagesmodelArrayList.get(i).getCity().split(","));

            for (int j=0;j<city_ar.size();j++)
            {
                if (TextUtils.equals(city_ar.get(j),currentcity)){
                    packagesfiltermodelArrayList.add(new Packagesmodel(packagesmodelArrayList.get(i).getP_id(),packagesmodelArrayList.get(i).getPackagename(),packagesmodelArrayList.get(i).getService(),city_ar.get(j),packagesmodelArrayList.get(i).getCar_type(),packagesmodelArrayList.get(i).getRate(),packagesmodelArrayList.get(i).getComment(),packagesmodelArrayList.get(i).getCommunication(),packagesmodelArrayList.get(i).getDriverid(),packagesmodelArrayList.get(i).getDrivername(),packagesmodelArrayList.get(i).getDrivernumber()));

                }
            }
        }
    }
    public void get_filterdata(){

        ArrayList<Packagesmodel> packagesmodelArrayListtemp=packagesfiltermodelArrayList;
        ArrayList<Packagesmodel> packagesmodelArrayListtempfilter=new ArrayList<>();
        if (suv_str!=null){
            Log.d("size", String.valueOf(packagesmodelArrayListtemp.size()));
            for (int i=0;i<packagesmodelArrayListtemp.size();i++){
                if (TextUtils.equals(suv_str,packagesmodelArrayListtemp.get(i).getCar_type())){

                    packagesmodelArrayListtempfilter.add(new Packagesmodel(packagesmodelArrayListtemp.get(i).getP_id(),packagesmodelArrayListtemp.get(i).getPackagename(),packagesmodelArrayListtemp.get(i).getService(),packagesmodelArrayListtemp.get(i).getCity(),packagesmodelArrayListtemp.get(i).getCar_type(),packagesmodelArrayListtemp.get(i).getRate(),packagesmodelArrayListtemp.get(i).getComment(),packagesmodelArrayListtemp.get(i).getCommunication(),packagesmodelArrayListtemp.get(i).getDriverid(),packagesmodelArrayListtemp.get(i).getDrivername(),packagesmodelArrayListtemp.get(i).getDrivernumber()));

                    Log.d("ff",packagesmodelArrayListtemp.get(i).getCar_type());

                }
            }


        }
        if (sedan_str!=null){
            Log.d("size", String.valueOf(packagesmodelArrayListtemp.size()));
            for (int i=0;i<packagesmodelArrayListtemp.size();i++){
                if (TextUtils.equals(sedan_str,packagesmodelArrayListtemp.get(i).getCar_type())){

                    packagesmodelArrayListtempfilter.add(new Packagesmodel(packagesmodelArrayListtemp.get(i).getP_id(),packagesmodelArrayListtemp.get(i).getPackagename(),packagesmodelArrayListtemp.get(i).getService(),packagesmodelArrayListtemp.get(i).getCity(),packagesmodelArrayListtemp.get(i).getCar_type(),packagesmodelArrayListtemp.get(i).getRate(),packagesmodelArrayListtemp.get(i).getComment(),packagesmodelArrayListtemp.get(i).getCommunication(),packagesmodelArrayListtemp.get(i).getDriverid(),packagesmodelArrayListtemp.get(i).getDrivername(),packagesmodelArrayListtemp.get(i).getDrivernumber()));

                    Log.d("ff",packagesmodelArrayListtemp.get(i).getCar_type());

                }
            }


        }
        if (hatchback_str!=null){
            Log.d("size", String.valueOf(packagesmodelArrayListtemp.size()));
            for (int i=0;i<packagesmodelArrayListtemp.size();i++){
                if (TextUtils.equals(hatchback_str,packagesmodelArrayListtemp.get(i).getCar_type())){

                    packagesmodelArrayListtempfilter.add(new Packagesmodel(packagesmodelArrayListtemp.get(i).getP_id(),packagesmodelArrayListtemp.get(i).getPackagename(),packagesmodelArrayListtemp.get(i).getService(),packagesmodelArrayListtemp.get(i).getCity(),packagesmodelArrayListtemp.get(i).getCar_type(),packagesmodelArrayListtemp.get(i).getRate(),packagesmodelArrayListtemp.get(i).getComment(),packagesmodelArrayListtemp.get(i).getCommunication(),packagesmodelArrayListtemp.get(i).getDriverid(),packagesmodelArrayListtemp.get(i).getDrivername(),packagesmodelArrayListtemp.get(i).getDrivernumber()));

                    Log.d("ff",packagesmodelArrayListtemp.get(i).getCar_type());

                }
            }


        }
        if (airporttransfar_str!=null){
            Log.d("size", String.valueOf(packagesmodelArrayListtemp.size()));
            for (int i=0;i<packagesmodelArrayListtemp.size();i++){
                Log.d("airport", String.valueOf(TextUtils.equals(airporttransfar_str,packagesmodelArrayListtemp.get(i).getService())));
                if (TextUtils.equals(airporttransfar_str,packagesmodelArrayListtemp.get(i).getService())){

                    packagesmodelArrayListtempfilter.add(new Packagesmodel(packagesmodelArrayListtemp.get(i).getP_id(),packagesmodelArrayListtemp.get(i).getPackagename(),packagesmodelArrayListtemp.get(i).getService(),packagesmodelArrayListtemp.get(i).getCity(),packagesmodelArrayListtemp.get(i).getCar_type(),packagesmodelArrayListtemp.get(i).getRate(),packagesmodelArrayListtemp.get(i).getComment(),packagesmodelArrayListtemp.get(i).getCommunication(),packagesmodelArrayListtemp.get(i).getDriverid(),packagesmodelArrayListtemp.get(i).getDrivername(),packagesmodelArrayListtemp.get(i).getDrivernumber()));

                    Log.d("ff",packagesmodelArrayListtemp.get(i).getService());

                }
            }


        }
        if (roundtrip_str!=null){
            Log.d("size", String.valueOf(packagesmodelArrayListtemp.size()));
            for (int i=0;i<packagesmodelArrayListtemp.size();i++){
                if (TextUtils.equals(roundtrip_str,packagesmodelArrayListtemp.get(i).getService())){

                    packagesmodelArrayListtempfilter.add(new Packagesmodel(packagesmodelArrayListtemp.get(i).getP_id(),packagesmodelArrayListtemp.get(i).getPackagename(),packagesmodelArrayListtemp.get(i).getService(),packagesmodelArrayListtemp.get(i).getCity(),packagesmodelArrayListtemp.get(i).getCar_type(),packagesmodelArrayListtemp.get(i).getRate(),packagesmodelArrayListtemp.get(i).getComment(),packagesmodelArrayListtemp.get(i).getCommunication(),packagesmodelArrayListtemp.get(i).getDriverid(),packagesmodelArrayListtemp.get(i).getDrivername(),packagesmodelArrayListtemp.get(i).getDrivernumber()));

                    Log.d("ff",packagesmodelArrayListtemp.get(i).getService());

                }
            }


        }
        if (local8hr_str!=null){
            Log.d("size", String.valueOf(packagesmodelArrayListtemp.size()));
            for (int i=0;i<packagesmodelArrayListtemp.size();i++){
                if (TextUtils.equals(local8hr_str,packagesmodelArrayListtemp.get(i).getService())){

                    packagesmodelArrayListtempfilter.add(new Packagesmodel(packagesmodelArrayListtemp.get(i).getP_id(),packagesmodelArrayListtemp.get(i).getPackagename(),packagesmodelArrayListtemp.get(i).getService(),packagesmodelArrayListtemp.get(i).getCity(),packagesmodelArrayListtemp.get(i).getCar_type(),packagesmodelArrayListtemp.get(i).getRate(),packagesmodelArrayListtemp.get(i).getComment(),packagesmodelArrayListtemp.get(i).getCommunication(),packagesmodelArrayListtemp.get(i).getDriverid(),packagesmodelArrayListtemp.get(i).getDrivername(),packagesmodelArrayListtemp.get(i).getDrivernumber()));

                    Log.d("ff",packagesmodelArrayListtemp.get(i).getService());

                }
            }


        }
        if (local12hr_str!=null){
            Log.d("size", String.valueOf(packagesmodelArrayListtemp.size()));
            for (int i=0;i<packagesmodelArrayListtemp.size();i++){
                if (TextUtils.equals(local12hr_str,packagesmodelArrayListtemp.get(i).getService())){

                    packagesmodelArrayListtempfilter.add(new Packagesmodel(packagesmodelArrayListtemp.get(i).getP_id(),packagesmodelArrayListtemp.get(i).getPackagename(),packagesmodelArrayListtemp.get(i).getService(),packagesmodelArrayListtemp.get(i).getCity(),packagesmodelArrayListtemp.get(i).getCar_type(),packagesmodelArrayListtemp.get(i).getRate(),packagesmodelArrayListtemp.get(i).getComment(),packagesmodelArrayListtemp.get(i).getCommunication(),packagesmodelArrayListtemp.get(i).getDriverid(),packagesmodelArrayListtemp.get(i).getDrivername(),packagesmodelArrayListtemp.get(i).getDrivernumber()));

                    Log.d("ff",packagesmodelArrayListtemp.get(i).getService());

                }
            }


        }
        if (local4hr_str!=null){
            Log.d("size", String.valueOf(packagesmodelArrayListtemp.size()));
            for (int i=0;i<packagesmodelArrayListtemp.size();i++){
                if (TextUtils.equals(local4hr_str,packagesmodelArrayListtemp.get(i).getService())){

                    packagesmodelArrayListtempfilter.add(new Packagesmodel(packagesmodelArrayListtemp.get(i).getP_id(),packagesmodelArrayListtemp.get(i).getPackagename(),packagesmodelArrayListtemp.get(i).getService(),packagesmodelArrayListtemp.get(i).getCity(),packagesmodelArrayListtemp.get(i).getCar_type(),packagesmodelArrayListtemp.get(i).getRate(),packagesmodelArrayListtemp.get(i).getComment(),packagesmodelArrayListtemp.get(i).getCommunication(),packagesmodelArrayListtemp.get(i).getDriverid(),packagesmodelArrayListtemp.get(i).getDrivername(),packagesmodelArrayListtemp.get(i).getDrivernumber()));

                    Log.d("ff",packagesmodelArrayListtemp.get(i).getService());

                }
            }


        }



        if (!packagesmodelArrayListtempfilter.isEmpty()){
            allServiceAdapter.filterdata(packagesmodelArrayListtempfilter);
        }
        else {
            allServiceAdapter.filterdata(packagesmodelArrayListtemp);
        }


    }

}