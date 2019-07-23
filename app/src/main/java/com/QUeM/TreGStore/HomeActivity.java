package com.QUeM.TreGStore;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.QUeM.TreGStore.GiocoPacman.GooglePacman;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

//debug message
//Toast.makeText(getContext(), "debug", Toast.LENGTH_LONG).show();
//Log.d(TAG, "GIOGIO "+ stringCode);


//GESTIRE LISTA E INTENT DELLA CAMERA


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //inizializzazioni variabili fab
    FloatingActionButton fabMenu;

    //arraylist con codici dei prodotti del carrello
    public ArrayList<Prodotti> carrello=new ArrayList<Prodotti>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //startActivity(new Intent(this, LoginActivity.class));

        //--------------------INIZIO GESTIONE SETUP ACTIVITY-------------------------------

        //inizializza Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //--------------------FINE GESTIONE SETUP ACTIVITY-------------------------------





        //--------------------INIZIO GESTIONE FLOATING ACTION BUTTON-------------------------------

        //inizializza il pulsante floating action button che fa da menù
        fabMenu = findViewById(R.id.aggiungi_prodotto);

        //azione del floating action button menu quando cliccato
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ScannedBarcodeActivity.class));
            }
        });

        //--------------------FINE GESTIONE FLOATING ACTION BUTTON-------------------------------





        //--------------------INIZIO GESTIONE TOOLBAR E NAVIGATION DRAWER------------------------

        //inizializza toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //metodo per nascondere il titolo dell'app
        getSupportActionBar().setTitle(null);

        //inizializzazione del menu laterale
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //azione quando clicco l'icona del menù laterale
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //funzione che gestisce la scelta effettuata nel menu
        navigationView.setNavigationItemSelectedListener(this);



        //--------------------FINE GESTIONE TOOLBAR E NAVIGATION DRAWER-------------------------------




        //--------------------INIZIO GESTIONE FRAGMENT-------------------------------

        //carica il fragment di default
        ShowFragment(R.id.nav_home);

        //creo possibilità di usare l'icona profilo come pulsante per accedere al fragment profilo
        View headerView= navigationView.getHeaderView(0);
        ImageView imageViewProfile= (ImageView) headerView.findViewById(R.id.nav_profilo);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowFragment(R.id.nav_profilo);
            }
        });

        //--------------------FINE GESTIONE FRAGMENT---------------------------------

    }

    //--------------------------------------------------------------------------------------------------
    //------------------------------FINE BLOCCO ONCREATE HOMEACTIVITY-----------------------------------
    //--------------------------------------------------------------------------------------------------





    //--------------------------------------------------------------------------------------------------
    //------------------------------INIZIO BLOCCO GESTIONE VITA HOMEACTIVITY----------------------------
    //--------------------------------------------------------------------------------------------------

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }

    @Override
    public void onResume() {
        // Always call the superclass method first
        super.onResume();

        //prendo l'intent e prendo il codice del prodotto scannerizzato se lo trovo
        Intent cam=getIntent();
        String stringCode="";
        if(cam.hasExtra("code")){
            //se c'è un extra lo aggiungo alla itemlist del carrello
            stringCode = cam.getStringExtra("code");
            if(!stringCode.equals("0")){
                aggiungiProdottoCarrello(stringCode);
            }
        }
        ShowFragment(R.id.nav_home);
    }


    //--------------------------------------------------------------------------------------------------
    //------------------------------FINE BLOCCO GESTIONE VITA HOMEACTIVITY------------------------------
    //--------------------------------------------------------------------------------------------------


    //---------------------------------------------------------------------------
    //-------------------------INIZIO FUNZIONI MENU------------------------------
    //---------------------------------------------------------------------------

    //gestisce il comportamento di ogni sezione per ogni item del menu
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.nav_game){
            //inserire l'activity del gioco

            //https://stackoverflow.com/questions/7074097/how-to-pass-integer-from-one-activity-to-another
            //guida al passaggio di int tramite intent tra activity

            Intent switchActivityGame= new Intent(HomeActivity.this, GooglePacman.class);
            startActivity(switchActivityGame);
        }else{
            //chiamo il metodo che gestisce i fragment per la scelta degli elementi del menu
            ShowFragment(item.getItemId());
        }
        return true;
    }


    @SuppressLint("RestrictedApi")
    private void ShowFragment(int itemId) {

        //inizializzo la variabile che conterrà il fragment da mostrare
        Fragment fragment = null;

        //in base alla selezione del menu assegno il fragment da mostrare
        switch (itemId) {
            case R.id.nav_home:
                //sceglie il fragment da mostrare in base al carrello
                if(checkCarrelloVuoto()){
                    //se il carrello è vuoto
                    fragment = new FragmentHomeVuoto();
                }else{
                    //se il carrello è pieno
                    fragment = new FragmentHomePieno();
                }
                if(!fabMenu.isClickable()){
                    showFABs();
                }
                break;
            case R.id.nav_promozioni:
                fragment = new FragmentPromozioni();
                if(fabMenu.isClickable()) {
                    hideFABs();
                }
                break;
            case R.id.nav_marangicoin:
                fragment = new FragmentMarangicoin();
                if(fabMenu.isClickable()) {
                    hideFABs();
                }
                break;
            case R.id.nav_profilo:
                fragment = new FragmentProfilo();
                if(fabMenu.isClickable()) {
                    hideFABs();
                }
                break;

        }

        //imposta il nuovo fragment
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        }

        //reimposto il menù laterale
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    //---------------------------------------------------------------------------
    //-------------------------FINE FUNZIONI MENU--------------------------------
    //---------------------------------------------------------------------------

    /*
    //metodo per mostrare la searchBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    */

    //----------------------------------------------------------------------------
    //-------------------------INIZIO FUNZIONI FAB--------------------------------
    //----------------------------------------------------------------------------


    //metodo che rende visibile il bottone fotocamera
    @SuppressLint("RestrictedApi")
    public void showFABs(){
        fabMenu.setClickable(true);
        fabMenu.setVisibility(View.VISIBLE);
    }

    //metodo che rende invisibile il bottone fotocamera
    @SuppressLint("RestrictedApi")
    public void hideFABs(){
        fabMenu.setClickable(false);
        fabMenu.setVisibility(View.INVISIBLE);
    }


    //----------------------------------------------------------------------------
    //-------------------------FINE FUNZIONI FAB----------------------------------
    //----------------------------------------------------------------------------


    //----------------------------------------------------------------------------
    //-------------------------INIZIO FUNZIONI CARRELLO---------------------------
    //----------------------------------------------------------------------------

    //metodo che permette di prendere il codice di un prodotto dalla lista
    public Prodotti prendiProdottoDallaLista(int index){
        Prodotti risultato;
        if(carrello.isEmpty()){
            //se la lista è vuota, restituisce la stringa "Lista vuota"
            risultato=null;
        }else{
            //se la lista è piena, restituisce il codice identificativo del prodotto
            risultato = carrello.get(index);
        }
        return risultato;
    }


    //metodo che risponde alla domanda "Il carrello è vuoto?"
    public boolean checkCarrelloVuoto(){
        boolean risposta=true;
        //se il carrello non è vuoto restituisce Falso
        if(!carrello.isEmpty()){
            risposta=false;
        }
        return risposta;
    }


    public void aggiungiProdottoCarrello(final String codiceProdotto){

        //if che controlla se il codice sta già nel carrello ---------------------->

        //DA SPOSTARE IN HOME ACTIVITY
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("prodotti").document(codiceProdotto);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Prodotti prod=document.toObject(Prodotti.class);
                        prod.id=codiceProdotto;
                        prod.totalePezziCarrello=1;
                        Log.d(TAG, "PRODOTTO: " + prod.toString());
                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }





    //TODO <-----------------
    public boolean checkProdottoNelCarrello(){
        boolean risposta=false;

        return risposta;
    }

    //----------------------------------------------------------------------------
    //-------------------------FINE FUNZIONI CARRELLO-----------------------------
    //----------------------------------------------------------------------------


}
//////////////////////////////////////////////////FINE CLASSE ACTIVITY//////////////////////////////////
