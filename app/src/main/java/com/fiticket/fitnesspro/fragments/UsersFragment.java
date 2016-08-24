package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.WebServices;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.GetUserResponseJson;
import com.fiticket.fitnesspro.pojos.UsersJson;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by InFinItY on 2/14/2016.
 */
public class UsersFragment extends Fragment {

    private static final String TAG = UsersFragment.class.getSimpleName();
    private TextView displayName;
    private TextView email;
    private TextView username;
    private TextView password;
    private TextView ConPassword;
    private TextView address;
    private TextView contactNo;
    private Spinner spinner;
    private Button saveButton;
    private Context context;
    private UsersJson userDetail;
    private boolean showUserDetail;
    ArrayAdapter<String> list;
    private TextView editButton;
    private LinearLayout pswdLayout;
    SharedPreferences sPref;
    private TextView text;
    private Activity parentActivity;
    private ProgressBar progressCreateUser;
    private LinearLayout spinnerRoleNameView;
    private LinearLayout roleNameView;
    private EditText RoleEditText;


    public void setUserDetail(UsersJson userDetail) {
        this.userDetail = userDetail;
    }

    public void setShowUserDetail(boolean showUserDetail) {
        this.showUserDetail = showUserDetail;
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.user_create_layout, container, false);
        sPref = getActivity().getSharedPreferences(Constant.PREF, getActivity().MODE_PRIVATE);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        progressCreateUser=(ProgressBar)view.findViewById(R.id.progressCreateUser);
        progressCreateUser.setVisibility(View.GONE);

        spinnerRoleNameView=(LinearLayout)view.findViewById(R.id.spinnerRoleNameView);
        roleNameView=(LinearLayout)view.findViewById(R.id.roleNameView);
        RoleEditText=(EditText)view.findViewById(R.id.RoleEditText);

        pswdLayout=(LinearLayout)view.findViewById(R.id.pswrdLayout);
        displayName = (TextView) view.findViewById(R.id.displaynameUser);
        email = (TextView) view.findViewById(R.id.emailUser);
        username = (TextView) view.findViewById(R.id.username);
        password = (TextView) view.findViewById(R.id.passwordUser);
        ConPassword = (TextView) view.findViewById(R.id.confirmPwdUser);
        address = (TextView) view.findViewById(R.id.addressUser);
        contactNo = (TextView) view.findViewById(R.id.numberUser);
        spinner = (Spinner) view.findViewById(R.id.RoleTypeSpinner);
        editButton=(TextView)view.findViewById(R.id.editbtnUser);
        editButton.setTypeface(font);
        text=(TextView)view.findViewById(R.id.txt);
        String [] values =
                {"<--Select-->","Tenant Admin","Teacher"};
        list = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        list.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(list);

        saveButton=(Button)view.findViewById(R.id.saveUserButton);

