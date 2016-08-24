package com.fiticket.fitnesspro.fragments;

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
import com.fiticket.fitnesspro.adapters.CustomerListAdapter;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.CustomerJson;
import com.fiticket.fitnesspro.pojos.GetCustomer;
import com.google.gson.Gson;

/**
 * Created by InFinItY on 11/30/2015.
 */
public class CustomerListFragment extends Fragment {
    private static final String TAG = CustomerListFragment.class.getSimpleName();
    onButtonClicked mCallBack;
    Context context;
    private Button addButton,deletebtn;
    ListView customerListView;
    CustomerJson[] customerList;
    ProgressBar progressBar;

    public interface onButtonClicked
    {
        void onCreateClicked();
        void onCustomerSelected(CustomerJson customer);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        try {

            mCallBack=(onButtonClicked)getActivity();
        }catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ "must implement onButtonClicked");
        }


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.customerlist_fragment, container, false);

        SharedPreferences sPref=context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);
        if((sPref.getInt(Constant.USER_ROLE_ID,3))>=Constant.ROLE_TEACHER) {
            addButton = (Button) view.findViewById(R.id.addCustomerlistButton);
            addButton.setVisibility(View.GONE);
        }
        else {
            addButton = (Button) view.findViewById(R.id.addCustomerlistButton);
            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    mCallBack.onCreateClicked();
                }
            });
        }
        /*deletebtn=(Button) view.findViewById(R.id.deleteCustomerlistButton);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

            }
        });
*/
        progressBar=(ProgressBar)view.findViewById(R.id.listCustomerProgressbar);

        customerListView=(ListView)view.findViewById(R.id.customersAttendance);
        if(((sPref.getInt(Constant.USER_ROLE_ID,3))!=Constant.ROLE_TEACHER)&&((sPref.getInt(Constant.USER_ROLE_ID,3))!=Constant.ROLE_EMPLOYEE)) {
            customerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    String url = Constant.DELETE_CUSTOMER_URL + customerList[position].getCustomerId();
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

            customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCallBack.onCustomerSelected(customerList[position]);
                }
            });
        }
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        triggerVolleyGetRequest();
    }

    private void triggerVolleyGetRequest() {
        WebServices.triggerVolleyGetRequest(context, Constant.GET_CUSTOMER_URL, new Response.Listener<String>() {
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
                        CustomerListAdapter adapter = new CustomerListAdapter(context, customerList, showDialog);
                        customerListView.setAdapter(adapter);
                    } else {
                        customerList = new CustomerJson[0];
                        Toast.makeText(context, "Add new customers", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                // Handle error
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GET_CUSTOMER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Get Customer response:"+response);
                        progressBar.setVisibility(View.GONE);
                        Gson gson=new Gson();
                        GetCustomer getCustomer=gson.fromJson(response,GetCustomer.class);
                        if (getCustomer!=null) {
                            customerList=getCustomer.getData().getCustomerList();
                            if (customerList != null) {
                                CustomerListAdapter adapter = new CustomerListAdapter(context, customerList);
                                customerListView.setAdapter(adapter);
                            } else {
                                customerList = new CustomerJson[0];
                                Toast.makeText(context, "Add new customers", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        // Handle error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            //TODO

                        }
                        else if (error instanceof ServerError) {
                            //TODO

                        }
                        else if (error instanceof NetworkError) {
                            //TODO

                        }
                        else if (error instanceof ParseError) {
                            //TODO

                        }
                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);*/
    }

    private void triggerVolleyDeleteRequest(String url) {
        WebServices.triggerVolleyDeleteRequest(context, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Delete response:" + response.toString());
                Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
                triggerVolleyGetRequest();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG,"error occured:"+error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*StringRequest deleteRequest=new StringRequest(Request.Method.DELETE,url ,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Delete response:" + response.toString());
                Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
                triggerVolleyGetRequest();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        *//*Toast.makeText(context, "error", Toast.LENGTH_LONG).show();*//*
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG,"error occured:"+error.toString());
                        *//*Log.e(TAG,error.getMessage());*//*
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            //TODO

                        }
                        else if (error instanceof ServerError) {
                            //TODO

                        }
                        else if (error instanceof NetworkError) {
                            //TODO

                        }
                        else if (error instanceof ParseError) {
                            //TODO

                        }
                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(deleteRequest);*/
    }
}