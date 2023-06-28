package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.shreejicabs.Adapter.Myrequiredvehicleadapter;
import com.example.shreejicabs.Adapter.Requirementvehicleadapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.Model.Requirementmodel;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link My_required_vehicle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class My_required_vehicle extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView requiredrec;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    LottieAnimationView loading_screen;
    int success;
    ArrayList<Requirementmodel> requirementmodelArrayList=new ArrayList<>();

    User user;
    UserSession userSession;
    String user_id;
    TextView norequirement;

    public My_required_vehicle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment My_required_vehicle.
     */
    // TODO: Rename and change types and number of parameters
    public static My_required_vehicle newInstance(String param1, String param2) {
        My_required_vehicle fragment = new My_required_vehicle();
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
        requirementmodelArrayList.clear();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_required_vehicle, container, false);
        requiredrec=(RecyclerView)view.findViewById(R.id.myrequiredrec);
        norequirement=(TextView)view.findViewById(R.id.norequiredtxt);
        loading_screen=(LottieAnimationView)view.findViewById(R.id.loading_screeen);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        requiredrec.setLayoutManager(linearLayoutManager);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        user_id=user.getId();
        new MyLoadRequirements().execute();
        return view;
    }
    class MyLoadRequirements extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "Requirements_id"));
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
                    products = c1.getJSONArray("reqid");
                    // looping through All Products

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String id    = c.getString("Requirement_id");
                        String from = c.getString("From_city");
                        String to = c.getString("To_city");
                        String date = c.getString("dt");
                        String time = c.getString("tm");
                        String car_type = c.getString("Car_type");
                        String service = c.getString("Service_type");
                        String comment = c.getString("Comment");
                        String communication = c.getString("Communication");

                        requirementmodelArrayList.add(new Requirementmodel(id,from,to,date,time,car_type,service,comment,communication));

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
            loading_screen.setVisibility(View.GONE);
            if (success==200) {
                norequirement.setVisibility(View.GONE);
                if (getActivity()!=null) {
                    Myrequiredvehicleadapter requirementvehicleadapter = new Myrequiredvehicleadapter(getActivity(), requirementmodelArrayList);
                    requiredrec.setAdapter(requirementvehicleadapter);
                    requirementvehicleadapter.setOnItemClickListener(new Myrequiredvehicleadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {
                            Update_required_details update_required_details = new Update_required_details();
                            Gson gson = new Gson();
                            String avaliability;
                            avaliability = gson.toJson(requirementmodelArrayList.get(pos));
                            Bundle bundle = new Bundle();
                            bundle.putString("r_details", avaliability);
                            update_required_details.setArguments(bundle);
                            loadFragment(update_required_details);
                        }
                    });
                }


            }else{
                norequirement.setVisibility(View.VISIBLE);
            }

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