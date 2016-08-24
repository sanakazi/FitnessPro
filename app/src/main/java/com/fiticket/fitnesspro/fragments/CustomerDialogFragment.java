package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.WebServices;
import com.fiticket.fitnesspro.adapters.CustomerListAdapter;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.CustomerJson;
import com.fiticket.fitnesspro.pojos.EnrollDrollPostResponse;
import com.fiticket.fitnesspro.pojos.EnrollJson;
import com.fiticket.fitnesspro.pojos.GetCustomer;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by InFinItY on 1/31/2016.
 */
public class CustomerDialogFragment extends DialogFragment {

    private static final String TAG = CreateScheduleFragment.class.getSimpleName();;
    private ListView customerListView;
    private Activity parentActivity;
    private ProgressBar progressBar;
    CustomerJson[] customerList;
    private int selectedClassId;
    EnrollJson enroll;
    private String url;
    private boolean showDialog;

    public void setSelectedClassId(int selectedClassId) {
        this.selectedClassId = selectedClassId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.customer_selectiondialog_layout, container, false);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        customerListView=(ListView)view.findViewById(R.id.showCustomerDeatails);
        customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnrollJson enrollJson=new EnrollJson();
                enrollJson.setClassId(selectedClassId);
                enrollJson.setCustomerId(customerList[position].getCustomerId());
                url=Constant.ENROLL_CUSTOMER_BY_CLASSID_URL;
                triggerVolleyPostRequest(url,enrollJson);
            }
        });
        return view;
    }

    private void triggerVolleyPostRequest(String url, EnrollJson enrollJson) {
        WebServices.sendPostRequest(parentActivity, url, enrollJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "volley response:" + response.toString());
                EnrollDrollPostResponse responseJson = new Gson().fromJson(response.toString(), EnrollDrollPostResponse.class);
                if (responseJson.getStatusCode().equals(WebServices.SUCCESS_CODE)) {
                    Toast.makeText(parentActivity, "added successfully", Toast.LENGTH_SHORT).show();
                    dismiss();
                    Intent intent= new Intent(CustomerByClassFragment.CUSTOMER_ADDED_EVENT);
                    LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(intent);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "error occured:" + error.toString());
                error.printStackTrace();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(parentActivity, "Check connection", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(parentActivity, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, EnrollJson.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        parentActivity=getActivity();
        triggerVolleyGetRequest();
    }

    private void triggerVolleyGetRequest() {
        WebServices.triggerVolleyGetRequest(parentActivity, Constant.GET_CUSTOMER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get Customer response:" + response);
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                GetCustomer getCustomer = gson.fromJson(response, GetCustomer.class);
                if (getCustomer != null) {
                    customerList = getCustomer.getData().getCustomerList();
                    if (customerList != null) {
                        boolean showDialog = false;
                        CustomerListAdapter adapter = new CustomerListAdapter(parentActivity, customerList, showDialog);
                        customerListView.setAdapter(adapter);
                    } else {
                        customerList = new CustomerJson[0];
                        Toast.makeText(parentActivity, "Add new customers", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                // Handle error
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(parentActivity, "Check connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
