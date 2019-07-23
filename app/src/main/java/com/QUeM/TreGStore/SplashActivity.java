package com.QUeM.TreGStore;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import android.os.Message;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    //copincollata da libro

    private static final String TAG_LOG = SplashActivity.class.getName();

    //costante per specificare gli intervalli di tempo per lo scambio di activity
        private static final long MIN_WAIT_INTERVAL = 2999L;
        private static final long MAX_WAIT_INTERVAL = 3000L;
        //caratterizzaziome del tipo di messaggio da dare all'handler
        private static final int GO_AHEAD_WHAT = 1;
        //segna l'inizio del conteggio dei secondi
        private long mStartTime;
        //flag per dichiarare se siamo pronti a switchare activity
        private boolean mIsDone;
        //dichiarazione di un Handler con il metodo handleMessage ridefinito ad hoc per questa occasione
        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GO_AHEAD_WHAT:
                        long elapsedTime = SystemClock.uptimeMillis() -
                                mStartTime;
                        if (elapsedTime >= MIN_WAIT_INTERVAL && !mIsDone) {
                            mIsDone = true;
                            goAhead();
                        }
                        break;
                }
            }
        };

        //metodo che specifica i comportamenti dell'actvity alla creazione
        @Override
        protected void onStart() {
            super.onStart();
            //procedura per la splash page (3 secondi e carica la successiva activity)
            mStartTime = SystemClock.uptimeMillis();
            final Message goAheadMessage =
                    mHandler.obtainMessage(GO_AHEAD_WHAT);
            mHandler.sendMessageAtTime(goAheadMessage, mStartTime +
                    MAX_WAIT_INTERVAL);
            Log.d(TAG_LOG, "Handler message sent!");
        }

        //metodo che attiva l'intent e permette di passare alla nuova activity
        private void goAhead() {
            //final Intent intent = new Intent(this, HomeActivity.class);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }