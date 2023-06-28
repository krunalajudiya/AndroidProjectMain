package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Packagesmodel;
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
 * Use the {@link Update_package#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Update_package extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText package_name,rate,comment;
    AutoCompleteTextView city;
    Spinner service,car_type;
    Button updatepackage;
    RadioGroup radioGroup;
    RadioButton chat,call;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;
    String package_name_str,rate_str,comment_str,user_id,service_str,car_str,city_str,commucation_str;
    ArrayList<String> service_ar=new ArrayList<>();
    ArrayList<String> cartype_ar=new ArrayList<>();
    ArrayList<String> city_ar=new ArrayList<>();
    User user;
    UserSession userSession;
    Packagesmodel packagesmodel;

    public Update_package() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Update_package.
     */
    // TODO: Rename and change types and number of parameters
    public static Update_package newInstance(String param1, String param2) {
        Update_package fragment = new Update_package();
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
        city_ar.clear();
        service_ar.clear();
        cartype_ar.clear();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_package, container, false);
        package_name=(EditText)view.findViewById(R.id.packagename);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(), User.class);
        user_id=user.getId();
        Bundle b=getArguments();
        String p_id=b.getString("p_details");
        packagesmodel=gson.fromJson(p_id,Packagesmodel.class);
        rate=(EditText)view.findViewById(R.id.rate);
        comment=(EditText)view.findViewById(R.id.comment);
        service=(Spinner)view.findViewById(R.id.service);
        car_type=(Spinner)view.findViewById(R.id.cartype);
        city=(AutoCompleteTextView)view.findViewById(R.id.city);
        chat=(RadioButton)view.findViewById(R.id.chat);
        call=(RadioButton)view.findViewById(R.id.call);
        comment=(EditText)view.findViewById(R.id.comment);
        radioGroup=(RadioGroup)view.findViewById(R.id.communicationgroup);
        updatepackage=(Button)view.findViewById(R.id.updatepackage);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (chat.isChecked())
                {
                    commucation_str=chat.getText().toString().trim();
                }else if (call.isChecked())
                {
                    commucation_str=call.getText().toString().trim();
                }
            }
        });
        updatepackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalue();
                if (TextUtils.isEmpty(package_name_str))
                {
                    package_name.setError("Please enter Package Name");
                    package_name.requestFocus();
                }else if (TextUtils.isEmpty(rate_str))
                {
                    rate.setError("Please Enter Rate");
                    rate.requestFocus();
                }else if (TextUtils.isEmpty(comment_str))
                {
                    comment.setError("Please Enter Comment");
                    comment.requestFocus();

                }
                else if (TextUtils.isEmpty(city_str))
                {
                    city.setError("Please Enter City");
                    city.requestFocus();

                }
                else if (!call.isChecked() && !chat.isChecked())
                {
                    Toast.makeText(getActivity(),"Please Choose Communication",Toast.LENGTH_LONG).show();

                }else if (service.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getActivity(),"Please Select Service",Toast.LENGTH_LONG).show();

                }
                else if (car_type.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getActivity(),"Please Select Car Type",Toast.LENGTH_LONG).show();
                }else {
                    new Update_Package_detail().execute();
                }

            }
        });
        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                service_str=service_ar.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        car_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                car_str=cartype_ar.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        new LoadAllservices().execute();
        return view;
    }
    public void getvalue()
    {
        package_name_str=package_name.getText().toString().trim();
        rate_str=rate.getText().toString().trim();
        comment_str=comment.getText().toString().trim();
        city_str=city.getText().toString().trim();

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
                    city_ar.add("Select City");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String city_name = c.getString("city_name");

                        city_ar.add(city_name);

                    }
                    products=c1.getJSONArray("cartype");
                    cartype_ar.add("Select Car");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String cartype = c.getString("cartype");
                        cartype_ar.add(cartype);

                    }
                    products=c1.getJSONArray("service");
                    service_ar.add("Select Service");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String service_type = c.getString("service_type");
                        service_ar.add(service_type);

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
                if (getActivity()!=null) {
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, city_ar);
                    city.setAdapter(adapter);
                    ArrayAdapter caradapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, cartype_ar);
                    car_type.setAdapter(caradapter);
                    ArrayAdapter serviceadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, service_ar);
                    service.setAdapter(serviceadapter);
                }

            }
            if (!TextUtils.isEmpty(packagesmodel.getCity()))
            {
                city.setText(packagesmodel.getCity());
            }
            if (!TextUtils.isEmpty(packagesmodel.getPackagename()))
            {
                package_name.setText(packagesmodel.getPackagename());
            }
            if (!TextUtils.isEmpty(packagesmodel.getRate()))
            {
                rate.setText(packagesmodel.getRate());

            }
            if (!TextUtils.isEmpty(packagesmodel.getComment()))
            {
                comment.setText(packagesmodel.getComment());
            }
            if (cartype_ar.contains(packagesmodel.getCar_type()))
            {
                car_type.setSelection(cartype_ar.indexOf(packagesmodel.getCar_type()));

            }
            if (service_ar.contains(packagesmodel.getService()))
            {
                service.setSelection(service_ar.indexOf(packagesmodel.getService()));
            }
            if (!TextUtils.isEmpty(packagesmodel.getCommunication()) && packagesmodel.getCommunication()=="Chat")
            {
                chat.setChecked(true);
            }else{
                call.setChecked(true);
            }


        }
    }
    class Update_Package_detail extends AsyncTask<String, String, String> {
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
                params.add(new BasicNameValuePair("tag", "edit_packages"));
                params.add(new BasicNameValuePair("id", packagesmodel.getP_id()));
                params.add(new BasicNameValuePair("Reg_id", user_id));
                params.add(new BasicNameValuePair("Packge_nm", package_name_str));
                params.add(new BasicNameValuePair("Service_type", service_str));
                params.add(new BasicNameValuePair("City", city_str));
                params.add(new BasicNameValuePair("Car_type", car_str));
                params.add(new BasicNameValuePair("Rate", rate_str));
                params.add(new BasicNameValuePair("Comment", comment_str));
                params.add(new BasicNameValuePair("Communication", commucation_str));
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
                load(new MyPackages());
                Toast.makeText(getActivity(),"Update Service SuccessFully",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getActivity(),"Update Service Not SuccessFully",Toast.LENGTH_LONG).show();
            }

        }
    }
    private void load(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}