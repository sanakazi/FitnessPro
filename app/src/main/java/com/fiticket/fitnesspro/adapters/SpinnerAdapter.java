package com.fiticket.fitnesspro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.pojos.VenueJson;

/**
 * Created by InFinItY on 1/18/2016.
 */
public class SpinnerAdapter extends BaseAdapter {
    Context context;
    VenueJson[] venueLists;
    TextView venueNames;
    public SpinnerAdapter(Context context, VenueJson[] venueLists) {
        this.context=context;
        this.venueLists=venueLists;
    }

    @Override
    public int getCount() {
        return  venueLists.length;
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
        View view= inflater.inflate(R.layout.spinner_item, null);
        venueNames=(TextView)view.findViewById(R.id.venueNames);
        venueNames.setText(venueLists[position].getVenueName());
        return null;
    }
}
