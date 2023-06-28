package com.example.shreejicabs.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shreejicabs.Adapter.Allavaliabilityadapter;
import com.example.shreejicabs.Adapter.SearchAvaliabilityadapter;
import com.example.shreejicabs.BuildConfig;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Avaliabilitymodel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link All_avaliability_display#newInstance} factory method to
 * create an instance of this fragment.
 */
public class All_avaliability_display extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    RecyclerView avaliabilityrec;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;
    ArrayList<Avaliabilitymodel> avaliabilitymodelArrayList=new ArrayList<>();
    ArrayList<String> city=new ArrayList<>();
    TextView noavaliability;
    Calendar myCalendar;
    String from_str,to_str,date_str;
    EditText date;
    AutoCompleteTextView from,to;
    AlertDialog alertDialog;
    Button search;
    private OptionsFabLayout fabWithOptions;
    UserSession userSession;
    User user;
    Button citypref_btn;
    LottieAnimationView loading_screen;
    private final int REQUEST_PERMISSION = 1001;

    int position;
    String button_status;


    public All_avaliability_display() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment All_avaliability_display.
     */
    // TODO: Rename and change types and number of parameters
    public static All_avaliability_display newInstance(String param1, String param2) {
        All_avaliability_display fragment = new All_avaliability_display();
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
        avaliabilitymodelArrayList.clear();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_avaliability_display, container, false);
        setHasOptionsMenu(true);
        loading_screen=(LottieAnimationView)view.findViewById(R.id.loading_screeen);
        loading_screen.setVisibility(View.GONE);
        noavaliability=(TextView)view.findViewById(R.id.noavaliabilitytxt);
        avaliabilityrec=(RecyclerView)view.findViewById(R.id.avaliabilityrec);
        fabWithOptions = (OptionsFabLayout)view.findViewById(R.id.fab_l);
        citypref_btn=(Button)view.findViewById(R.id.cityprefbtn);
        userSession=new UserSession(getContext());
        Gson gson=new Gson();
        user=gson.fromJson(userSession.getUserDetails(),User.class);
        //Set mini fab's colors.
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
        //Set mini fabs clicklisteners.
        fabWithOptions.setMiniFabSelectedListener(new OptionsFabLayout.OnMiniFabSelectedListener() {
            @Override
            public void onMiniFabSelected(MenuItem fabItem) {
                switch (fabItem.getItemId()) {
                    case R.id.fab_add:
                        loadFragment(new Add_avaliability());
                        Log.d("addddd","adddd");
                        break;
                    case R.id.fab_edit:
                        loadFragment(new MyAvaliability());
                        Log.d("myyyyyy","muuuuu");
                    default:
                        break;
                }
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        avaliabilityrec.setLayoutManager(linearLayoutManager);
        myCalendar = Calendar.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_search_item, viewGroup, false);
        from=(AutoCompleteTextView)dialogView.findViewById(R.id.from);
        to=(AutoCompleteTextView)dialogView.findViewById(R.id.to);
        date=(EditText)dialogView.findViewById(R.id.date);
        search=(Button)dialogView.findViewById(R.id.search);
        builder.setView(dialogView);
        builder.setCancelable(true);
        alertDialog = builder.create();
        if (TextUtils.isEmpty(user.getCity_pref().trim()))
        {
            avaliabilityrec.setVisibility(View.GONE);
            noavaliability.setVisibility(View.GONE);
            citypref_btn.setVisibility(View.VISIBLE);
        }else {
            citypref_btn.setVisibility(View.GONE);
            new LoadAvaliabity().execute();
            new LoadAllcity().execute();
        }
        citypref_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new City_preference());
            }
        });

