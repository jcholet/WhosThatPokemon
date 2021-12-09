package com.example.androidgame;

public class Score implements Comparable{

    //Le score contient le score et l'user id de l'utilisateur

    private final int score;
    private final String uid;

    //Constructeur de la classe Score
    public Score(int score, String uid){
        this.score = score;
        this.uid = uid;
    }

    //Getters
    public String getUid() {
        return uid;
    }

    public int getScore() {
        return score;
    }

    //On implemente 'Comparable' pour pouvoir trier la liste dans CompetitionActivity
    /**
     * A function that permit to compare two score between them
     * @param o An other score
     * @return 0 if the two score are equals, value less than 0 if this.score < score.getScore() and value greater than zero if this.score > score.getScore()
     */
    @Override
    public int compareTo(Object o) {
        Score score = (Score) o;
        return Integer.compare(this.score, score.getScore());
    }
}
