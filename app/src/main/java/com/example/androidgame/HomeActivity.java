package com.example.androidgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(FirebaseAuth.getInstance().getCurrentUser() ==  null){
            startActivity(new Intent(this, LobbyActivity.class));
        }

        sharedPreferences = getSharedPreferences("com.example.androidgame", MODE_PRIVATE);
//        sharedPreferences.edit().putBoolean("firstrun", true).apply();
        if (sharedPreferences.getBoolean("firstrun", true)) {
            startActivity(new Intent(this, ProfessorRowanActivity.class));
            sharedPreferences.edit().putBoolean("firstrun", false).apply();
        }
    }

}
