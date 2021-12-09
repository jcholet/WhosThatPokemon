package com.example.androidgame;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import androidx.appcompat.app.AppCompatActivity;

public class GameMusicHandler {

    private final AppCompatActivity appCompatActivity;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int idButton;


    /** Constructor de la classe de gestion de musique/effets sonores
     * @param appCompatActivity permet de récupérer le context pour le soundPool
     */
    public GameMusicHandler(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
        build();
        mediaPlayer = new MediaPlayer();
    }

    /**
     * A function that build a soundpool to manage sonor effects (Button pressed)
     */
    private void build(){
        SoundPool.Builder soundPoolBuilder = new SoundPool.Builder();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                                .setUsage(AudioAttributes.USAGE_GAME)
                                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                                .build();
        soundPoolBuilder.setAudioAttributes(audioAttributes).setMaxStreams(5);
        soundPool = soundPoolBuilder.build();

        idButton = soundPool.load(appCompatActivity ,R.raw.button_effect, 1);
    }

    /**
     * A function that play the rowan theme (used in Rowan Activity)
     */
    public void playRowanTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.rowan_theme);
    }

    /**
     * A function that play the home theme (used in Home Activity)
     */
    public void playHomeTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.home_theme);
    }

    /**
     * A function that play the training theme (used in Training Activity)
     */
    public void playTrainingTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.training_theme);
    }

    /**
     * A function that play the competition theme (used in Competition Activity)
     */
    public void playCompetitionTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.competition_theme);
    }

    /**
     * A function that play the scoreboard theme (used in Scoreboard Activity)
     */
    public void playScoreboardTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.scoreboard_theme_new);
    }

    /**
     * A function that play the lobby theme (used in Lobby Activity)
     */
    public void playLobbyTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.lobby_theme);
    }

    /**
     * A function that play the sign in theme (used in Login Activity)
     */
    public void playSignInTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.sign_in_theme);
    }

    /**
     * A function that play the sign up theme (used in Sign Up Activity)
     */
    public void playSignUpTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.sign_up_theme);
    }

    /**
     * A function that play the pressing sound that we have created in build()
     */
    public void pressingButton(){
        soundPool.play(idButton, 0.5f, 0.5f, 1, 0, 1f);
    }


    /**
     * A function that permits to play a music using its id in raw file
     * @param id correspond to the id of a file in raw (R.raw.id)
     */
    private void play(int id){
        mediaPlayer = MediaPlayer.create(appCompatActivity.getApplicationContext(), id);
        mediaPlayer.setVolume(0.25f, 0.25f);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    /**
     * A function that permit to pause the music
     */
    public void pause(){
        mediaPlayer.pause();
    }


    /**
     * A function that permit to start the music
     */
    public void start(){
        mediaPlayer.start();
    }
}
