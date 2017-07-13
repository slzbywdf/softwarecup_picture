package com.picture.picture_manager.Beans;

import android.app.Fragment;

/**
 * Created by 13307 on 2017/5/3.
 */

public class OneFragment {
    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    private Fragment fragment;
    private int fragmentId;

    public OneFragment(Fragment f, int id) {
        fragment = f;
        fragmentId = id;
    }
}
