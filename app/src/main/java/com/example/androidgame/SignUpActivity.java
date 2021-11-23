package com.example.androidgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText signUpEmail;
    private TextInputEditText signUpPassword;
    private TextInputEditText signUpPseudo;
    private MaterialButton signUp;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        signUpEmail = (TextInputEditText) findViewById(R.id.signUpEmail);
        signUpPassword = (TextInputEditText) findViewById(R.id.signUpPassword);
        signUpPseudo = (TextInputEditText) findViewById(R.id.signUpPseudo);
        signUp = (MaterialButton) findViewById(R.id.signUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this, LobbyActivity.class));
        finish();
    }

    private void registerUser(){
        String mailS = signUpEmail.getText().toString().trim();
        String passwordS = signUpPassword.getText().toString().trim();
        String pseudoS = signUpPseudo.getText().toString().trim();

        if(mailS.isEmpty()){
            signUpEmail.setError("Champ requis");
            signUpEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mailS).matches()){
            signUpEmail.setError("Email non valide");
            signUpEmail.requestFocus();
            return;
        }

        if(passwordS.isEmpty()){
            signUpPassword.setError("Champ requis");
            signUpPassword.requestFocus();
            return;
        }

        if(passwordS.length() < 6){
            signUpPassword.setError("Longueur minimum de 6 caractères");
            signUpPassword.requestFocus();
            return;
        }

        if(pseudoS.isEmpty()){
            signUpPseudo.setError("Champ requis");
            signUpPseudo.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(mailS, passwordS)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            User user = new User(pseudoS, mailS, uid);

                            FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                                    .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "Nouvel utilisateur ajouté", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Ajout non réussi", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }
}
