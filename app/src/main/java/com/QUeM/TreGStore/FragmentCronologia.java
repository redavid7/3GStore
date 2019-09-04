package com.QUeM.TreGStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Iterator;

import static android.support.constraint.Constraints.TAG;

public class FragmentCronologia extends Fragment {

    //collegamento al firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    //prendo l'utente attualmente connesso
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    //imposto il riferimento alla collezione che contiene le cronologie degli ordini
    private CollectionReference cronOrdiniRef=db.collection("cronologiaOrdini").document(auth.getUid()).collection("dataOrdine");
    //riferimento ai prodotti di un determinato ordine
    private CollectionReference prodottiRef;
    //vista del fragment
    private View fragmentProfilo;
    //Array delle stringe degli ordini trovati nella cronologia
    private ArrayList<String> arrayList1;
    //Array delle stringe dei prodotti trovati nel singolo ordine
    private ArrayList<String> arrayList2;
    //lista dei prodotti da mostrare nel layout
    ListView mylist2 ;
    //adapter delle date
    ArrayAdapter<String> adapterS2 ;

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
        //inizializzo l'array  degli ordini
        arrayList1 = new ArrayList<>();
        //riempio l'array degli ordini con quelli trovati nella cronologia e chiamo il metodo che inizializzera la lista dei prodotti
        cronOrdiniRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //iteratore degli ordini trovati nel database
                        Iterator<DocumentSnapshot> q = queryDocumentSnapshots.iterator();
                       while (q.hasNext()) {
                           //aggiungo gli id all'array
                           String dataOrdine="Giorno ";
                           dataOrdine=dataOrdine.concat(q.next().getId());
                           dataOrdine=dataOrdine.replace("_", ", Ora:");
                           addToArrayList1(dataOrdine);
                       }
                       //riempio la lista dei prodotti
                        creaListaAdapter1();
                    }
                });
    }
    //metodo che inserisce il valore della data sottoforma di stringa
    public void addToArrayList1(String s){
        arrayList1.add(s);
    }
    //metodo che inserisce il valore del prodotto sottoforma di stringa
    public void addToArrayList2(String s){
        arrayList2.add(s);
    }

    //metodo per impostare la lista dei prodotti
    public void creaListaAdapter1(){
        //inizializzo l'array dei prodotti
        arrayList2 = new ArrayList<>();
        //oggetto contenente la lista degli ordini da mostrare nel layout
        final ListView mylist =  getActivity().findViewById(R.id.listView1);
        //creo l'adapter delle date degli ordini
        final ArrayAdapter<String> adapterS = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, arrayList1);
        //imposto la lista
        mylist.setAdapter(adapterS);
        //imposto cosa fare quando viene cliccato un certo ordine
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //prendo il valore sottoforma di stringa dell'oggetto cliccato
                String item= mylist.getItemAtPosition(position).toString();
                item=item.substring(7);

                item=item.replace(", Ora:", "_");
                Log.d(TAG, item);
                //prendo il riferimento ai prodotti associati alla data cliccata
                prodottiRef = db.collection("cronologiaOrdini").document(auth.getUid()).collection("dataOrdine").document(item).collection("prodottiAcquistati");
                //prendo tutti prodotti associati al riferimento
                prodottiRef.get()
                       .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                           @Override
                           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                               //prendo l'iterator dei prodotti trovati
                               Iterator<DocumentSnapshot> q = queryDocumentSnapshots.iterator();
                               //controllo se l'array non è vuoto
                               if(!arrayList2.isEmpty()){
                               //pulisco l'array
                                   arrayList2.clear();
                               //invio una notifica alla lista dei prodotti avvisando che i dati da inserire cambieranno
                               adapterS2.notifyDataSetChanged();
                               }
                               //inizializzo la lista dei prodotti acquistati
                               mylist2 =  getActivity().findViewById(R.id.listView2);
                               //riempio la lista dei prodotti tramite l'iterator
                               while (q.hasNext()) {
                                   //aggiungo gli id  del prodotto all'array
                                   addToArrayList2(q.next().toObject(Prodotti.class).toString());
                               }
                               //inizializzo l'adapter della lista dei prodotti
                               adapterS2 = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, arrayList2);
                               //riempio la lista dei prodotti
                               mylist2.setAdapter(adapterS2);
                           }
                       });
            }
        });
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