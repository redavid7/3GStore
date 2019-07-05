package com.QUeM.TreGStore;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //--------------------INIZIO SEARCHBAR-------------------------------

        // expand searchView as EditText (default is a search icon)
        SearchView simpleSearchView = (SearchView) findViewById(R.id.searchview_info);
        CharSequence query = simpleSearchView.getQuery();





        //--------------------FINE SEARCHBAR-------------------------------



        //--------------------INIZIO FLOATING ACTION BUTTON-------------------------------

        //inizializza il pulsante floating action button che fa da menù
        FloatingActionButton fabMenu = findViewById(R.id.aggiungi_prodotto);

        //inizializza i pulsanti floating action button che fanno da opzioni
        final FloatingActionButton fabQR = findViewById(R.id.aggiungi_qr_code);
        final FloatingActionButton fabNFC = findViewById(R.id.aggiungi_nfc);
        final FloatingActionButton fabBC = findViewById(R.id.aggiungi_codice_barre);

        //azione del floating action button menu quando cliccato
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fabBC.isClickable() && fabNFC.isClickable() && fabQR.isClickable()){
                    closeFABMenu(fabQR, fabNFC, fabBC);
                }else{
                    showFABMenu(fabQR, fabNFC, fabBC);
                }

            }
        });

        //azione (provvisoria) del fab quando clicchi
        fabQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Metodo QR", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        //azione (provvisoria) del fab quando clicchi
        fabNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Metodo NFC", Toast.LENGTH_LONG);
                toast.show();
            }

        //azione (provvisoria) del fab quando clicchi
        });fabBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Metodo BarCode", Toast.LENGTH_LONG);
                toast.show();
            }
        });



        //--------------------FINE FLOATING ACTION BUTTON-------------------------------



        //--------------------INIZIO TOOLBAR E NAVIGATION DRAWER-------------------------------

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

        //--------------------FINE TOOLBAR E NAVIGATION DRAWER-------------------------------

    }

    //gestisce il comportamento di ogni sezione per ogni item del menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // raccoglie la scelta dell'utente
        int id = item.getItemId();

        //definisce azione per ogni voce del menu
        if (id == R.id.nav_home) {
            //gestisce la selezione Carrello (home)

        } else if (id == R.id.nav_promozioni) {
            //gestisce la selezione Promozioni

        } else if (id == R.id.nav_game) {
            //gestisce la selezione I Tuoi Marangicoin

        } else if (id == R.id.nav_marangicoin) {
            //gestisce la selezione MarangiMan

        } else if (id == R.id.nav_contatti) {
            //gestisce la selezione I tuoi Contatti

        }else if (id == R.id.nav_quit) {
            //gestisce la selezione Esci
        }

        //reimposta a lato il menu chiudendolo e mantanendo selezionata la sezione scelta
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    //metodo per mostrare la searchBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }
    */


    //metodo per mostrare i floating action point
    private void showFABMenu(FloatingActionButton fab1, FloatingActionButton fab2, FloatingActionButton fab3){

        //animazione per fare uscire i 3 buttons
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_180));

        //faccio diventare non cliccabili i floating action buttons
        fab1.setClickable(true);
        fab2.setClickable(true);
        fab3.setClickable(true);

    }


    //metodo per nascondere i floating action point
    private void closeFABMenu(FloatingActionButton fab1, FloatingActionButton fab2, FloatingActionButton fab3){

        //animazione per farli tornare dietro il pulsante +
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);

        //faccio diventare non cliccabili i floating action buttons
        fab1.setClickable(false);
        fab2.setClickable(false);
        fab3.setClickable(false);

    }

}
