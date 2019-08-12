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
        final TextView marangiCoinAmount = (TextView) fragmentMarangiCoin.findViewById(R.id.marangiCoinTextView);
        final TextView marangiCoinAmount2 = (TextView) fragmentMarangiCoin.findViewById(R.id.marangiCoinTextView);
        final TextView discountAmount = (TextView) fragmentMarangiCoin.findViewById(R.id.marangiCoin_exchange);
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

                        marangiCoinAmount.setText("MarangiCoin: "+conto.getCoinAmount());


                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //returning our layout file
        return fragmentMarangiCoin;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

