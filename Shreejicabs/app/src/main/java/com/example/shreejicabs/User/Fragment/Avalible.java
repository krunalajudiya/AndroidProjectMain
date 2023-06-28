package com.example.shreejicabs.User.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.User.Adapter.AvailabletaxiAdapter;
import com.example.shreejicabs.User.Utlity.LoadingView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Avalible extends Fragment {


    SearchableSpinner from_city_spinner,to_city_spinner;
    RecyclerView availabletaxirecyclerview;
    LinearLayoutManager linearLayoutManager;
    private static final String TAG_SUCCESS = "error";
    JSONParser jParser=new JSONParser();
    ArrayList<String> city;
    ArrayList<Avaliabilitymodel> availabilitymodelArrayList;
    AvailabletaxiAdapter availabletaxiAdapter;
    LoadingView loadingView;
    ImageView filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_avalible, container, false);

//        from_city_spinner=view.findViewById(R.id.from_city_spinner);
//        to_city_spinner=view.findViewById(R.id.to_city_spinner);
        availabletaxirecyclerview=view.findViewById(R.id.availabletaxirecyclerview);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        filter=view.findViewById(R.id.filter);

        city=new ArrayList<>();
        availabilitymodelArrayList=new ArrayList<>();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Filter());
            }
        });

//        new Loadcity().execute();
        new LoadAvalibletaxi().execute();

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
            from_city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
            loadingView=new LoadingView(getContext());
            loadingView.ShowLoadingView();
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
                int success= json.getInt(TAG_SUCCESS);

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
            availabletaxiAdapter=new AvailabletaxiAdapter(getActivity(),availabilitymodelArrayList);
            availabletaxirecyclerview.setLayoutManager(linearLayoutManager);
            availabletaxirecyclerview.setAdapter(availabletaxiAdapter);


        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }
}