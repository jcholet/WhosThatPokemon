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

    private SharedPreferences sharedPreferences = null;
    private MaterialButton goToTraining;
    private MaterialButton goToCompetition;
    private MaterialButton goToResults;
    private ImageView signOut;
    private ImageView help;
    private GameMusicHandler gameMusicHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(FirebaseAuth.getInstance().getCurrentUser() ==  null){
            startActivity(new Intent(this, LobbyActivity.class));
        }

        gameMusicHandler = new GameMusicHandler(this);
        signOut = (ImageView) findViewById(R.id.signOut);
        goToTraining = (MaterialButton) findViewById(R.id.goToTraining);
        goToCompetition = (MaterialButton) findViewById(R.id.goToCompetition);
        goToResults = (MaterialButton) findViewById(R.id.goToResults);
        help = (ImageView) findViewById(R.id.help);

        sharedPreferences = getSharedPreferences("com.example.androidgame", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("firstrun", true)) {
            startActivity(new Intent(this, ProfessorRowanActivity.class));
            sharedPreferences.edit().putBoolean("firstrun", false).apply();
        }

        gameMusicHandler.playHomeTheme();

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        goToTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameMusicHandler.pressingButton();
                startActivity(new Intent(HomeActivity.this, TrainingActivity.class));
                finish();
            }
        });

        goToCompetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameMusicHandler.pressingButton();
                startActivity(new Intent(HomeActivity.this, CompetitionActivity.class));
                finish();
            }
        });

        goToResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameMusicHandler.pressingButton();
                startActivity(new Intent(HomeActivity.this, ScoreboardActivity.class));
                finish();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameMusicHandler.pressingButton();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LobbyActivity.class));
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
