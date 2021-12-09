package com.example.androidgame;

public class User {

    private final String pseudo;
    private final String mail;
    private final String uid;


    public User(String pseudo, String mail, String uid) {
        this.mail = mail;
        this.pseudo = pseudo;
        this.uid = uid;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMail(){
        return mail;
    }

    public String getUid() {
        return uid;
    }

}
