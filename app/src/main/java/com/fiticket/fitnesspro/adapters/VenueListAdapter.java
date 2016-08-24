package com.fiticket.fitnesspro.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.pojos.VenueJson;

/**
 * Created by InFinItY on 12/9/2015.
 */
public class VenueListAdapter extends BaseAdapter {
    Context context;
    VenueJson[] venueLists;
    ImageView venueImage;
    TextView addressIcon;
    TextView contactIcon;
    TextView venueName;
    TextView venueAddress;
    TextView venueCity;
    TextView venueState;
    TextView venuePincode;
    TextView venueContactNumber;

    public VenueListAdapter(Context context, VenueJson[] venueLists) {
        this.context=context;
        this.venueLists=venueLists;
    }


    @Override
    public int getCount() {
        return venueLists.length;
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
        View view= inflater.inflate(R.layout.listitem_venue, null);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        addressIcon=(TextView)view.findViewById(R.id.addressIcon);
        addressIcon.setTypeface(font);
        contactIcon=(TextView)view.findViewById(R.id.contactIcon);
        contactIcon.setTypeface(font);

        venueImage=(ImageView)view.findViewById(R.id.venueListImage);
        venueName=(TextView)view.findViewById(R.id.listVenueName);
        venueCity=(TextView)view.findViewById(R.id.listVenueCity);
        venueState=(TextView)view.findViewById(R.id.listVenueState);
        venuePincode=(TextView)view.findViewById(R.id.listVenuePincode);
        venueContactNumber=(TextView)view.findViewById(R.id.listVenueMobileNumber);
        venueName.setText(venueLists[position].getVenueName());

        venueCity.setText(venueLists[position].getCity());
        venueState.setText(venueLists[position].getArea());
        venuePincode.setText("" + venueLists[position].getPinCode());
        venueContactNumber.setText(venueLists[position].getContactNumber());
        return view;
    }
}