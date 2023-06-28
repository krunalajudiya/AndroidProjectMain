package com.example.shreejicabs.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Search_Package extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardView airport,hourlyrate,outstation,oneway,local8card,local12card;
    EditText date;
    AutoCompleteTextView from,to;
    AlertDialog alertDialog;
    Button search;
    String from_str,to_str;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;
    ArrayList<String> city=new ArrayList<>();
    LottieAnimationView loading_screen;
    LinearLayout layout_main;



    public Search_Package() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search_Package.
     */
    // TODO: Rename and change types and number of parameters
    public static Search_Package newInstance(String param1, String param2) {
        Search_Package fragment = new Search_Package();
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
        View view= inflater.inflate(R.layout.fragment_search__package, container, false);
        airport=(CardView)view.findViewById(R.id.airport_tranfer);
        hourlyrate=(CardView)view.findViewById(R.id.hourlyrate);
        local8card=(CardView)view.findViewById(R.id.local8card);
        local12card=(CardView)view.findViewById(R.id.local12card);
        outstation=(CardView)view.findViewById(R.id.outstation);
        oneway=(CardView)view.findViewById(R.id.oneway);
        loading_screen=(LottieAnimationView)view.findViewById(R.id.loading_screeen);
        layout_main=(LinearLayout)view.findViewById(R.id.layout_main);

        airport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Package_list package_list=new Package_list();
                Bundle b=new Bundle();
                b.putString("service","Airport Transfer");
                b.putString("from",from_str);
                b.putString("to",to_str);
                package_list.setArguments(b);
                loadFragment(package_list);

            }
        });
        hourlyrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Package_list package_list=new Package_list();
                Bundle b=new Bundle();
                b.putString("service","Local 4hr/40km");
                b.putString("from",from_str);
                b.putString("to",to_str);
                package_list.setArguments(b);
                loadFragment(package_list);
            }
        });
        outstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Package_list package_list=new Package_list();
                Bundle b=new Bundle();
                b.putString("service","Round Trip");
                b.putString("from",from_str);
                b.putString("to",to_str);
                package_list.setArguments(b);
                loadFragment(package_list);

            }
        });
        local8card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Package_list package_list=new Package_list();
                Bundle b=new Bundle();
                b.putString("service","Local 8hr/80km");
                b.putString("from",from_str);
                b.putString("to",to_str);
                package_list.setArguments(b);
                loadFragment(package_list);
            }
        }) ;
        local12card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Package_list package_list=new Package_list();
                Bundle b=new Bundle();
                b.putString("service","Local 12hr/120km");
                b.putString("from",from_str);
                b.putString("to",to_str);
                package_list.setArguments(b);
                loadFragment(package_list);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_search_item, viewGroup, false);
        from=(AutoCompleteTextView)dialogView.findViewById(R.id.from);
        to=(AutoCompleteTextView)dialogView.findViewById(R.id.to);
        date=(EditText)dialogView.findViewById(R.id.date);
        date.setVisibility(View.GONE);
        search=(Button)dialogView.findViewById(R.id.search);
        builder.setView(dialogView);
        builder.setCancelable(true);
        alertDialog = builder.create();
        oneway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from_str=from.getText().toString().trim();
                to_str=to.getText().toString().trim();
                Package_list package_list=new Package_list();
                Bundle b=new Bundle();
                b.putString("service","One way");
                b.putString("from",from_str);
                b.putString("to",to_str);
                package_list.setArguments(b);
                loadFragment(package_list);
                alertDialog.dismiss();
            }
        });
        new LoadAllcity().execute();

        return view;
    }
    class LoadAllcity extends AsyncTask<String, String, String> {
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
                params.add(new BasicNameValuePair("tag", "carservicecity"));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    products = c1.getJSONArray("city");
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


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            layout_main.setVisibility(View.VISIBLE);
            loading_screen.setVisibility(View.GONE);
            if (success==200) {
                if (getActivity()!=null) {
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, city);
                    from.setAdapter(adapter);
                    to.setAdapter(adapter);
                }



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
