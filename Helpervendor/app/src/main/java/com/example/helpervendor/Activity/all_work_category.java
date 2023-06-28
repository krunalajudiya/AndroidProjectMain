package com.example.helpervendor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.helpervendor.Adapter.CustomExpandableListAdapter;
import com.example.helpervendor.Model.Categorymodel;
import com.example.helpervendor.Model.ExpandableListDataPump;
import com.example.helpervendor.Model.Insertresultmodel;
import com.example.helpervendor.R;
import com.example.helpervendor.Remote.Retrofitclient;
import com.github.saeedjassani.ExpandableLayout;
import com.github.saeedjassani.ExpandableLayoutListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class all_work_category extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle=new ArrayList<>();
    HashMap<String,List<Categorymodel.Subcategory>> expandableListDetail=new HashMap<>();

    ProgressDialog pdialog;
    List<Categorymodel.Category> categorymodelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_work_category);
        ExpandableLayout expandableLayout;
        

        expandableListView = findViewById(R.id.expandable_listview);
        //expandableListDetail = ExpandableListDataPump.getData();
        //expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        //expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        //expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
        getcategory();

    }
    public void getcategory()
    {
        pdialog = new ProgressDialog(all_work_category.this);
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
                            for (int i=0;i<categorymodelList.size();i++)
                            {
                                expandableListTitle.add(categorymodelList.get(i).getC_name());
                                expandableListDetail.put(categorymodelList.get(i).getC_name(),categorymodelList.get(i).getSubcategory());
                            }
                            expandableListAdapter=new CustomExpandableListAdapter(all_work_category.this,expandableListTitle,expandableListDetail);
                            expandableListView.setAdapter(expandableListAdapter);

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
}