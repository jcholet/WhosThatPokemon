package com.example.androidgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences = null;
    private MaterialButton goToTraining;
    private ImageView signOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(FirebaseAuth.getInstance().getCurrentUser() ==  null){
            startActivity(new Intent(this, LobbyActivity.class));
        }

        signOut = (ImageView) findViewById(R.id.signOut);
        goToTraining = (MaterialButton) findViewById(R.id.goToTraining);
        sharedPreferences = getSharedPreferences("com.example.androidgame", MODE_PRIVATE);
//      sharedPreferences.edit().putBoolean("firstrun", true).apply();
        if (sharedPreferences.getBoolean("firstrun", true)) {
            startActivity(new Intent(this, ProfessorRowanActivity.class));
            sharedPreferences.edit().putBoolean("firstrun", false).apply();
        }

        goToTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TrainingActivity.class));
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LobbyActivity.class));
                finish();
            }
        });

    }

}
