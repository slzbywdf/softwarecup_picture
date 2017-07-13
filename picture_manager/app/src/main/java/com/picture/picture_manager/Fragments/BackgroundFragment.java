package com.picture.picture_manager.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.picture.picture_manager.R;


/**
 * Created by 13307 on 2017/3/26.
 */

public class BackgroundFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.background_fragment, container, false);
        return view;
    }
}
