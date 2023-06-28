package com.example.shreejicabs.User.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.User.Adapter.AvailabletaxiAdapter;
import com.example.shreejicabs.User.Home;
import com.example.shreejicabs.User.Service.LocationUpdater;
import com.example.shreejicabs.User.Utlity.LoadingView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Search_taxi extends Fragment {

    SearchableSpinner from_city_spinner, to_city_spinner;
    RecyclerView availabletaxirecyclerview;
    LinearLayoutManager linearLayoutManager;
    private static final String TAG_SUCCESS = "error";
    JSONParser jParser = new JSONParser();
    ArrayList<String> city;
    ArrayList<Avaliabilitymodel> availabilitymodelArrayList;
    AvailabletaxiAdapter availabletaxiAdapter;
    LoadingView loadingView;
    int success;
    ArrayList<Avaliabilitymodel> availabilitymodelArrayListfilter=new ArrayList<>();

    FusedLocationProviderClient locationProviderClient;
    private final int PERMISSION_CODE = 1000;

    //LocationUpdater locationUpdater;

    public  static String Current_City_location=null;
    String city_location;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_taxi, container, false);

        from_city_spinner = view.findViewById(R.id.from_city_spinner);
        to_city_spinner = view.findViewById(R.id.to_city_spinner);
        availabletaxirecyclerview = view.findViewById(R.id.availabletaxirecyclerview);


        linearLayoutManager = new LinearLayoutManager(getContext());
        locationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());


        city = new ArrayList<>();
        availabilitymodelArrayList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
                String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions,PERMISSION_CODE);

            }else {
                getloc();
                //new getlocation().execute();
            }
        }else {
            getloc();
            //new getlocation().execute();
        }


        //new getlocation().execute();


        new Loadcity().execute();
        new LoadAvalibletaxi().execute();


        return view;
    }
    public void getloc(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Log.d("location parmisson","approve");
            locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Location> task) {

                    Location location=task.getResult();
                    if (location!=null){

                        Geocoder geocoder=new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                        try {
                            Log.d("ff","enter");
                            if (geocoder.isPresent()){
                                List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                Log.d("lattitude", String.valueOf(addressList.get(0).getLatitude()));
                                Log.d("longtitude", String.valueOf(addressList.get(0).getLongitude()));
                                Log.d("addressline", String.valueOf(addressList.get(0).getAddressLine(0)));
                                Log.d("locality", String.valueOf(addressList.get(0).getLocality()));
                                Log.d("countary", String.valueOf(addressList.get(0).getCountryName()));

                                Current_City_location=addressList.get(0).getLocality();
                                city_location=Current_City_location;


//                                    Toast.makeText(getActivity(),addressList.get(0).getLatitude()+"\n"+
//                                            addressList.get(0).getLongitude()+"\n"+addressList.get(0).getAddressLine(0)+"\n"+
//                                            addressList.get(0).getLocality()+"\n"+addressList.get(0).getCountryName(),Toast.LENGTH_LONG).show();

                            }


                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"exception",Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Log.d("location","location value null");
                    }
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {

                }
            });


            //return TODO;
        }

    }
//    class getlocation extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                Log.d("location parmisson","approve");
//                locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Location> task) {
//
//                        Location location=task.getResult();
//                        if (location!=null){
//
//                            Geocoder geocoder=new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
//                            try {
//                                Log.d("ff","enter");
//                                if (geocoder.isPresent()){
//                                    List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//                                    Log.d("lattitude", String.valueOf(addressList.get(0).getLatitude()));
//                                    Log.d("longtitude", String.valueOf(addressList.get(0).getLongitude()));
//                                    Log.d("addressline", String.valueOf(addressList.get(0).getAddressLine(0)));
//                                    Log.d("locality", String.valueOf(addressList.get(0).getLocality()));
//                                    Log.d("countary", String.valueOf(addressList.get(0).getCountryName()));
//
//                                    City_location=addressList.get(0).getLocality();
//
////                                    Toast.makeText(getActivity(),addressList.get(0).getLatitude()+"\n"+
////                                            addressList.get(0).getLongitude()+"\n"+addressList.get(0).getAddressLine(0)+"\n"+
////                                            addressList.get(0).getLocality()+"\n"+addressList.get(0).getCountryName(),Toast.LENGTH_LONG).show();
//
//                                }
//
//
//                            }catch (Exception e){
//                                e.printStackTrace();
//                                Toast.makeText(getActivity(),"exception",Toast.LENGTH_LONG).show();
//                            }
//
//                        }else {
//                            Log.d("location","location value null");
//                        }
//                    }
//                }).addOnCanceledListener(new OnCanceledListener() {
//                    @Override
//                    public void onCanceled() {
//
//                    }
//                });
//
//
//                //return TODO;
//            }
//
//
//            return null;
//        }
//
//    @Override
//    protected void onPostExecute(Void unused) {
//        super.onPostExecute(unused);
//
//    }
//}

    class Loadcity extends AsyncTask<Void,Void,Void>{
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

                int success= json.getInt(TAG_SUCCESS);

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
            loadingView.HideLoadingView();

            ArrayAdapter fromcityadapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,city);
            from_city_spinner.setAdapter(fromcityadapter);
            if (city_location!=null){
                for (int i=0;i<city.size();i++){
                    if (TextUtils.equals(city.get(i),city_location)){
                        from_city_spinner.setSelection(i);
                    }

                }
            }
            from_city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position!=0){
                        city_location=city.get(position);
                        updatecitylocation();
                        Log.d("filterarraysize", String.valueOf(availabilitymodelArrayListfilter.size()));
                        availabletaxiAdapter.updatedata(availabilitymodelArrayListfilter,availabletaxiAdapter);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ArrayAdapter tocityadapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,city);

            to_city_spinner.setAdapter(tocityadapter);
            to_city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    class LoadAvalibletaxi extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loadingView=new LoadingView(getContext());
