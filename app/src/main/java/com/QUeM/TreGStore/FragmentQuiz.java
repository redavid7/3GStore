package com.QUeM.TreGStore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.QUeM.TreGStore.DatabaseClass.Domanda;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FragmentQuiz extends Fragment {
    // Dichiarazione variabili
    private ArrayList<Domanda> domande = new ArrayList<>();
    private TextView domanda ;
    private Button A;
    private Button B;
    private Button C;
    private Button D;
    private Button Avanti;
    private String choice;
    private int point=0;
    private int tmp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentHomeView;
        fragmentHomeView=inflater.inflate(R.layout.fragment_fragment_quiz, container, false);
        return fragmentHomeView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Scrivi();
        init();
        routine();
    }
    //Metodo controllo risposta corretta
    public void Controllo(String choiced){
            //Azioni da eseguire se la risposta è corretta
            if (domande.get(tmp).corretta.equals(choiced)) {
                domanda.setText("Corretto");
                A.setVisibility(View.INVISIBLE);
                B.setVisibility(View.INVISIBLE);
                C.setVisibility(View.INVISIBLE);
                D.setVisibility(View.INVISIBLE);
                Avanti.setVisibility(View.VISIBLE);
                Avanti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        point++;
                        routine();
                    }
                });
            }
            //Azioni da eseguire se è sbagliata
            else {
                domanda.setText("Sbagliato, il gioco è finito il tuo punteggio è: "+point);
                A.setVisibility(View.INVISIBLE);
                B.setVisibility(View.INVISIBLE);
                C.setVisibility(View.INVISIBLE);
                D.setVisibility(View.INVISIBLE);
                Avanti.setVisibility(View.VISIBLE);
                Avanti.setText("Fine");
                Avanti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
    }//Fine controllo risposto

    //Inizializzazione variabili
    public void init(){
        domanda= getActivity().findViewById(R.id.textDomanda);
        A=getActivity().findViewById(R.id.rispostaA);
        B=getActivity().findViewById(R.id.rispostaB);
        C=getActivity().findViewById(R.id.rispostaC);
        D=getActivity().findViewById(R.id.rispostaD);
        Avanti=getActivity().findViewById(R.id.Avanti);
        try{
            ObjectInputStream ois = new ObjectInputStream(getContext().openFileInput("quiz.txt"));
            domande.add((Domanda)ois.readObject());
            int c=0;
            while (domande.get(c)!= null){
                domande.add((Domanda)ois.readObject());
                c++;
            }
            domande.remove(c);
            ois.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }//Fine inizializzazione variabili
    //Routine del quiz
    public void routine(){
        tmp=(int)(Math.random()*domande.size());
        //Controllo se ho fatto 3 domande in quel caso termino il gioco
        if(point<3) {
            Avanti.setVisibility(View.INVISIBLE);
            A.setVisibility(View.VISIBLE);
            B.setVisibility(View.VISIBLE);
            C.setVisibility(View.VISIBLE);
            D.setVisibility(View.VISIBLE);
            //variabili dipendenti dal numero di domande
            domanda.setText(domande.get(tmp).testo);
            A.setText(domande.get(tmp).risposta1);
            B.setText(domande.get(tmp).risposta2);
            C.setText(domande.get(tmp).risposta3);
            D.setText(domande.get(tmp).risposta4);
            //Metodi on click per le varie risposte
            A.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = (String) A.getText();
                    Controllo(choice);
                }
            });
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = (String) B.getText();
                    Controllo(choice);
                }
            });
            C.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = (String) C.getText();
                    Controllo(choice);
                }
            });
            D.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = (String) D.getText();
                    Controllo(choice);
                }
            });
        }
        //Azioni da eseguire a fine gioco
        else{
            domanda.setText("Il gioco è finito complimenti il tuo punteggio è: "+point);
            A.setVisibility(View.INVISIBLE);
            B.setVisibility(View.INVISIBLE);
            C.setVisibility(View.INVISIBLE);
            D.setVisibility(View.INVISIBLE);
            Avanti.setVisibility(View.VISIBLE);
            Avanti.setText("Fine");
            Avanti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }//Fine routine di gioco
    public  void Scrivi(){
        try{
        ObjectOutputStream oos = new ObjectOutputStream(getContext().openFileOutput("quiz.txt",Context.MODE_PRIVATE));
            oos.writeObject(new Domanda("Chi è il pelato del trio Aldo, Giovanni e Giacomo?","Aldo","Giacomo","Giovanni","Debora","Aldo"));
            oos.writeObject(new Domanda("In che anno è nato il regista Steven Spielberg?","1960","1935","1946","1956","1946"));
            oos.writeObject(new Domanda("Quanti oscar ha vinto in totale la trilogia del signore degli anelli?","10","17","20","42","17"));
            oos.writeObject(new Domanda("Quale particella possiede carica posiva nell'atomo?","Elettrone","Protone","Neutrone","Positrone","Protone"));
            oos.writeObject(new Domanda("Quale elemento è indispensabile nella fotosintesi clorofiliana?","Ossigeno","Anidride carbonica","Sale","Vitamina k","Anidride carbonica"));
            oos.writeObject(new Domanda("Qual'è l'unità di misura dell'energia?","Caloria","Watt","Ampere","Joule","Joule"));
            oos.writeObject(new Domanda("In che anno è caduto l'impero romano d'occidente","476 d.c.","33 d.c.","1453 d.c.","1453 a.c.","476 d.c."));
            oos.writeObject(new Domanda("Chi è stato il primo re d'Italia","Enzo Paolo Turchi II","Vittorio Emanuele II di Savoia","Federico Barbarossa","Vittorio Emanuele","Vittorio Emanuele II di Savoia"));
            oos.writeObject(new Domanda("Quale nazione è tra le fondatrici dell'ONU?","China","Giappone","Russia","India","India"));
            oos.writeObject(new Domanda("Quale di questi attori è stato anche un famoso nuotatore italiano?","Prierfrancesco Favino","Silvester Stallone","Bud Spencer","Sossio Aruta","Bud Spencer"));
            oos.writeObject(new Domanda("Quale squadra di serie A detiene più campionati vinti?","Milan","Juventus","Roma","Spal","Juventus"));
            oos.writeObject(new Domanda("Alla nazione italiana di quale sport ci si riferisce con il nome Settebello?","Pallavolo","Calcio","Tennis","Pallanuoto","Pallanuoto"));
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
