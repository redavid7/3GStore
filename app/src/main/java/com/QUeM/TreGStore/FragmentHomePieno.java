package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentHomePieno extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentHomeView;
        fragmentHomeView=inflater.inflate(R.layout.fragment_layout_home_pieno, container, false);
        return fragmentHomeView;
    }

    //debug message
    //Toast.makeText(getContext(), "debug", Toast.LENGTH_LONG).show();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}
