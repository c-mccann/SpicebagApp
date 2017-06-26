package com.example.carlmccann2.spicebag.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carlmccann2.spicebag.R;

/**
 * Created by carlmccann2 on 19/06/2017.
 */

public class SearchFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabbed_hub_1_search, container, false);

    }
}

