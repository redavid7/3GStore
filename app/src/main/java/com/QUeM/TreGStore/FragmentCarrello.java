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
import android.widget.ImageView;
import android.widget.TextView;

import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.QUeM.TreGStore.DatabaseClass.ProdottiAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.support.constraint.Constraints.TAG;


public class FragmentCarrello extends Fragment {

    //collegamento del firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    //ottengo le info riguardo l'utente connesso
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    //imposto il riferimento alla collezione che contiene i prodotti da mostrare
    private CollectionReference carrelloRef=db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello");
    //inizializzo la classe che serve ad adattare il contenuto di ogni occorenza di un documento al mio schema
    private ProdottiAdapter adapter;
    //vista del fragment
    private View fragmentHomeView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeView=inflater.inflate(R.layout.fragment_layout_home, container, false);
        //inizializzo le variabili che mostrano il carrello vuoto
        final TextView testoCarrelloVuoto=fragmentHomeView.findViewById(R.id.text_view_carrellovuoto);
        final ImageView iconaCarrelloVuoto=fragmentHomeView.findViewById(R.id.image_view_carrellovuoto);
        //verifico se il documento cancellami esiste, perchè se esiste vuol dire che il carrello è vuoto
        DocumentReference cancellami=db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello").document("cancellami");
        //provo a prendere il documento
        cancellami.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //se la connessione è riuscita, vedo se il documento esiste o meno
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //se il documento esiste, rendo visibile la visualizzazione "carrello vuoto"
                        Log.d(TAG, "cancellami esiste");
                        testoCarrelloVuoto.setVisibility(View.VISIBLE);
                        iconaCarrelloVuoto.setVisibility(View.VISIBLE);
                    } else {
                        Log.d(TAG, "cancellami non esiste");
                        testoCarrelloVuoto.setVisibility(View.INVISIBLE);
                        iconaCarrelloVuoto.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //chiamo il metodo per la settare la recycler view
        Log.d(TAG, "mmm prima di setUpRecyclerView in home Fragment");
        setUpRecyclerView();
        Log.d(TAG, "mmm dopo di setUpRecyclerView in home Fragment");
        return fragmentHomeView;
    }

    //debug message
    //Toast.makeText(getContext(), "debug", Toast.LENGTH_LONG).show();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //metodi per evitare un ascolto continuo e che crashi

    @Override
    public void onStart() {
        //inizia il collegamento
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        //smette di prendere i dati
        super.onStop();
        adapter.stopListening();
    }

    private void setUpRecyclerView(){
        //creo la query per prelevare e ordinare i dati
        Query query=carrelloRef.orderBy("nome", Query.Direction.ASCENDING);
        //creo un file che abbia le imèostazioni per far funzionare l'adapter
        FirestoreRecyclerOptions<Prodotti> opzioni=new FirestoreRecyclerOptions.Builder<Prodotti>().setQuery(query, Prodotti.class).build();
        //inizializzo adapter
        adapter=new ProdottiAdapter(opzioni);
        //inizializzo recyclerview
        RecyclerView recyclerView= fragmentHomeView.findViewById(R.id.recyclerview);
        //metodo che migliora le performance
        recyclerView.setHasFixedSize(true);
        //imposta la recyclerview nel layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //applica l'adapter nel fragment
        recyclerView.setAdapter(adapter);
    }

}