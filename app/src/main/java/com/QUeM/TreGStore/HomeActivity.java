package com.QUeM.TreGStore;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.QUeM.TreGStore.GiocoPacman.GooglePacman;

import java.util.ArrayList;

//debug message
//Toast.makeText(getContext(), "debug", Toast.LENGTH_LONG).show();


//GESTIRE LISTA E INTENT DELLA CAMERA


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //inizializzazioni variabili fab
    FloatingActionButton fabMenu;
    FloatingActionButton fabQR;
    FloatingActionButton fabNFC;

    //arraylist con codici dei prodotti del carrello
    public ArrayList<Integer> prodottiCarrello=new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //--------------------INIZIO GESTIONE SETUP ACTIVITY-------------------------------

        //inizializza Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //--------------------FINE GESTIONE SETUP ACTIVITY-------------------------------





        //--------------------INIZIO GESTIONE FLOATING ACTION BUTTON-------------------------------

        //inizializza il pulsante floating action button che fa da menù
        fabMenu = findViewById(R.id.aggiungi_prodotto);

        //inizializza i pulsanti floating action button che fanno da opzioni
        fabQR = findViewById(R.id.aggiungi_qr_code);
        fabNFC = findViewById(R.id.aggiungi_nfc);


        //azione del floating action button menu quando cliccato
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fabNFC.isClickable() && fabQR.isClickable()){
                    closeFABMenu(fabQR, fabNFC);
                }else{
                    showFABMenu(fabQR, fabNFC);
                }

            }
        });

        //azione (provvisoria) del fab quando clicchi
        fabQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fab che passa all'activity dello scanner tramite camera
                startActivity(new Intent(HomeActivity.this, ScannedBarcodeActivity.class));
            }
        });

        //azione (provvisoria) del fab quando clicchi
        fabNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Metodo NFC", Toast.LENGTH_LONG).show(); }

            //azione (provvisoria) del fab quando clicchi
        });

        //--------------------FINE GESTIONE FLOATING ACTION BUTTON-------------------------------





        //--------------------INIZIO GESTIONE TOOLBAR E NAVIGATION DRAWER-------------------------------

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
        super.onResume();  // Always call the superclass method first
        Intent cam=getIntent();
        String stringCode="";


        if(cam.hasExtra("code")){
            stringCode = cam.getStringExtra("code");
            Toast.makeText(getBaseContext(), stringCode, Toast.LENGTH_LONG).show();
            Integer codice = Integer.valueOf(stringCode);
            if(codice.intValue()!=0){
                prodottiCarrello.add(codice);
            }
        }


        //

        /*

        */
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
                /*
                Bundle data = new Bundle();//create bundle instance
                if(prodottiCarrello.isEmpty()){
                    data.putString("switch", "0");//put string to pass with a key value
                }else{
                    data.putString("switch", "1");//put string to pass with a key value
                }
                */
                fragment = new FragmentHome();
                //fragment.setArguments(data);
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
            case R.id.nav_contatti:
                fragment = new FragmentContatti();
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

    //metodo per mostrare i floating action point
    private void showFABMenu(FloatingActionButton fab1, FloatingActionButton fab2){

        //animazione per fare uscire i 3 buttons
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_120));

        //faccio diventare non cliccabili i floating action buttons
        fab1.setClickable(true);
        fab2.setClickable(true);

    }


    //metodo per nascondere i floating action point
    private void closeFABMenu(FloatingActionButton fab1, FloatingActionButton fab2){

        //animazione per farli tornare dietro il pulsante +
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);

        //faccio diventare non cliccabili i floating action buttons
        fab1.setClickable(false);
        fab2.setClickable(false);

    }

    @SuppressLint("RestrictedApi")
    public void showFABs(){
        fabMenu.setClickable(true);
        fabMenu.setVisibility(View.VISIBLE);
        fabNFC.setVisibility(View.VISIBLE);
        fabQR.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    public void hideFABs(){
        fabMenu.setClickable(false);
        fabMenu.setVisibility(View.INVISIBLE);
        fabNFC.setVisibility(View.INVISIBLE);
        fabQR.setVisibility(View.INVISIBLE);
        if(fabNFC.isClickable()){
            closeFABMenu(fabQR, fabNFC);
        }
    }


    //----------------------------------------------------------------------------
    //-------------------------FINE FUNZIONI FAB----------------------------------
    //----------------------------------------------------------------------------


    //----------------------------------------------------------------------------
    //-------------------------INIZIO FUNZIONI CARRELLO---------------------------
    //----------------------------------------------------------------------------


    public String getItemFromList(int index){
        String risultato="";
        int codicelista=prodottiCarrello.get(index);
        risultato=String.valueOf(codicelista);
        return risultato;
    }

    //----------------------------------------------------------------------------
    //-------------------------FINE FUNZIONI CARRELLO-----------------------------
    //----------------------------------------------------------------------------


}
//////////////////////////////////////////////////FINE CLASSE ACTIVITY//////////////////////////////////
