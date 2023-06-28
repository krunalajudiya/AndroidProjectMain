package com.technocometsolutions.salesdriver.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.technocometsolutions.salesdriver.fregment.AcceptedLeaveFragment;
import com.technocometsolutions.salesdriver.fregment.CompletePaymentFragment;
import com.technocometsolutions.salesdriver.fregment.PendingLeaveFragment;
import com.technocometsolutions.salesdriver.fregment.PendingPaymentFragment;
import com.technocometsolutions.salesdriver.fregment.RejectedLeaveFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class LeaveReportPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    public LeaveReportPagerAdapter(FragmentManager fragmentManager) {
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
                return new PendingLeaveFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new AcceptedLeaveFragment();
            case 2: // Fragment # 1 - This will show SecondFragment
                return new RejectedLeaveFragment();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Pending Leave";
        } else if (position == 1) {
            return "Accepted Leave";
        } else if (position == 2) {
            return "Rejected Leave";
        }
        else
        {
            return null;
        }

    }




}