package com.QUeM.TreGStore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class FragmentCambiaEmail extends Fragment {

    private EditText newEmail, password, confermaPassword;
    private Button change;
    private View fragmentCambiaEmail;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //returning our layout file
        fragmentCambiaEmail = inflater.inflate(R.layout.fragment_cambia_email, container, false);




        return fragmentCambiaEmail;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        newEmail = fragmentCambiaEmail.findViewById(R.id.nuova_mail_text);
        password = fragmentCambiaEmail.findViewById(R.id.password_cambioemail);
        confermaPassword = fragmentCambiaEmail.findViewById(R.id.conf_password_cambioemail);
        change = fragmentCambiaEmail.findViewById(R.id.btnChangeEmail);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passwordInput = password.getText().toString().trim();
                final String passwordConfermaInput = confermaPassword.getText().toString().trim();
                final String nuovaEmailInput = newEmail.getText().toString().trim();
                if(nuovaEmailInput.length()>0 && passwordInput.length()>0 && passwordConfermaInput.length()>0){
                    if(passwordInput.equals(passwordConfermaInput)){
                        final String email = user.getEmail();
                        AuthCredential credential = EmailAuthProvider.getCredential(email,passwordInput);

                        if (user != null ) {

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        user.updateEmail(nuovaEmailInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(!task.isSuccessful()){ Toast.makeText(getActivity(), R.string.email_change_failed, Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(getActivity(),R.string.email_change_success, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    }else {
                                        Toast.makeText(getActivity(),R.string.auth_error_mail, Toast.LENGTH_SHORT).show();

                                    }
                                    Fragment fragment = new FragmentImpostazioni();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            });

                        }
                    }else{
                        Toast.makeText(getActivity(), R.string.wrong_psw, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), R.string.camps, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), R.string.camps, Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}
