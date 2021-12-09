package com.example.androidgame;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CompetitionActivity extends AppCompatActivity {

    private final static boolean PLAYING = true;
    private final static boolean ANSWER = false;
    private final static int NB_POKEMON = 808;

    private boolean state;
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
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);

        nbErrors = 0;
        score = 0;

        //Instantiation de tous attributs (Gestion de la musique + attributs présents dans le layout "activity_competition"
        gameMusicHandler = new GameMusicHandler(this);
        reponseCompetition = findViewById(R.id.reponseCompetition);
        ImageView backInHomeCompetition = findViewById(R.id.backInHomeCompetition);
        scoreInARowCompetition = findViewById(R.id.scoreInARowCompetition);
        pokemonDisplayingCompetition = findViewById(R.id.pokemonDisplayingCompetition);
        answerPokemonCompetition = findViewById(R.id.answerPokemonCompetition);
        cross1 = findViewById(R.id.cross1);
        cross2 = findViewById(R.id.cross2);
        cross3 = findViewById(R.id.cross3);

        //On commence à jouer la musique liée au mode compétition
        gameMusicHandler.playCompetitionTheme();

        //On charge un pokémon
        loadPokemon();

        //Lorsqu'on clique sur l'image du pokémon
        pokemonDisplayingCompetition.setOnClickListener(view -> {
            // Si le state est ANSWER
            if (state == ANSWER) {
                //Alors on joue l'effet d'un bouton pressé
                gameMusicHandler.pressingButton();
                //On recharge un nouveau pokémon
                loadPokemon();
                //On clear le text présent dans notre TextInput
                reponseCompetition.setText("");
                //et le state passe à PLAYING
                state = PLAYING;
            }
        });

        //Quand le text input est édité
        answerPokemonCompetition.setOnEditorActionListener((textView, i, keyEvent) -> {
            //Si l'utilisateur clique sur le bouton send sur le clavier
            if (i == EditorInfo.IME_ACTION_SEND) {
                //On récupère la référence pour arriver jusqu'au bon pokémon
                DocumentReference pokemonRef = FirebaseFirestore.getInstance().collection("Pokemons").document(String.valueOf(idPokemon));
                //On récupère le pokémon dans Firestore et quand on l'a bien récupéré
                pokemonRef.get().addOnCompleteListener(task -> {
                    //On vérifie si la tache a bien réussi
                    if (task.isSuccessful()) {

                        //On récupère ce que la tache nous renvoie
                        DocumentSnapshot documentSnapshot = task.getResult();

                        //Grâce a collator, on supprime tous les accents pour rendre le jeu un peu plus facile (Les accents me semblaient être trop compliqués)
                        Collator instance = Collator.getInstance();
                        instance.setStrength(Collator.NO_DECOMPOSITION);

                        //On compare le nom dans la BDD et le nom que l'utilisateur a entré dans le text input
                        if (instance.compare(Objects.requireNonNull(Objects.requireNonNull(documentSnapshot).get("frenchName")).toString().toLowerCase(), Objects.requireNonNull(answerPokemonCompetition.getText()).toString()) == 0) {

                            //Si ce sont les deux mêmes, la réponse est affiché et le score augmente
                            showAnswers();
                            score++;

                            //Sinon
                        } else {
                            //On augmente le nombre d'erreurs et on recharge un nouveau pokémon
                            nbErrors++;
                            loadPokemon();

                            if (nbErrors == 1) {
                                //Si 1 erreur, on passe la 1ère croix au rouge
                                cross1.setImageResource(R.drawable.cross_icon_red);
                            } else if (nbErrors == 2) {
                                //Si 2 erreur, on passe la 2ème croix au rouge
                                cross2.setImageResource(R.drawable.cross_icon_red);
                            } else if (nbErrors == 3) {
                                //Si 3 erreur, on passe la 3ème croix au rouge
                                cross3.setImageResource(R.drawable.cross_icon_red);

                                //On instantie une liste qui va contenir tous les scores stockés dans notre BDD
                                List<Score> scores = new ArrayList<>();

                                //On exécute la requête pour accéder au score dans RealtimeDatabase
                                FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/")
                                        .getReference("Scores")
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            // Si la tache est réussie
                                            if (task1.isSuccessful()) {
                                                //On instantie un compteur
                                                int i1 = 0;

                                                //On parcourt les résultats obtenus
                                                for (DataSnapshot dataSnapshot : Objects.requireNonNull(task1.getResult()).getChildren()) {
                                                    //On ne veut pas plus de 10 résultats donc au bout de 10, on break la boucle
                                                    if (i1 > 10) {
                                                        break;
                                                    }
                                                    //On récupère les scores et les met dans la list score
                                                    String uid = Objects.requireNonNull(dataSnapshot.child("uid").getValue()).toString();
                                                    long score = (long) dataSnapshot.child("score").getValue();
                                                    scores.add(new Score((int) score, uid));

                                                    //On incrémente notre compteur
                                                    i1++;
                                                }

                                                //On ajoute le score réalisé par le joueur actuel
                                                scores.add(new Score(score, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    //On trie la liste par ordre croissant
                                                    Collections.sort(scores);
                                                    Collections.reverse(scores);
                                                }

                                                //On supprime tout ce qu'il y avait dans la collection "Scores" pour ne pas avoir de soucis
                                                FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/")
                                                        .getReference("Scores")
                                                        .removeValue();

                                                for (int j = 0; j < scores.size(); j++) {
                                                    //On ajoute les 10 premiers score de la liste dans RealtimeDatabase
                                                    FirebaseDatabase.getInstance("https://pokemon-979c3-default-rtdb.europe-west1.firebasedatabase.app/")
                                                            .getReference("Scores")
                                                            .child("score" + j)
                                                            .setValue(new Score(scores.get(j).getScore(), scores.get(j).getUid()));
                                                }

                                            }

                                        });

                                //On fait un alert dialog pour savoir si le joueur veut continuer à jouer ou s'il veut retourner à l'accueil
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CompetitionActivity.this, R.style.my_dialog);
                                LayoutInflater inflater = CompetitionActivity.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.dialog_view, null);
                                dialogBuilder.setView(dialogView);
                                MaterialButton restartCompetition = dialogView.findViewById(R.id.restartCompetition);
                                restartCompetition.setOnClickListener(view -> {
                                    startActivity(new Intent(CompetitionActivity.this, CompetitionActivity.class));
                                    finish();
                                });
                                MaterialButton goBackToHome = dialogView.findViewById(R.id.goBackToHome);
                                goBackToHome.setOnClickListener(view -> {
                                    startActivity(new Intent(CompetitionActivity.this, HomeActivity.class));
                                    finish();
                                });
                                alertDialog = dialogBuilder.create();
                                alertDialog.show();
                            }
                        }

                        //On clear la réponse et on fait évoluer l'affichage du score
                        Objects.requireNonNull(answerPokemonCompetition.getText()).clear();
                        scoreInARowCompetition.setText("Score :" + String.valueOf(score));
                    }
                });
            }
            return false;
        });

        //Quand on clique sur le bouton de retour
        backInHomeCompetition.setOnClickListener(view -> {
            //On lance l'effet sonore
            gameMusicHandler.pressingButton();
            //On retourne à l'accueil
            startActivity(new Intent(CompetitionActivity.this, HomeActivity.class));
            finish();
        });

    }


    /**
     * A function that show answers to the player
     */
    private void showAnswers() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        //On fait en sorte que le text input ne soit plus accessible
        answerPokemonCompetition.setEnabled(false);
        //On clear le text input
        Objects.requireNonNull(answerPokemonCompetition.getText()).clear();
        //On récupère et charge l'image du pokémon en couleur et non tout en noir
        storageReference.child("original_png")
                .child(stringIdPokemon)
                .getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.get().load(uri).into(pokemonDisplayingCompetition));

        //On récupère ici grâce à l'id du pokémon son nom et on l'affiche dans un text view
        FirebaseFirestore.getInstance()
                .collection("Pokemons")
                .document(String.valueOf(idPokemon))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        reponseCompetition.setText(Objects.requireNonNull(Objects.requireNonNull(documentSnapshot).get("frenchName")).toString());
                    }
                });
        //On passe le state à ANSWER
        state = ANSWER;
    }

    /**
     * A function that load a pokemon and permits to play
     */
    private void loadPokemon() {
        Random random = new Random();
        //On fait en sorte que le text input soit bien fonctionnel et qu'il soit bien vide
        answerPokemonCompetition.setEnabled(true);
        answerPokemonCompetition.setText(null);

        //On récupère un nombre random entre 1 et 809
        idPokemon = random.nextInt(NB_POKEMON) + 1;

        //On fait en sorte de récupérer le bon nom pour récupérer l'image (Format : '001.png', '010.png', '100.png')
        if (idPokemon < 10) {
            stringIdPokemon = "00" + idPokemon + ".png";
        } else if (idPokemon < 100) {
            stringIdPokemon = "0" + idPokemon + ".png";
        } else {
            stringIdPokemon = idPokemon + ".png";
        }

        //On récupère l'image en noir et on la charge dans l'image view
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("black_png")
                .child(stringIdPokemon)
                .getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.get().load(uri).into(pokemonDisplayingCompetition));

        //Pour éviter que le cache explose, on fait en sorte qu'il soit vidé à chaque fois
        deleteCache(getApplicationContext());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } else if (dir != null && dir.isFile()) {
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
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        gameMusicHandler.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        gameMusicHandler.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        gameMusicHandler.pause();
    }
}
