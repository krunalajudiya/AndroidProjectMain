package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.shreejicabs.Adapter.FraudListAdapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Fraudlistmodel;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FraudList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FraudList extends Fragment {


    JSONParser jparser=new JSONParser();
    private static final String TAG_SUCCESS = "error";
    int success;
    ArrayList<Fraudlistmodel> fraudlistmodelArrayList=new ArrayList<>();
    FraudListAdapter fraudListAdapter;
    private ProgressDialog pDialog;
    RecyclerView fraudlistrecyclerview;
    LinearLayoutManager linearLayoutManager;
    LottieAnimationView loading_screen;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FraudList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FraudList.
     */
    // TODO: Rename and change types and number of parameters
    public static FraudList newInstance(String param1, String param2) {
        FraudList fragment = new FraudList();
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
        View view=inflater.inflate(R.layout.fragment_fraud_list, container, false);
        fraudlistrecyclerview=view.findViewById(R.id.fraudlistrecyclerview);
        loading_screen=(LottieAnimationView)view.findViewById(R.id.loading_screeen);

        new getfraudlist().execute();


        return view;
    }

    class getfraudlist extends AsyncTask<String,String,String>{

        @Override
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

            List<NameValuePair> params=new ArrayList<>();
            params.add(new BasicNameValuePair("tag","fetch_froud_list"));
            JSONObject jsonObject=jparser.makeHttpRequest(Constants.url,"POST",params);
            Log.d("all data",jsonObject.toString());

            try {
                success=jsonObject.getInt(TAG_SUCCESS);

                if (success==200){
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    JSONArray jsonArray=jsonObject1.getJSONArray("flist");
                    Log.d("jsonarray",jsonArray.toString());

                    //Fraudlistmodel fraudlistmodel = new Fraudlistmodel();
//                    for (int i=0;i<jsonArray.length();i++){
//                        JSONObject c=jsonArray.getJSONObject(i);
//
//                        fraudlistmodel.setId(c.getString("id"));
//                        fraudlistmodel.setName(c.getString("Name"));
//                        fraudlistmodel.setMobile(c.getString("Mobile"));
//
//                        fraudlistmodelList.add(fraudlistmodel);
//
//                    }
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject2=jsonArray.getJSONObject(i);

                        fraudlistmodelArrayList.add(new Fraudlistmodel(jsonObject2.getString("id"),jsonObject2.getString("Name"),jsonObject2.getString("Mobile")));
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
            //pDialog.dismiss();


            fraudListAdapter=new FraudListAdapter(getActivity(),fraudlistmodelArrayList);
            Log.d("ff", String.valueOf(fraudlistmodelArrayList.size()));
            linearLayoutManager=new LinearLayoutManager(getActivity());
            fraudlistrecyclerview.setLayoutManager(linearLayoutManager);
            fraudlistrecyclerview.setAdapter(fraudListAdapter);
            fraudlistrecyclerview.setVisibility(View.VISIBLE);
            loading_screen.setVisibility(View.GONE);
        }
    }
}