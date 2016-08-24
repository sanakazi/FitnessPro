package com.fiticket.fitnesspro.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiticket.fitnesspro.R;

/**
 * Created by InFinItY on 11/27/2015.
 */
public class NotificationFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.notification_fragment, container, false);
        return view;
    }
}
