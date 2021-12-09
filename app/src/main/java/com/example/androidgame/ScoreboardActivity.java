package com.example.androidgame;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ScoreboardActivity extends AppCompatActivity {

    private ListView scoreList;
    private GameMusicHandler gameMusicHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        //On instancie le gestionnaire de musique et tous les attributs du layout
        scoreList = findViewById(R.id.scoreList);
        ImageView goBackToHomeScoreboard = findViewById(R.id.goBackToHomeScoreboard);
        gameMusicHandler = new GameMusicHandler(this);

        //On joue la musique liée à l'activité
        gameMusicHandler.playScoreboardTheme();

        //On récupère les données liées aux score
        getData();

        //Quand on clique sur le bouton "Retour"
        goBackToHomeScoreboard.setOnClickListener(view -> {
            //On joue l'effet sonore
            gameMusicHandler.pressingButton();
            //On retourne à l'accueil
            startActivity(new Intent(ScoreboardActivity.this, HomeActivity.class));
            finish();
        });
    }

    /**
     * A function that permits to recover all the scores in collection "Scores" of RealtimeDatabase (Firebase)
     */
    private void getData(){
        //On instantie une liste pour récupérer les scores
        ArrayList<Score> scores = new ArrayList<>();
        //On exécute la requête voulue
        FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Scores")
                .get()
                .addOnCompleteListener(task -> {
                    //Si on réussit à récupérer les données
                    if (task.isSuccessful()) {
                        //On itère sur tous les résultats retournés
                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                            //On ajoute à notre liste chaque score un par un
                            String uid = dataSnapshot.child("uid").getValue().toString();
                            long score = (long) dataSnapshot.child("score").getValue();
                            scores.add(new Score((int) score, uid));
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //On trie notre liste dans l'ordre croissant
                            Collections.sort(scores);
                            Collections.reverse(scores);
                        }

                        // On affiche la liste dans une listView
                        display(scores);
                    }
                });
    }

    private void display(ArrayList<Score> scores){
        //On instancie l'adapter correspondant à notre ListView
        ScoreboardAdapter findFriendsAdapter = new ScoreboardAdapter(this, scores);
        //et on le set
        scoreList.setAdapter(findFriendsAdapter);
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
