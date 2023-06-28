package com.technocometsolutions.salesdriver.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.technocometsolutions.salesdriver.R;
import com.technocometsolutions.salesdriver.model.DealerListItemModel;

import java.util.ArrayList;
import java.util.List;

import gr.escsoft.michaelprimez.searchablespinner.interfaces.ISpinnerSelectedView;

public class SimpleArrayListAdapter extends ArrayAdapter<DealerListItemModel> implements Filterable, ISpinnerSelectedView {
    private Context mContext;
    private ArrayList<DealerListItemModel> mBackupStrings;
    private ArrayList<DealerListItemModel> mStrings;
    private StringFilter mStringFilter = new StringFilter();

    public SimpleArrayListAdapter(Context context, ArrayList<DealerListItemModel> strings) {
        super(context, R.layout.view_list_item);
        mContext = context;
        mStrings = strings;
        mBackupStrings = strings;
    }

    @Override
    public int getCount() {
        return mStrings == null ? 0 : mStrings.size() + 1;
    }

    @Override
    public DealerListItemModel getItem(int position) {
        if (mStrings != null && position > 0)
            return mStrings.get(position);
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

            dispalyName.setText(""+mStrings.get(position).getDealerName());
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

            dispalyName.setText(""+mStrings.get(position).getDealerName());
        }
        return view;
    }

    @Override
    public View getNoSelectionView() {
        View view = View.inflate(mContext, R.layout.view_list_no_selection_item, null);
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
            final ArrayList<DealerListItemModel> filterStrings = new ArrayList<>();
            for (DealerListItemModel text : mBackupStrings) {
                if (text.getDealerName().toLowerCase().contains(constraint)) {
                    filterStrings.add(text);
                }
            }
            filterResults.count = filterStrings.size();
            filterResults.values = filterStrings;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
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
