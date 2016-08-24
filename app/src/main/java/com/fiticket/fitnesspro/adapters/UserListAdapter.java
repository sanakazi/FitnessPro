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
import com.fiticket.fitnesspro.pojos.UsersJson;

import java.util.ArrayList;

/**
 * Created by InFinItY on 2/14/2016.
 */
public class UserListAdapter extends BaseAdapter{
    private final ArrayList<UsersJson> usersList;
    private final Context context;
    private TextView contactIcon;
    private TextView userEmailIcon;
    private ImageView userImage;
    private TextView userRoleName;
    private TextView userEmail;
    private TextView contact;
    private TextView diasplayName;
    private TextView userListRoleNameIcon;

    public UserListAdapter(Context context, ArrayList<UsersJson> usersList) {
        this.context=context;
        this.usersList=usersList;
    }

    @Override
    public int getCount() {
        return usersList.size();
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
        View view= inflater.inflate(R.layout.listitem_user, null);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        contactIcon=(TextView)view.findViewById(R.id.contactIcon);
        contactIcon.setTypeface(font);
        userEmailIcon=(TextView)view.findViewById(R.id.userEmailIcon);
        userEmailIcon.setTypeface(font);
        userListRoleNameIcon=(TextView)view.findViewById(R.id.userListRoleNameIcon);
        userListRoleNameIcon.setTypeface(font);

        diasplayName=(TextView)view.findViewById(R.id.diasplayName);
        userRoleName=(TextView)view.findViewById(R.id.userListRoleName);
        userEmail=(TextView)view.findViewById(R.id.listUserEmail);
        contact=(TextView)view.findViewById(R.id.listContactNumber);

        diasplayName.setText(usersList.get(position).getDisplayName());
        contact.setText(usersList.get(position).getContactNumber());
        userRoleName.setText(usersList.get(position).getRoleName());
        userEmail.setText(usersList.get(position).getEmail());
        return view;
    }
}
