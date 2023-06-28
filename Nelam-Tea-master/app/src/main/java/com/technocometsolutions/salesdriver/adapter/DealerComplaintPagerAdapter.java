package com.technocometsolutions.salesdriver.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.technocometsolutions.salesdriver.fregment.ActiveComplaintFragment;
import com.technocometsolutions.salesdriver.fregment.CompletePaymentFragment;
import com.technocometsolutions.salesdriver.fregment.PendingPaymentFragment;
import com.technocometsolutions.salesdriver.fregment.ResolveComplaintFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class DealerComplaintPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;

    public DealerComplaintPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new ActiveComplaintFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new ResolveComplaintFragment();
           /* case 2: // Fragment # 1 - This will show SecondFragment
                return SecondFragment.newInstance(2, "Page # 3");*/
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0)
        {
            return "Active Complaint";
        }
        else
        {
            return "Resolve Complaint";
        }

    }





    /*@StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }*/





}