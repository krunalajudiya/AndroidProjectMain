package com.example.shreejicabs.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shreejicabs.Adapter.AddedVehicleadapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Addvehiclemodel;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.gson.Gson;
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
 * Use the {@link add_vehicle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_vehicle extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button addNew;
    RecyclerView addedRecycler;
    LinearLayout layout_main;
    LottieAnimationView loading_bar;
    UserSession userSession;
    String user_id;
    JSONParser jsonParser;
    ArrayList<Addvehiclemodel> vehicle_detail;
    User user;
    LinearLayoutManager linearLayoutManager;
    AddedVehicleadapter addedVehicleadapter;
    String car_id;
    int removepostition;

    private static final int PERMISSION_CODE_READ = 2000;
    private static final int PERMISSION_CODE_WRITE= 2001;

    public static final String TAG_SUCCESS="error";

    public add_vehicle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add_vehicle.
     */
    // TODO: Rename and change types and number of parameters
    public static add_vehicle newInstance(String param1, String param2) {
        add_vehicle fragment = new add_vehicle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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

        View v= inflater.inflate(R.layout.fragment_add_vehicle, container, false);
        addNew=v.findViewById(R.id.addnew);
        addedRecycler=v.findViewById(R.id.addedrecyclerview);
        layout_main=v.findViewById(R.id.layout_add_vehicle);
        loading_bar=v.findViewById(R.id.loading_screeen);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        user_id=user.getId();
        jsonParser=new JSONParser();
        vehicle_detail=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(getContext());
        addedVehicleadapter=new AddedVehicleadapter(getActivity(),vehicle_detail);


//        addvehiclemodel=new Addvehiclemodel();

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AddNew_Vehicle());
            }
        });


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_CODE_READ);
                Log.d("parmission","request");
            }else {
                Log.d("parmission","parmission already granted");
            }
        }else {

        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_CODE_WRITE);
                Log.d("parmission","request");
            }else {
                Log.d("parmission","parmission already granted");
            }
        }else {

        }

        //     if (isStoragePermissionGranted())
//     {
//
//     }
        new LoadAllData().execute();

        addedVehicleadapter.setOnItemClickListener(new AddedVehicleadapter.OnItemClickListener() {

            @Override
            public void onRemoveClick(int pos) {
                removepostition=pos;
                new RemoveData().execute();
            }
        });
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("ff22", String.valueOf(grantResults));
        if (requestCode == PERMISSION_CODE_READ){
            Log.d("read","accepted");
        }
        if (requestCode == PERMISSION_CODE_WRITE){
            Log.d("write","accepted");

        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

//                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    private void removedataadapter(int Position){
        vehicle_detail.remove(Position);
        addedVehicleadapter.notifyItemRemoved(Position);
    }

    class RemoveData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_bar.setVisibility(View.VISIBLE);
            layout_main.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("tag","delete_driver_car"));
            list.add(new BasicNameValuePair("id",vehicle_detail.get(removepostition).getId()));
            JSONObject removeitem=jsonParser.makeHttpRequest(Constants.url,"POST",list);
            try {
                int removesuccess = removeitem.getInt(TAG_SUCCESS);
                if (removesuccess==200){
                    removedataadapter(removepostition);
                }
                else {
                    Toast.makeText(getActivity(),"Somthind wrong",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading_bar.setVisibility(View.GONE);
            layout_main.setVisibility(View.VISIBLE);
        }
    }

    class LoadAllData extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> send_details=new ArrayList<>();
            send_details.add(new BasicNameValuePair("tag","fetch_driver_car"));
            send_details.add(new BasicNameValuePair("user_id",user_id));
            JSONObject jsonObject=jsonParser.makeHttpRequest(Constants.url,"POST",send_details);

            int success= 0;
            try {
                success = jsonObject.getInt(TAG_SUCCESS);

                if (success == 200){
                    JSONObject a1=jsonObject.getJSONObject("data");
                    JSONArray a2=a1.getJSONArray("addcar");
                    for (int i=0;i<a2.length();i++){

                        JSONObject a3=a2.getJSONObject(i);
                        String id=a3.getString("id");
                        String car_name=a3.getString("car_name");
                        String fuel=a3.getString("fuel");
                        String color=a3.getString("color");
                        String carrier=a3.getString("carrier");
                        String model_year=a3.getString("model_year");
                        String photo=a3.getString("photo");

                        vehicle_detail.add(new Addvehiclemodel(id,car_name,fuel,color,carrier,model_year,photo));
                        Log.d("ff",id);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            addedRecycler.setLayoutManager(linearLayoutManager);
            addedRecycler.setAdapter(addedVehicleadapter);
            loading_bar.setVisibility(View.GONE);
            layout_main.setVisibility(View.VISIBLE);

        }

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}