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

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputEditText signInEmail;
    private TextInputEditText signInPassword;
    private GameMusicHandler gameMusicHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //On instancie le gestionnaire de musique et tous les attributs du layout
        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);
        MaterialButton signIn = findViewById(R.id.signIn);
        gameMusicHandler = new GameMusicHandler(this);

        firebaseAuth = FirebaseAuth.getInstance();
        //On vérifie bien que l'utilisateur n'est pas déjà connecté (On sait jamais avec les bugs..)
        if(firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
        }

        //On joue la musique liée à l'activité
        gameMusicHandler.playSignInTheme();

        //Quand on clique sur le bouton "Se connecter"
        signIn.setOnClickListener(view -> {
            //On joue l'effet sonore
            gameMusicHandler.pressingButton();
            //On appelle la méthode qui permet de connecter l'utilisateur
            userLogin();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Renvoie au lobby et nul part ailleurs
        startActivity(new Intent(this, LobbyActivity.class));
        finish();
    }

    /**
     * A function that permits to the user to connect to the game
     */
    private void userLogin(){
        //On formate et récupère ce que l'utilisateur a rentré
        String email = Objects.requireNonNull(signInEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(signInPassword.getText()).toString().trim();

        //On fait des vérifications (Exemple : si c'est pas vide, si ça ressemble bien à un email, si le mot de passe est d'au moins 6 char)
        if(email.isEmpty()){
            signInEmail.setError("Email required");
            signInEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signInEmail.setError("Not a valid item");
            signInEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            signInPassword.setError("Password required");
            signInPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            signInPassword.setError("Min length is 6");
            signInPassword.requestFocus();
            return;
        }

        //On utilise la méthode de firebase Auth pour que l'utilisateur se connecte
        //Si on arrive bien à utiliser cette méthode
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                //Si ça a bien marché, on accède à l'accueil
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                //Sinon on met un toast pour montrer à l'utilisateur qu'il y a un problème
                Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
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
