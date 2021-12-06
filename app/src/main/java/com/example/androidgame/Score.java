package com.example.androidgame;

public class Score implements Comparable{

    private int score;
    private String uid;

    public Score(int score, String uid){
        this.score = score;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Object o) {
        Score score = (Score) o;
        return Integer.compare(this.score, score.getScore());
    }
}
