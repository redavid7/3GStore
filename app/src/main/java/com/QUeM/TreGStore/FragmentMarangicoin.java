package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.content.Context;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Button;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.QUeM.TreGStore.DatabaseClass.Conti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.support.constraint.Constraints.TAG;

public class FragmentMarangicoin extends Fragment {

    private View fragmentMarangiCoin;
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private Context ctx;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        fragmentMarangiCoin = inflater.inflate(R.layout.fragment_layout_marangicoin, container, false);
        final TextView marangiCoinAmount = fragmentMarangiCoin.findViewById(R.id.marangiCoinTextView);
        final TextView current_discount = fragmentMarangiCoin.findViewById(R.id.current_discount);
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

                        final Conti conto=document.toObject(Conti.class);

                        String textToSet = "" + conto.getCoinAmount();
                        marangiCoinAmount.setText(textToSet);
                        String textToSet2 = "" + conto.getCurrentDiscount();
                        current_discount.setText(textToSet2);


                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        final Button button = fragmentMarangiCoin.findViewById(R.id.exchange_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exchangeMarangiCoin();
            }
        });

        listenText();

        //returning our layout file
        return fragmentMarangiCoin;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    //listener per la modifica dinamica del testo
    public void listenText(){
        final EditText marangiCoin_amount = fragmentMarangiCoin.findViewById(R.id.marangiCoin_amount);
        final TextView marangiCoin_exchange = fragmentMarangiCoin.findViewById(R.id.marangiCoin_exchange);

        marangiCoin_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //controllo se la stringa è vuota. crasha se non lo faccio.
                if(s.toString().trim().length()==0)
                    return;

                //modifico il valore in setText con quello dello sconto che si ottiene con l'attuale ammontare di marangicoin
                Integer value = Integer.parseInt(marangiCoin_amount.getText().toString());
                String euro="€ ";
                value = value / 20;
                String textToSet = euro.concat(value.toString());
                marangiCoin_exchange.setText(textToSet);
            }
        });
    }

    public void exchangeMarangiCoin(){
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
                        int mc_available, mc_toExchange; double current_discount, discount_toAdd;
                        final EditText mc_exchange = fragmentMarangiCoin.findViewById(R.id.marangiCoin_amount);
                        final TextView current_mc = fragmentMarangiCoin.findViewById(R.id.marangiCoinTextView);
                        final TextView current_discount_view = fragmentMarangiCoin.findViewById(R.id.current_discount);
                        String mc_exchange_text, current_mc_text, current_discount_text;

                        final Conti conto=document.toObject(Conti.class);
                        //ottengo i dati su cui agire
                        mc_available = conto.getCoinAmount();
                        current_discount = conto.getCurrentDiscount();
                        mc_toExchange = Integer.parseInt(mc_exchange.getText().toString());
                        discount_toAdd = mc_toExchange/20;

                        if(mc_available < mc_toExchange){
                            return;
                        }
                        current_discount = current_discount + discount_toAdd;
                        mc_available -= discount_toAdd*20;
                        contoRef.update("coinAmount", mc_available);
                        contoRef.update("currentDiscount", current_discount);
                        mc_exchange_text = "0";
                        current_mc_text = "" + mc_available;
                        current_discount_text = "" + current_discount;
                        mc_exchange.setText(mc_exchange_text);
                        current_mc.setText(current_mc_text);
                        current_discount_view.setText(current_discount_text);

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}

