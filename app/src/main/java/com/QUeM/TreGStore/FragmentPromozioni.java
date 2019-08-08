package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.QUeM.TreGStore.DatabaseClass.ProdottiAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FragmentPromozioni extends Fragment {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference prodottiRef=db.collection("prodotti");
    private ProdottiAdapter adapter;
    private View fragmentPromView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        fragmentPromView=inflater.inflate(R.layout.fragment_layout_promozioni, container, false);
        setUpRecyclerView();
        return fragmentPromView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    //Set up reciclerView
    private void setUpRecyclerView(){
        Query query=prodottiRef.whereEqualTo("promozione",true);
        FirestoreRecyclerOptions<Prodotti> opzioni=new FirestoreRecyclerOptions.Builder<Prodotti>().setQuery(query, Prodotti.class).build();
        adapter=new ProdottiAdapter(opzioni);
        RecyclerView recyclerView= fragmentPromView.findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

}