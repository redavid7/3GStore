package com.QUeM.TreGStore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    //dichiarazione variabili
    private ImageButton bottoneMario;
    private TextView testo1;
    private TextView testo2;
    private Button switchaActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //inizializzazione activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inizializzazione della toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("TOOLBAR");

        //inizializzazione delle variabili
        bottoneMario= (ImageButton) findViewById(R.id.malio);
        testo1=(TextView) findViewById(R.id.testo_prima_bottone);
        testo2=(TextView) findViewById(R.id.testo_dopo_bottone);
        switchaActivity= (Button) findViewById(R.id.passaActivity);

        bottoneMario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testo1.setText("");
                testo2.setText(getString(R.string.stringa_benvenuto2));
            }
        });

        switchaActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login=new Intent(MainActivity.this, loginActivity.class);
                startActivity(login);
            }
        });

    }



}
