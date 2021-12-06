package com.example.androidgame;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import androidx.appcompat.app.AppCompatActivity;

public class GameMusicHandler {

    private AppCompatActivity appCompatActivity;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int idButton;

    public GameMusicHandler(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
        build();
        mediaPlayer = new MediaPlayer();
    }

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

    public void playHomeTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.home_theme);
    }

    public void playTrainingTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.training_theme);
    }

    public void playCompetitionTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.competition_theme);
    }

    public void playScoreboardTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.scoreboard_theme_new);
    }

    public void playLobbyTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.lobby_theme);
    }

    public void playSignInTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.sign_in_theme);
    }

    public void playSignUpTheme(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        play(R.raw.sign_up_theme);
    }

    public void pressingButton(){
        int id = soundPool.play(idButton, 0.5f, 0.5f, 1, 0, 1f);
    }



    private void play(int id){
        mediaPlayer = MediaPlayer.create(appCompatActivity.getApplicationContext(), id);
        mediaPlayer.setVolume(0.25f, 0.25f);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void start(){
        mediaPlayer.start();
    }
}
