package com.example.androidgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ScoreboardAdapter extends ArrayAdapter<Score> {

    public ScoreboardAdapter(Context context, ArrayList<Score> scores){
        super(context, R.layout.score_item,R.id.scoreRank, scores);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Score score = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.score_item, parent, false);
        }

        TextView scoreRank = view.findViewById(R.id.scoreRank);
        TextView scorePseudo = view.findViewById(R.id.scorePseudo);
        TextView scoreNb = view.findViewById(R.id.score);

        scoreRank.setText(String.valueOf(position + 1));
        FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
                .child(score.getUid())
                .child("pseudo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            for(DataSnapshot dataSnapshot : task.getResult().getChildren()){
                                scorePseudo.setText(dataSnapshot.getValue().toString());
                            }
                        }
                    }
                });

        scoreNb.setText(String.valueOf(score.getScore()));

        return view;
    }
}
