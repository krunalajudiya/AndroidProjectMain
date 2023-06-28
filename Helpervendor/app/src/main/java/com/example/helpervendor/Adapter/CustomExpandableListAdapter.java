package com.example.helpervendor.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.helpervendor.Model.Categorymodel;
import com.example.helpervendor.R;

import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String,List<Categorymodel.Subcategory>> expandableListDetail;


    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<Categorymodel.Subcategory>> expandableListDetail){
        this.context=context;
        this.expandableListTitle=expandableListTitle;
        this.expandableListDetail=expandableListDetail;
    }
    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return this.expandableListTitle.size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listitle= (String) getGroup(groupPosition);
        if (convertView==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.list_category,null);
        }
        TextView listTitleTextView=convertView.findViewById(R.id.list_category);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //final String expandedListText= (String) getChild(groupPosition,childPosition);
        if (convertView==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.sublist_category,null);
        }
        LinearLayout linearLayout1=convertView.findViewById(R.id.child_show_data);

//            for (int k=0;k<expandableListTitle.size();k++){
                LinearLayout linearLayout=new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                for (int i=0;i<expandableListDetail.get(expandableListTitle.get(groupPosition)).size();i++){
                    LinearLayout row =new LinearLayout(context);
                    row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    CheckBox checkBox=new CheckBox(context);
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout1.addView(checkBox);
                    linearLayout.addView(row);
                }

                Button button=new Button(context);
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout1.addView(button);
                //linearLayout.addView(button);


//        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