        if(showUserDetail)
        {
            pswdLayout.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            spinnerRoleNameView.setVisibility(View.INVISIBLE);
            roleNameView.setVisibility(View.VISIBLE);

            RoleEditText.setText(userDetail.getRoleName());
            RoleEditText.setTag(RoleEditText.getKeyListener());
            RoleEditText.setKeyListener(null);

            displayName.setText(userDetail.getDisplayName());
            displayName.setTag(displayName.getKeyListener());
            displayName.setKeyListener(null);
            email.setText(userDetail.getEmail());
            email.setTag(email.getKeyListener());
            email.setKeyListener(null);
            username.setText(userDetail.getUserName());
            username.setTag(username.getKeyListener());
            username.setKeyListener(null);
            address.setText(userDetail.getAddress());
            address.setTag(address.getKeyListener());
            address.setKeyListener(null);
            contactNo.setText(userDetail.getContactNumber());
            contactNo.setTag(contactNo.getKeyListener());
            contactNo.setKeyListener(null);
            if ((userDetail.getRoleId())==2)
            {
                spinner.setSelection(1);
                spinner.setEnabled(false);
            }
            else if ((userDetail.getRoleId())==3)
            {
                spinner.setSelection(2);
                spinner.setEnabled(false);
            }
            else if ((userDetail.getRoleId())==4)
            {
                spinner.setSelection(3);
                spinner.setEnabled(false);
            }
            else
                spinner.setEnabled(false);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editButton.setVisibility(View.GONE);
                    saveButton.setVisibility(View.VISIBLE);
                    pswdLayout.setVisibility(View.VISIBLE);
                    spinnerRoleNameView.setVisibility(View.VISIBLE);
                    roleNameView.setVisibility(View.INVISIBLE);
                    displayName.setKeyListener((KeyListener) displayName.getTag());
                    username.setKeyListener((KeyListener) username.getTag());
                    address.setKeyListener((KeyListener) address.getTag());
                    contactNo.setKeyListener((KeyListener) contactNo.getTag());
                    email.setKeyListener((KeyListener) email.getTag());
                    spinner.setEnabled(true);
                }
            });
        }



        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean allvalid = true;
                if (displayName.getText().toString().length() == 0) {
                    displayName.setError("Name is required!");

                    allvalid = false;
                } else {
                    // do async task
                    displayName.setError(null);
                }
                if (email.getText().toString().length() == 0) {
                    email.setError("Email required");
                    allvalid = false;
                } else {
                    email.setError(null);
                }
                if (username.getText().toString().length() == 0) {
                    username.setError("Username is required!");
                    allvalid = false;
                } else {
                    // do async task
                    username.setError(null);
                }
                if (password.getText().toString().length() == 0) {
                    password.setError("Password is required!");
                    allvalid = false;
                } else {
                    // do async task
                    password.setError(null);
                }
                if ((ConPassword.getText().toString().length() == 0)|!(password.getText().toString().equals(ConPassword.getText().toString()))) {
                    ConPassword.setError("Confirm password!");
                    allvalid = false;
                }
                else {
                    // do async task
                    ConPassword.setError(null);
                }
                if (contactNo.getText().toString().length() == 0) {
                    contactNo.setError("Mobile Number is required!");
                    allvalid = false;
                } else {
                    // do async task
                    contactNo.setError(null);
                }
                if (address.getText().toString().length() == 0) {
                    address.setError("Address is required!");
                    allvalid = false;
                } else {
                    // do async task
                    address.setError(null);
                }
                if (allvalid) {
                    progressCreateUser.setVisibility(View.VISIBLE);
                    UsersJson createUser = new UsersJson();
                    createUser.setAddress(address.getText().toString());
                    createUser.setUserName(username.getText().toString());
                    createUser.setDisplayName(displayName.getText().toString());
                    createUser.setContactNumber(contactNo.getText().toString());
                    createUser.setEmail(email.getText().toString());
                    createUser.setPassword(ConPassword.getText().toString());
                    int pos=spinner.getSelectedItemPosition();
                    if (pos>0) {
                        createUser.setRoleId(pos+1);
                    }
                    int tenantId = sPref.getInt(Constant.USER_TENANT_ID,0);
                    createUser.setTenantId(tenantId);

                    String url;

                    // To check if is editing(UPDATE) customer or adding(CREATE) customer
                    if (showUserDetail) {
                        createUser.setUserId(userDetail.getUserId());
                        url = Constant.UPDATE_USER_URL;
                    } else {
                        url=Constant.CREATE_USER_URL;
                    }
                    WebServices.sendPostRequest(parentActivity, url, createUser, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "volley response:" + response.toString());
                            GetUserResponseJson getUserResponseJson=new Gson().fromJson(response.toString(),GetUserResponseJson.class);
                            progressCreateUser.setVisibility(View.GONE);
                            if(getUserResponseJson.getStatusCode().equals(WebServices.SUCCESS_CODE_ERR))
                            {
                                username.setError("Username already exist!");
                            }
                            else if (getUserResponseJson.getStatusCode().equals(WebServices.SUCCESS_CODE)) {
                                Toast.makeText(parentActivity, "added successfully", Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            }
                            else {
                                Toast.makeText(parentActivity, "Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "error occured:" + error.toString());
                            progressCreateUser.setVisibility(View.GONE);
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(parentActivity, "Check connection", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, UsersJson.class);
                    /*Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(createVenue, VenueJson.class));
                        //progressBar.setVisibility(View.VISIBLE);
                        triggerVolleyPostRequest(jsonObject,url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }


            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity=getActivity();
    }
}
