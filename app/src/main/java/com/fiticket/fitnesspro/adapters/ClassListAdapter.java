package com.fiticket.fitnesspro.adapters;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.fragments.ClassDetailsViewPageFragment;
import com.fiticket.fitnesspro.pojos.ClassJson;
import com.fiticket.fitnesspro.pojos.ClassTypeJson;
import com.fiticket.fitnesspro.singleton.MySingleton;
import com.fiticket.fitnesspro.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by InFinItY on 12/9/2015.
 */
public class ClassListAdapter extends BaseAdapter {
    AppCompatActivity parentActivity;
    NetworkImageView classImage;
    TextView className;
    TextView location;
    TextView startDate;
    TextView endDate;
    TextView startTime;
    TextView endTime;
    TextView price;
    LinearLayout customersByClassidBtn;
    CustomerButtonClickListener listener;
    ClassTypeJson[] classTypes;
    ArrayList<ClassJson> classLists;
    private TextView customersIcon;
    private LinearLayout attendanceBtn;
    private TextView attendanceIcon;
    private LinearLayout scheduleBtn;

    public ClassListAdapter(AppCompatActivity activity, ArrayList<ClassJson> classLists, ClassTypeJson[] classTypes) {
        this.parentActivity=activity;
        this.classLists=classLists;
        this.classTypes=classTypes;
        listener= (CustomerButtonClickListener)activity;
    }


    @Override
    public int getCount() {
        return classLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)parentActivity.getSystemService(parentActivity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listitem_class, null);

        Typeface font = Typeface.createFromAsset(parentActivity.getAssets(), "fontawesome-webfont.ttf");
        TextView scheduleIcon = (TextView) view.findViewById(R.id.scheduleIcon);
        scheduleIcon.setTypeface(font);
        TextView classPriceIcon = (TextView) view.findViewById(R.id.priceClass);
        classPriceIcon.setTypeface(font);
        TextView classDateIcon = (TextView) view.findViewById(R.id.calender);
        classDateIcon.setTypeface(font);
        ImageLoader imageLoader = MySingleton.getInstance(parentActivity).getImageLoader();

        classImage=(NetworkImageView)view.findViewById(R.id.classListImage);
        classImage.setDefaultImageResId(R.drawable.fitpro_def);
        className=(TextView)view.findViewById(R.id.listClassName);

        startDate = (TextView)view.findViewById(R.id.listClassStartDate);
        endDate = (TextView)view.findViewById(R.id.listClassEndDate);
        price = (TextView)view.findViewById(R.id.listClassPrice);
        scheduleBtn = (LinearLayout)view.findViewById(R.id.scheduleBtn);
        customersByClassidBtn = (LinearLayout)view.findViewById(R.id.customerBtn);
        attendanceBtn = (LinearLayout)view.findViewById(R.id.attendanceBtn);
        customersIcon = (TextView)view.findViewById(R.id.customersIcon);
        customersIcon.setTypeface(font);
        attendanceIcon = (TextView)view.findViewById(R.id.attendanceIcon);
        attendanceIcon.setTypeface(font);
        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClassSelected(classLists.get(position), ClassDetailsViewPageFragment.SCHEDULE_TAB,classTypes);
            }
        });
        customersByClassidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClassSelected(classLists.get(position), ClassDetailsViewPageFragment.CUSTOMER_DETAILS_TAB,classTypes);
            }
        });
        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClassSelected(classLists.get(position), ClassDetailsViewPageFragment.ATTENDANCE_TAB,classTypes);
            }
        });
        int tempClassTypeId=classLists.get(position).getClassTypeId();
        String url;
        for (int i=0;i<classTypes.length;i++)
        {
            if (classTypes[i].getClassTypeId()==tempClassTypeId)
            {
                if(classTypes[i].getClassTypeLogoURL()==null)
                {
                    url=classTypes[i].getClassTypeImgURL();
                }
                else
                {
                    url=classTypes[i].getClassTypeLogoURL();
                }
                classImage.setImageUrl(url,imageLoader);
            }
        }

        /*classImage.setImageResource
                (MySingleton.getInstance(parentActivity).getClassTypeImages()[classLists.get(position).getClassTypeId()]);
*/

        className.setText(classLists.get(position).getClassName());
        if(classLists.get(position).getStartDate() != null){
            startDate.setText(Utilities.convertEpochToDate(classLists.get(position).getStartDate()));
        }
        if(classLists.get(position).getEndDate() != null){
            endDate.setText(Utilities.convertEpochToDate(classLists.get(position).getEndDate()));
        }
        price.setText(""+classLists.get(position).getPrice());
        return view;
    }

    public interface CustomerButtonClickListener{;


        void onClassSelected(ClassJson classJson,int tabCount,ClassTypeJson[] classTypes);
    }
}
