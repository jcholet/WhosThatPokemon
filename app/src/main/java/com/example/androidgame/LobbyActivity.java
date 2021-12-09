package com.example.androidgame;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LobbyActivity extends AppCompatActivity {

    private GameMusicHandler gameMusicHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        //On instancie le gestionnaire de musique et tous les attributs du layout
        gameMusicHandler = new GameMusicHandler(this);
        MaterialButton goToSignIn = findViewById(R.id.goToSignIn);
        MaterialButton goToSignUp = findViewById(R.id.goToSignUp);
        gameMusicHandler.playLobbyTheme();

        //Quand on clique sur le bouton "Se connecter"
        goToSignIn.setOnClickListener(view -> {
            //On joue l'effet sonore
            gameMusicHandler.pressingButton();
            //On accède à l'écran de connexion
            startActivity(new Intent(LobbyActivity.this, LoginActivity.class));
            finish();
        });

        //Quand on clique sur le bouton "S'inscrire"
        goToSignUp.setOnClickListener(view -> {
            //On joue l'effet sonore
            gameMusicHandler.pressingButton();
            //On accède à l'écran d'inscription
            startActivity(new Intent(LobbyActivity.this, SignUpActivity.class));
            finish();
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
