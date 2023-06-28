package com.example.shreejicabs.User.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.User.Utlity.LoadingView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Filter extends Fragment {

    SearchableSpinner from_city_spinner,to_city_spinner;
    LoadingView loadingView;
    private static final String TAG_SUCCESS = "error";
    JSONParser jParser=new JSONParser();
    ArrayList<String> city;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_filter, container, false);


        from_city_spinner=view.findViewById(R.id.from_city_spinner);
        to_city_spinner=view.findViewById(R.id.to_city_spinner);

        city=new ArrayList<>();
        new Loadcity().execute();
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

}