package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.activity.OrderScNewActivity;
import com.technocometsolutions.salesdriver.model.CategoryItemModel;

import com.technocometsolutions.salesdriver.model.ProductCatModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CategoryItemModel> continentList;
    private List<CategoryItemModel> originalList;

    public MyListAdapter(Context context, List<CategoryItemModel> continentList) {
        this.context = context;
        this.continentList = new ArrayList<CategoryItemModel>();
        this.continentList=continentList;
        this.originalList = new ArrayList<CategoryItemModel>();
        this.originalList=continentList;
        Log.d("Dixit", "getGroupCount: "+continentList.size());
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ProductCatModel> countryList = continentList.get(groupPosition).getProducts();
        Log.d("Dixit", "getGroupCount: "+countryList.size());
        return countryList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //Holder holder;
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,View view, ViewGroup parent) {


        ProductCatModel country = (ProductCatModel) getChild(groupPosition, childPosition);

        if (view == null) {
            TextView tv_product_name;
            TextView tv_price;
            TextView tv_wait;
            EditText integer_number;
           // holder = new Holder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.child_row, null);
            tv_product_name =  view.findViewById(R.id.tv_product_name);
            tv_price =  view.findViewById(R.id.price);
            tv_wait =  view.findViewById(R.id.tv_wait);
            integer_number = view.findViewById(R.id.integer_number);
            tv_price.setText("₹"+country.getPrice());
            tv_wait.setText(""+country.getWeight()+" KG");
            tv_product_name.setText(country.getProduct().trim());

            integer_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() > 0)
                    {

                        double total_price= Double.parseDouble(country.getPrice()) * Double.parseDouble(s.toString());
                        double total_weight= Double.parseDouble(country.getWeight()) * Double.parseDouble(s.toString());


                        continentList.get(groupPosition).getProducts().get(childPosition).setTotal_price(total_price);
                        continentList.get(groupPosition).getProducts().get(childPosition).setTotal_qty(Double.parseDouble(""+s.toString()));
                        continentList.get(groupPosition).getProducts().get(childPosition).setTotal_kg(total_weight);
                        // integer_number.setText(s.toString());
                        tv_price.setText("₹"+total_price);
                        tv_wait.setText(""+total_weight+" KG");
                        OrderScNewActivity.getActivity().getProgucteed();



                    }
                    else
                    {
                        tv_price.setText("₹ 0");
                        tv_wait.setText("0 KG");
                    }
                }
            });
        }




       // ImageView decrease=view.findViewById(R.id.decrease);
       // ImageView increase=view.findViewById(R.id.increase);
        // TextView population = (TextView) view.findViewById(R.id.population);


       /* decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (country.getMinteger()>0)
                {
                    country.setMinteger(country.getMinteger() - 1);
                     integer_number.setText(""+country.getMinteger());
                    // new_price=new_price / minteger;
                    // new_price1 = new_price * minteger;
                    // price.setText("$"+new_price1);
                }
                else
                {
                   integer_number.setText(""+0);
                  // price.setText("$"+new_price);
                }
                // Log.d("Dixit", "onClick: "+integer_number);
            }
        });*/
        /*increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
               // minteger = minteger + 1;
                country.setMinteger(country.getMinteger() + 1);
                integer_number.setText(""+country.getMinteger());
                // new_price1=new_price * minteger;
                // new_price=new_price * minteger;
                // price.setText("$"+new_price1);
            }
        });*/
      // population.setText(NumberFormat.getNumberInstance(Locale.US).format(country.getPopulation()));
        return view;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        List<ProductCatModel> countryList = continentList.get(groupPosition).getProducts();
        return countryList.size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return continentList.get(groupPosition);
    }
    @Override
    public int getGroupCount() {
        Log.d("Dixit", "getGroupCount: "+continentList.size());
        return continentList.size();
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
        CategoryItemModel continent = (CategoryItemModel) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.group_row, null);
        }
        TextView heading = view.findViewById(R.id.heading);
        heading.setText(continent.getCategory().trim());
        return view;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    public void filterData(String query){
        query = query.toLowerCase();
        Log.v("MyListAdapter", String.valueOf(continentList.size()));
        continentList.clear();
        if(query.isEmpty()){
            continentList.addAll(originalList);
        }
        else {
            for(CategoryItemModel continent: originalList){
                List<ProductCatModel> countryList = continent.getProducts();
                List<ProductCatModel> newList = new ArrayList<ProductCatModel>();
                for(ProductCatModel country: countryList){
                    if(country.getProduct().toLowerCase().contains(query) ||
                            country.getProduct().toLowerCase().contains(query)){
                        newList.add(country);
                    }
                }
                if(newList.size() > 0){
                    CategoryItemModel nContinent = new CategoryItemModel(continent.getCategory(),newList);
                    continentList.add(nContinent);
                }
            }
        }
        Log.v("MyListAdapter", String.valueOf(continentList.size()));
        notifyDataSetChanged();
    }

   public class Holder {
        public TextView tv_product_name;
        public TextView tv_price;
        public TextView tv_wait;
        public EditText integer_number;

    }
}