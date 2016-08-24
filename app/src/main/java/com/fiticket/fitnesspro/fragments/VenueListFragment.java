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
import com.fiticket.fitnesspro.adapters.VenueListAdapter;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.GetVenueJson;
import com.fiticket.fitnesspro.pojos.VenueJson;
import com.google.gson.Gson;

/**
 * Created by InFinItY on 12/9/2015.
 */
public class VenueListFragment extends Fragment {
    private static final String TAG = VenueListFragment.class.getSimpleName();
    Context context;
    ListView venueListView;
    onVenueButtonClicked mCallBack;
    private Button addNewButton;
    VenueJson[] venueLists;
    ProgressBar progressBar;

    public interface onVenueButtonClicked {
        void onCreateNewVenueClicked();

        void onVenueSelected(VenueJson venue, int i, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {

            mCallBack = (onVenueButtonClicked) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onButtonClicked");
        }


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.venuelist_fragment, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.venueListProgressbar);

        SharedPreferences sPref = context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);
        if ((sPref.getInt(Constant.USER_ROLE_ID, 3)) >= Constant.ROLE_TEACHER) {
            addNewButton = (Button) view.findViewById(R.id.addVenuelistButton);
            addNewButton.setVisibility(View.GONE);
        } else {
            addNewButton = (Button) view.findViewById(R.id.addVenuelistButton);
            addNewButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    mCallBack.onCreateNewVenueClicked();
                }
            });
        }
        venueListView = (ListView) view.findViewById(R.id.venueList);
        if (((sPref.getInt(Constant.USER_ROLE_ID, 3)) != Constant.ROLE_TEACHER) && ((sPref.getInt(Constant.USER_ROLE_ID, 3)) != Constant.ROLE_EMPLOYEE)) {
            venueListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCallBack.onVenueSelected(venueLists[position], position, venueLists.length);
                }
            });

            venueListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete

                                    String url = Constant.DELETE_VENUE_URL + venueLists[position].getVenueId();
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
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        triggerVolleyGetVenueRequest();
    }

    private void triggerVolleyGetVenueRequest() {

        WebServices.triggerVolleyGetRequest(context, Constant.GET_VENUE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                GetVenueJson getVenueJson = gson.fromJson(response, GetVenueJson.class);
                if (getVenueJson != null) {
                    venueLists = getVenueJson.getData().getVenueList();
                    if (venueLists != null) {
                        VenueListAdapter adapter = new VenueListAdapter(context, venueLists);
                        venueListView.setAdapter(adapter);
                    } else {
                        venueLists = new VenueJson[0];
                        Toast.makeText(context, "Add new venue", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "Check connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GET_VENUE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        GetVenueJson getVenueJson=gson.fromJson(response, GetVenueJson.class);
                        if (getVenueJson!=null) {
                            venueLists = getVenueJson.getData().getVenueList();
                            if (venueLists!=null)
                            {
                            VenueListAdapter adapter = new VenueListAdapter(context, venueLists);
                            venueListView.setAdapter(adapter);
                            }
                            else
                            {
                                venueLists=new VenueJson[0];
                                Toast.makeText(context, "Add new venue", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
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

                        // Handle error
                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);*/
    }

    private void triggerVolleyDeleteRequest(String url) {
        WebServices.triggerVolleyDeleteRequest(context, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "volley response:" + response.toString());
                Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
                triggerVolleyGetVenueRequest();
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
        /*StringRequest deleteRequest=new StringRequest(Request.Method.DELETE,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "volley response:" + response.toString());
                        Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
                        triggerVolleyGetVenueRequest();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        *//*Toast.makeText(context, "error", Toast.LENGTH_LONG).show();*//*
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "error occured:" + error.toString());
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
                        *//*Log.e(TAG,error.getMessage());*//*
                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(deleteRequest);*/
    }
}