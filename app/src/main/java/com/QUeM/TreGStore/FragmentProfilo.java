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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class FragmentProfilo extends Fragment {

    //collegamento al firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    //prendo l'utente attualmente connesso
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    //imposto il riferimento alla collezione che contiene le cronologie degli ordini
    private CollectionReference cronOrdiniRef=db.collection("cronologiaOrdini").document(auth.getUid()).collection("dataOrdine");
    //inizializzo la classe che serve ad adattare il contenuto di ogni occorenza di un documento al mio schema
    //vista del fragment
    private View fragmentProfilo;
    //lista delle stringe degli ordini trovati nella cronologia
    private ArrayList<String> list;
    private TextView txt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        fragmentProfilo=inflater.inflate(R.layout.fragment_layout_profilo, container, false);
        return fragmentProfilo;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt=getActivity().findViewById(R.id.Bella);
        list = new ArrayList<>();
        //riempio la lista degli ordini con quelli trovati nella cronologia
        cronOrdiniRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            txt.setText("Documento trovato");//questo lo fa
                            list.add("Completo");//questi no
                            list.add("Finito");//questi no
                        } else {
                            txt.setText("Errore");
                        }
                    }
                });
        //oggetto contenente la lista da mostrare nel layout
        final ListView mylist =  getActivity().findViewById(R.id.listView1);
        final ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
        mylist.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}