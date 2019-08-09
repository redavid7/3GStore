package com.QUeM.TreGStore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.QUeM.TreGStore.DatabaseClass.Conti;
import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.QUeM.TreGStore.DatabaseClass.ProdottiAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import static android.support.constraint.Constraints.TAG;


//todo: acquisto carrello, click modifica?, recicler view vuota?

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
    //inizializzazioni variabili fab
    private FloatingActionButton fabAggiungiProdotto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeView=inflater.inflate(R.layout.fragment_layout_home, container, false);

        //inizializza il pulsante floating action button che fa da menù
        fabAggiungiProdotto = fragmentHomeView.findViewById(R.id.aggiungi_prodotto);

        //azione del floating action button menu quando cliccato per aggiungere i prodotti
        fabAggiungiProdotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ScannedBarcodeActivity.class));
            }
        });

        //chiamo il metodo per la settare la recycler view
        Log.d(TAG, "mmm prima di setUpRecyclerView in home Fragment");
        setUpRecyclerView();
        Log.d(TAG, "mmm dopo di setUpRecyclerView in home Fragment");
        //dopo aver settato la recycler view aggiungo un collegamento continuo per il conto del carrello
        final TextView testoCarrello=fragmentHomeView.findViewById(R.id.totaleCarrello);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference totCarrelloRef=db.collection("conti").document(auth.getUid());
        final String testoCarrelloResource=getString(R.string.shopping_amount);
        totCarrelloRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    Conti contoDaAggiornare=snapshot.toObject(Conti.class);

                    testoCarrello.setText(testoCarrelloResource+" "+contoDaAggiornare.getTotaleCarrello());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

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
        //impostazione dell'animazione per far scomparire il fab mentre scrolla la lista
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        fabAggiungiProdotto.show();
                        break;
                    default:
                        fabAggiungiProdotto.hide();
                        break;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        };
        //aggiunge il comportamento alla recycler view
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(scrollListener);

        //applica l'adapter nel fragment
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

}