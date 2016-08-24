package com.fiticket.fitnesspro.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.adapters.ClassDetailPagerAdapter;
import com.fiticket.fitnesspro.pojos.ClassJson;
import com.fiticket.fitnesspro.pojos.ClassTypeJson;

/**
 * Created by InFinItY on 2/3/2016.
 */
public class ClassDetailsViewPageFragment extends Fragment {

    public static final int CLASS_DETAILS_TAB =0;
    public static final int SCHEDULE_TAB =1;
    public static final int CUSTOMER_DETAILS_TAB = 2;
    public static final int ATTENDANCE_TAB = 3;

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;



    private ViewPager viewPager;
    private ClassDetailPagerAdapter adapter;
    private TabLayout tabLayout;
    private ClassJson classDetail;
    private boolean showClaassDetail;
    private int tabNumber;
    private ClassTypeJson[] classTypes;

    public void setClassDetail(ClassJson classDetail, ClassTypeJson[] classTypes) {
        this.classDetail = classDetail;
        this.classTypes=classTypes;
    }

    public void setShowClaassDetail(boolean showClaassDetail) {
        this.showClaassDetail = showClaassDetail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("Fragment","ClassDetailsViewPageFragment");
        return inflater.inflate(R.layout.products_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*PagerTabStrip pagerTabStrip = (PagerTabStrip) view.findViewById(R.id.pager_title_strip);*/

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Class"));
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
        tabLayout.addTab(tabLayout.newTab().setText("Students"));
        tabLayout.addTab(tabLayout.newTab().setText("Attendance"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        view.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });
        adapter = new ClassDetailPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),classDetail,classTypes);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(tabNumber);
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void setTabNumber(int tabNumber) {
        this.tabNumber = tabNumber;
    }
}

