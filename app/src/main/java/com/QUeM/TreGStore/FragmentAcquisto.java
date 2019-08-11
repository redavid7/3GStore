package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.QUeM.TreGStore.DatabaseClass.Conti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        //prendo le informazioni del conto e le inserisco nel fragment
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference contoRef=db.collection("conti").document(auth.getUid());
        //prendo il documento del conto corrispondente all'utente connesso
        contoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //se la connessione è riuscita, vedo se il documento esiste o meno
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //conto trovato ed esiste
                        Conti conto=document.toObject(Conti.class);
                        //imposto il testo che corrisponde al totale del carrello
                        TextView acquistoTotPrezzo= fragmentView.findViewById(R.id.acquisto_cardview_info_tot_articoli_txt_prezzo);
                        acquistoTotPrezzo.setText(String.valueOf(conto.getTotaleCarrello())+" €");
                        TextView acquistoPuntiGuadagnati=fragmentView.findViewById(R.id.acquisto_cardview_info_punti_guadagnati_txt_prezzo);
                        int punti=((int)conto.getTotaleCarrello()/5);
                        acquistoPuntiGuadagnati.setText(String.valueOf(punti));
                        TextView acquistoScontoDisponibile=fragmentView.findViewById(R.id.acquisto_cardview_impo_sconto_disponibile_txt_prezzo);
                        acquistoScontoDisponibile.setText(String.valueOf(conto.getCurrentDiscount()));
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



}
