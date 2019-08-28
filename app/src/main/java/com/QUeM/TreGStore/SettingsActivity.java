package com.QUeM.TreGStore;


import com.QUeM.TreGStore.HomeActivity;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v14.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.zip.Inflater;

import static android.support.constraint.Constraints.TAG;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener{

    //private final static String TAG_SETTINGS__ACTIVITY = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        //Gestione della switch per ricevere le notifiche riguardandi le offerte
        boolean promozioni = settings.getBoolean("offerte", false);
            //settings.registerOnSharedPreferenceChangeListener();
        if(promozioni){
            //qui va il codice per ricevere le notifiche sulle promozioni
            Toast.makeText(getApplicationContext(), "Switch offerte acceso", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Switch offerte spento", Toast.LENGTH_SHORT).show();
        }


/**        Preference myPref =  findPreference("myKey");
*        myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
 *           public boolean onPreferenceClick(Preference preference) {
  *              //open browser or intent here
   *             return true;
    *        }
     *   });
      */



        //setContentView(R.layout.activity_settings);
        //ToDo: capire perchè crasha con questo errore ( serve per includere navView e toolbar nella
        //ToDo: pagina delle impostazioni
       // java.lang.RuntimeException: Unable to start activity ComponentInfo{com.QUeM.TreGStore/com.QUeM.TreGStore.SettingsActivity}:
        //java.lang.RuntimeException: Your content must have a ListView whose id attribute is 'android.R.id.list'


        //caricamento del settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainSettingsFragment()).commit();
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}


    @SuppressLint("ValidFragment")
    class MainSettingsFragment extends PreferenceFragment{
        private static final String TAG_SETTINGS__ACTIVITY = SettingsActivity.class.getSimpleName();
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences);

            Preference psw =  findPreference("psw");
            Preference email = findPreference("email");
            Preference cards = findPreference("cards");

            psw.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
               public boolean onPreferenceClick(Preference psw) {
                           Log.d(TAG_SETTINGS__ACTIVITY, "---------------------------psw---------------------------------");
                           FragmentCambioPassword fragment = new FragmentCambioPassword();
                           FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                           fragmentTransaction.add(0, fragment);
                           fragmentTransaction.addToBackStack(null);
                           fragmentTransaction.commit();
                           return true;
               }
            });

            email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference email) {
                    Log.d(TAG_SETTINGS__ACTIVITY, "-----------------------------email-------------------------------");
                    return false;
                }
            });

            cards.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference cards) {
                    Log.d(TAG_SETTINGS__ACTIVITY, "-----------------------------cards-------------------------------");
                    return false;
                }
            });

    }


    }

