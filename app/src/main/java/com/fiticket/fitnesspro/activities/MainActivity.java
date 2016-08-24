/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fiticket.fitnesspro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.adapters.ClassListAdapter;
import com.fiticket.fitnesspro.adapters.NavigationDrawerAdapter;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.fragments.AttendanceFragment;
import com.fiticket.fitnesspro.fragments.ClassDetailsViewPageFragment;
import com.fiticket.fitnesspro.fragments.ClassFragment;
import com.fiticket.fitnesspro.fragments.ComingSoon;
import com.fiticket.fitnesspro.fragments.CustomerClassSelectionFragment;
import com.fiticket.fitnesspro.fragments.CustomerFragment;
import com.fiticket.fitnesspro.fragments.CustomerListFragment;
import com.fiticket.fitnesspro.fragments.EmployeeFragment;
import com.fiticket.fitnesspro.fragments.FragmentCreateClass;
import com.fiticket.fitnesspro.fragments.NotificationFragment;
import com.fiticket.fitnesspro.fragments.ProductsFragment;
import com.fiticket.fitnesspro.fragments.UpdateScheduleFragment;
import com.fiticket.fitnesspro.fragments.UsersFragment;
import com.fiticket.fitnesspro.fragments.UsersListFragment;
import com.fiticket.fitnesspro.fragments.VenueFragment;
import com.fiticket.fitnesspro.fragments.VenueListFragment;
import com.fiticket.fitnesspro.pojos.ClassJson;
import com.fiticket.fitnesspro.pojos.ClassTypeJson;
import com.fiticket.fitnesspro.pojos.CustomerJson;
import com.fiticket.fitnesspro.pojos.UsersJson;
import com.fiticket.fitnesspro.pojos.VenueJson;

