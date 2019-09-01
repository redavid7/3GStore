package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentCambioPassword extends Fragment {

    private View fragmentCambiaPsw;
    private EditText oldPsw, newPsw, confPsw;
    private Button change;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //returning our layout file
        fragmentCambiaPsw = inflater.inflate(R.layout.fragment_cambio_password, container, false);


        return fragmentCambiaPsw;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        oldPsw = fragmentCambiaPsw.findViewById(R.id.oldPassword);
        newPsw = fragmentCambiaPsw.findViewById(R.id.newPassword);
        confPsw = fragmentCambiaPsw.findViewById(R.id.confPassword);
        change = fragmentCambiaPsw.findViewById(R.id.changePsw);



        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passwordVecchia = oldPsw.getText().toString().trim();
                final String passwordNuova = newPsw.getText().toString().trim();
                String passwordNuovaConferma = confPsw.getText().toString().trim();

                if (TextUtils.isEmpty(passwordVecchia) || TextUtils.isEmpty(passwordNuova) || TextUtils.isEmpty(passwordNuovaConferma)) {
                    Toast.makeText(getActivity(), "Compila tutti i campi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwordNuova.length() < 6) {
                    Toast.makeText(getActivity(), "La nuova password è troppo corta! Inserisci almeno 6 caratteri", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passwordNuova.equals(passwordNuovaConferma)) {
                    Toast.makeText(getActivity(), "Le password non sono uguali", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (user != null){

                    final String email = user.getEmail();
                    AuthCredential credential = EmailAuthProvider.getCredential(email,passwordVecchia);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updatePassword(passwordNuova).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(!task.isSuccessful()){ Toast.makeText(getActivity(), "Cambio password fallito!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getActivity(),"Password cambiata con successo!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }else {
                                Toast.makeText(getActivity(),"Errore nell'autenticazione, forse hai inserito la password sbagliata", Toast.LENGTH_SHORT).show();

                            }
                            Fragment fragment = new FragmentImpostazioni();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });

                }
            }
        });
    }
}
