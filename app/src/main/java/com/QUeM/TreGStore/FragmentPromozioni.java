package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class FragmentPromozioni extends Fragment {

    TextView txt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_layout_promozioni, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt=getActivity().findViewById(R.id.textViewPromozioni);
        prendiProd();
    }

    public ArrayList<Prodotti> prendiProd (){
        ArrayList<Prodotti> toRet = new ArrayList<Prodotti>();
        //creo la connessione al documento che contiene i prodotti
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //prendo i prodotti
        final CollectionReference collrefprod = db.collection("prodotti");
        final DocumentReference docrefprod=collrefprod.getParent();
        //leggo i prodotti
        return toRet;
    }

}