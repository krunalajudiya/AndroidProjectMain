package com.example.shreejicabs.Fragment;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shreejicabs.Adapter.Requirementvehicleadapter;
import com.example.shreejicabs.Adapter.SearchAvaliabilityadapter;
import com.example.shreejicabs.BuildConfig;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.MainActivity;
import com.example.shreejicabs.Model.Avaliabilitymodel;
import com.example.shreejicabs.Model.Requirementmodel;
import com.example.shreejicabs.Model.User;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;
import com.example.shreejicabs.Session.UserSession;
import com.github.ag.floatingactionmenu.OptionsFabLayout;
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
 * Use the {@link required_Vehicle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class required_Vehicle extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView requirementrec;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;
    ArrayList<Requirementmodel> requirementmodelArrayList=new ArrayList<>();
    TextView norequirement;
    UserSession userSession;
    User user;
    Button citypref_btn;
    private OptionsFabLayout fabWithOptions;
    LottieAnimationView progresbar;
    int reportpos;
    Requirementvehicleadapter requirementvehicleadapter;

    private final int REQUEST_PERMISSION = 1001;

    int position;
    String button_status;

    public required_Vehicle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment required_Vehicle.
     */
    // TODO: Rename and change types and number of parameters
    public static required_Vehicle newInstance(String param1, String param2) {
        required_Vehicle fragment = new required_Vehicle();
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
        requirementmodelArrayList.clear();
        View view= inflater.inflate(R.layout.fragment_required__vehicle, container, false);
        requirementrec=(RecyclerView)view.findViewById(R.id.requiredvehiclelist);

        progresbar=view.findViewById(R.id.progres_bar);
        norequirement=(TextView)view.findViewById(R.id.norequiredtxt);
        citypref_btn=(Button)view.findViewById(R.id.cityprefbtn);
        fabWithOptions = (OptionsFabLayout)view.findViewById(R.id.fab_l);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        requirementrec.setLayoutManager(linearLayoutManager);
        userSession=new UserSession(getContext());

        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(), User.class);
        Log.d("city_pref",user.getCity_pref());
        if (TextUtils.isEmpty(user.getCity_pref().trim()))
        {
            requirementrec.setVisibility(View.GONE);
            norequirement.setVisibility(View.GONE);
            citypref_btn.setVisibility(View.VISIBLE);
            progresbar.setVisibility(View.GONE);
        }else {
            citypref_btn.setVisibility(View.GONE);
            new LoadRequirements().execute();
//            progresbar.setVisibility(View.GONE);
        }
        citypref_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.title.setText("City");
                loadFragment(new City_preference());
            }
        });

        fabWithOptions.setMiniFabsColors(
                R.color.colorPrimary,
                R.color.green_fab);

        //Set main fab clicklistener.
        fabWithOptions.setMainFabOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Main fab clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        fabWithOptions.setMiniFabSelectedListener(new OptionsFabLayout.OnMiniFabSelectedListener() {
            @Override
            public void onMiniFabSelected(MenuItem fabItem) {
                switch (fabItem.getItemId()) {
                    case R.id.fab_add:
                        loadFragment(new Add_required_vehicle());
                        Log.d("addddd","adddd");
                        break;
                    case R.id.fab_edit:
                        loadFragment(new My_required_vehicle());
                        Log.d("myyyyyy","muuuuu");
                    default:
                        break;
                }
            }
        });


        return view;

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.add_required_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.search_avaliability);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.add_required_m)
        {
            loadFragment(new Add_required_vehicle());


        }
        return super.onOptionsItemSelected(item);
    }
    class LoadRequirements extends AsyncTask<String, String, String> {
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
                params.add(new BasicNameValuePair("tag", "All_requirements"));
                params.add(new BasicNameValuePair("cities", user.getCity_pref()));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);
                Log.d("city",user.getCity_pref());

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    products = c1.getJSONArray("driverid");
                    // looping through All Products

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String id    = c.getString("Requirement_id");
//                        if(c.getString("User_Name")!=null){
//                            String name=c.getString("User_Name");
//                            Log.d("name",name);
//
//                        }
                        JSONObject user_detail=c.getJSONObject("user_detail");
                        String driver_id=null,driver_name,driver_number;
                        //Log.d("comp", String.valueOf());
                        if (!TextUtils.equals(user_detail.getString("Reg_id"),"null")){
                            driver_id=user_detail.getString("Reg_id");
                            driver_name=user_detail.getString("Name");
                            driver_number=user_detail.getString("Mobile_no");
                            Log.d("Reg_id",driver_id);
                        }
                        else {
                            driver_name=user_detail.getString("Name");
                            driver_number=user_detail.getString("Mobile_no");
                        }

                        String from = c.getString("From_city");
                        String to = c.getString("To_city");
                        String date = c.getString("dt");
                        String time = c.getString("tm");
                        String car_type = c.getString("Car_type");
                        String service = c.getString("Service_type");
                        String comment = c.getString("Comment");
                        String communication = c.getString("Communication");
                        String fraud_status=c.getString("froud_status");
                        String Create_at=c.getString("Create_at");

                        requirementmodelArrayList.add(new Requirementmodel(id,from,to,date,time,car_type,service,comment,communication,driver_id,driver_name,driver_number,fraud_status,Create_at));

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
            progresbar.setVisibility(View.GONE);
            if (success==200) {
                norequirement.setVisibility(View.GONE);
                if (getActivity()!=null) {
                    requirementvehicleadapter = new Requirementvehicleadapter(getActivity(), requirementmodelArrayList);
                    requirementrec.setAdapter(requirementvehicleadapter);
                    requirementvehicleadapter.setOnItemClickListener(new Requirementvehicleadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {

                        }

                        @Override
                        public void onFraudClick(int pos) {
                            Log.d("ff", String.valueOf(pos));
                            new ReportFraud().execute();

                            position=pos;
                            button_status="1";
                            new ReportClick().execute();
                        }

                        @Override
                        public void onCompanyProfileClick(int pos) {
                            //Log.d("driver_id", String.valueOf(TextUtils.equals(driver_id,"null")));
                            //Log.d("driver_", String.valueOf());
                            if (requirementmodelArrayList.get(pos).getDriverid()!=null){
                                MainActivity.title.setText("Profile");
                                loadFragment(new profile_show(requirementmodelArrayList.get(pos).getDriverid()));
                            }

//                Log.d("ff_id", String.valueOf(id));
                        }

                        @Override
                        public void onShareClick(int pos) {


                            Log.d("share","share");
                            position=pos;
                            button_status="2";
                            new ReportClick().execute();

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            String shareMessage;
                            shareMessage="Required Taxi : "+requirementmodelArrayList.get(position).getFrom()+ " To "+requirementmodelArrayList.get(position).getTo()+
                                    "\nDate : "+requirementmodelArrayList.get(position).getDate()+","+requirementmodelArrayList.get(position).getTime()+
                                    "\nVehicle : "+requirementmodelArrayList.get(position).getCartype()+"\nComment : "+requirementmodelArrayList.get(position).getComment()+
                                    "\n\nPut Your Taxi Availability on TaxiTrip App and Share,absolutely free."+
                                    "\n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                            intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                            intent.setType("text/plain");
                            getActivity().startActivity(intent);
                        }

                        @Override
                        public void onCallClick(int pos) {


                            position=pos;
                            button_status="3";
                            new ReportClick().execute();

                            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                            {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION);
                            }else {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + requirementmodelArrayList.get(position).getDrivernumber()));
                                getActivity().startActivity(intent);
                            }
                        }

                        @Override
                        public void onChatClick(int pos) {

                            position=pos;
                            button_status="4";
                            new ReportClick().execute();

                            PackageManager packageManager=getActivity().getPackageManager();
                            boolean app_installed=false;
                            String url = null;
                            Boolean first=true;
                            if (first){
                                try{
                                    packageManager.getPackageInfo("com.whatsapp.w4b",PackageManager.GET_PERMISSIONS);
                                    app_installed=true;
                                    url="com.whatsapp.w4b";
                                    first=false;
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                    app_installed=false;
                                }
                            }
                            if (first){
                                try{
                                    packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_PERMISSIONS);
                                    app_installed=true;
                                    url="com.whatsapp";
                                    first=false;
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                    app_installed=false;
                                    first=false;
                                }
                            }


                            if (app_installed){
                                Intent intent=new Intent(Intent.ACTION_VIEW);
                                intent.setPackage(url);
                                intent.setData(Uri.parse("https://wa.me/"+"+91"+requirementmodelArrayList.get(position).getDrivernumber()+"?text="+""));
                                getActivity().startActivity(intent);
                            }else {
                                Toast.makeText(getActivity(),"whatsapp not install in you phone",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


            }else{
                norequirement.setVisibility(View.VISIBLE);
            }

        }
    }

    class ReportFraud extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                List<NameValuePair> ReportValue=new ArrayList<>();
                ReportValue.add(new BasicNameValuePair("tag","update_froud_status"));
                ReportValue.add(new BasicNameValuePair("id",requirementmodelArrayList.get(reportpos).getId()));
                ReportValue.add(new BasicNameValuePair("user_id",user.getId()));
                JSONObject jsonObject1=jParser.makeHttpRequest(Constants.url,"POST",ReportValue);
                if (jsonObject1.getInt("error")==200){

                }
            }catch (Exception e){

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
        }

    }

    class ReportClick extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "report_btn"));
                params.add(new BasicNameValuePair("Reg_id", user.getId()));
                params.add(new BasicNameValuePair("d_id", requirementmodelArrayList.get(position).getId()));
                params.add(new BasicNameValuePair("btn_status",button_status));
                params.add(new BasicNameValuePair("table_status","2"));


                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    //JSONObject c1 = json.getJSONObject("data");
                    Log.d("report","add");



                } else {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
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