import java.util.Calendar;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class MainActivity extends AppCompatActivity implements CustomerListFragment.onButtonClicked,ClassFragment.onClassButtonClicked,
        VenueListFragment.onVenueButtonClicked,CustomerClassSelectionFragment.onShowCustomerClassItemSelected,
        FragmentCreateClass.onSaveButtonClicked,ClassListAdapter.CustomerButtonClickListener,
        UsersListFragment.onAddUserButtonClicked{
    public static final String DATE_STRING = "DATE_STRING";
    private static final String SCHEDULE_FRAGMENT = "SCHEDULE_FRAGMENT";
    private static final String HOME = "HOME";
    private static final String CUSTOMER_LIST = "CUSTOMER";
    private static final String VENUE_LIST = "VENUE";
    private static final String NOTIFICATION_LIST = "NOTIFICATION";
    private static final String ATTENDANCE_LIST = "ATTENDANCE";
    private static final String EMPLOYEE_LIST = "EMPLOYEE";
    private static final String FRAG = "FRAG";
    private static final String PRODUCTS ="Products" ;
    private static final String CUSTOMERS = "Customers";
    private static final String VENUES = "Venues";
    private static final String NOTIFICATION = "Notification";
    private static final String ATTENDANCE = "Attendance";
    private static final String EMPLOYEES = "Employees";
    private static final String FITNESSPRO = "Fitness PRO";
    private static final String NEW_VENUE = "New venue";
    private static final String NEW_CUSTOMER = "New customer";
    private static final String NEW_CLASS = "New class";
    private static final String CLASS_DETAILS = "Class Details";
    private static final String CUSTOMER_BY_CLASS = "Customers";
    private static final String USERS = "Users";
    private static final String NEW_USER = "New User";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    Context context;
    int currentFragment=0;
    android.support.v4.app.Fragment newFragment;
    android.support.v4.app.FragmentTransaction transaction;
    CustomerFragment customerFrgmentNew;
    FragmentCreateClass fragmentCreateClass;
    Calendar mcurrentDate;


    /*public static int[] pgmImage={R.drawable.schedule,R.drawable.customers,R.drawable.notification};*/
    public String[] listIcon;
    public static String[]listItem;

    boolean tagIsThere;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    String title;
    boolean classFragCheck;
    int backStack=getSupportFragmentManager().getBackStackEntryCount();
    private int selectedItem;
    private CharSequence previousTitle;
    private SharedPreferences sPref;
    private long expiryDate;
    //private String[] mPlanetTitles;


    @Override
    protected void onStart() {
        super.onStart();
        mcurrentDate = Calendar.getInstance();
        sPref=getSharedPreferences(Constant.PREF, MODE_PRIVATE);

        expiryDate=sPref.getLong(Constant.EXPIRY_DATE, 0);
        if(expiryDate<mcurrentDate.getTimeInMillis())
        {
            sPref.edit().clear().commit();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            Toast.makeText(context,"Account expired",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {

        int backStack=getSupportFragmentManager().getBackStackEntryCount();
        if(backStack==1)
        {
            getSupportActionBar().setTitle(PRODUCTS);
            selectedItem=1;
            final NavigationDrawerAdapter adapter=new NavigationDrawerAdapter(this,listIcon,listItem,selectedItem);
            mDrawerList.setAdapter(adapter);
        }
        else if ((backStack==2)&&(previousTitle != null)/*||(classFragCheck=true)*/)
        {
            getSupportActionBar().setTitle(previousTitle);
            //getSupportFragmentManager().popBackStack();
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        }
        super.onBackPressed();
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        mcurrentDate = Calendar.getInstance();


        listItem = new String[]{"Products","Users","Customers", "Venue", "Notification", "Reports", "Cost", "Revenue", "Logout"};
        listIcon = new String[]{getResources().getString(R.string.ic_products),getResources().getString(R.string.ic_customers), getResources().getString(R.string.ic_customer), getResources().getString(R.string.ic_venue), getResources().getString(R.string.ic_notification),  getResources().getString(R.string.ic_reports),  getResources().getString(R.string.ic_cost), getResources().getString(R.string.ic_revenue), getResources().getString(R.string.ic_logout)};


        mTitle = mDrawerTitle = getTitle();
        //mPlanetTitles = new String[]{"hello","hi"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        if(toolbar!=null){
            setSupportActionBar(toolbar);
            /*getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        }

        //Handle when activity is recreated like on orientation Change
        
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,
        //      R.layout.drawer_list_item, mPlanetTitles));
        final NavigationDrawerAdapter adapter=new NavigationDrawerAdapter(this,listIcon,listItem,selectedItem);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                selectItem(position);
                final NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(MainActivity.this, listIcon, listItem, selectedItem);
                mDrawerList.setAdapter(adapter);
            }

        });

        android.support.v4.app.Fragment newFragment= new ProductsFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, newFragment, HOME);
        getSupportActionBar().setTitle(PRODUCTS);
        transaction.commit();

        // enable ActionBar app icon to behave as action to toggle nav drawer

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        if (savedInstanceState == null) {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void replaceFragment(Fragment fragment, String newTitle,boolean addToBackStack)
    {
        android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        if(addToBackStack) {
            transaction.addToBackStack(null);
            this.previousTitle=getSupportActionBar().getTitle();
        }
        transaction.commit();
        getSupportActionBar().setTitle(newTitle);
    }

    @Override
    public void onCreateClicked() {
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);*/

        toolbarNavigationListener();
        customerFrgmentNew= new CustomerFragment();
        replaceFragment(customerFrgmentNew, NEW_CUSTOMER, true);


    }

    @Override
    public void onCustomerSelected(CustomerJson customer) {
        toolbarNavigationListener();
        CustomerFragment newFragment= new CustomerFragment();
        newFragment.setCustomerDetail(customer);
        newFragment.setShowCustomerDetail(true);
        replaceFragment(newFragment, NEW_CUSTOMER, true);
    }

    private void toolbarNavigationListener() {
        /*if((backStack==2)*//*||(classFragCheck=true)*//*)
        {*/
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        /*}*/
    }

    public void selectItem(int position) {
        if(currentFragment!=0)
            getSupportFragmentManager().popBackStack();

        switch (position) {
            case 1:
                showProductsFragment();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 2:
                if(sPref.getInt(Constant.USER_ROLE_ID,3)>=Constant.ROLE_TEACHER) {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show();
                }
                else
                {
                    showUsersFragment();
                }
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 3:
                showCustomersFragment();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 4:
                showVenuesFragment();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 5:
                showComingSoonFragment();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 6:
                /*var = true;*/
                showComingSoonFragment();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 7:
                /*var = true;*/
                showComingSoonFragment();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 8:
                /*var = true;*/
                showComingSoonFragment();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 9:
                sPref.edit().clear().commit();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            default:
                //showProductsFragment();
                mDrawerLayout.closeDrawer(mDrawerList);
                break;

        }
        currentFragment=position;

    }

    private void showUsersFragment() {
        newFragment=new UsersListFragment();
        replaceFragment(newFragment,USERS,true);
    }

    private void showComingSoonFragment() {
        newFragment=new ComingSoon();
        replaceFragment(newFragment,FITNESSPRO,true);

    }
    private void showEmployeesFragment() {

        newFragment = new EmployeeFragment();
        replaceFragment(newFragment,EMPLOYEES,true);
    }
    private void showAttendanceFragment() {

        newFragment = new AttendanceFragment();
        replaceFragment(newFragment,ATTENDANCE,true);
    }

    private void showNotificationFragment() {

        newFragment = new NotificationFragment();
        replaceFragment(newFragment,NOTIFICATION,true);
    }

    private void showVenuesFragment() {
        newFragment = new VenueListFragment();
        replaceFragment(newFragment,VENUES,true);

    }

    private void showCustomersFragment() {
        newFragment = new CustomerListFragment();
        replaceFragment(newFragment, CUSTOMERS, true);

    }

    private void showProductsFragment() {
        ProductsFragment newFragment= (ProductsFragment)getSupportFragmentManager().findFragmentByTag(HOME);
        if(newFragment==null){
            newFragment= new ProductsFragment();
        }
        replaceFragment(newFragment, PRODUCTS, false);
    }

    /*private void showScheduleFragment() {
        CaldroidFragment caldroidFragment=(CaldroidFragment)getSupportFragmentManager().findFragmentByTag(SCHEDULE_FRAGMENT);
        if(caldroidFragment==null)
        {
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        }
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, caldroidFragment);
            transaction.addToBackStack(null);
        transaction.commit();
            final CaldroidListener listener=new CaldroidListener(){
            public void onSelectDate(Date date,View view)
            {
                java.text.DateFormat f = java.text.DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                String dateString=f.format(date.getTime());
                Bundle args=new Bundle();
                args.putString(DATE_STRING, dateString);
                android.support.v4.app.Fragment newFragment= new FragmentCreateClass();
                newFragment.setArguments(args);
                android.support.v4.app.FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        caldroidFragment.setCaldroidListener(listener);
    }*/
    /**
     * When using the ActionBarDrawerToggle, you mus
     * t call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onCreateNewClicked(ClassTypeJson[] classTypes) {
        //mDrawerToggle.setDrawerIndicatorEnabled(false);
        //classFragCheck=true;
        toolbarNavigationListener();
        fragmentCreateClass = new FragmentCreateClass();
        fragmentCreateClass.setClassTypes(classTypes);
        replaceFragment(fragmentCreateClass,NEW_CLASS,true);

    }

    @Override
    public void onClassSelected(ClassJson classes, int tabNumber,ClassTypeJson[] classTypes) {
        toolbarNavigationListener();
        ClassDetailsViewPageFragment classDetailsViewPageFragment=new ClassDetailsViewPageFragment();
        classDetailsViewPageFragment.setClassDetail(classes,classTypes);
        classDetailsViewPageFragment.setTabNumber(tabNumber);
        classDetailsViewPageFragment.setShowClaassDetail(true);
        replaceFragment(classDetailsViewPageFragment, CLASS_DETAILS, true);
    }

    @Override
    public void onCreateNewVenueClicked() {
        toolbarNavigationListener();
        Fragment newFragment = new VenueFragment();
        replaceFragment(newFragment,NEW_VENUE,true);
    }

    @Override
    public void onVenueSelected(VenueJson venue, int i, int position) {
        toolbarNavigationListener();
        VenueFragment newFragment=new VenueFragment();
        newFragment.setVenueDetail(venue,position,i);
        newFragment.setShowVenueDetail(true);
        replaceFragment(newFragment, NEW_VENUE, true);
    }

    @Override
    public void onClassItemSelect(ClassJson classes) {
        customerFrgmentNew.setSelectedClassDetail(classes);
    }

    @Override
    public void showScheduleDialog(int classid) {
        UpdateScheduleFragment scheduleFragment=new UpdateScheduleFragment();
        scheduleFragment.setCreatedClassId(classid);
    }

    @Override
    public void onCreateNewUserClicked() {
        toolbarNavigationListener();
        Fragment newFragment = new UsersFragment();
        replaceFragment(newFragment,NEW_USER,true);

    }

    @Override
    public void onUserSelected(UsersJson user) {
        toolbarNavigationListener();
        UsersFragment newFragment=new UsersFragment();
        newFragment.setUserDetail(user);
        newFragment.setShowUserDetail(true);
        replaceFragment(newFragment, NEW_USER, true);

    }

    /*@Override
    public void onCustomerButtonClicked(int classID) {
        CustomerByClassFragment customerByClassFragment=new CustomerByClassFragment();
        customerByClassFragment.setClassid(classID);
        customerByClassFragment.showCustomerByClass(true);
        replaceFragment(customerByClassFragment,CUSTOMER_BY_CLASS,true);
    }*/
}