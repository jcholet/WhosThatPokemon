package com.example.androidgame;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class CompetitionActivity extends AppCompatActivity {

    private ImageView backInHomeCompetition;
    private TextView scoreInARowCompetition;
    private ImageView pokemonDisplayingCompetition;
    private TextInputEditText answerPokemonCompetition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        backInHomeCompetition = (ImageView) findViewById(R.id.backInHomeCompetition);
        scoreInARowCompetition = (TextView) findViewById(R.id.scoreInARowCompetition);
        pokemonDisplayingCompetition = (ImageView) findViewById(R.id.pokemonDisplayingCompetition);
        answerPokemonCompetition = (TextInputEditText) findViewById(R.id.answerPokemonCompetition);
    }
}
