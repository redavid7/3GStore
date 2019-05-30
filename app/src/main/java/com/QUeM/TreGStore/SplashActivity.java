package com.QUeM.TreGStore;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
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
        private static final long MIN_WAIT_INTERVAL = 2999L;
        private static final long MAX_WAIT_INTERVAL = 3000L;
        private static final int GO_AHEAD_WHAT = 1;
        private long mStartTime;
        private boolean mIsDone;
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

        @Override
        protected void onStart() {
            super.onStart();
            mStartTime = SystemClock.uptimeMillis();
            final Message goAheadMessage =
                    mHandler.obtainMessage(GO_AHEAD_WHAT);
            mHandler.sendMessageAtTime(goAheadMessage, mStartTime +
                    MAX_WAIT_INTERVAL);
            Log.d(TAG_LOG, "Handler message sent!");
        }


        private void goAhead() {
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }