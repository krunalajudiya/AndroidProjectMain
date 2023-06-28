package com.example.shreejicabs.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shreejicabs.Adapter.Cardetailsshowadapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Addvehiclemodel;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.google.gson.JsonObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_show#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_show extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView name,phonenumber,company_name,city,website,address;
    ImageView conpany_profile_photo;
    RecyclerView cardetailsrecyclerview;
    JSONParser jsonParser;
    LinearLayout layoutmain;
    LottieAnimationView loading_screen;
    public final String Reg_id;
    ArrayList<Addvehiclemodel> cardetailsarraylist;
    Cardetailsshowadapter cardetailsshowadapter;
    GridLayoutManager gridLayoutManager;




    public profile_show(String Reg_id) {
        // Required empty public constructor
        this.Reg_id=Reg_id;

    }


    // TODO: Rename and change types and number of parameters
//    public static profile_show newInstance(String param1, String param2) {
//        profile_show fragment = new profile_show();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile_show, container, false);

        name=view.findViewById(R.id.name_);
        phonenumber=view.findViewById(R.id.phonenumber_);
        company_name=view.findViewById(R.id.companyname_);
        city=view.findViewById(R.id.city_);
        website=view.findViewById(R.id.city_);
        address=view.findViewById(R.id.address_);
        conpany_profile_photo=view.findViewById(R.id.conpanyprofilephoto);
        cardetailsrecyclerview=view.findViewById(R.id.photorecyclerview);
        jsonParser=new JSONParser();
        layoutmain=view.findViewById(R.id.layoutmain);
        loading_screen=view.findViewById(R.id.loading_screeen);
        cardetailsarraylist=new ArrayList<>();
        gridLayoutManager=new GridLayoutManager(getActivity(),2);

        new ProfileData().execute();
        return view;
    }

    class ProfileData extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            List<NameValuePair> param=new ArrayList<>();
            param.add(new BasicNameValuePair("tag","fetch_profile_and_car"));
            param.add(new BasicNameValuePair("Reg_id",Reg_id));
            Log.d("ffid",Reg_id);
            JSONObject jsonObject1=jsonParser.makeHttpRequest(Constants.url,"POST",param);
            try {
                if (jsonObject1.getInt("error")==200){

                    JSONObject jsonObject11=jsonObject1.getJSONObject("data");
                    JSONArray jsonArray=jsonObject11.getJSONArray("fetchp");
                    JSONObject jsonObject12=jsonArray.getJSONObject(0);
                    String reg_id_t=jsonObject12.getString("Reg_id");
                    String name_t=jsonObject12.getString("Name");
                    String mobile_t=jsonObject12.getString("Mobile_no");
                    String email_t=jsonObject12.getString("Email");
                    String company_name_t=jsonObject12.getString("Company_name");
                    String address_t=jsonObject12.getString("Address");
                    String city_t=jsonObject12.getString("City");
                    String website_t=jsonObject12.getString("Website");
                    JSONArray jsonArray1=jsonObject12.getJSONArray("car_list");
                    for (int i=0;i<jsonArray1.length();i++){
                        JSONObject jsonObject=jsonArray1.getJSONObject(i);
                        String id_t=jsonObject.getString("id");
                        String reg_id=jsonObject.getString("Reg_id");
                        String car_name_t=jsonObject.getString("car_nm");
                        String fuel_t=jsonObject.getString("fuel");
                        String color_t=jsonObject.getString("color");
                        String carrier_t=jsonObject.getString("carrier");
                        String model_year_t=jsonObject.getString("model_year");
                        String photo_t=jsonObject.getString("photo");

                        cardetailsarraylist.add(new Addvehiclemodel(id_t,car_name_t,fuel_t,color_t,carrier_t,model_year_t,photo_t));
                    }
                    if (name_t!=null){
                        name.setText(name_t);
                    }else {

                    }
                    if (mobile_t!=null){
                        phonenumber.setText(mobile_t);
                    }else {

                    }
                    if (company_name_t!=""){
                        company_name.setText(company_name_t);
                    }else {
                        Log.d("ff",company_name_t);
                    }
                    if (address_t!=""){
                        address.setText(address_t);
                    }else {

                    }
                    if (website_t!=null){
                        website.setText(website_t);
                    }else {

                    }
                    if (city_t!=null){
                        city.setText(city_t);
                    }else {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cardetailsrecyclerview.setLayoutManager(gridLayoutManager);
            cardetailsshowadapter=new Cardetailsshowadapter(getActivity(),cardetailsarraylist);
            cardetailsrecyclerview.setAdapter(cardetailsshowadapter);

            layoutmain.setVisibility(View.VISIBLE);
            loading_screen.setVisibility(View.GONE);

        }
    }
}