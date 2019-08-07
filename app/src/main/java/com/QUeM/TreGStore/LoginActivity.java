package com.QUeM.TreGStore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.QUeM.TreGStore.DatabaseClass.Conti;
import com.QUeM.TreGStore.DatabaseClass.Prodotti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import static android.support.constraint.Constraints.TAG;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth=FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            //l'utente si è già autenticato
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            controlloCarrello(db);
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btnSignup = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        btnReset = findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //pulsante per Sign Up
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        //pulsante per resettare la password
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //fase di autenticazione
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    final Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    // prima di accedere alla Home Activity, controlla che sia stato creato il carrello relativo al cliente e la sezione MarangiCoin
                                    // accedo al database
                                    final FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    controlloCarrello(db);

                                    DocumentReference docRef2 = db.collection("conti").document(auth.getUid());
                                    docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    //se la sezione conti esiste già non fa nulla
                                                    Log.d(TAG, "CONTI esiste");
                                                } else {
                                                    //se non esiste la crea vuota
                                                    Log.d(TAG, "CONTI non esiste");
                                                    //operazione per scrivere sul db
                                                    WriteBatch batch = db.batch();
                                                    //creo riferimento da creare
                                                    DocumentReference conti = db.collection("conti").document(auth.getUid());
                                                    //imposto il comando di creazione con .set dove inserisco percorso e campo del documento
                                                    batch.set(conti, new Conti());
                                                    //eseguo il comando di creazione
                                                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            // ...
                                                        }
                                                    });
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });

                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    public void ripristinaCarrrello(final FirebaseFirestore db, DocumentReference docRef){
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //se non esiste il carrello lo crea vuoto
                    Log.d(TAG, "CCCC carrello non esiste");
                    //operazione per scrivere sul db
                    WriteBatch batch = db.batch();
                    //creo riferimento da creare
                    DocumentReference carrello = db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello").document("cancellami");
                    //imposto il comando di creazione con .set dove inserisco percorso e campo del documento
                    batch.set(carrello, new Prodotti());
                    //eseguo il comando di creazione
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void controlloCarrello(final FirebaseFirestore db){
        //creo il riferimento per un documento che abbia come id l'user id dell'utente
        final DocumentReference docRef = db.collection("carrelli").document(auth.getUid());
        CollectionReference colref=db.collection("carrelli").document(auth.getUid()).collection("prodottiCarrello");

        colref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().size() >0) {
                        //se la collezione esiste non faccio nulla
                        Log.d(TAG, "coll esiste");
                    }else{
                        Log.d(TAG, "coll non esiste ");

                        ripristinaCarrrello(db, docRef);

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}

