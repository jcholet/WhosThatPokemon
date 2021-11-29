package com.example.androidgame;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.text.Collator;
import java.util.Locale;
import java.util.Random;

public class TrainingActivity extends AppCompatActivity {

    private final static boolean NEXT = true;
    private final static boolean ANSWER = false;

    private ImageView pokemonDisplaying;
    private ImageView backInHome;
    private TextInputEditText answerPokemon;
    private TextView reponseTraining;
    private TextView scoreInARowTraining;
    private MaterialButton showReponseTraining;
    private int idPokemon;
    private String stringIdPokemon;
    private boolean nextOrAnswer;
    private int scoreInARow = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        pokemonDisplaying = (ImageView) findViewById(R.id.pokemonDisplaying);
        answerPokemon = (TextInputEditText) findViewById(R.id.answerPokemon);
        backInHome = (ImageView) findViewById(R.id.backInHome);
        reponseTraining = (TextView) findViewById(R.id.reponseTraining);
        showReponseTraining = (MaterialButton) findViewById(R.id.showReponseTraining);
        scoreInARowTraining = (TextView) findViewById(R.id.scoreInARowTraining);

        loadPokemon();


        answerPokemon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                                Log.d("PbString", answerPokemon.getText().toString());
                                Log.d("PbString", documentSnapshot.get("frenchName").toString().toLowerCase());
                                if(instance.compare(documentSnapshot.get("frenchName").toString().toLowerCase(), answerPokemon.getText().toString()) == 0){
                                    showAnswers();
                                    answerPokemon.getText().clear();
                                    scoreInARow++;
                                    scoreInARowTraining.setText("Score :" + String.valueOf(scoreInARow));
                                } else {
                                    scoreInARow = 0;
                                    scoreInARowTraining.setText("Score :" + String.valueOf(scoreInARow));
                                }
                            }
                        }
                    });
                }
                return false;
            }
        });

        showReponseTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nextOrAnswer == NEXT){
                    loadPokemon();
                    answerPokemon.setEnabled(true);
                } else if(nextOrAnswer == ANSWER){
                    showAnswers();
                    scoreInARow = 0;
                    answerPokemon.setEnabled(false);
                }
            }
        });

        backInHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrainingActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void loadPokemon(){
        Random random = new Random();

        reponseTraining.setText(null);
        showReponseTraining.setText("Reponse");
        idPokemon = random.nextInt(808) + 1;

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
                        Picasso.get().load(uri).into(pokemonDisplaying);
                    }
                });

        nextOrAnswer = ANSWER;
        deleteCache(getApplicationContext());
    }

    private void showAnswers(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        answerPokemon.setEnabled(false);
        answerPokemon.getText().clear();
        storageReference.child("original_png")
                .child(stringIdPokemon)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(pokemonDisplaying);
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
                    reponseTraining.setText(documentSnapshot.get("frenchName").toString());
                    showReponseTraining.setText("Continuer");
                    nextOrAnswer = NEXT;
                }
            }
        });

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
}
