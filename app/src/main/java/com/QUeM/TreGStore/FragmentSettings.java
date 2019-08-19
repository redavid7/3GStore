package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import static android.support.constraint.Constraints.TAG;

public class FragmentSettings extends PreferenceFragmentCompat {

    private View fragmentView;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        fragmentView=inflater.inflate(R.layout.fragment_layout_settings, container, false);
        Log.d(TAG, "mmm dentro FragmentSettings, prima di addPreferencesFromResource ");
        addPreferencesFromResource(R.xml.preferences);
        Log.d(TAG, "mmm dopo di addPreferencesFromResource ");
        return fragmentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}