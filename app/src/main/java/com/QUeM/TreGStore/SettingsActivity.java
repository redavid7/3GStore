package com.QUeM.TreGStore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.QUeM.TreGStore.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

import static android.support.constraint.Constraints.TAG;


public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();

        //inizializza Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //inizializza toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        //inizializzazione del menu laterale
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //azione quando clicco l'icona del menù laterale
        toggle = new ActionBarDrawerToggle(
                SettingsActivity.this, drawer , toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //funzione che gestisce la scelta effettuata nel menu
        navigationView.setNavigationItemSelectedListener(this);


        Log.d(TAG, "mmm prima dell' IF");

        if (findViewById(R.id.fragment_container)!= null){

            Log.d(TAG, "mmm dentro primo if ");

            if(savedInstanceState!= null){

                Log.d(TAG, "mmm dentro secondo if ");
                return;
            }
            Log.d(TAG, "mmm prima di getSupportFragmentManager ");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSettings()).commit();
            Log.d(TAG, "mmm dopo di getSupportFragmentManager ");
        }
    }

    @Override
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
}
