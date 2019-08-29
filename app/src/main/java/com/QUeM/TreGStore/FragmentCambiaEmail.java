package com.QUeM.TreGStore;

import android.os.Bundle;
import android.app.Fragment;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentCambiaEmail extends Fragment {

    private EditText newEmail;
    private Button change;
    private View fragmentCambiaEmail;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //returning our layout file
        fragmentCambiaEmail = inflater.inflate(R.layout.fragment_cambia_email, container, false);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        newEmail = fragmentCambiaEmail.findViewById(R.id.etNewEmail);
        change = fragmentCambiaEmail.findViewById(R.id.btnChangeEmail);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && !newEmail.getText().toString().trim().equals("")) {
                    user.updateEmail(newEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "E-mail modificata con successo", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                            }
                            else{
                                Toast.makeText(getActivity(), "Cambio mail fallito!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        return fragmentCambiaEmail;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
