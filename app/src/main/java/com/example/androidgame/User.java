package com.example.androidgame;

import java.util.ArrayList;

public class User {

    private String pseudo, mail;
    private String uid;


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
