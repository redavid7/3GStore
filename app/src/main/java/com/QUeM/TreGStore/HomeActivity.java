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

                //inizializzo parametri di layout per muovere i foating action point
                FrameLayout.LayoutParams lpQR = (FrameLayout.LayoutParams) fabQR.getLayoutParams();
                FrameLayout.LayoutParams lpNFC = (FrameLayout.LayoutParams) fabNFC.getLayoutParams();
                FrameLayout.LayoutParams lpBC = (FrameLayout.LayoutParams) fabBC.getLayoutParams();

                if(fabBC.isClickable() && fabNFC.isClickable() && fabQR.isClickable()){

                    //reimposto i margini per ogni bottone per tornare nella posizione iniziale
                    //QR CODE
                    lpQR.rightMargin -= 90;
                    lpQR.bottomMargin -= 54;

                    //BARCODE
                    lpBC.bottomMargin -= 100;

                    //NFC
                    lpNFC.leftMargin -= 90;
                    lpNFC.bottomMargin -= 54;


                    //inizializzo la variabile che contiene le informazioni dell'animazione
                    Animation hide_fab_ltc = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide_animation_left_to_center);
                    Animation hide_fab_utc = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide_animation_up_to_center);
                    Animation hide_fab_rtc = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide_animation_right_to_center);

                    //imposto la nuova posizione dei floating action buttons
                    fabQR.setLayoutParams(lpQR);
                    fabNFC.setLayoutParams(lpNFC);
                    fabBC.setLayoutParams(lpBC);

                    //faccio partire l'animazione per nascondere i floating action buttons
                    fabQR.startAnimation(hide_fab_ltc);
                    fabNFC.startAnimation(hide_fab_rtc);
                    fabBC.startAnimation(hide_fab_utc);

                    //faccio diventare non cliccabili i floating action buttons
                    fabQR.setClickable(false);
                    fabNFC.setClickable(false);
                    fabBC.setClickable(false);

                }else{

                    //imposto i nuovi margini per ogni bottone
                    //QR CODE
                    lpQR.rightMargin += 90;
                    lpQR.bottomMargin += 54;

                    //BARCODE
                    lpBC.bottomMargin += 100;

                    //NFC
                    lpNFC.leftMargin += 90;
                    lpNFC.bottomMargin += 54;

                    //inizializzo la variabile che contiene le informazioni dell'animazione
                    Animation show_fab_ctl = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show_animation_center_to_left);
                    Animation show_fab_ctu = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show_animation_center_to_up);
                    Animation show_fab_ctr = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show_animation_center_to_right);

                    //imposto la nuova posizione dei floating action buttons
                    fabQR.setLayoutParams(lpQR);
                    fabNFC.setLayoutParams(lpNFC);
                    fabBC.setLayoutParams(lpBC);

                    //faccio partire l'animazione per renderli visibili
                    fabQR.startAnimation(show_fab_ctl);
                    fabNFC.startAnimation(show_fab_ctr);
                    fabBC.startAnimation(show_fab_ctu);

                    //faccio diventare cliccabili le opzioni
                    fabQR.setClickable(true);
                    fabNFC.setClickable(true);
                    fabBC.setClickable(true);
                }

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


}
