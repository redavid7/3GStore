package com.QUeM.TreGStore;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentCarte extends Fragment {

    private View fragmentCarte;
    private Button back, add;
    private TextView newCard;
    private EditText addCard;
    private String cardNum;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //returning our layout file
        fragmentCarte = inflater.inflate(R.layout.fragment_carte, container, false);
        return fragmentCarte;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back = fragmentCarte.findViewById(R.id.btnBack);
        add = fragmentCarte.findViewById(R.id.btnAdd);
        addCard = fragmentCarte.findViewById(R.id.addCard);
        newCard = fragmentCarte.findViewById(R.id.card_3);
        final TextInputLayout c3Layout = fragmentCarte.findViewById(R.id.til_card3);
        final TextInputLayout addLayout = fragmentCarte.findViewById(R.id.add_card);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FragmentImpostazioni();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNum = addCard.getText().toString();
                addLayout.setVisibility(View.GONE);
                newCard.setText(cardNum.toString());
                c3Layout.setVisibility(View.VISIBLE);
            }
        });

    }
}
