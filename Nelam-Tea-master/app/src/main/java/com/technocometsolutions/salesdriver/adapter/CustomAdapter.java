package com.technocometsolutions.salesdriver.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<DealerListItemModel> {

    LayoutInflater flater;
    ArrayList<DealerListItemModel> list;
    public CustomAdapter(Activity context, int resouceId, int textviewId, ArrayList<DealerListItemModel> list){

        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
        this.list=list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    private View rowview(View convertView , int position){

        DealerListItemModel rowItem = list.get(position);

        viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.view_list_item, null, false);

            holder.txtTitle = (TextView) rowview.findViewById(R.id.tv_product_name1);

            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }

        holder.txtTitle.setText(" "+list.get(position).getDealerName().toString());

        return rowview;
    }

    private class viewHolder{
        TextView txtTitle;

    }
}