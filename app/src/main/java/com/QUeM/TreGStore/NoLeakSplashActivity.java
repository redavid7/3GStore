package com.QUeM.TreGStore;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

//Faccio in modo che la SplashActivity non faccia memory leak
public class NoLeakSplashActivity extends Activity {
    private UiHandler mHandler;
    private long mStartTime;
    private boolean mIsDone;

    private static class UiHandler extends Handler {
        private static final long MIN_WAIT_INTERVAL = 0;
        private static final String TAG_LOG = null;
        private static final int GO_AHEAD_WHAT = 1;
        //inizializzazione riferimento debole
        private WeakReference<NoLeakSplashActivity> mActivityRef;
        public UiHandler(final NoLeakSplashActivity srcActivity) {
            this.mActivityRef = new WeakReference<NoLeakSplashActivity>
                    (srcActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            final NoLeakSplashActivity srcActivity = this.mActivityRef.get();
            if (srcActivity == null) {
                Log.d(TAG_LOG, "Reference to NoLeakSplashActivity lost!");
                return;
            }
            switch (msg.what) {
                case GO_AHEAD_WHAT:
                    long elapsedTime = SystemClock.uptimeMillis() -
                            srcActivity.mStartTime;
                    if (elapsedTime >= MIN_WAIT_INTERVAL && !srcActivity.mIsDone)
                    {
                        srcActivity.mIsDone = true;
                        srcActivity.goAhead();
                    }
                    break;
            }
        }
    }

    private void goAhead() {
    }


    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.splash_page);
    	View decorView = getWindow().getDecorView();
    	int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    	decorView.setSystemUiVisibility(uiOptions);
		// Inizializzo l'handler
    	mHandler = new UiHandler(this);
    }

    //elimino messaggi di callback qualora venisse distrutta la classe
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}