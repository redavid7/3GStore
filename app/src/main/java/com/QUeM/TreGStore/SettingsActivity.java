package com.QUeM.TreGStore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import static android.support.constraint.Constraints.TAG;


public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        Log.d(TAG, "mmm prima dell' IF");

        if (findViewById(R.id.fragment_container)!= null){

            Log.d(TAG, "mmm dentro primo if ");

            if(savedInstanceState!= null){

                Log.d(TAG, "mmm dentro secondo if ");
                return;
            }
            Log.d(TAG, "mmm prima di getSupportFragmentManager ");
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new FragmentSettings()).commit();
            Log.d(TAG, "mmm dopo di getSupportFragmentManager ");
        }
    }
}
