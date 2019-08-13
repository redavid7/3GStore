package com.QUeM.TreGStore;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.support.constraint.Constraints.TAG;


//debug message
//Toast.makeText(getContext(), "debug", Toast.LENGTH_LONG).show();
//Log.d(TAG, "PRODOTTO "+ stringCode);

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ActionBarDrawerToggle toggle;

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
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //funzione che gestisce la scelta effettuata nel menu
        navigationView.setNavigationItemSelectedListener(this);

        //angelo divertiti
        View innerview =  navigationView.getHeaderView(0);
        TextView user_view= (TextView)innerview.findViewById(R.id.nomeCognome); //any you need
        user_view.setText(user.getEmail());

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

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    //--------------------------------------------------------------------------------------------------
    //------------------------------FINE BLOCCO GESTIONE VITA HOMEACTIVITY------------------------------
    //--------------------------------------------------------------------------------------------------


    //---------------------------------------------------------------------------
    //-------------------------INIZIO FUNZIONI MENU------------------------------
    //---------------------------------------------------------------------------

    //gestisce il comportamento di ogni sezione per ogni item del menu
    @Override
    @SuppressLint("RestrictedApi")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.syncState();

        //inizializzo la variabile che conterrà il fragment da mostrare
        Fragment fragment = null;

        //in base alla selezione del menu assegno il fragment da mostrare o l'activity da startare
        switch(item.getItemId()){
            case R.id.nav_quit:
                //Se viene cliccato il tasto logout, chiamo il metodo signOut()
                auth.signOut();
                break;
            case R.id.nav_settings:
                //Se viene cliccato il tasto delle impostazioni, starto SettingsActivity
                Log.d(TAG, "mmm prima di startActivity ");
               onPause();
                startActivity(new Intent(this, SettingsActivity.class));
                Log.d(TAG, "mmm dopo di startActivity");
                break;
            case R.id.nav_home:
                fragment=new FragmentCarrello();
                break;
            case R.id.nav_promozioni:
                fragment = new FragmentPromozioni();
                break;
            case R.id.nav_marangicoin:
                fragment = new FragmentMarangicoin();
                break;
            case R.id.nav_profilo:
                fragment = new FragmentProfilo();
                break;
            case R.id.nav_game:
                fragment = new FragmentQuiz();
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

        return true;
    }


    @SuppressLint("RestrictedApi")
    protected void ShowFragment(int itemId) {

        //inizializzo la variabile che conterrà il fragment da mostrare
        Fragment fragment = null;

        //in base alla selezione del menu assegno il fragment da mostrare
        switch (itemId) {
            case R.id.nav_home:
                fragment=new FragmentCarrello();
                break;
            case R.id.nav_promozioni:
                fragment = new FragmentPromozioni();
                break;
            case R.id.nav_marangicoin:
                fragment = new FragmentMarangicoin();
                break;
            case R.id.nav_profilo:
                fragment = new FragmentProfilo();
                break;
            case R.id.nav_game:
                fragment = new FragmentQuiz();
                break;
            case R.id.nav_hidden_acquista:
                fragment = new FragmentAcquisto();
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





}
//////////////////////////////////////////////////FINE CLASSE ACTIVITY//////////////////////////////////
