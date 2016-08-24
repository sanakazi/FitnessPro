package com.fiticket.fitnesspro.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by InFinItY on 1/19/2016.
 */
public class VenueDialogFragment extends DialogFragment {
    private static final String TAG = VenueListFragment.class.getSimpleName();
    ListView venueListView;
    private Activity parentActivity;
    AddVenueClickListener mCallBack;
    private Button addNewButton;
    VenueJson[] venueLists;
    ProgressBar progressBar;

    public void setVenueSelectedListener(FragmentCreateClass venueSelectedListener) {
        this.mCallBack = (AddVenueClickListener)venueSelectedListener;
    }

    public interface AddVenueClickListener
    {
        void onVenueClick(VenueJson venues);
    }

    static VenueDialogFragment newInstance()
    {
        VenueDialogFragment c=new VenueDialogFragment();
        return c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.venue_dialog, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.classProgressBar);
        venueListView=(ListView)view.findViewById(R.id.showVenueClass);
        venueListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallBack.onVenueClick(venueLists[position]);
                dismiss();
            }
        });

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        parentActivity=getActivity();
        triggerVolleyGetVenueRequest();

    }

    private void triggerVolleyGetVenueRequest() {
        WebServices.triggerVolleyGetRequest(parentActivity, Constant.GET_VENUE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                GetVenueJson getVenueJson = gson.fromJson(response, GetVenueJson.class);
                if (getVenueJson != null) {
                    venueLists = getVenueJson.getData().getVenueList();
                    if (venueLists != null) {
                        VenueListAdapter adapter = new VenueListAdapter(parentActivity, venueLists);
                        venueListView.setAdapter(adapter);
                    } else {
                        venueLists = new VenueJson[0];
                        Toast.makeText(parentActivity, "Add new venue", Toast.LENGTH_LONG).show();
                    }
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
