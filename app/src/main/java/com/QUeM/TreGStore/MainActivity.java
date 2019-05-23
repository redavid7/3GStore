package com.QUeM.TreGStore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView button;
        button = findViewById(R.id.Cazzeggio);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final TextView textView = (TextView) findViewById(R.id.MaranGGGstore);
                final TextView textView2 = (TextView) findViewById(R.id.provaGuardami);
                    textView.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.VISIBLE);
            }
        });
    }
}
