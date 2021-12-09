package com.example.androidgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class ScoreboardAdapter extends ArrayAdapter<Score> {

    public ScoreboardAdapter(Context context, ArrayList<Score> scores){
        super(context, R.layout.score_item,R.id.scoreRank, scores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        //On récupère le score grâce à sa position
        Score score = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.score_item, parent, false);
        }

        //On instantie les attributs de notre layout
        TextView scoreRank = view.findViewById(R.id.scoreRank);
        TextView scorePseudo = view.findViewById(R.id.scorePseudo);
        TextView scoreNb = view.findViewById(R.id.score);

        //On met le bon rank
        scoreRank.setText(String.valueOf(position + 1));

        //On récupère le pseudo du joueur grâce à son uid
        FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
                .child(score.getUid())
                .child("pseudo")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DataSnapshot dataSnapshot : task.getResult().getChildren()){
                            //On met le pseudo du joueur dans le text view correspondant
                            scorePseudo.setText(dataSnapshot.getValue().toString());
                        }
                    }
                });

        //On met le score dans le text view correspondant
        scoreNb.setText(String.valueOf(score.getScore()));

        return view;
    }
}
