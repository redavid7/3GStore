package com.QUeM.TreGStore;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FragmentHome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //String getArgument = getArguments().getString("switch");//Get pass data with its key value
        View fragmentHomeView;
        /*
        if(getArgument.equalsIgnoreCase("0")){
            fragmentHomeView=inflater.inflate(R.layout.fragment_layout_home_vuoto, container, false);
        }else{
            fragmentHomeView=inflater.inflate(R.layout.fragment_layout_home_pieno, container, false);
        }
        */
        fragmentHomeView=inflater.inflate(R.layout.fragment_layout_home_vuoto, container, false);
        return fragmentHomeView;
    }

    //debug message
    //Toast.makeText(getContext(), "debug", Toast.LENGTH_LONG).show();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final TextView nulla=(TextView) view.findViewById(R.id.textCarrelloVuoto);
        ImageView carr=(ImageView) view.findViewById(R.id.immagineCarrelloVuoto);





        carr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text= String.valueOf(((HomeActivity) getActivity()).getItemFromList(0));

                nulla.setText(text);
            }
        });


    }

}