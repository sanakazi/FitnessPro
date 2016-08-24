package com.fiticket.fitnesspro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fiticket.fitnesspro.fragments.ComingSoon;
import com.fiticket.fitnesspro.fragments.CustomerByClassFragment;
import com.fiticket.fitnesspro.fragments.FragmentCreateClass;
import com.fiticket.fitnesspro.fragments.UpdateScheduleFragment;
import com.fiticket.fitnesspro.pojos.ClassJson;
import com.fiticket.fitnesspro.pojos.ClassTypeJson;

/**
 * Created by InFinItY on 2/3/2016.
 */
public class ClassDetailPagerAdapter extends FragmentStatePagerAdapter {
    private final ClassTypeJson[] classTypes;
    int mNumOfTabs;
    ClassJson classDetail;

    public ClassDetailPagerAdapter(FragmentManager fm, int NumOfTabs, ClassJson classDetail, ClassTypeJson[] classTypes) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.classDetail=classDetail;
        this.classTypes=classTypes;
    }


    @Override
    public Fragment getItem(int num) {
        int classId;
        switch (num) {
            case 0:
                FragmentCreateClass fragmentCreateClass = new FragmentCreateClass();
                fragmentCreateClass.setClassDetail(classDetail,classTypes);
                fragmentCreateClass.setShowClaassDetail(true);
                return fragmentCreateClass;
            case 1:
                UpdateScheduleFragment newFragment=new UpdateScheduleFragment();
                classId=classDetail.getClassId();
                newFragment.setCreatedClassId(classId);
                newFragment.setShowScheduleDetail(true);
                return newFragment;
            case 2:
                CustomerByClassFragment customerByClassFragment=new CustomerByClassFragment();
                classId=classDetail.getClassId();
                customerByClassFragment.setClassid(classId);
                customerByClassFragment.showCustomerByClass(true);
                return customerByClassFragment;
            case 3:
                return new ComingSoon();
        }
        return null;

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