//            loadingView.ShowLoadingView();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", "All_availablity"));
            params.add(new BasicNameValuePair("cities", "rajkot"));
            JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try{
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    JSONArray products = c1.getJSONArray("aviid");

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String id    = c.getString("availablity_id");
                        String from = c.getString("From_city");
                        String to = c.getString("To_city");
                        String date = c.getString("dt");
                        String time = c.getString("tm");
                        JSONObject user_detail=c.getJSONObject("user_detail");
                        String driver_id=user_detail.getString("Reg_id");
                        String driver_name=user_detail.getString("Name");
                        String driver_number=user_detail.getString("Mobile_no");
                        String car_type = c.getString("Car_type");
                        String service = c.getString("Service_type");
                        String comment = c.getString("Comment");
                        String communication = c.getString("Communication");
                        String Create_at=c.getString("Create_at");


                        availabilitymodelArrayList.add(new Avaliabilitymodel(id,from,to,date,time,car_type,service,comment,communication,driver_id,driver_name,driver_number,Create_at));

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
            loadingView.HideLoadingView();
            if (success==200){

                if (city_location!=null){
                    availabilitymodelArrayListfilter.clear();
                    for (int i=0;i<availabilitymodelArrayList.size();i++){

                        if (TextUtils.equals(availabilitymodelArrayList.get(i).getFrom(),city_location)){
                            availabilitymodelArrayListfilter.add(new Avaliabilitymodel(availabilitymodelArrayList.get(i).getId(),availabilitymodelArrayList.get(i).getFrom(),availabilitymodelArrayList.get(i).getTo(),availabilitymodelArrayList.get(i).getDate(),availabilitymodelArrayList.get(i).getTime(),availabilitymodelArrayList.get(i).getCartype(),availabilitymodelArrayList.get(i).getService(),availabilitymodelArrayList.get(i).getComment(),availabilitymodelArrayList.get(i).getCommunication(),availabilitymodelArrayList.get(i).getDriverid(),availabilitymodelArrayList.get(i).getDrivername(),availabilitymodelArrayList.get(i).getDrivernumber(),availabilitymodelArrayList.get(i).getCreate_at()));

                            //Log.d("ff",availabilitymodelArrayList.get(i).getDrivername());

                        }

                    }
                    if (availabilitymodelArrayListfilter!=null){
                        availabletaxiAdapter=new AvailabletaxiAdapter(getActivity(),availabilitymodelArrayListfilter);
                    }
                    else {
                        availabletaxiAdapter=new AvailabletaxiAdapter(getActivity(),availabilitymodelArrayList);
                    }

                }
                else {
                    availabletaxiAdapter=new AvailabletaxiAdapter(getActivity(),availabilitymodelArrayList);
                }

                availabletaxirecyclerview.setLayoutManager(linearLayoutManager);
                availabletaxirecyclerview.setAdapter(availabletaxiAdapter);

            }


        }
    }
    void updatecitylocation(){
        availabilitymodelArrayListfilter.clear();
        for (int i=0;i<availabilitymodelArrayList.size();i++){

            if (TextUtils.equals(availabilitymodelArrayList.get(i).getFrom(),city_location)){
                availabilitymodelArrayListfilter.add(new Avaliabilitymodel(availabilitymodelArrayList.get(i).getId(),availabilitymodelArrayList.get(i).getFrom(),availabilitymodelArrayList.get(i).getTo(),availabilitymodelArrayList.get(i).getDate(),availabilitymodelArrayList.get(i).getTime(),availabilitymodelArrayList.get(i).getCartype(),availabilitymodelArrayList.get(i).getService(),availabilitymodelArrayList.get(i).getComment(),availabilitymodelArrayList.get(i).getCommunication(),availabilitymodelArrayList.get(i).getDriverid(),availabilitymodelArrayList.get(i).getDrivername(),availabilitymodelArrayList.get(i).getDrivernumber(),availabilitymodelArrayList.get(i).getCreate_at()));

                Log.d("ff",availabilitymodelArrayList.get(i).getDrivername());

            }

        }
        if (availabilitymodelArrayListfilter!=null){
            availabletaxiAdapter=new AvailabletaxiAdapter(getActivity(),availabilitymodelArrayListfilter);
        }
        else {
            availabletaxiAdapter=new AvailabletaxiAdapter(getActivity(),availabilitymodelArrayList);
        }
    }
}