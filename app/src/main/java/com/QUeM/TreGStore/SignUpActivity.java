package com.QUeM.TreGStore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.QUeM.TreGStore.DatabaseClass.Conti;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import static android.support.constraint.Constraints.TAG;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputConfPassword, inputFName, inputSName;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private StringBuilder displayName = new StringBuilder();
    private static final String TAG_SIGN_UP_ACTIVITY = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        inputFName = findViewById(R.id.firstName);
        inputSName = findViewById(R.id.secondName);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputConfPassword = findViewById(R.id.confPassword);
        progressBar = findViewById(R.id.progressBar);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confPassword = inputConfPassword.getText().toString().trim();
                String fName = inputFName.getText().toString().trim();
                String sName = inputSName.getText().toString().trim();

                displayName.append(fName + " " + sName);


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Inserisci un indirizzo email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Inserisci una password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password troppo corta! Inserisci almeno 6 caratteri", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.compareTo(confPassword) != 0) {
                    Toast.makeText(getApplicationContext(), "Le passwords non sono uguali", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Registrazione fallita" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                                    //se non esiste il carrello lo crea vuoto
                                    Log.d(TAG_SIGN_UP_ACTIVITY, "LOGIN carrello non esiste");
                                    //operazione per scrivere sul db
                                    WriteBatch batch = db.batch();


                                    //aggiungo Conti al DB per la gestione dei MarangiCoin
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

                                    //memorizzo nome e cognome nell'istanza di FirebaseUser
                                    updateProfile(displayName.toString());

                                    startActivity(new Intent(SignUpActivity.this, WelcomeActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    public void updateProfile(final String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG_SIGN_UP_ACTIVITY, "Utente registrato -> " + name);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