//        Allavaliabilityadapter allavaliabilityadapter = new Allavaliabilityadapter(getActivity(), avaliabilitymodelArrayList);
//        avaliabilityrec.setAdapter(allavaliabilityadapter);
//        allavaliabilityadapter.setOnItemClickListener(new Allavaliabilityadapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int pos, View v) {
//
//            }
//
//            @Override
//            public void onDriverNameClick(int pos) {
//                String id=avaliabilitymodelArrayList.get(pos).getDriverid();
//                loadFragment(new profile_show(id));
//            }
//        });
        return view;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.add_required_menu, menu);
        menu.removeItem(R.id.add_required_m);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.search_avaliability)
        {

            alertDialog.show();
            final DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(getActivity(),R.style.MyAlertDialogStyle, datelistener, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avaliabilitymodelArrayList.clear();
                    getvalue();
                    if (TextUtils.isEmpty(from_str))
                    {
                     from.setError("Please Enter From");
                     from.requestFocus();
                    }else {
                        new LoadSearchAvaliabity().execute();
                        alertDialog.dismiss();
                    }

                }
            });


        }
        return super.onOptionsItemSelected(item);
    }
    public void getvalue()
    {
        date_str=date.getText().toString();
        from_str=from.getText().toString();
        to_str=to.getText().toString();
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        date.setText(sdf.format(myCalendar.getTime()));
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
    class LoadAvaliabity extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            avaliabilitymodelArrayList.clear();
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
//            pDialog.show();
            loading_screen.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "All_availablity"));
                params.add(new BasicNameValuePair("cities", user.getCity_pref()));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    products = c1.getJSONArray("aviid");
                    // looping through All Products

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String id    = c.getString("availablity_id");
                        String from = c.getString("From_city");
                        String to = c.getString("To_city");
                        String date = c.getString("dt");
                        String time = c.getString("tm");
                        JSONObject user_detail=c.getJSONObject("user_detail");
                        String driver_id=user_detail.getString("Reg_id");
                        String driver_name=user_detail.getString("Name");
                        String driver_number=user_detail.getString("Mobile_no");
                        String car_type = c.getString("Car_type");
                        String service = c.getString("Service_type");
                        String comment = c.getString("Comment");
                        String communication = c.getString("Communication");
                        String Create_at=c.getString("Create_at");


                        avaliabilitymodelArrayList.add(new Avaliabilitymodel(id,from,to,date,time,car_type,service,comment,communication,driver_id,driver_name,driver_number,Create_at));

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
                noavaliability.setVisibility(View.GONE);
                if (getActivity()!=null) {

                    Allavaliabilityadapter allavaliabilityadapter = new Allavaliabilityadapter(getActivity(), avaliabilitymodelArrayList);
                    avaliabilityrec.setAdapter(allavaliabilityadapter);
                    allavaliabilityadapter.setOnItemClickListener(new Allavaliabilityadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {

                        }

                        @Override
                        public void onDriverNameClick(int pos) {
                            String id=avaliabilitymodelArrayList.get(pos).getDriverid();
                            loadFragment(new profile_show(id));
                        }

                        @Override
                        public void onShareClick(int pos) {
                            position=pos;
                            button_status="2";
                            new ReportClick().execute();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            String shareMessage;
                            shareMessage="Taxi Available: "+avaliabilitymodelArrayList.get(pos).getFrom()+ " To "+avaliabilitymodelArrayList.get(pos).getTo()+
                                    "\nDate : "+avaliabilitymodelArrayList.get(pos).getDate()+","+avaliabilitymodelArrayList.get(pos).getTime()+
                                    "\nVehicle : "+avaliabilitymodelArrayList.get(pos).getCartype()+"\nComment : "+avaliabilitymodelArrayList.get(pos).getComment()+
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
                                intent.setData(Uri.parse("tel:" + avaliabilitymodelArrayList.get(pos).getDrivernumber()));
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
                                intent.setData(Uri.parse("https://wa.me/"+"+91"+avaliabilitymodelArrayList.get(pos).getDrivernumber()+"?text="+""));
                                getActivity().startActivity(intent);
                            }else {
                                Toast.makeText(getActivity(),"whatsapp not install in you phone",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }



            }else{
                noavaliability.setVisibility(View.VISIBLE);
                avaliabilityrec.setVisibility(View.GONE);
            }
            //new LoadAllcity().execute();

        }
    }
    class LoadSearchAvaliabity extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            avaliabilitymodelArrayList.clear();
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
//            pDialog.show();
            loading_screen.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tag", "datetimeavailable"));
                params.add(new BasicNameValuePair("date", date_str));
                params.add(new BasicNameValuePair("fromcity", from_str));
                params.add(new BasicNameValuePair("tocity", to_str));
                Log.d("fromcity",from_str);
                Log.d("tocity",to_str);
                //Log.d("date",date_str);
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    products = c1.getJSONArray("datatm");
                    // looping through All Products

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String id    = c.getString("Available_id");
                        String from = c.getString("From_city");
                        String to = c.getString("To_city");
                        String date = c.getString("dt");
                        String time = c.getString("tm");
                        JSONObject user_detail=c.getJSONObject("user_detail");
                        String driver_id=user_detail.getString("Reg_id");
                        String driver_name=user_detail.getString("Name");
                        String driver_number=user_detail.getString("Mobile_no");
                        String car_type = c.getString("Car_type");
                        String service = c.getString("Service_type");
                        String comment = c.getString("Comment");
                        String communication = c.getString("Communication");
                        String Create_at=c.getString("Create_at");


                        avaliabilitymodelArrayList.add(new Avaliabilitymodel(id,from,to,date,time,car_type,service,comment,communication,driver_id,driver_name,driver_number,Create_at));

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
                noavaliability.setVisibility(View.GONE);
                if (getActivity()!=null) {

                    Allavaliabilityadapter allavaliabilityadapter = new Allavaliabilityadapter(getActivity(), avaliabilitymodelArrayList);
                    avaliabilityrec.setAdapter(allavaliabilityadapter);
                    allavaliabilityadapter.setOnItemClickListener(new Allavaliabilityadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {

                        }

                        @Override
                        public void onDriverNameClick(int pos) {
                            String id=avaliabilitymodelArrayList.get(pos).getDriverid();
                            loadFragment(new profile_show(id));
                        }

                        @Override
                        public void onShareClick(int pos) {
                            position=pos;
                            button_status="2";
                            new ReportClick().execute();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            String shareMessage;
                            shareMessage="Taxi Available: "+avaliabilitymodelArrayList.get(pos).getFrom()+ " To "+avaliabilitymodelArrayList.get(pos).getTo()+
                                    "\nDate : "+avaliabilitymodelArrayList.get(pos).getDate()+","+avaliabilitymodelArrayList.get(pos).getTime()+
                                    "\nVehicle : "+avaliabilitymodelArrayList.get(pos).getCartype()+"\nComment : "+avaliabilitymodelArrayList.get(pos).getComment()+
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
                                intent.setData(Uri.parse("tel:" + avaliabilitymodelArrayList.get(pos).getDrivernumber()));
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
                                intent.setData(Uri.parse("https://wa.me/"+"+91"+avaliabilitymodelArrayList.get(pos).getDrivernumber()+"?text="+""));
                                getActivity().startActivity(intent);
                            }else {
                                Toast.makeText(getActivity(),"whatsapp not install in you phone",Toast.LENGTH_LONG).show();
                            }
                        }
                    });




                }


            }else{
                noavaliability.setVisibility(View.VISIBLE);
                avaliabilityrec.setVisibility(View.GONE);
            }

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
                params.add(new BasicNameValuePair("d_id", avaliabilitymodelArrayList.get(position).getId()));
                params.add(new BasicNameValuePair("btn_status",button_status));
                params.add(new BasicNameValuePair("table_status","1"));


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

}