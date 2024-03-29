package com.example.androidgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfessorRowanActivity extends AppCompatActivity {

    private TextView sorbierText;
    private Thread thread;
    private String string;
    private GameMusicHandler gameMusicHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_rowan);

        //On instancie le gestionnaire de musique et tous les attributs du layout
        sorbierText = findViewById(R.id.sorbierText);
        gameMusicHandler = new GameMusicHandler(this);

        //On joue la musique liée à l'activité
        gameMusicHandler.playRowanTheme();

        string = "Bonjour !\nQuelle joie de te rencontrer !\nBienvenue sur \"Qui est ce pokémon ?\"";

        //Thread pour que le texte s'affiche petit à petit (comme au début de pokémon diamant)
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                for(int i = 0; i < string.length(); i++){
                    try {
                        sorbierText.setText(sorbierText.getText() + String.valueOf(string.charAt(i)));
                        if(sorbierText.getText().toString().length() == string.length()){
                            thread.sleep(1000);
                            string = "Dans ce jeu, vous pourrez vous amuser a retrouver le nom de vos pokémon favoris !\n" +
                                    "Selon plusieurs modes : Entrainement et compétition";
                            if(sorbierText.getText().toString().equals(string)){
                                string = "Commencons !";
                                sorbierText.setText(null);
                                for(int j = 0; j < string.length(); j++){
                                    sorbierText.setText(sorbierText.getText() + String.valueOf(string.charAt(j)));
                                    thread.sleep(100);
                                }
                                thread.sleep(1000);
                                startActivity(new Intent(ProfessorRowanActivity.this, HomeActivity.class));
                            }
                            sorbierText.setText(null);
                            i = -1;
                        }
                        thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();
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
