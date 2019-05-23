package com.QUeM.TreGStore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    //dichiarazione variabili
    private ImageButton bottoneMario;
    private TextView testo1;
    private TextView testo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //inizializzazione activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inizializzazione delle variabili
        bottoneMario= (ImageButton) findViewById(R.id.malio);
        testo1=(TextView) findViewById(R.id.testo_prima_bottone);
        testo2=(TextView) findViewById(R.id.testo_dopo_bottone);

        bottoneMario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testo1.setText("");
                testo2.setText(getString(R.string.stringa_benvenuto2));
            }
        });

    }



}
