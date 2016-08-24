package com.fiticket.fitnesspro.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.constants.Constant;


/**
 * Created by InFinItY on 11/25/2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter {
    Context context;
    String[] listId;
    String [] iconId;
    TextView iconTextview;
    TextView listTextview;
    int selectedItem;
    SharedPreferences sPref;
    private TextView displayNameHeader;
    private TextView roleNameHeader;


    public NavigationDrawerAdapter(Context context, String[] listIcon, String[] listItem,int selectedItem) {
        listId=listItem;
        iconId=listIcon;
        this.context=context;
        this.selectedItem=selectedItem;

    }
    @Override
    public int getCount() {
        return listId.length+1;
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
        View rowView;
        sPref=context.getSharedPreferences(Constant.PREF, context.MODE_PRIVATE);

        if ((sPref.getInt(Constant.USER_ROLE_ID,3)>=3)&&(position==2))
        {
            rowView=inflater.inflate(R.layout.navigation_item_null, null);
            /*if (position==selectedItem)
            {
                iconTextview.setTextColor(context.getResources().getColor(R.color.GrayGoose));
                listTextview.setTextColor(context.getResources().getColor(R.color.GrayGoose));
            }
            else
            {
                iconTextview.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                listTextview.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }*/
        }
        else  if (position==0)
        {
            rowView=inflater.inflate(R.layout.navigation_header_item, null);
            sPref=context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);

            String displayName=sPref.getString(Constant.DISPLAY_NAME,"");
            displayNameHeader=(TextView)rowView.findViewById(R.id.displayNameHeader);
            roleNameHeader=(TextView)rowView.findViewById(R.id.roleNameHeader);
            displayNameHeader.setText(displayName);
            roleNameHeader.setText(sPref.getString(Constant.USER_ROLE_NAME,""));



        }
        else {
            rowView=inflater.inflate(R.layout.navigation_item, null);
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
            iconTextview = (TextView)rowView.findViewById( R.id.navigation_icon_item);
            iconTextview.setTypeface(font);
            listTextview=(TextView)rowView.findViewById(R.id.navigation_text_item);
            iconTextview.setText(iconId[position-1]);
            listTextview.setText(listId[position-1]);

            if (position == selectedItem) {
                iconTextview.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                listTextview.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
        }
        return rowView;
    }
}

