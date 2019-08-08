package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import static android.support.constraint.Constraints.TAG;

public class FragmentSettings extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file

        Log.d(TAG, "mmm dentro FragmentSettings, prima di addPreferencesFromResource ");
        addPreferencesFromResource(R.xml.preferences);
        Log.d(TAG, "mmm dopo di addPreferencesFromResource ");
        return inflater.inflate(R.layout.fragment_fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}