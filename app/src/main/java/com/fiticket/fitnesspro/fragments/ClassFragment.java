package com.fiticket.fitnesspro.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.fiticket.fitnesspro.adapters.ClassListAdapter;
import com.fiticket.fitnesspro.constants.Constant;
import com.fiticket.fitnesspro.pojos.ClassJson;
import com.fiticket.fitnesspro.pojos.ClassTypeJson;
import com.fiticket.fitnesspro.pojos.GetClassJson;
import com.fiticket.fitnesspro.pojos.GetClassTypes;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by InFinItY on 11/30/2015.
 */
public class ClassFragment extends Fragment {
    private static final String TAG = CustomerFragment.class.getSimpleName();
    onClassButtonClicked mCallBack;
    private Context context;
    private Button createNewButton,exitButton;
    ListView classListView;
    private ArrayList<ClassJson> classLists= new ArrayList<ClassJson>();
    ClassTypeJson[] classTypes;
    ProgressBar progressBar;
    private AppCompatActivity parentActivity;



    //Setting an interface for to create new fragment
    public interface onClassButtonClicked
    {
        void onCreateNewClicked(ClassTypeJson[] classTypes);
        void onClassSelected(ClassJson classes,int tabCount,ClassTypeJson[] classTypes);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        try {

            mCallBack=(onClassButtonClicked)getActivity();
        }catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ "must implement onButtonClicked");
        }


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.classlist_fragment, container, false);

        progressBar=(ProgressBar)view.findViewById(R.id.classListProgressBar);
        Log.i("Fragment","ClassFragment");
        classListView=(ListView)view.findViewById(R.id.classList);
        classListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallBack.onClassSelected(classLists.get(position), ClassDetailsViewPageFragment.CLASS_DETAILS_TAB,classTypes);
            }
        });
        classListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                String url = Constant.DELETE_CLASS_URL + classLists.get(position).getClassId();
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


        createNewButton = (Button) view.findViewById(R.id.createClasslistButton);
        createNewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallBack.onCreateNewClicked(classTypes);
            }
        });

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        parentActivity=(AppCompatActivity)getActivity();
        triggerVolleyGetClassTypeRequest();

    }

    private void triggerVolleyGetClassTypeRequest() {
        WebServices.triggerVolleyGetRequest(context, Constant.GET_CLASS_TYPES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"GetClassTypes response:"+response);
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                GetClassTypes getClassTypes = gson.fromJson(response, GetClassTypes.class);
                if(getClassTypes!=null)
                {
                    classTypes=getClassTypes.getData().getClassTypeList();
                    triggerVolleyGetClassRequest();
                }
                else
                {
                    Toast.makeText(context, "Class type not defined", Toast.LENGTH_LONG).show();
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
    }

    private void triggerVolleyGetClassRequest() {
        WebServices.triggerVolleyGetRequest(context, Constant.GET_CLASS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"GetClass response:"+response);
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                GetClassJson getClassJson;
                try {
                    getClassJson = gson.fromJson(response, GetClassJson.class);

                if(getClassJson!=null)
                {
                    classLists=getClassJson.getData().getClassList();

                    if(classLists!=null) {
                        if (classTypes!=null)
                        {
                            ClassListAdapter adapter = new ClassListAdapter(parentActivity, classLists, classTypes);
                            classListView.setAdapter(adapter);
                        }
                        else
                        {
                            classTypes=new ClassTypeJson[0];
                            ClassListAdapter adapter = new ClassListAdapter(parentActivity, classLists, classTypes);
                            classListView.setAdapter(adapter);
                        }
                    }
                    else
                    {
                        classLists= new ArrayList<ClassJson>();
                    }
                }
                else
                {
                    Toast.makeText(context, "Add new classes", Toast.LENGTH_LONG).show();
                }
                }catch (Exception e){
                    e.printStackTrace();
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
        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GET_CLASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"GetClass response:"+response);
                        progressBar.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        GetClassJson getClassJson = gson.fromJson(response, GetClassJson.class);
                        if(getClassJson!=null)
                        {
                            classLists=getClassJson.getData().getClassList();

                            if(classLists!=null) {
                                ClassListAdapter adapter = new ClassListAdapter(parentActivity, classLists);
                                classListView.setAdapter(adapter);
                            }
                            else
                            {
                                *//*ArrayList<ClassJson> classLists = new ArrayList<ClassJson>();*//*
                                classLists= new ArrayList<ClassJson>();


                            }
                        }
                        else
                        {
                            Toast.makeText(context, "Add new classes", Toast.LENGTH_LONG).show();
                        }

                       *//* *//*
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
                Log.d(TAG, "Delete response:" + response.toString());
                Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
                triggerVolleyGetClassRequest();
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
                        Log.d(TAG, "Delete response:" + response.toString());
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