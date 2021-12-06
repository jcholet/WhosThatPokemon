package com.example.androidgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LobbyActivity extends AppCompatActivity {

    private MaterialButton goToSignIn;
    private MaterialButton goToSignUp;
    private GameMusicHandler gameMusicHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        gameMusicHandler = new GameMusicHandler(this);
        goToSignIn = (MaterialButton) findViewById(R.id.goToSignIn);
        goToSignUp = (MaterialButton) findViewById(R.id.goToSignUp);
        gameMusicHandler.playLobbyTheme();

        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameMusicHandler.pressingButton();
                startActivity(new Intent(LobbyActivity.this, LoginActivity.class));
                finish();
            }
        });

        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameMusicHandler.pressingButton();
                startActivity(new Intent(LobbyActivity.this, SignUpActivity.class));
                finish();
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
