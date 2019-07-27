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


import com.QUeM.TreGStore.DatabaseClass.Carrello;
import com.QUeM.TreGStore.GiocoPacman.GooglePacman;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.support.constraint.Constraints.TAG;


//debug message
//Toast.makeText(getContext(), "debug", Toast.LENGTH_LONG).show();
//Log.d(TAG, "PRODOTTO "+ stringCode);




public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //inizializzazioni variabili fab
    FloatingActionButton fabMenu;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    //inizializzo la variabile che conterrà il fragment da mostrare
    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //--------------------------INIZIO GESTIONE UTENTE------------------------------------
        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    //lo stato di auth è cambiato oppure user è nullo
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        //-----------------------------FINE GESTIONE UTENTE------------------------------------


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
        //TextView profilo = drawer.findViewById(R.id.nomeCognome);
        //profilo.setText(user.getDisplayName());


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
        ShowFragment(R.id.nav_home);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        ShowFragment(R.id.nav_home);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
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
        } else if (item.getItemId() == R.id.nav_quit) {
            auth.signOut();
        } else {
            //chiamo il metodo che gestisce i fragment per la scelta degli elementi del menu
            ShowFragment(item.getItemId());
        }
        return true;
    }


    @SuppressLint("RestrictedApi")
    private void ShowFragment(int itemId) {



        //in base alla selezione del menu assegno il fragment da mostrare
        switch (itemId) {
            case R.id.nav_home:

                //sceglie il fragment da mostrare in base al carrello
                /*
                if(carrello.isEmpty()){
                    //se il carrello è vuoto
                    fragment = new FragmentHomeVuoto();
                }else{
                    //se il carrello è pieno
                    fragment = new FragmentHomePieno();
                }
                */
                fragment=new FragmentHomeVuoto();
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


}
//////////////////////////////////////////////////FINE CLASSE ACTIVITY//////////////////////////////////
