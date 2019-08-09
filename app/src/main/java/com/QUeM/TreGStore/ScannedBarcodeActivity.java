package com.QUeM.TreGStore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static android.support.constraint.Constraints.TAG;
import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.IOException;

public class ScannedBarcodeActivity extends AppCompatActivity {

    //DICHIARAZIONE VARIABILI
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";

    //AZIONI DA ESEGUIRE ALLA CREAZIONE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);
        initViews();
    }
    //INIZIALIZZAZIONE VARIABLILI
    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        //ATTIVAZIONE BOTTONE NEL MOMENTO IN CUI VIENE RILEVATO UN CODICE
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intentData.length() > 0) {
                    Intent cameraIntent=new Intent(ScannedBarcodeActivity.this, HomeActivity.class);
                    //-------------------------------------------------
                    //Inizio a inserire nel carrello online il prodotto
                    //-------------------------------------------------
                    //mi salvo il codice del prodotto scannerizzato (dichiarati final per via del metodo AddOnCompleteListener, dato che è asincrono
                    final String codiceProdottoScannerizzato=String.valueOf(intentData);

                    //creo la connessione al documento dell'utente che contiene il carrello
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference docrefprodotti = db.collection("prodotti").document(codiceProdottoScannerizzato);

                    //provo ad aprire la connessione
                    docrefprodotti.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                //se la connessione è riuscita, vedo se il documento esiste o meno
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //se il documento esiste, creo un oggetto che corrisponde al prodotto legato al codice a barre
                                    Prodotti prod=document.toObject(Prodotti.class);
                                    //controllo se il prodotto è disponibile all'acquisto
                                    if(prod.isDisponibile()){
                                        //inizializzo l'id del prodotto
                                        prod.setId(codiceProdottoScannerizzato);
                                        //imposto il contatore del numero di prodotti di quel tipo presenti nel carrello
                                        prod.setTotalePezziCarrello(1);
                                        //chiamo la funzione per inserirlo nel carrello dell'utente
                                        aggiungiProdottoCarrello(prod, db);
                                    }else{
                                        //per ora scrivo un toast di avviso che non è più disp
                                        Toast.makeText(getApplicationContext(), R.string.product_unavailable, Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Log.d(TAG, "No such document");

                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });

                    startActivity(cameraIntent);
                }
            }
        });
    }

    //CICLO DI VITA FOTOCAMERA
    private void initialiseDetectorsAndSources() {
        //NOTIFICA AVVIO ACTIVITY

        //INIZIALIZZO LE RISORSE NECESSARIE
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE|Barcode.CODABAR)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();
        //RICHIEDO AUTORIZZAZIONE E ATTIVAZIONE FOTOCAMERA
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            //RICHIESTA AUTORIZZAZIONE
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //ATTIVAZIONE
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            //DISATTIVAZIONE
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        //NOTIFICA DI DISATTIVAZIONE BARCODE
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                //Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }
            //RILEVAZIONE DEL CODICE
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                                btnAction.setText("AGGIUNGI AL CARRELLO");
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData); }
                    });

                }
            }
        });
    }
    //--------------------FINE CICLO DI VITA----------------------------------------------
    //METODO ON PAUSE
    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }
    //METODO ON RESUME
    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }



    public void aggiungiProdottoCarrello(Prodotti prod, FirebaseFirestore db1){
        //prendo l'utente attualmente connesso
        FirebaseAuth auth= FirebaseAuth.getInstance();
        //imposto la variabile Prodotto da usare nella funzione asincrona
        final Prodotti prodottoUtente=prod;
        //imposto la variabile del collegamento del FireStore da usare nella funzione asincrona
        final FirebaseFirestore db=db1;
        //creo il riferimento al carrello dell'utente attualmento connesso
        final DocumentReference carrello = db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello").document(prodottoUtente.getId());

        carrello.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //se la connessione è riuscita, vedo se il documento esiste o meno
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Prodotti prod=document.toObject(Prodotti.class);
                        //se il documento esiste, creo un oggetto che corrisponde al prodotto legato al codice a barre
                        carrello.update("totalePezziCarrello", prod.getTotalePezziCarrello()+1);
                    } else {
                        Log.d(TAG, "No such document");
                        carrello.set(prodottoUtente).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        DocumentReference cancellami=db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello").document("cancellami");
        cancellami.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "LOGIN cancellami eliminato con successo");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "LOGIN cancellami non trovato/non eliminato", e);
            }
        });

    }


}
