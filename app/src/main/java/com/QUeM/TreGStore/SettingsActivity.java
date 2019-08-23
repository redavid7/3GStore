package com.QUeM.TreGStore;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.Preference;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.zip.Inflater;

public class SettingsActivity extends PreferenceActivity{


    private View settingsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        //Gestione della switch per ricevere le notifiche riguardandi le offerte
        boolean promozioni = settings.getBoolean("offerte", false);
        if(promozioni){
            //qui va il codice per ricevere le notifiche sulle promozioni
            Toast.makeText(getApplicationContext(), "Switch offerte acceso", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Switch offerte spento", Toast.LENGTH_LONG).show();
        }

        //setContentView(R.layout.activity_settings);
        //ToDo: capire perchè crasha con questo errore ( serve per includere navView e toolbar nella
        //ToDo: pagina delle impostazioni
       // java.lang.RuntimeException: Unable to start activity ComponentInfo{com.QUeM.TreGStore/com.QUeM.TreGStore.SettingsActivity}:
        //java.lang.RuntimeException: Your content must have a ListView whose id attribute is 'android.R.id.list'


        //caricamento del settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainSettingsFragment()).commit();
    }


    public static class MainSettingsFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences);
        }

    }

}

