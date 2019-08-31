package com.QUeM.TreGStore;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener{

    //private final static String TAG_SETTINGS__ACTIVITY = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //imposto il layout
        setContentView(R.layout.activity_settings);

        //implementazione della toolbar
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        android.support.v7.widget.Toolbar bar = (android.support.v7.widget.Toolbar) LayoutInflater.from(this).inflate(R.layout.setting_toolbar, root, false);
        root.addView(bar, 0);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //caricamento del settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainSettingsFragment()).commit();
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}

    // fragment che gestisce le preferences
    @SuppressLint("ValidFragment")
    class MainSettingsFragment extends PreferenceFragment{

        private static final String TAG_SETTINGS__ACTIVITY = SettingsActivity.class.getSimpleName();
        @Override

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            //carica le preferences dal file preferences.xml
            addPreferencesFromResource(R.xml.preferences);

            Preference psw =  findPreference("psw");
            Preference email = findPreference("email");
            Preference cards = findPreference("cards");

            //imposto i listener su ogni preference, in modo tale da aprire un fragment on click
            psw.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
               public boolean onPreferenceClick(Preference psw) {
                           Log.d(TAG_SETTINGS__ACTIVITY, "---------------------------psw---------------------------------");
                           FragmentCambioPassword fragment = new FragmentCambioPassword();
                           FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                           fragmentTransaction.replace(R.id.fragment_container, fragment);
                           fragmentTransaction.addToBackStack(null);
                           fragmentTransaction.commit();
                           return true;
               }
            });

            email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference email) {
                    Log.d(TAG_SETTINGS__ACTIVITY, "-----------------------------email-------------------------------");
                    FragmentCambiaEmail fragment = new FragmentCambiaEmail();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                }
            });

            cards.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference cards) {
                    Log.d(TAG_SETTINGS__ACTIVITY, "-----------------------------cards-------------------------------");
                    FragmentCarte fragment = new FragmentCarte();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                }
            });

        }
    }

