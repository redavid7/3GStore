package com.QUeM.TreGStore;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        //imposta la freccia per tornare indietro
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                        //imposto il testo che corrisponde allo sconto disponibile
                        TextView acquistoScontoDisponibile=fragmentView.findViewById(R.id.acquisto_cardview_impo_sconto_disponibile_txt_prezzo);
                        acquistoScontoDisponibile.setText(conto.getCurrentDiscount()+" €");
                        //imposto il testo per il totale da pagare
                        final TextView acquistoTotaleOrdine=fragmentView.findViewById(R.id.acquisto_cardview_finale_totale_ordine_txt_prezzo);
                        final TextView acquistoScontoRimanente=fragmentView.findViewById(R.id.acquisto_cardview_finale_totale_sconto_rimasto_txt_prezzo);
                        final TextView acquistoScontoRimanenteText=fragmentView.findViewById(R.id.acquisto_cardview_finale_totale_sconto_rimasto_txt_label);
                        acquistoTotaleOrdine.setText(conto.getTotaleCarrello()+" €");
                        acquistoScontoRimanente.setText(conto.getCurrentDiscount()+" €");
                        acquistoScontoRimanente.setVisibility(View.INVISIBLE);
                        acquistoScontoRimanenteText.setVisibility(View.INVISIBLE);
                        //controlla se si vuole attivare lo sconto
                        switchSconto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    // se si vuole attivare lo sconto
                                    double roundoff;
                                    //se lo sconto è maggiore del totale ordine
                                    if(conto.getTotaleCarrello()<=conto.getCurrentDiscount()){
                                        roundoff=0;
                                        acquistoScontoRimanente.setText((conto.getCurrentDiscount()-conto.getTotaleCarrello())+" €");
                                    }else{
                                        roundoff=Math.round((conto.getTotaleCarrello()-conto.getCurrentDiscount())* 100.0)/100.0;
                                        acquistoScontoRimanente.setText("0 €");
                                    }
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
                                if(switchSconto.isChecked()){
                                    //se lo sconto deve essere sottratto
                                    pagamentoConSconto(conto, contoRef, db, punti);
                                }else{
                                    //se lo sconto non deve essere sottratto
                                    pagamentoSenzaSconto(conto, contoRef, db, punti);
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

    //todo: implementare cronologia ordini, testare, non cancella ordini

    public void pagamentoSenzaSconto(Conti conto, DocumentReference contoRef, FirebaseFirestore db, int coinGuadagnati){
        //se i soldi disponibili sono maggiori della spesa
        if(conto.getSaldoDisponibile()>=conto.getTotaleCarrello()){
            contoRef.update("saldoDisponibile", conto.getSaldoDisponibile()-conto.getTotaleCarrello());
            contoRef.update("totaleCarrello", 0);
            contoRef.update("coinAmount", conto.getCoinAmount()+coinGuadagnati);

            //non cancella carrello
            cancellaCollezione(db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello"));

            ((HomeActivity)getActivity()).ShowFragment(R.id.nav_home);
        }else{
            //se i soldi non sono abbastanza
            Toast.makeText(getContext(), "Non puoi permettertelo", Toast.LENGTH_SHORT).show();
            ((HomeActivity)getActivity()).ShowFragment(R.id.nav_home);
        }
    }

    public void pagamentoConSconto(Conti conto, DocumentReference contoRef, FirebaseFirestore db, int coinGuadagnati){
        //se i soldi disponibili sono maggiori della spesa
        if(conto.getSaldoDisponibile()>=(conto.getTotaleCarrello()-conto.getCurrentDiscount())){
            contoRef.update("saldoDisponibile", conto.getSaldoDisponibile()-conto.getTotaleCarrello());
            contoRef.update("totaleCarrello", 0);
            if(conto.getTotaleCarrello()<=conto.getCurrentDiscount()){
                //non ha tolto dalla spesa lo sconto
                contoRef.update("currentDiscount", conto.getCurrentDiscount()-conto.getTotaleCarrello());
            }else{
                contoRef.update("currentDiscount", 0);
            }
            contoRef.update("coinAmount", conto.getCoinAmount()+coinGuadagnati);

            //non cancella
            cancellaCollezione(db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello"));

        }else{
            //se i soldi non sono abbastanza
            Toast.makeText(getContext(), "Non puoi permettertelo, anche con lo sconto", Toast.LENGTH_SHORT).show();

        }
        ((HomeActivity)getActivity()).ShowFragment(R.id.nav_home);
    }

    public void cancellaCollezione(final CollectionReference carrello){
        carrello.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                GregorianCalendar gc = new GregorianCalendar();
                String dataOrdine=""+gc.get(Calendar.DAY_OF_MONTH)+"/"+gc.get(Calendar.MONTH)+"/"+gc.get(Calendar.YEAR)+"_"+gc.get(Calendar.HOUR)+":"+gc.get(Calendar.MINUTE)+gc.get(Calendar.SECOND);
                CollectionReference cronologia=FirebaseFirestore.getInstance().collection("cronologiaOrdini").document(auth.getUid()).collection(dataOrdine);
                if (task.isSuccessful()) {
                    for(DocumentSnapshot document : task.getResult()){
                        Prodotti prodottoAttuale = document.toObject(Prodotti.class);
                        cronologia.document(prodottoAttuale.getId()).set(prodottoAttuale);
                        carrello.document(prodottoAttuale.getId()).delete();
                    }
                }
                else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
