package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        fragmentMarangiCoin = inflater.inflate(R.layout.fragment_layout_marangicoin, container, false);
        final TextView marangiCoinAmount = fragmentMarangiCoin.findViewById(R.id.marangiCoinTextView);
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

                        String textToSet = "MarangiCoin: "+conto.getCoinAmount();
                        marangiCoinAmount.setText(textToSet);


                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
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

    public void listenText(){
        final EditText marangiCoin_amount = fragmentMarangiCoin.findViewById(R.id.marangiCoin_amount);
        final TextView marangiCoin_exchange = fragmentMarangiCoin.findViewById(R.id.marangiCoin_exchange);

        marangiCoin_amount.addTextChangedListener(new TextWatcher() {
            boolean _ignore = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().length()==0)
                    return;
                if (_ignore)
                    return;

                _ignore = true;
                Integer value = Integer.parseInt(marangiCoin_amount.getText().toString());
                String euro="€ ";
                value = value / 20;
                String textToSet = euro.concat(value.toString());
                marangiCoin_exchange.setText(textToSet);
                _ignore = false;
            }
        });
    }
}

