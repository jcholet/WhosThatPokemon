package com.example.androidgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;

import java.util.concurrent.CountDownLatch;

public class ProfessorRowanActivity extends AppCompatActivity {

    private TextView sorbierText;
    private Thread thread;
    private String string;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_rowan);

        sorbierText = (TextView) findViewById(R.id.sorbierText);

        string = "Bonjour !\nQuelle joie de te rencontrer !\nBienvenue sur \"Qui est ce pokémon ?\"";

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

}
