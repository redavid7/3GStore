package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.QUeM.TreGStore.DatabaseClass.Conti;
import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class FragmentAcquisto extends Fragment {

    //vista del fragment
    private View fragmentView;
    //ottengo le info riguardo l'utente connesso
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.fragment_layout_acquisto, container, false);
        //inizializzo switch
        final Switch switchSconto= (Switch) fragmentView.findViewById(R.id.acquisto_cardview_impo_applica_sconto_switch);
        //prendo le informazioni del conto e le inserisco nel fragment
        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        //prendo il documento del conto corrispondente all'utente connesso
        final DocumentReference contoRef=db.collection("conti").document(auth.getUid());
        contoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //se la connessione è riuscita, vedo se il documento esiste o meno
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //conto trovato ed esiste
                        final Conti conto=document.toObject(Conti.class);

                        //imposto il testo che corrisponde al totale del carrello
                        TextView acquistoTotPrezzo= fragmentView.findViewById(R.id.acquisto_cardview_info_tot_articoli_txt_prezzo);
                        acquistoTotPrezzo.setText(conto.getTotaleCarrello()+" €");

                        //imposto il testo che corrisponde ai punti derivati dall'acquisto
                        final TextView acquistoPuntiGuadagnati=fragmentView.findViewById(R.id.acquisto_cardview_info_punti_guadagnati_txt_prezzo);
                        final int punti=((int)conto.getTotaleCarrello()/5);
                        acquistoPuntiGuadagnati.setText(String.valueOf(punti));

                        //imposto il testo che corrisponde al saldo disponibile
                        TextView acquistoSaldoDisponibile=fragmentView.findViewById(R.id.acquisto_cardview_impo_carta_addebitata_txt_prezzo);
                        acquistoSaldoDisponibile.setText(conto.getSaldoDisponibile()+" €");

                        //imposto il testo che corrisponde allo sconto disponibile
                        TextView acquistoScontoDisponibile=fragmentView.findViewById(R.id.acquisto_cardview_impo_sconto_disponibile_txt_prezzo);
                        acquistoScontoDisponibile.setText(((float)conto.getCurrentDiscount())+" €");

                        //imposto il testo per il totale da pagare
                        final TextView acquistoTotaleOrdine=fragmentView.findViewById(R.id.acquisto_cardview_finale_totale_ordine_txt_prezzo);
                        final TextView acquistoScontoRimanente=fragmentView.findViewById(R.id.acquisto_cardview_finale_totale_sconto_rimasto_txt_prezzo);
                        final TextView acquistoScontoRimanenteText=fragmentView.findViewById(R.id.acquisto_cardview_finale_totale_sconto_rimasto_txt_label);
                        acquistoTotaleOrdine.setText(conto.getTotaleCarrello()+" €");
                        //rendo invisibile la label dello sconto rimanente
                        acquistoScontoRimanente.setText(conto.getCurrentDiscount()+" €");
                        acquistoScontoRimanente.setVisibility(View.INVISIBLE);
                        acquistoScontoRimanenteText.setVisibility(View.INVISIBLE);
                        //controlla se l'opzione sconto è attivata o meno
                        switchSconto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {  // se si vuole attivare lo sconto
                                    //variabile per arrotondare lo sconto alla seconda cifra decimale
                                    double roundoff;
                                    //se lo sconto è minore del totale ordine
                                    if(conto.getTotaleCarrello()<=conto.getCurrentDiscount()){
                                        roundoff=0;
                                        acquistoScontoRimanente.setText((conto.getCurrentDiscount()-conto.getTotaleCarrello())+" €");
                                    }else{
                                        roundoff=Math.round((conto.getTotaleCarrello()-conto.getCurrentDiscount())* 100.0)/100.0;
                                        acquistoScontoRimanente.setText("0 €");
                                    }
                                    //imposto la label dello sconto visibile
                                    acquistoTotaleOrdine.setText(roundoff+" €");
                                    acquistoScontoRimanente.setVisibility(View.VISIBLE);
                                    acquistoScontoRimanenteText.setVisibility(View.VISIBLE);
                                }else{
                                    //se lo switch è su off, non applico nessuno sconto quindi visualizzo
                                    acquistoTotaleOrdine.setText(conto.getTotaleCarrello()+" €");
                                    acquistoScontoRimanente.setVisibility(View.INVISIBLE);
                                    acquistoScontoRimanenteText.setVisibility(View.INVISIBLE);
                                    acquistoScontoRimanente.setText(conto.getCurrentDiscount()+" €");
                                }

                            }
                        });
                        //azione se viene cliccato acquista
                        Button acquista=fragmentView.findViewById(R.id.acquisto_cardview_finale_bottone_acquisto_bottone);
                        acquista.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(conto.getTotaleCarrello()==0){
                                    //se il carrello è vuoto mostra messaggio e ritorna alla home
                                    String carrelloVuoto=getActivity().getString(R.string.carrello_vuoto);
                                    Toast.makeText(getContext(), carrelloVuoto, Toast.LENGTH_SHORT).show();
                                    ((HomeActivity)getActivity()).recreate();
                                }else{
                                    if(switchSconto.isChecked()){
                                        //se lo sconto deve essere sottratto proseguo
                                        pagamentoConSconto(conto, contoRef, db, punti);
                                    }else{
                                        //se lo sconto non deve essere sottratto
                                        pagamentoSenzaSconto(conto, contoRef, db, punti);
                                    }
                                }

                            }
                        });

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //returning our layout file
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public void pagamentoSenzaSconto(Conti conto, DocumentReference contoRef, FirebaseFirestore db, int coinGuadagnati){
        //se i soldi disponibili sono maggiori della spesa
        if(conto.getSaldoDisponibile()>=conto.getTotaleCarrello()){

            //eseguo operazioni
            double roundoff=Math.round((conto.getSaldoDisponibile()-conto.getTotaleCarrello())*100.0)/100.0;
            contoRef.update("saldoDisponibile", roundoff);
            contoRef.update("totaleCarrello", 0);
            contoRef.update("coinAmount", conto.getCoinAmount()+coinGuadagnati);

            //cancella e crea la cronologia dell'ordine
            cancellaCollezione(db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello"));

            //mando messaggio con la riuscita dell'acquisto e ricarico la pagina
            String text=getActivity().getString(R.string.acquisto_riuscito);
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

            ((HomeActivity)getActivity()).recreate();
        }else{
            //se i soldi non sono abbastanza
            String text=getActivity().getString(R.string.saldo_insufficente_nosconto);
            //mando messaggio con il fallimento dell'acquisto e ricarico la pagina
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            ((HomeActivity)getActivity()).recreate();
        }
    }

    public void pagamentoConSconto(Conti conto, DocumentReference contoRef, FirebaseFirestore db, int coinGuadagnati){

        if(conto.getSaldoDisponibile()>=(conto.getTotaleCarrello()-conto.getCurrentDiscount())){ //se posso permettermi la spesa

            if(conto.getSaldoDisponibile()==0 && conto.getCurrentDiscount()>=conto.getTotaleCarrello()){ //se il saldo è 0 ma lo sconto è maggiore della spesa
                //saldo invariato
                //carrello == 0
                //sconto= sconto-carrello
                double roundoff=Math.round((conto.getCurrentDiscount()-conto.getTotaleCarrello()) *100.0)/100.0;
                contoRef.update("totaleCarrello", 0);
                contoRef.update("currentDiscount", roundoff);
            }else if(conto.getSaldoDisponibile()>0 &&
                    (conto.getSaldoDisponibile()+conto.getCurrentDiscount())>=conto.getTotaleCarrello()){ //se conto esiste ma non è sufficiente a pagare, ma con lo sconto ce la faccio

                //sconto=sconto-totale
                //carrello=0
                //saldo=saldo-(totcarrello-sconto)

                if(conto.getCurrentDiscount()>=conto.getTotaleCarrello()){  //se sconto è maggiore della spesa totale
                    double rd=Math.round((conto.getCurrentDiscount()-conto.getTotaleCarrello()) *100.0)/100.0;
                    contoRef.update("currentDiscount", rd);
                    contoRef.update("totaleCarrello", 0);
                }else{  //se lo sconto non copre tutta la spesa
                    contoRef.update("currentDiscount", 0);
                    double roundoff=Math.round((conto.getSaldoDisponibile()-(conto.getTotaleCarrello()-conto.getCurrentDiscount())) *100.0)/100.0;
                    contoRef.update("saldoDisponibile", roundoff);
                    contoRef.update("totaleCarrello", 0);
                }
            }

            contoRef.update("coinAmount", conto.getCoinAmount()+coinGuadagnati);

            //cancella e crea la cronologia dell'ordine
            cancellaCollezione(db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello"));
            String text=getActivity().getString(R.string.acquisto_riuscito);
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }else{
            //se i soldi non sono abbastanza
            String text=getActivity().getString(R.string.saldo_insufficente_sisconto);
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

        }

        ((HomeActivity)getActivity()).recreate();
    }


    //funzione che serve a cancellare l'intero carrello anche nella recycler view
    public void cancellaCollezione(final CollectionReference carrello){
        GregorianCalendar gc = new GregorianCalendar();
        //imposto il nome dell'ordine corrispondente alla data odierna fino ai secondi
        final String dataOrdine=""+gc.get(Calendar.DAY_OF_MONTH)+"."+(gc.get(Calendar.MONTH)+1)+"."+gc.get(Calendar.YEAR)+"_"+gc.get(Calendar.HOUR)+":"+gc.get(Calendar.MINUTE)+"."+gc.get(Calendar.SECOND);
        carrello.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                //creo riferimento del nuovo ordine
                CollectionReference cronologia=FirebaseFirestore.getInstance().collection("cronologiaOrdini").document(auth.getUid()).collection("dataOrdine").document(dataOrdine).collection("prodottiAcquistati");
                if (task.isSuccessful()) {
                    for(DocumentSnapshot document : task.getResult()){
                        Prodotti prodottoAttuale = document.toObject(Prodotti.class);
                        //aggiungo il documento al documento della cronologia ordine
                        cronologia.document(prodottoAttuale.getId()).set(prodottoAttuale);
                        //elimino il documento
                        carrello.document(prodottoAttuale.getId()).delete();
                    }
                }
                else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        FirebaseFirestore db=FirebaseFirestore.getInstance();

        //operazione per scrivere sul db
        WriteBatch batch = db.batch();

        DocumentReference cronologiaText=FirebaseFirestore.getInstance().collection("cronologiaOrdini").document(auth.getUid()).collection("dataOrdine").document(dataOrdine);

        Map<String, String> city = new HashMap<>();
        city.put("data", dataOrdine);


        cronologiaText.set(city).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}
