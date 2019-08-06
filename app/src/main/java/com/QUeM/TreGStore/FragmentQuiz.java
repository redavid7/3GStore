package com.QUeM.TreGStore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.QUeM.TreGStore.DatabaseClass.Conti;
import com.QUeM.TreGStore.DatabaseClass.Domanda;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class FragmentQuiz extends Fragment {
    //Dichiarazione variabili
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
        //scrivo le domande su fie(provvisorio)
        Scrivi();
        //inizializzo oggetti a schermo
        init();
        //creo variabili data corrente e data ultimo quiz
        Calendar last = null;
        Calendar now = Calendar.getInstance();
        //leggo la data dell'ultimo quiz
        try {
             last= leggiData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //prendo gli interi per fare il confronto delle date in questo caso prendo i minuti per far vedere  che una volta terminato il quiz viene reso prima indisponibile poi rireso disponibile
        int l = 0;
        int n = 1;
        if(last!=null){
         l=last.get(Calendar.MINUTE);
         n=now.get(Calendar.MINUTE);}
        // se la data in cui si sta facendo il quiz e maggiore dell'ultimo quiz eseguito ti viene concesso di eseguire un nuovo quiz
        if(n>l){
            try {
                routine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            domanda.setText("Hai già giocato per oggi ritorna domani");
            A.setVisibility(View.INVISIBLE);
            B.setVisibility(View.INVISIBLE);
            C.setVisibility(View.INVISIBLE);
            D.setVisibility(View.INVISIBLE);
            Avanti.setVisibility(View.INVISIBLE);
        }
    }

    //Metodo controllo risposta corretta
    public void Controllo(String choiced) throws IOException {
            //Azioni da eseguire se la risposta è corretta
            if (domande.get(tmp).corretta.equals(choiced)) {
                domanda.setText("Corretto!");
                A.setVisibility(View.INVISIBLE);
                B.setVisibility(View.INVISIBLE);
                C.setVisibility(View.INVISIBLE);
                D.setVisibility(View.INVISIBLE);
                Avanti.setVisibility(View.VISIBLE);
                Avanti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        point++;
                        try {
                            routine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            //Azioni da eseguire se è sbagliata
            else {
                domanda.setText("Sbagliato! Il gioco è finito hai guadagnato: "+point+" MarangiCoin");
                updateCoin();
                scriviData();
                A.setVisibility(View.INVISIBLE);
                B.setVisibility(View.INVISIBLE);
                C.setVisibility(View.INVISIBLE);
                D.setVisibility(View.INVISIBLE);
                Avanti.setVisibility(View.VISIBLE);
                Avanti.setText("Fine");
                Avanti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentMarangicoin frpr = new FragmentMarangicoin();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(getId(),frpr);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
    }//Fine controllo e risposte

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
    public void routine() throws IOException {
        //prendo un valore randomico per prendere una domanda randomica
        tmp=(int)(Math.random()*domande.size());
        //Controllo se ho fatto 3 domande giuste in quel caso termino il gioco
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
            //Metodi on click per le varie risposte imposto il contenuto del bottone come scelta alla domanda su cui effetuo il controllo
            A.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = (String) A.getText();
                    try {
                        Controllo(choice);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = (String) B.getText();
                    try {
                        Controllo(choice);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            C.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = (String) C.getText();
                    try {
                        Controllo(choice);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            D.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choice = (String) D.getText();
                    try {
                        Controllo(choice);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        //Azioni da eseguire a fine gioco
        else{
            domanda.setText("Il gioco è finito, complimenti! Hai vinto : "+point+" MarangiCoin");
            updateCoin();
            scriviData();
            A.setVisibility(View.INVISIBLE);
            B.setVisibility(View.INVISIBLE);
            C.setVisibility(View.INVISIBLE);
            D.setVisibility(View.INVISIBLE);
            Avanti.setVisibility(View.VISIBLE);
            Avanti.setText("Fine");
            Avanti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentMarangicoin frpr = new FragmentMarangicoin();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(getId(),frpr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }//Fine routine di gioco

    // Metodo per aggiornare i coin vinti nel conto
    public void updateCoin(){
        //creo la connessione al documento dell'utente che contiene i conti
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //prendo l'utente attualmente connesso
        FirebaseAuth auth= FirebaseAuth.getInstance();
        //prendo il conto dell'utente
        final DocumentReference docrefconti = db.collection("conti").document(auth.getUid());
        //leggo i conti
        docrefconti.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //prendo il conti
                       Conti contoUtente=document.toObject(Conti.class);
                       int tmp = (contoUtente.getCoinAmount()+point);
                       contoUtente.setCoinAmount(tmp);
                       aggiungiConto(contoUtente,db);

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }//Fine metodo updateCoin

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
    //Metodo per aggiundere il conto
    public void aggiungiConto(Conti contoUtenteAggiornato, FirebaseFirestore db){
        //prendo l'utente
        FirebaseAuth auth= FirebaseAuth.getInstance();
        //operazione per scrivere sul db
        WriteBatch batch = db.batch();
        //creo riferimento da aggiornare
        DocumentReference conto = db.collection("conti").document(auth.getUid());
        //imposto il comando con il nuovo carrello
        batch.set(conto, contoUtenteAggiornato);
        //eseguo il comando di aggiornamento
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // ...
            }
        });
    } //Fine metodo per aggiundere il conto

    //Scrivi data
    public void scriviData() throws IOException {
        Calendar c = Calendar.getInstance();
        ObjectOutputStream oos = new ObjectOutputStream(getContext().openFileOutput("dat.txt",Context.MODE_PRIVATE));
        oos.writeObject(c);
    }

    //Leggi data
    public Calendar leggiData() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(getContext().openFileInput("dat.txt"));
        Calendar toRet = (Calendar) ois.readObject();
        return toRet;
    }
}
