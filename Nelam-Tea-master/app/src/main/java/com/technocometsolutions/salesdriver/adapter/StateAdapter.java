package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.technocometsolutions.salesdriver.R;

import java.util.ArrayList;
import java.util.Collection;

import gr.escsoft.michaelprimez.searchablespinner.interfaces.ISpinnerSelectedView;

public class StateAdapter extends BaseAdapter implements Filterable, ISpinnerSelectedView {

    private Context mContext;
    private ArrayList<String> mBackupStrings;
    private ArrayList<String> mStrings;
    private StringFilter mStringFilter = new StringFilter();
    private String name;

    public StateAdapter(Context context, ArrayList<String> strings,String name) {
        mContext = context;
        mStrings = strings;
        mBackupStrings = strings;
        this.name=name;
    }

    @Override
    public int getCount() {
        return mStrings == null ? 0 : mStrings.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (mStrings != null && position > 0)
            return mStrings.get(position - 1);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        if (mStrings == null && position > 0)
            return mStrings.get(position).hashCode();
        else
            return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else {
            view = View.inflate(mContext, R.layout.view_list_item, null);

            TextView dispalyName = (TextView) view.findViewById(R.id.tv_product_name1);

            dispalyName.setText(Html.fromHtml(mStrings.get(position-1)));
        }
        return view;
    }

    @Override
    public View getSelectedView(int position) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else {
            view = View.inflate(mContext, R.layout.view_list_item, null);

            TextView dispalyName = (TextView) view.findViewById(R.id.tv_product_name1);

            dispalyName.setText(Html.fromHtml(mStrings.get(position-1)));
        }
        return view;
    }

    @Override
    public View getNoSelectionView() {
        View view = View.inflate(mContext, R.layout.view_list_no_selection_item, null);
        TextView dispalyName = (TextView) view.findViewById(R.id.tv_product_name);
        dispalyName.setText(Html.fromHtml(""+name));
        return view;
    }


    @Override
    public Filter getFilter() {
        return mStringFilter;
    }

    public class StringFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            final FilterResults filterResults = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                filterResults.count = mBackupStrings.size();
                filterResults.values = mBackupStrings;
                return filterResults;
            }
            final ArrayList<String> filterStrings = new ArrayList<>();
            for (String text : mBackupStrings) {
                if (text.toLowerCase().contains(constraint.toString().toLowerCase().trim()) || text.toUpperCase().contains(constraint.toString().toUpperCase().trim())) {
                    filterStrings.add(text);
                }
            }
            filterResults.count = filterStrings.size();
            filterResults.values = filterStrings;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //mStrings.clear();
            //mStrings.addAll((Collection<? extends String>) results.values);
            mStrings = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    }

    private class ItemView {
        public ImageView mImageView;
        public TextView mTextView;
    }

    public enum ItemViewType {
        ITEM, NO_SELECTION_ITEM;
    }
}