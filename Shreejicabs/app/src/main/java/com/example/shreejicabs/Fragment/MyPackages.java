package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
import android.widget.TextView;

import com.example.shreejicabs.Adapter.Avaliabilityadapter;
import com.example.shreejicabs.Adapter.Mypackageadapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.Model.Packagesmodel;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyPackages extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView packagerec;
    FloatingActionButton floatingActionButton;
    User user;
    UserSession userSession;
    String user_id;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;
    ArrayList<Packagesmodel> packagesmodelArrayList=new ArrayList<>();
    TextView nopackage;



    public MyPackages() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPackages.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPackages newInstance(String param1, String param2) {
        MyPackages fragment = new MyPackages();
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
        packagesmodelArrayList.clear();
        View view= inflater.inflate(R.layout.fragment_my_packages, container, false);
        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.addpackage);
        nopackage=(TextView)view.findViewById(R.id.nopackagetxt);
        packagerec=(RecyclerView)view.findViewById(R.id.packagerec);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        packagerec.setLayoutManager(linearLayoutManager);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        user_id=user.getId();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Add_Packages());
            }
        });
        new LoadPackage().execute();

        return view;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    class LoadPackage extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "Driver_package"));
                params.add(new BasicNameValuePair("Reg_id", user_id));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    products = c1.getJSONArray("servicecity");
                    // looping through All Products

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String id    = c.getString("service_id");
                        String packge_name = c.getString("Packge_name");
                        String city = c.getString("city");
                        String rate = c.getString("Rate");

                        String car_type = c.getString("Car_type");
                        String service = c.getString("Service_type");
                        String comment = c.getString("Comment");
                        String communication = c.getString("Communication");

                       packagesmodelArrayList.add(new Packagesmodel(id,packge_name,service,city,car_type,rate,comment,communication));

                    }



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
                nopackage.setVisibility(View.GONE);
                if (getActivity()!=null) {
                    Mypackageadapter mypackageadapter = new Mypackageadapter(getActivity(), packagesmodelArrayList);
                    packagerec.setAdapter(mypackageadapter);
                    mypackageadapter.setOnItemClickListener(new Mypackageadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {
                            Update_package update_package = new Update_package();
                            Gson gson = new Gson();
                            String packages;
                            packages = gson.toJson(packagesmodelArrayList.get(pos));
                            Bundle bundle = new Bundle();
                            bundle.putString("p_details", packages);
                            update_package.setArguments(bundle);
                            loadFragment(update_package);


                        }
                    });
                }


            }else{
                nopackage.setVisibility(View.VISIBLE);
            }

        }
    }






}
