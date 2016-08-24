package com.fiticket.fitnesspro.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiticket.fitnesspro.R;

/**
 * Created by InFinItY on 12/23/2015.
 */
public class ComingSoon extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.coming_soon_layout, container, false);

        return view;
    }
}
