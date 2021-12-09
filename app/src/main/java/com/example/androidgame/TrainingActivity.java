package com.example.androidgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.Collator;
import java.util.Objects;
import java.util.Random;

public class TrainingActivity extends AppCompatActivity {

    private final static boolean NEXT = true;
    private final static boolean ANSWER = false;
    private static final int NB_POKEMON = 808;

    private ImageView pokemonDisplaying;
    private TextInputEditText answerPokemon;
    private TextView reponseTraining;
    private TextView scoreInARowTraining;
    private MaterialButton showReponseTraining;
    private int idPokemon;
    private String stringIdPokemon;
    private boolean nextOrAnswer;
    private int scoreInARow = 0;
    private GameMusicHandler gameMusicHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        //On instantie les attributs du Layout et le gestionnaire pour la musique
        pokemonDisplaying = findViewById(R.id.pokemonDisplaying);
        answerPokemon = findViewById(R.id.answerPokemon);
        ImageView backInHome = findViewById(R.id.backInHome);
        reponseTraining = findViewById(R.id.reponseTraining);
        showReponseTraining = findViewById(R.id.showReponseTraining);
        scoreInARowTraining = findViewById(R.id.scoreInARowTraining);
        gameMusicHandler = new GameMusicHandler(this);

        //On joue le theme de l'entrainement
        gameMusicHandler.playTrainingTheme();

        //On charge le pokémon
        loadPokemon();

        //Quand le text input est édité
        answerPokemon.setOnEditorActionListener((textView, i, keyEvent) -> {
            //Si l'utilisateur clique sur le bouton send sur le clavier
            if(i == EditorInfo.IME_ACTION_SEND){
                //On récupère la référence pour arriver jusqu'au bon pokémon
                DocumentReference pokemonRef = FirebaseFirestore.getInstance().collection("Pokemons").document(String.valueOf(idPokemon));
                //On récupère le pokémon dans Firestore et quand on l'a bien récupéré
                pokemonRef.get().addOnCompleteListener(task -> {
                    //On vérifie si la tache a bien réussi
                    if(task.isSuccessful()){

                        //On récupère ce que la tache nous renvoie
                        DocumentSnapshot documentSnapshot = task.getResult();

                        //Grâce a collator, on supprime tous les accents pour rendre le jeu un peu plus facile (Les accents me semblaient être trop compliqués)
                        Collator instance = Collator.getInstance();
                        instance.setStrength(Collator.NO_DECOMPOSITION);

                        //On compare le nom dans la BDD et le nom que l'utilisateur a entré dans le text input
                        if(instance.compare(Objects.requireNonNull(Objects.requireNonNull(documentSnapshot).get("frenchName")).toString().toLowerCase(), Objects.requireNonNull(answerPokemon.getText()).toString()) == 0){
                            //Si ce sont les deux mêmes, la réponse est affiché et le score augmente, on clear aussi la réponse
                            showAnswers();
                            answerPokemon.getText().clear();
                            scoreInARow++;
                        } else {
                            //Sinon on met le score à 0
                            scoreInARow = 0;
                        }
                        scoreInARowTraining.setText("Score :" + String.valueOf(scoreInARow));
                    }
                });
            }
            return false;
        });

        showReponseTraining.setOnClickListener(view -> {
            //On met le son du bouton
            gameMusicHandler.pressingButton();
            //Si l'état est next
            if(nextOrAnswer == NEXT){
                //On charge un pokemon et on fait en sorte que le text input marche
                loadPokemon();
                answerPokemon.setEnabled(true);

                //Si l'état est answer
            } else if(nextOrAnswer == ANSWER){
                //On montre la réponse, on remet le score à 0 et on fait en sorte que le text input ne marche plus
                showAnswers();
                scoreInARow = 0;
                answerPokemon.setEnabled(false);
            }
        });

        //Quand on clique sur le bouton de retour (en haut à gauche)
        backInHome.setOnClickListener(view -> {
            //On met le son du bouton
            gameMusicHandler.pressingButton();
            //On revient à l'accueil
            startActivity(new Intent(TrainingActivity.this, HomeActivity.class));
            finish();
        });
    }

    /**
     * A function that load a pokemon and permits to play
     */
    private void loadPokemon(){
        Random random = new Random();

        //On fait en sorte que le text input soit bien vide
        reponseTraining.setText(null);
        //On met le bouton à Réponse
        showReponseTraining.setText("Reponse");

        //On récupère un nombre random entre 1 et 809
        idPokemon = random.nextInt(NB_POKEMON) + 1;

        //On fait en sorte de récupérer le bon nom pour récupérer l'image (Format : '001.png', '010.png', '100.png')
        if(idPokemon < 10){
            stringIdPokemon = "00"+idPokemon+".png";
        } else if (idPokemon < 100){
            stringIdPokemon = "0"+idPokemon+".png";
        } else {
            stringIdPokemon = idPokemon + ".png";
        }

        //On récupère l'image en noir et on la charge dans l'image view
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("black_png")
                .child(stringIdPokemon)
                .getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.get().load(uri).into(pokemonDisplaying));

        //On met l'état à ANSWER
        nextOrAnswer = ANSWER;
        //Pour éviter que le cache explose, on fait en sorte qu'il soit bien vidé
        deleteCache(getApplicationContext());
    }

    /**
     * A function that show answers to player
     */
    private void showAnswers(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        //On fait en sorte que le text input ne soit plus accessible
        answerPokemon.setEnabled(false);
        //On clear le text input
        Objects.requireNonNull(answerPokemon.getText()).clear();
        //On récupère et charge l'image du pokémon en couleur et non tout en noir
        storageReference.child("original_png")
                .child(stringIdPokemon)
                .getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.get().load(uri).into(pokemonDisplaying));

        //On récupère ici grâce à l'id du pokemon son nom et on l'affiche dans un text, on met le bouton à "Continuer" et l'état à NEXT
        FirebaseFirestore.getInstance()
                .collection("Pokemons")
                .document(String.valueOf(idPokemon))
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        reponseTraining.setText(Objects.requireNonNull(Objects.requireNonNull(documentSnapshot).get("frenchName")).toString());
                        showReponseTraining.setText("Continuer");
                        nextOrAnswer = NEXT;
                    }
                });

    }

    /**
     * A function that delete cache from this application
     *
     * @param context get the context of the application
     */
    private void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    /**
     * A function that delete a directory of the phone
     *
     * @param dir file from phone that we can delete
     * @return true if successful and false if not
     */
    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : Objects.requireNonNull(children)) {
                boolean success = deleteDir(new File(dir, child));
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
