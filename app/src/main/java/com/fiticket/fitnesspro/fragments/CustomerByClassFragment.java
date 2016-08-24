package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.fiticket.fitnesspro.adapters.CustomerListAdapter;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.CustomerJson;
import com.fiticket.fitnesspro.pojos.DrollJson;
import com.fiticket.fitnesspro.pojos.EnrollDrollPostResponse;
import com.fiticket.fitnesspro.pojos.GetCustomerByClassId;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by InFinItY on 1/31/2016.
 */
public class CustomerByClassFragment extends Fragment {


    public static final String CUSTOMER_ADDED_EVENT ="CUSTOMER_ADDED_EVENT" ;
    private int classid;
    private boolean b;
    private static CustomerJson selectedCustomer;
    private Activity parentActivity;
    private ListView studentListView;
    private Button addStudentlistButton;
    CustomerJson[] customerList;
    private ProgressBar progressBar;
    private String url;
    private String TAG=CustomerByClassFragment.class.getSimpleName();
    private boolean showDialog;


    public void setClassid(int classid) {
        this.classid = classid;
    }

    public void showCustomerByClass(boolean b) {
        this.b=b;
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.customer_by_class_layout, container, false);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        addStudentlistButton=(Button)view.findViewById(R.id.addStudentlistButton);
        studentListView=(ListView)view.findViewById(R.id.studentList);
        studentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(parentActivity)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                DrollJson drollJson=new DrollJson();
                                drollJson.setClassId(classid);
                                drollJson.setCustomerId(customerList[position].getCustomerId());
                                url = Constant.REMOVE_CUSTOMER_FROM_CLASS_URL;
                                triggerVolleyPostRequest(url, drollJson);
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
        addStudentlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerDialogFragment newFragment = new CustomerDialogFragment();
                newFragment.setSelectedClassId(classid);
                newFragment.show(getActivity().getFragmentManager(), "dialog");
            }
        });
        return view;
    }
    private void triggerVolleyPostRequest(String url, DrollJson drollJson) {
        WebServices.sendPostRequest(parentActivity, url, drollJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "volley response:" + response.toString());
                EnrollDrollPostResponse responseJson = new Gson().fromJson(response.toString(), EnrollDrollPostResponse.class);
                if (responseJson.getStatusCode().equals(WebServices.SUCCESS_CODE)) {
                    triggerVolleyGetRequest();
                    Toast.makeText(parentActivity, "Customer removed successfully", Toast.LENGTH_SHORT).show();

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
                    //Toast.makeText(parentActivity, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(parentActivity, "Customer could not be deleted", Toast.LENGTH_LONG).show();
                }
            }
        }, DrollJson.class);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity=getActivity();

        triggerVolleyGetRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(parentActivity).registerReceiver(mMessageReceiver,
                new IntentFilter(CUSTOMER_ADDED_EVENT));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(parentActivity).unregisterReceiver(mMessageReceiver);
    }

    private void triggerVolleyGetRequest() {
        url=Constant.GET_CUSTOMER_BY_CLASSID_URL+classid;
        WebServices.triggerVolleyGetRequest(parentActivity, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                GetCustomerByClassId getCustomerResponse = gson.fromJson(response, GetCustomerByClassId.class);
                if (getCustomerResponse.getStatusCode().equals("0")) {
                    if (getCustomerResponse != null) {
                        customerList = getCustomerResponse.getData().getCustomerList();
                        if (customerList != null) {
                            showDialog=false;
                            CustomerListAdapter adapter = new CustomerListAdapter(parentActivity, customerList, showDialog);
                            studentListView.setAdapter(adapter);
                        } else {

                            Toast.makeText(parentActivity, "Add new customers", Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    customerList=new CustomerJson[0];
                    CustomerListAdapter adapter = new CustomerListAdapter(parentActivity, customerList, showDialog);
                    studentListView.setAdapter(adapter);
                    Toast.makeText(parentActivity,""+getCustomerResponse.getStatusMsg(),Toast.LENGTH_LONG).show();
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

    // handler for received Intents for the "my-event" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent

            triggerVolleyGetRequest();
        }
    };

}
