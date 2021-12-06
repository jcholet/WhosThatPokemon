package com.example.androidgame;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import io.alterac.blurkit.BlurLayout;

public class CompetitionActivity extends AppCompatActivity {

    private final static boolean PLAYING = true;
    private final static boolean ANSWER = false;

    private boolean state;
    private ImageView backInHomeCompetition;
    private ImageView cross1;
    private ImageView cross2;
    private ImageView cross3;
    private TextView scoreInARowCompetition;
    private TextView reponseCompetition;
    private ImageView pokemonDisplayingCompetition;
    private TextInputEditText answerPokemonCompetition;
    private int idPokemon;
    private int score;
    private String stringIdPokemon;
    private int nbErrors;
    private GameMusicHandler gameMusicHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        nbErrors = 0;
        score = 0;
        gameMusicHandler = new GameMusicHandler(this);
        reponseCompetition = (TextView) findViewById(R.id.reponseCompetition);
        backInHomeCompetition = (ImageView) findViewById(R.id.backInHomeCompetition);
        scoreInARowCompetition = (TextView) findViewById(R.id.scoreInARowCompetition);
        pokemonDisplayingCompetition = (ImageView) findViewById(R.id.pokemonDisplayingCompetition);
        answerPokemonCompetition = (TextInputEditText) findViewById(R.id.answerPokemonCompetition);
        cross1 = (ImageView) findViewById(R.id.cross1);
        cross2 = (ImageView) findViewById(R.id.cross2);
        cross3 = (ImageView) findViewById(R.id.cross3);

        gameMusicHandler.playCompetitionTheme();

        loadPokemon();

        pokemonDisplayingCompetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state == ANSWER){
                    gameMusicHandler.pressingButton();
                    loadPokemon();
                    reponseCompetition.setText("");
                    state = PLAYING;
                }
            }
        });

        answerPokemonCompetition.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEND){
                    DocumentReference pokemonRef = FirebaseFirestore.getInstance().collection("Pokemons").document(String.valueOf(idPokemon));
                    pokemonRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Collator instance = Collator.getInstance();
                                instance.setStrength(Collator.NO_DECOMPOSITION);
                                if(instance.compare(documentSnapshot.get("frenchName").toString().toLowerCase(), answerPokemonCompetition.getText().toString()) == 0){
                                    showAnswers();
                                    score++;
                                } else {
                                    nbErrors++;
                                    loadPokemon();
                                    if(nbErrors == 1){
                                        cross1.setImageResource(R.drawable.cross_icon_red);
                                    } else if(nbErrors == 2){
                                        cross2.setImageResource(R.drawable.cross_icon_red);
                                    } else if(nbErrors == 3){
                                        cross3.setImageResource(R.drawable.cross_icon_red);

                                        List<Score> scores = new ArrayList<>();

                                        FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/")
                                                .getReference("Scores")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            int i = 0;
                                                            for(DataSnapshot dataSnapshot : Objects.requireNonNull(task.getResult()).getChildren()){
                                                                if(i > 10){
                                                                    break;
                                                                }
                                                                String uid = dataSnapshot.child("uid").getValue().toString();
                                                                long score = (long) dataSnapshot.child("score").getValue();
                                                                scores.add(new Score((int) score, uid));
                                                                i++;
                                                            }

                                                            scores.add(new Score(score, FirebaseAuth.getInstance().getCurrentUser().getUid()));

                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                                Collections.sort(scores);
                                                                Collections.reverse(scores);
                                                            }

                                                            for(int j = 0; j < 10; j++){
                                                                FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/")
                                                                            .getReference("Scores")
                                                                            .child("score"+i)
                                                                            .setValue(new Score(scores.get(i).getScore(), scores.get(i).getUid()));
                                                            }

                                                        }

                                                    }
                                                });

                                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CompetitionActivity.this, R.style.my_dialog);
                                        LayoutInflater inflater = CompetitionActivity.this.getLayoutInflater();
                                        View dialogView = inflater.inflate(R.layout.dialog_view, null);
                                        dialogBuilder.setView(dialogView);
                                        MaterialButton restartCompetition = (MaterialButton) dialogView.findViewById(R.id.restartCompetition);
                                        restartCompetition.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(CompetitionActivity.this, CompetitionActivity.class));
                                                finish();
                                            }
                                        });
                                        MaterialButton goBackToHome = (MaterialButton) dialogView.findViewById(R.id.goBackToHome);
                                        goBackToHome.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(CompetitionActivity.this, HomeActivity.class));
                                                finish();
                                            }
                                        });
                                        AlertDialog alertDialog = dialogBuilder.create();
                                        alertDialog.show();
                                    }
                                }
                                answerPokemonCompetition.getText().clear();
                                scoreInARowCompetition.setText("Score :" + String.valueOf(score));
                            }
                        }
                    });
                }
                return false;
            }
        });

        backInHomeCompetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameMusicHandler.pressingButton();
                startActivity(new Intent(CompetitionActivity.this, HomeActivity.class));
                finish();
            }
        });

    }

    private void showAnswers(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        answerPokemonCompetition.setEnabled(false);
        answerPokemonCompetition.getText().clear();
        storageReference.child("original_png")
                .child(stringIdPokemon)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(pokemonDisplayingCompetition);
                    }
                });
        FirebaseFirestore.getInstance()
                .collection("Pokemons")
                .document(String.valueOf(idPokemon))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            reponseCompetition.setText(documentSnapshot.get("frenchName").toString());
                        }
                    }
                });
        state = ANSWER;
    }

    private void loadPokemon(){
        Random random = new Random();
        answerPokemonCompetition.setEnabled(true);
        answerPokemonCompetition.setText(null);
        idPokemon = random.nextInt(808) + 1;

        System.out.println("Id pokémon : " + idPokemon);

        if(idPokemon < 10){
            stringIdPokemon = "00"+idPokemon+".png";
        } else if (idPokemon < 100){
            stringIdPokemon = "0"+idPokemon+".png";
        } else {
            stringIdPokemon = idPokemon + ".png";
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("black_png")
                .child(stringIdPokemon)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(pokemonDisplayingCompetition);
                    }
                });

        deleteCache(getApplicationContext());
    }

    private void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
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
