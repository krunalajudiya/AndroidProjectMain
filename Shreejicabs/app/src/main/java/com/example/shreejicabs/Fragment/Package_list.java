package com.example.shreejicabs.Fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.SearchView;
import android.widget.TextView;

import com.example.shreejicabs.Adapter.Mypackageadapter;
import com.example.shreejicabs.Adapter.PackagelistAdapter;
import com.example.shreejicabs.Constants;
import com.example.shreejicabs.Model.Packagesmodel;
import com.example.shreejicabs.Other.JSONParser;
import com.example.shreejicabs.R;

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
 * Use the {@link Package_list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Package_list extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String service_type;
    JSONParser jParser = new JSONParser();
    private static final String TAG_SUCCESS = "error";
    JSONArray products = null;
    private ProgressDialog pDialog;
    int success;
    ArrayList<Packagesmodel> packagesmodelArrayList=new ArrayList<>();
    ArrayList<Packagesmodel> packagesfiltermodelArrayList=new ArrayList<>();
    RecyclerView packagerec;
    TextView nopackage;
    private SearchView searchView;
    PackagelistAdapter packagelistAdapter;
    String from,to;

    public Package_list() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Package_list.
     */
    // TODO: Rename and change types and number of parameters
    public static Package_list newInstance(String param1, String param2) {
        Package_list fragment = new Package_list();
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
        packagesfiltermodelArrayList.clear();
        packagesmodelArrayList.clear();
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_package_list, container, false);
        nopackage=(TextView)view.findViewById(R.id.nopackagetxt);
        packagerec=(RecyclerView)view.findViewById(R.id.packagelistrec);
        setHasOptionsMenu(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        packagerec.setLayoutManager(gridLayoutManager);
        packagerec.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int spanCount = 2;
                int spacing = 10;//spacing between views in grid

                if (position >= 0) {
                    int column = position % spanCount; // item column

                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });
        Bundle b=getArguments();

        if (b!=null)
        {
            service_type=b.getString("service");
            from=b.getString("from");
            to=b.getString("to");
        }
        if (TextUtils.equals(service_type,"One way"))
        {
            new OnewayLoadPackagelist().execute();
        }else {
            new LoadPackagelist().execute();
        }
        packagelistAdapter = new PackagelistAdapter(getActivity(), packagesfiltermodelArrayList);
        packagerec.setAdapter(packagelistAdapter);
        packagelistAdapter.setOnItemClickListener(new PackagelistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {

            }

            @Override
            public void onVendornameClick(int pos) {

            }
        });
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                packagelistAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                packagelistAdapter.getFilter().filter(query);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.action_search)
        {

        }
        return super.onOptionsItemSelected(item);
    }
    class LoadPackagelist extends AsyncTask<String, String, String> {
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
                params.add(new BasicNameValuePair("tag", "serch_servicecity"));
                params.add(new BasicNameValuePair("service_type", service_type));
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
                        JSONObject user_detail=c.getJSONObject("user_detail");
                        String driver_id=user_detail.getString("Reg_id");
                        String driver_name=user_detail.getString("Name");
                        String driver_number=user_detail.getString("Mobile_no");
                        String car_type = c.getString("Car_type");
                        String service = c.getString("Service_type");
                        String comment = c.getString("Comment");
                        String communication = c.getString("Communication");


                        packagesmodelArrayList.add(new Packagesmodel(id,packge_name,service,city,car_type,rate,comment,communication,driver_id,driver_name,driver_number));

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

                for (int i=0;i<packagesmodelArrayList.size();i++)
                {
                    List<String> city_ar= Arrays.asList(packagesmodelArrayList.get(i).getCity().split(","));
                    for (int j=0;j<city_ar.size();j++)
                    {
                        packagesfiltermodelArrayList.add(new Packagesmodel(packagesmodelArrayList.get(i).getP_id(),packagesmodelArrayList.get(i).getPackagename(),packagesmodelArrayList.get(i).getService(),city_ar.get(j),packagesmodelArrayList.get(i).getCar_type(),packagesmodelArrayList.get(i).getRate(),packagesmodelArrayList.get(i).getComment(),packagesmodelArrayList.get(i).getCommunication(),packagesmodelArrayList.get(i).getDriverid(),packagesmodelArrayList.get(i).getDrivername(),packagesmodelArrayList.get(i).getDrivernumber()));
                    }
                }
                nopackage.setVisibility(View.GONE);
                if (getActivity()!=null) {
                    packagelistAdapter = new PackagelistAdapter(getActivity(), packagesfiltermodelArrayList);
                    packagerec.setAdapter(packagelistAdapter);
                    packagelistAdapter.setOnItemClickListener(new PackagelistAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {

                        }

                        @Override
                        public void onVendornameClick(int pos) {

                        }
                    });
                }


            }
            else{
                nopackage.setVisibility(View.VISIBLE);
            }

        }
    }
    class OnewayLoadPackagelist extends AsyncTask<String, String, String> {
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
                params.add(new BasicNameValuePair("tag", "onewayfromto"));
                params.add(new BasicNameValuePair("service_type", service_type));
                params.add(new BasicNameValuePair("fromcity", from));
                params.add(new BasicNameValuePair("tocity", to));
                JSONObject json = jParser.makeHttpRequest(Constants.url, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", json.toString());
                success= json.getInt(TAG_SUCCESS);

                if (success == 200) {
                    Log.d("succes", String.valueOf(success));
                    JSONObject c1 = json.getJSONObject("data");
                    // products found
                    // Getting Array of Products
                    products = c1.getJSONArray("datatm1");
                    // looping through All Products

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String id    = c.getString("service_id");
                        String packge_name = c.getString("Packge_name");
                        String city = c.getString("city");
                        String rate = c.getString("Rate");
                        JSONObject user_detail=c.getJSONObject("user_detail");
                        String driver_id=user_detail.getString("Reg_id");
                        String driver_name=user_detail.getString("Name");
                        String driver_number=user_detail.getString("Mobile_no");
                        String car_type = c.getString("Car_type");
                        String service = c.getString("Service_type");
                        String comment = c.getString("Comment");
                        String communication = c.getString("Communication");


                        packagesmodelArrayList.add(new Packagesmodel(id,packge_name,service,city,car_type,rate,comment,communication,driver_id,driver_name,driver_number));

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

                for (int i=0;i<packagesmodelArrayList.size();i++)
                {
                    List<String> city_ar= Arrays.asList(packagesmodelArrayList.get(i).getCity().split(","));
                    for (int j=0;j<city_ar.size();j++)
                    {
                        packagesfiltermodelArrayList.add(new Packagesmodel(packagesmodelArrayList.get(i).getP_id(),packagesmodelArrayList.get(i).getPackagename(),packagesmodelArrayList.get(i).getService(),city_ar.get(j),packagesmodelArrayList.get(i).getCar_type(),packagesmodelArrayList.get(i).getRate(),packagesmodelArrayList.get(i).getComment(),packagesmodelArrayList.get(i).getCommunication(),packagesmodelArrayList.get(i).getDriverid(),packagesmodelArrayList.get(i).getDrivername(),packagesmodelArrayList.get(i).getDrivernumber()));
                    }
                }
                nopackage.setVisibility(View.GONE);
                if (getActivity()!=null) {


                }


            }
            else{
                nopackage.setVisibility(View.VISIBLE);
            }

        }
    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

}