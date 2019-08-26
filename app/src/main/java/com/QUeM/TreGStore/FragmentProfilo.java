package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Iterator;

public class FragmentProfilo extends Fragment {

    //collegamento al firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    //prendo l'utente attualmente connesso
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    //imposto il riferimento alla collezione che contiene le cronologie degli ordini
    private CollectionReference cronOrdiniRef=db.collection("cronologiaOrdini").document(auth.getUid()).collection("dataOrdine");
    //riferimento ai prodotti di un determinato ordine
    private CollectionReference prodottiRef;
    //inizializzo la classe che serve ad adattare il contenuto di ogni occorenza di un documento al mio schema
    //vista del fragment
    private View fragmentProfilo;
    //lista delle stringe degli ordini trovati nella cronologia
    private ArrayList<String> list;
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
        list = new ArrayList<>();
        //riempio la lista degli ordini con quelli trovati nella cronologia
        cronOrdiniRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Iterator<DocumentSnapshot> q = queryDocumentSnapshots.iterator();
                       while (q.hasNext()) {
                           //aggiungo gli id alla lista
                           addToList(q.next().getId());
                       }
                       //riempio la lista
                        creaListaAdapter1();
                    }
                });
    }
    public void addToList(String s){
        list.add(s);
    }
    public void creaListaAdapter1(){
        //oggetto contenente la lista da mostrare nel layout
        final ListView mylist =  getActivity().findViewById(R.id.listView1);
        //creo l'adapter delle date
        final ArrayAdapter<String> adapterS = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
        //imposto la lista
        mylist.setAdapter(adapterS);
        //imposto cosa fare quando viene cliccato un certo ordine
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = mylist.getItemAtPosition(position).toString();
                prodottiRef = db.collection("cronologiaOrdini").document(auth.getUid()).collection("dataOrdine").document(item).collection("prodottiAcquistati");
               prodottiRef.get()
                       .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                           @Override
                           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                               Iterator<DocumentSnapshot> q = queryDocumentSnapshots.iterator();
                               list.clear();
                               while (q.hasNext()) {
                                   //aggiungo gli id alla lista
                                   addToList(q.next().toObject(Prodotti.class).toString());
                               }
                               //rendo invisibile la prima lista in modo da mostrare solamente la seconda
                               mylist.setVisibility(View.INVISIBLE);
                               //riempio la lista
                               creaListaAdapter2();
                           }
                       });
            }
        });
    }
    public void creaListaAdapter2(){
        //oggetto contenente la lista da mostrare nel layout
        final ListView mylist =  getActivity().findViewById(R.id.listView2);
        //creo l'adapter delle date
        final ArrayAdapter<String> adapterS = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
        //imposto la lista
        mylist.setAdapter(adapterS);
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