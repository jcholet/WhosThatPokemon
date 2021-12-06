package com.example.androidgame;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScoreboardActivity extends AppCompatActivity {

    private ListView scoreList;
    private ImageView goBackToHomeScoreboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreList = (ListView) findViewById(R.id.scoreList);
        goBackToHomeScoreboard = (ImageView) findViewById(R.id.goBackToHomeScoreboard);

        getData();

        goBackToHomeScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScoreboardActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void getData(){
        ArrayList<Score> scores = new ArrayList<>();
        FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Scores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DataSnapshot dataSnapshot : Objects.requireNonNull(task.getResult()).getChildren()) {
                                String uid = dataSnapshot.child("uid").getValue().toString();
                                long score = (long) dataSnapshot.child("score").getValue();
                                scores.add(new Score((int) score, uid));
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Collections.sort(scores);
                                Collections.reverse(scores);
                            }

                            display(scores);

                        }
                    }
                });
    }

    private void display(ArrayList<Score> scores){
        ScoreboardAdapter findFriendsAdapter = new ScoreboardAdapter(this, scores);
        scoreList.setAdapter(findFriendsAdapter);
    }

}
