package com.fiticket.fitnesspro.activities;

/**
 * Created by InFinItY on 1/14/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fiticket.fitnesspro.fragments.ClassFragment;
import com.fiticket.fitnesspro.fragments.SubscriptionFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int num) {

       if(num==0)
       {


           return new ClassFragment();

       }
        else
       {
           return new SubscriptionFragment();
       }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
