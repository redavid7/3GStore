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



public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    //inizializzazioni variabili fab
    FloatingActionButton fabMenu;
    FloatingActionButton fabQR;
    FloatingActionButton fabNFC;
    FloatingActionButton fabBC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //inizializza Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //--------------------INIZIO FLOATING ACTION BUTTON-------------------------------

        //inizializza il pulsante floating action button che fa da menù
        fabMenu = findViewById(R.id.aggiungi_prodotto);

        //inizializza i pulsanti floating action button che fanno da opzioni
        fabQR = findViewById(R.id.aggiungi_qr_code);
        fabNFC = findViewById(R.id.aggiungi_nfc);
        fabBC = findViewById(R.id.aggiungi_codice_barre);

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
                Toast.makeText(getApplicationContext(), "Metodo QR", Toast.LENGTH_LONG).show();

            }
        });

        //azione (provvisoria) del fab quando clicchi
        fabNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Metodo NFC", Toast.LENGTH_LONG).show();
            }

        //azione (provvisoria) del fab quando clicchi
        });fabBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Metodo BarCode", Toast.LENGTH_LONG).show();
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

        //Add this line of code here to open the default selected menu on app start time.
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

        //--------------------FINE TOOLBAR E NAVIGATION DRAWER-------------------------------

    } //------------------------------FINE BLOCCO HOMEACTIVITY-----------------------------------

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

            //Intent switchActivityGame= new Intent(HomeActivity.this, MainActivity.class);
            //startActivity(switchActivityGame);
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
                fragment = new FragmentHome();
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

    @SuppressLint("RestrictedApi")
    public void showFABs(){
        fabMenu.setClickable(true);
        fabMenu.setVisibility(View.VISIBLE);
        fabNFC.setVisibility(View.VISIBLE);
        fabBC.setVisibility(View.VISIBLE);
        fabQR.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    public void hideFABs(){
        fabMenu.setClickable(false);
        fabMenu.setVisibility(View.INVISIBLE);
        fabNFC.setVisibility(View.INVISIBLE);
        fabBC.setVisibility(View.INVISIBLE);
        fabQR.setVisibility(View.INVISIBLE);
        if(fabNFC.isClickable()){
            closeFABMenu(fabQR, fabNFC, fabBC);
        }
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

    //----------------------------------------------------------------------------
    //-------------------------FINE FUNZIONI FAB----------------------------------
    //----------------------------------------------------------------------------


}
