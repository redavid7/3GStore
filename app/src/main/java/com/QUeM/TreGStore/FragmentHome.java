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


public class FragmentHome extends Fragment {


    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private CollectionReference carrelloRef=db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello");
    private ProdottiAdapter adapter;
    private View fragmentHomeView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeView=inflater.inflate(R.layout.fragment_layout_home, container, false);
        final TextView testoCarrelloVuoto=fragmentHomeView.findViewById(R.id.text_view_carrellovuoto);
        final ImageView iconaCarrelloVuoto=fragmentHomeView.findViewById(R.id.image_view_carrellovuoto);
        //testo per carrello vuoto
        DocumentReference cancellami=db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello").document("cancellami");
        cancellami.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //se la connessione è riuscita, vedo se il documento esiste o meno
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //se il documento esiste, creo un oggetto che corrisponde al prodotto legato al codice a barre
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

        setUpRecyclerView();
        return fragmentHomeView;
    }

    //debug message
    //Toast.makeText(getContext(), "debug", Toast.LENGTH_LONG).show();

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

    private void setUpRecyclerView(){
        Query query=carrelloRef.orderBy("nome", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Prodotti> opzioni=new FirestoreRecyclerOptions.Builder<Prodotti>().setQuery(query, Prodotti.class).build();
        adapter=new ProdottiAdapter(opzioni);
        RecyclerView recyclerView= fragmentHomeView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

}