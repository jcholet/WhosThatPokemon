package com.example.androidgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private GameMusicHandler gameMusicHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Si l'utilisateur n'est pas connecté sur firebase, on le renvoie au lobby
        if(FirebaseAuth.getInstance().getCurrentUser() ==  null){
            startActivity(new Intent(this, LobbyActivity.class));
        }

        //On instancie le gestionnaire de musique et tous les attributs du layout
        gameMusicHandler = new GameMusicHandler(this);
        ImageView signOut = findViewById(R.id.signOut);
        MaterialButton goToTraining = findViewById(R.id.goToTraining);
        MaterialButton goToCompetition = findViewById(R.id.goToCompetition);
        MaterialButton goToResults = findViewById(R.id.goToResults);
        ImageView help = findViewById(R.id.help);

        //On récupère les préférences de notre jeu
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.androidgame", MODE_PRIVATE);

        //S'il s'agit de la première fois qu'on l'utilise
        if (sharedPreferences.getBoolean("firstrun", true)) {
            //On lance la ProfessorRowanActivity (Explication du jeu)
            startActivity(new Intent(this, ProfessorRowanActivity.class));
            //On passe first run à faux
            sharedPreferences.edit().putBoolean("firstrun", false).apply();
        }

        //On joue la musique liée à l'activity
        gameMusicHandler.playHomeTheme();

        //Quand on clique sur le '?'
        help.setOnClickListener(view -> {

            //On crée une pop up avec dedans un view pager (3 pages et on peut slider entre)
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.my_dialog);
            LayoutInflater inflater = HomeActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.tutorial_view_pager, null);
            dialogBuilder.setView(dialogView);
            ViewPager viewPager = dialogView.findViewById(R.id.viewPager);
            viewPager.setAdapter(new TutorialPagerAdapter(getApplicationContext()));
            TabLayout tabLayout = dialogView.findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager, true);
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        });

        //Quand on clique sur le bouton "Entrainement"
        goToTraining.setOnClickListener(view -> {
            //On joue l'effet sonore
            gameMusicHandler.pressingButton();
            //On va au mode Entrainement
            startActivity(new Intent(HomeActivity.this, TrainingActivity.class));
            finish();
        });

        //Quand on clique sur le bouton "Competition"
        goToCompetition.setOnClickListener(view -> {
            //On joue l'effet sonore
            gameMusicHandler.pressingButton();
            //On va au mode entrainement
            startActivity(new Intent(HomeActivity.this, CompetitionActivity.class));
            finish();
        });

        //Quand on clique sur le bouton "Résultats"
        goToResults.setOnClickListener(view -> {
            //On joue l'effet sonore
            gameMusicHandler.pressingButton();
            //On accède au tableau des scores (avec les 10 meilleurs scores globaux)
            startActivity(new Intent(HomeActivity.this, ScoreboardActivity.class));
            finish();
        });

        //Quand on clique sur le bouton pour se déconnecter
        signOut.setOnClickListener(view -> {
            //On joue l'effet sonore
            gameMusicHandler.pressingButton();
            //On déconnecte l'utilisateur
            FirebaseAuth.getInstance().signOut();
            //On accède au lobby (choix entre se connecter et s'inscrire)
            startActivity(new Intent(HomeActivity.this, LobbyActivity.class));
            finish();
        });

    }

    @Override
    public void onBackPressed() {
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
