package com.QUeM.TreGStore.DatabaseClass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.QUeM.TreGStore.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;

import static com.QUeM.TreGStore.DatabaseClass.App.CHANNEL_ID;

public class NotificaProdotti extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 5;

    //Override OBBLIGATORIO. Normalmente questo metodo serve unicamente per i servizi i cui componenti
    //comunicano tra loro, e sono legati tramite questo metodo. La notifica non funziona se non lo metto,
    //ma a me non serve quindi lo faccio Nullable e ritorno null
    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public void onCreate() {
        Intent notificationIntent =  new Intent (this, HomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        /*Costruisco la notifica.
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(com.QUeM.TreGStore.R.string.titolo_notifica))
                .setSmallIcon(com.QUeM.TreGStore.R.drawable.logo3gstore)
                .setContentText(getString(com.QUeM.TreGStore.R.string.contenuto_notifica))
                .setContentIntent(pendingIntent)
                .build();

        //faccio in modo che il servizio non venga distrutto dopo 1 minuto
        startForeground(1, notification);
*/
    }

    //Metodo che viene chiamato nonappena viene chiamato startService
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //return stabilisce cosa fare una volta mandata la notifica. STICKY ricomincia il sistema
        //appena possibile; NOT_STICKY lo termina una volta mandata la notifica;
        //REDIRECT_INTENT è come STICY ma conserva l'intent già usato.
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        stopSelf();

        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();
    }



    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        notifica();

                    }
                });
            }
        };
    }

    public void notifica(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RSSPullService");

        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        Context ctx = getApplicationContext();

        Notification.Builder builder;

        builder = new Notification.Builder(ctx)
                .setContentTitle(getString(com.QUeM.TreGStore.R.string.titolo_notifica))
                .setContentText(getString(com.QUeM.TreGStore.R.string.contenuto_notifica))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(com.QUeM.TreGStore.R.drawable.logo3gstore);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }
}
