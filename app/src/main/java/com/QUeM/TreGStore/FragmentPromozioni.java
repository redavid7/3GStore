package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.QUeM.TreGStore.DatabaseClass.ProdottiAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

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

    //Metodo per leggere tutti i prodotti nel db
    public ArrayList<Prodotti> readAllProd(){
        //prodotti da restituire
        final ArrayList<Prodotti> toRet=new ArrayList<Prodotti>();
        //creo la connessione
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //cerco tutti i file nel documento prodotti
        db.collection("prodotti").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int c=0;
                // per ogni elemento in task prendo l'oggetto di tipo prodotti e lo aggiundo ai valori da ritornare
                if (task.isSuccessful()) {
                    for(DocumentSnapshot document : task.getResult()){
                        toRet.add(document.toObject(Prodotti.class));
                        Log.d(TAG, "Prodotto "+toRet.get(c).getNome());
                        c++;
                    }
                }
                else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return toRet;
    }

    public boolean isDiscount(Prodotti p){
        Calendar c = Calendar.getInstance();
        int tmp =   p.getTempoPromozione().compareTo(c.getTime());
        if(tmp<=0){
            return true;
        }
        else{
            return false;
        }
    }

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