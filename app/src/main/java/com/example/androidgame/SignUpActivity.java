package com.example.androidgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText signUpEmail;
    private TextInputEditText signUpPassword;
    private TextInputEditText signUpPseudo;
    private FirebaseAuth firebaseAuth;
    private GameMusicHandler gameMusicHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //On instantie le gestionnaire de musique, l'authentification via Firebase et les attributs du layout
        firebaseAuth = FirebaseAuth.getInstance();
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpPseudo = findViewById(R.id.signUpPseudo);
        MaterialButton signUp = findViewById(R.id.signUp);
        gameMusicHandler = new GameMusicHandler(this);

        //On joue le theme lié à l'activité
        gameMusicHandler.playSignUpTheme();

        signUp.setOnClickListener(view -> {
            gameMusicHandler.pressingButton();
            registerUser();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Renvoie au lobby et nul part ailleurs
        startActivity(new Intent(SignUpActivity.this, LobbyActivity.class));
        finish();
    }

    private void registerUser(){
        String mailS = Objects.requireNonNull(signUpEmail.getText()).toString().trim();
        String passwordS = Objects.requireNonNull(signUpPassword.getText()).toString().trim();
        String pseudoS = Objects.requireNonNull(signUpPseudo.getText()).toString().trim();

        //On fait des vérifications (Exemple : si c'est pas vide, si ça ressemble bien à un email, si le mot de passe est d'au moins 6 char, si le pseudo n'est pas vide...)
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


        //On utilise la méthode de firebase Auth pour que l'utilisateur créé un compte
        //Si on arrive bien à utiliser cette méthode
        firebaseAuth.createUserWithEmailAndPassword(mailS, passwordS)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        //On récupère son uid
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String uid = Objects.requireNonNull(firebaseUser).getUid();
                        //On créé un nouvel user
                        User user = new User(pseudoS, mailS, uid);

                        //On accède à realtime database, et on met l'user avec comme id son uid
                        FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        //Si la tache est réussie
                                        Toast.makeText(SignUpActivity.this, "Nouvel utilisateur ajouté", Toast.LENGTH_LONG).show();
                                        //On renvoie vers l'activity de Login
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Ajout non réussi", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameMusicHandler.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameMusicHandler.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameMusicHandler.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameMusicHandler.pause();
    }
}
