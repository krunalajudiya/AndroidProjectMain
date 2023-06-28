package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;

import java.util.ArrayList;

import gr.escsoft.michaelprimez.searchablespinner.interfaces.ISpinnerSelectedView;

public class StateAdapterNew extends BaseAdapter implements Filterable, ISpinnerSelectedView {

    private Context mContext;
    private ArrayList<DealerListItemModel> mBackupStrings;
    private ArrayList<DealerListItemModel> mStrings;
    private StringFilter mStringFilter = new StringFilter();
    private String name;

    public StateAdapterNew(Context context, ArrayList<DealerListItemModel> strings) {
        mContext = context;
        mStrings = strings;
        mBackupStrings = strings;

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

            dispalyName.setText(mStrings.get(position-1).getDealerName());
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

            dispalyName.setText(mStrings.get(position-1).getDealerName());
        }
        return view;
    }

    @Override
    public View getNoSelectionView() {
        View view = View.inflate(mContext, R.layout.view_list_no_selection_item, null);
        TextView dispalyName = (TextView) view.findViewById(R.id.tv_product_name);
        dispalyName.setText(""+name);
        return view;
    }


    @Override
    public Filter getFilter() {
        return mStringFilter;

    }





    public class StringFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DealerListItemModel> dealerListItemModel=new ArrayList<DealerListItemModel>();
            if (constraint == null || constraint.length() == 0) {
                dealerListItemModel.addAll(mBackupStrings);

            }
            else {

                for (DealerListItemModel text : dealerListItemModel) {
                    if (text.getDealerName().contains(constraint.toString())) {
                        dealerListItemModel.add(text);
                    }
                }
            }
            FilterResults filterResults1=new FilterResults();
            //filterResults.count = dealerListItemModel.size();
            filterResults1.values = dealerListItemModel;
            return filterResults1;
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