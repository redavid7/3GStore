package com.QUeM.TreGStore;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.QUeM.TreGStore.DatabaseClass.ProdottiAdapterProm;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FragmentPromozioni extends Fragment {
    private static final String CHANNEL_ID = "com.QUeM.TreGStore.ANDROID";
    //collegamento al firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    //imposto il riferimento alla collezione che contiene i prodotti da mostrare
    private CollectionReference prodottiRef=db.collection("prodotti");
    //inizializzo la classe che serve ad adattare il contenuto di ogni occorenza di un documento al mio schema
    private ProdottiAdapterProm adapter;
    //vista del fragment
    private View fragmentProdView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        fragmentProdView=inflater.inflate(R.layout.fragment_layout_promozioni, container, false);
        setUpRecyclerView();
        return fragmentProdView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNotif();
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
        adapter= new ProdottiAdapterProm(opzioni);
        RecyclerView recyclerView= fragmentProdView.findViewById(R.id.recyclerviewP);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    //inizializzazione notifica
    public void initNotif(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("C'è un nuovo prodotto in sconto!!")
                .setContentText("sostituire con il nome del prodotto")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(getTag(),0,builder.build());
    }

}