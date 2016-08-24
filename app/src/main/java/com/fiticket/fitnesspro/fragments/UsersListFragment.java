package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.WebServices;
import com.fiticket.fitnesspro.adapters.UserListAdapter;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.GetUserJson;
import com.fiticket.fitnesspro.pojos.UsersJson;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by InFinItY on 2/14/2016.
 */
public class UsersListFragment extends Fragment {

    private static final String TAG = UsersListFragment.class.getSimpleName();
    private ProgressBar progressBar;
    private Context context;
    private Button addNewButton;
    private onAddUserButtonClicked mCallBack;

    UsersJson[] usersList;
    Activity parentActivity;
    private ListView usersListView;
    ArrayList<UsersJson> tempUserList;
    private int count;


    public interface onAddUserButtonClicked
    {
        void onCreateNewUserClicked();
        void onUserSelected(UsersJson user);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        try {

            mCallBack=(onAddUserButtonClicked)getActivity();
        }catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ "must implement onButtonClicked");
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.userslist_layout, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.userListProgressbar);
        SharedPreferences sPref = getActivity().getSharedPreferences(Constant.PREF, getActivity().MODE_PRIVATE);
        if ((sPref.getInt(Constant.USER_ROLE_ID, 3)) == 3) {
            addNewButton = (Button) view.findViewById(R.id.addUserButton);
            addNewButton.setVisibility(View.GONE);
        } else {
            addNewButton = (Button) view.findViewById(R.id.addUserButton);
            addNewButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    mCallBack.onCreateNewUserClicked();
                }
            });
        }

        usersListView = (ListView) view.findViewById(R.id.userslist);
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<usersList.length;i++) {
                    if (usersList[i]==tempUserList.get(position)) {
                        mCallBack.onUserSelected(usersList[i]);
                    }
                }
            }
        });
        usersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                String url = Constant.DELETE_USER_URL + usersList[position].getUserId();
                                triggerVolleyDeleteRequest(url);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();

                return true;
            }
        });
        return view;

    }

    private void triggerVolleyDeleteRequest(String url) {
        WebServices.triggerVolleyDeleteRequest(context, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "volley response:" + response.toString());
                Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
                triggerVolleyGetUserRequest();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "error occured:" + error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity=getActivity();
        progressBar.setVisibility(View.VISIBLE);
        triggerVolleyGetUserRequest();
    }
    private void triggerVolleyGetUserRequest() {
        WebServices.triggerVolleyGetRequest(parentActivity, Constant.GET_USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                GetUserJson getUserJson = gson.fromJson(response, GetUserJson.class);
                if (getUserJson.getStatusCode().equals(WebServices.SUCCESS_CODE)) {
                    if (getUserJson != null) {
                        usersList = getUserJson.getData().getUserList();

                        if (usersList != null) {
                            tempUserList=new ArrayList<UsersJson>();
                            for (UsersJson user : usersList)
                        /*for (int i=0;i<usersList.length;i++)*/ {

                                if (user.getRoleId() != 1) {

                                    tempUserList.add(user);
                                    UserListAdapter adapter = new UserListAdapter(parentActivity, tempUserList);
                                    usersListView.setAdapter(adapter);
                                }
                            }
                            //Toast.makeText(parentActivity, "user list", Toast.LENGTH_LONG).show();
                        } else {
                            usersList = new UsersJson[0];
                            Toast.makeText(parentActivity, "Add new user", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(parentActivity,""+getUserJson.getStatusMsg(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(parentActivity, "Check connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
