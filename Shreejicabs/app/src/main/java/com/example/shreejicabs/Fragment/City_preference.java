package com.example.shreejicabs.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shreejicabs.Constants;
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
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link City_preference#newInstance} factory method to
 * create an instance of this fragment.
 */
public class City_preference extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    User user;
    UserSession userSession;
    TextView citynottxt;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;
    ArrayList<String> city=new ArrayList<>();
    AutoCompleteTextView city_edt;
    String city_str,combine_city;
    LinearLayout citylayout,leftlayout,rightlayout;
    Button addcity;
    String user_detail;
    LottieAnimationView loading_screen;
    ArrayAdapter adapter;
    LinearLayout layoutmain;

    public City_preference() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment City_preference.
     */
    // TODO: Rename and change types and number of parameters
    public static City_preference newInstance(String param1, String param2) {
        City_preference fragment = new City_preference();
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

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_city_preference, container, false);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        citynottxt=(TextView)view.findViewById(R.id.citynotavailable);
        city_edt=(AutoCompleteTextView)view.findViewById(R.id.city);
        citylayout=(LinearLayout)view.findViewById(R.id.citypreferencelayout);
        leftlayout=(LinearLayout)view.findViewById(R.id.leftlayout);
        rightlayout=(LinearLayout)view.findViewById(R.id.rightlayout);
        addcity=(Button)view.findViewById(R.id.addcity);
        loading_screen=(LottieAnimationView)view.findViewById(R.id.loading_screeen);
        layoutmain=view.findViewById(R.id.layoutmain);
        if (TextUtils.isEmpty(user.getCity_pref().trim()))
        {
            citynottxt.setVisibility(View.VISIBLE);
        }else{
            final String[] city_ar=user.getCity_pref().split(",");
            final ArrayList<String> cityArrayList=new ArrayList<>(Arrays.asList(city_ar));
            for (int i=0;i<city_ar.length;i++)
            {
                LinearLayout layout=new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,50);
                layout.setLayoutParams(layoutParams);
                layout.setBackground(getActivity().getDrawable(R.drawable.rounded_shape));
                layout.setPadding(30,30,30,30);

                TextView textView=new TextView(getActivity());
                textView.setText(city_ar[i]);
                textView.setTextColor(getActivity().getColor(R.color.gen_black));
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textView.setTextSize(20);
                layout.setGravity(Gravity.CENTER);
                layout.addView(textView);
                ImageView imageView=new ImageView(getActivity());
                LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(50,50);
                layoutParams1.setMargins(15,0,0,0);
                imageView.setLayoutParams(layoutParams1);
                imageView.setImageDrawable(getActivity().getDrawable(R.drawable.close));
                final int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("btn",city_ar[finalI]);
                        if (cityArrayList.contains(city_ar[finalI]))
                        {
                            cityArrayList.remove(cityArrayList.indexOf(city_ar[finalI]));
                        }
                        System.out.println(TextUtils.join(",",cityArrayList));
                        combine_city=TextUtils.join(",",cityArrayList);
                        new Add_city().execute();
                    }
                });
                layout.addView(imageView);
                if (i%2==0)
                {
                    leftlayout.addView(layout);
                }else{
                    rightlayout.addView(layout);

                }
                //citylayout.addView(layout);
            }
        }
        addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city_str=city_edt.getText().toString().trim();
                combine_city=user.getCity_pref();
                if (TextUtils.isEmpty(user.getCity_pref().trim()))
                {
                    combine_city=city_edt.getText().toString().trim();

                    new Add_city().execute();
                }else{
                    city_str=city_edt.getText().toString().trim();
                    combine_city=combine_city+","+city_str;
                    new Add_city().execute();
                }

            }
        });

        new LoadAllservices().execute();

        return view;
    }
    class LoadAllservices extends AsyncTask<String, String, String> {
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
//            loading_screen.setVisibility(View.GONE);
            if (success==200) {
                if (getActivity()!=null) {
                    adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, city);
                    city_edt.setThreshold(1);
                    city_edt.setAdapter(adapter);
                }
            }

        }
    }
    class Add_city extends AsyncTask<String, String, String> {
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
                params.add(new BasicNameValuePair("tag", "City_preference"));
                params.add(new BasicNameValuePair("cities", combine_city));
                params.add(new BasicNameValuePair("Reg_id", user.getId()));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));

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
                user.setCity_pref(combine_city);
                Gson gson=new Gson();
                user_detail=gson.toJson(user);
                userSession.createLoginSession(user_detail);
                user=gson.fromJson(userSession.getUserDetails(),User.class);
                if (TextUtils.isEmpty(user.getCity_pref().trim()))
                {
                    leftlayout.removeAllViews();
                    rightlayout.removeAllViews();
                    citynottxt.setVisibility(View.VISIBLE);
                }else{
                    leftlayout.removeAllViews();
                    rightlayout.removeAllViews();
                    citynottxt.setVisibility(View.GONE);
                    final String[] city_ar=user.getCity_pref().split(",");
                    final ArrayList<String> cityArrayList=new ArrayList<>(Arrays.asList(city_ar));
                    for (int i=0;i<city_ar.length;i++)
                    {
                        LinearLayout layout=new LinearLayout(getActivity());
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0,0,0,50);
                        layout.setLayoutParams(layoutParams);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            layout.setBackground(getActivity().getDrawable(R.drawable.rounded_shape));
                        }
                        layout.setPadding(30,30,30,30);

                        TextView textView=new TextView(getActivity());
                        textView.setText(city_ar[i]);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setTextColor(getActivity().getColor(R.color.gen_black));
                        }
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        textView.setTextSize(20);
                        layout.setGravity(Gravity.CENTER);
                        layout.addView(textView);
                        ImageView imageView=new ImageView(getActivity());
                        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(50,50);
                        layoutParams1.setMargins(15,0,0,0);
                        imageView.setLayoutParams(layoutParams1);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.close));
                        }
                        final int finalI = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("btn",city_ar[finalI]);
                                if (cityArrayList.contains(city_ar[finalI]))
                                {
                                    cityArrayList.remove(cityArrayList.indexOf(city_ar[finalI]));
                                }
                                System.out.println(TextUtils.join(",",cityArrayList));
                                combine_city=TextUtils.join(",",cityArrayList);
                                new Add_city().execute();
                            }
                        });
                        layout.addView(imageView);
                        if (i%2==0)
                        {
                            leftlayout.addView(layout);
                        }else{
                            rightlayout.addView(layout);

                        }
                        //citylayout.addView(layout);
                    }
                }
                city_edt.setText("");
                Toast.makeText(getActivity(),"Add City SuccessFully",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getActivity(),"Add City Not SuccessFully",Toast.LENGTH_LONG).show();
            }

        }
    }



}