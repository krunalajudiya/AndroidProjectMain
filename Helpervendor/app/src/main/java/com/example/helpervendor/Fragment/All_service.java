package com.example.helpervendor.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpervendor.Activity.all_work_category;
import com.example.helpervendor.Adapter.Categoryadapter;
import com.example.helpervendor.Adapter.CustomExpandableListAdapter;
import com.example.helpervendor.Model.Categorymodel;
import com.example.helpervendor.Model.Insertresultmodel;
import com.example.helpervendor.Model.Resultmodel;
import com.example.helpervendor.R;
import com.example.helpervendor.Remote.Retrofitclient;
import com.example.helpervendor.Session.UserSession;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link All_service#newInstance} factory method to
 * create an instance of this fragment.
 */
public class All_service extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView categoryrec;
    List<Categorymodel.Category> categorymodelList;
    ArrayList<LinearLayout> linearLayoutArrayList=new ArrayList<>();
    ProgressDialog pdialog;
    LinearLayout sub_cat_layout;
    TextView total_selected_item;
    Button add_service;
    int total_service=0;
    ArrayList<String> total_sub_cat_ar=new ArrayList<>();
    String total_sub_cat;
    UserSession userSession;
    Resultmodel.Data data;
    List<String> subcategory_ar=new ArrayList<>();
    TextView warning_txt;


    public All_service() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment All_service.
     */
    // TODO: Rename and change types and number of parameters
    public static All_service newInstance(String param1, String param2) {
        All_service fragment = new All_service();
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
        View view= inflater.inflate(R.layout.fragment_all_service, container, false);

        categoryrec=(RecyclerView)view.findViewById(R.id.categoryrec);
        sub_cat_layout=(LinearLayout)view.findViewById(R.id.sub_cat_layout);
        total_selected_item=(TextView)view.findViewById(R.id.total_selected_service);
        add_service=(Button)view.findViewById(R.id.add_service);
        warning_txt=view.findViewById(R.id.warning_txt);

        userSession =new UserSession(getActivity());
        Gson gson=new Gson();
        data=gson.fromJson(userSession.getUserDetails(), Resultmodel.Data.class);
        if (data.getSub_cat()!=null && !TextUtils.isEmpty(data.getSub_cat()))
        {
            subcategory_ar= Arrays.asList(data.getSub_cat().split(","));
            add_service.setEnabled(false);
            warning_txt.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        categoryrec.setLayoutManager(linearLayoutManager);
        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    total_sub_cat= TextUtils.join(",",total_sub_cat_ar);
                    Log.d("sub_cat_name",total_sub_cat);
                    Add_service();
            }
        });
        getcategory();
        return view;
    }
    public void getcategory()
    {
        pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Categorymodel> call = Retrofitclient.getInstance().getMyApi().Category("cat_sub_cat_details");
        call.enqueue(new Callback<Categorymodel>() {
            @Override
            public void onResponse(Call<Categorymodel> call, Response<Categorymodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Categorymodel categorymodel = response.body();
                    Log.d("fffff111", categorymodel.getError());
                    if (Integer.parseInt(categorymodel.getError()) == 200) {
                        categorymodelList=new ArrayList<>();
                        categorymodelList=categorymodel.getData().getCategory();
                        Categoryadapter categoryadapter=new Categoryadapter(getActivity(),categorymodelList);
                        categoryrec.setAdapter(categoryadapter);
                        for (int i=0;i<categorymodelList.size();i++)
                        {
                            LinearLayout linearLayout=new LinearLayout(getActivity());
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            for (int j=0;j<categorymodelList.get(i).getSubcategory().size();j++)
                            {
                                CheckBox checkBox=new CheckBox(getActivity());
                                checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                checkBox.setText(categorymodelList.get(i).getSubcategory().get(j).getSub_name());
                                for (int p=0;p<subcategory_ar.size();p++)
                                {
                                    if (Integer.parseInt(categorymodelList.get(i).getSubcategory().get(j).getSub_id())==Integer.parseInt(subcategory_ar.get(p)))
                                    {
                                        checkBox.setChecked(true);
                                    }
                                }
                                linearLayout.addView(checkBox);
                                int finalI = i;
                                int finalJ = j;
                                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked)
                                        {
                                            total_sub_cat_ar.add(categorymodelList.get(finalI).getSubcategory().get(finalJ).getSub_id());
                                            total_service=total_service+1;
                                            total_selected_item.setText("Total service :"+String.valueOf(total_service));
                                        }else{
                                            if (total_sub_cat_ar.contains(categorymodelList.get(finalI).getSubcategory().get(finalJ).getSub_id()))
                                            {
                                                total_sub_cat_ar.remove(total_sub_cat_ar.indexOf(categorymodelList.get(finalI).getSubcategory().get(finalJ).getSub_id()));
                                            }
                                            total_service=total_service-1;
                                            total_selected_item.setText("Total service :"+String.valueOf(total_service));
                                        }
                                    }
                                });
                                Log.d("sss",categorymodelList.get(i).getSubcategory().get(j).getSub_name());

                            }
                            linearLayoutArrayList.add(linearLayout);
                        }
                        sub_cat_layout.removeAllViews();
                        sub_cat_layout.addView(linearLayoutArrayList.get(0));
                        categoryadapter.setOnItemClickListener(new Categoryadapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int pos, View v) {
                               // Log.d("pos", String.valueOf(pos));
                                sub_cat_layout.removeAllViews();
                                sub_cat_layout.addView(linearLayoutArrayList.get(pos));

                            }
                        });

                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Categorymodel> call, Throwable t) {
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
                pdialog.dismiss();
            }
        });

    }
    public void Add_service()
    {
        pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        pdialog.show();
        Call<Insertresultmodel> call = Retrofitclient.getInstance().getMyApi().Add_service("add_services",data.getVendor_id(),total_sub_cat);
        call.enqueue(new Callback<Insertresultmodel>() {
            @Override
            public void onResponse(Call<Insertresultmodel> call, Response<Insertresultmodel> response) {
                pdialog.dismiss();
                //In this point we got our hero list
                //thats damn easy right ;)
                if (response.isSuccessful()) {
                    Insertresultmodel insertresultmodel = response.body();
                    Log.d("fffff111", insertresultmodel.getError());
                    if (Integer.parseInt(insertresultmodel.getError()) == 200) {
                        data.setService_status("1");
                        data.setSub_cat(total_sub_cat);
                        String detail;
                        Gson gson=new Gson();
                        detail=gson.toJson(data);
                        userSession.createLoginSession(detail);
                        Toast.makeText(getActivity(),"Add service Successfully",Toast.LENGTH_LONG).show();
                    }
                }


                //now we can do whatever we want with this list
            }

            @Override
            public void onFailure(Call<Insertresultmodel> call, Throwable t) {
                //handle error or failure cases here
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("fffff",t.getMessage());
                pdialog.dismiss();
            }
        });
    }

}