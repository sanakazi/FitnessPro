package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fiticket.fitnesspro.R;
import com.fiticket.fitnesspro.adapters.ClassListAdapter;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.ClassJson;
import com.fiticket.fitnesspro.pojos.ClassTypeJson;
import com.fiticket.fitnesspro.pojos.GetClassJson;
import com.fiticket.fitnesspro.singleton.MySingleton;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by InFinItY on 1/11/2016.
 */
public class CustomerClassSelectionFragment extends DialogFragment {
    private ProgressBar progressBar;
    private AppCompatActivity parentActivity;
    String className;
    private ListView customerClassListView;
    private ArrayList<ClassJson> classLists= new ArrayList<ClassJson>();
    onShowCustomerClassItemSelected mCallBack;
    private ClassTypeJson[] classTypes;


    public interface onShowCustomerClassItemSelected
    {
        void onClassItemSelect(ClassJson classes);
    }

    static CustomerClassSelectionFragment newInstance()
    {
        CustomerClassSelectionFragment c=new CustomerClassSelectionFragment();
        return c;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallBack = (onShowCustomerClassItemSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.class_selection_fragment, container, false);
        progressBar=(ProgressBar)view.findViewById(R.id.classProgressBar);
        customerClassListView=(ListView)view.findViewById(R.id.classCustomer);
        customerClassListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallBack.onClassItemSelect(classLists.get(position));


                dismiss();
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        parentActivity=(AppCompatActivity)getActivity();
        triggerVolleyGetVenueRequest();
    }

    private void triggerVolleyGetVenueRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GET_CLASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        GetClassJson getClassJson = gson.fromJson(response, GetClassJson.class);
                        if(getClassJson!=null)
                        {
                            classLists=getClassJson.getData().getClassList();

                            if(classLists!=null) {
                                ClassListAdapter adapter = new ClassListAdapter(parentActivity, classLists, classTypes);
                                customerClassListView.setAdapter(adapter);


                            }
                            else
                            {
                                /*ArrayList<ClassJson> classLists = new ArrayList<ClassJson>();*/
                                classLists= new ArrayList<ClassJson>();


                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(parentActivity, "Check connection", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(parentActivity).addToRequestQueue(stringRequest);
    }
}
