package com.fiticket.fitnesspro.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.pojos.CustomerJson;

/**
 * Created by InFinItY on 12/3/2015.
 */
public class CustomerListAdapter extends BaseAdapter {
    private final boolean showDialog;
    Context context;
    CustomerJson[] customerList;
    TextView customerName;
    TextView customerMobile;
    TextView customerEmail;
    TextView customerLocation;
    ImageView genderIcon;

    public CustomerListAdapter(Context context, CustomerJson[] customerList, boolean showDialog)
    {
        this.context=context;
        this.customerList=customerList;
        this.showDialog=showDialog;
    }

    @Override
    public int getCount() {
        return customerList.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.listitem_customers, null);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        TextView customerContactIcon = (TextView) view.findViewById(R.id.contactIcon);
        customerContactIcon.setTypeface(font);
        TextView customerEmailIcon = (TextView) view.findViewById(R.id.emailIcon);
        customerEmailIcon.setTypeface(font);
        TextView customerLocationIcon = (TextView) view.findViewById(R.id.customerLocationIcon);
        customerLocationIcon.setTypeface(font);

        if(!TextUtils.isEmpty(customerList[position].getGender())&&customerList[position].getGender().equals("male"))
        {
            genderIcon=(ImageView)view.findViewById(R.id.genderIcon);
            genderIcon.setImageResource(R.drawable.male2);

        }
        else if (!TextUtils.isEmpty(customerList[position].getGender())&&(customerList[position].getGender()).equals("female"))
        {
            genderIcon=(ImageView)view.findViewById(R.id.genderIcon);
            genderIcon.setImageResource(R.drawable.female2);
        }
        else
        {
            genderIcon=(ImageView)view.findViewById(R.id.genderIcon);
            genderIcon.setImageResource(R.drawable.no_gender);
        }
        customerName=(TextView)view.findViewById(R.id.customerNameList);
        customerLocation=(TextView)view.findViewById(R.id.customerLocation);
        customerMobile=(TextView)view.findViewById(R.id.customerMobileList);
        customerEmail=(TextView)view.findViewById(R.id.customerEmailList);
        if(showDialog==true)
        {
            customerMobile.setLinksClickable(false);
            customerEmail.setLinksClickable(false);
        }
        customerName.setText(customerList[position].getCustomerName());
        customerLocation.setText(customerList[position].getCity());
        customerMobile.setText(customerList[position].getContactNumber());
        customerEmail.setText(customerList[position].getEmail());
        return view;
    }
}
