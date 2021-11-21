package com.example.androidgame;

import com.google.gson.annotations.SerializedName;

public class Base {

    @SerializedName("HP")
    public int hP;
    @SerializedName("Attack")
    public int attack;
    @SerializedName("Defense")
    public int defense;
    @SerializedName("Sp. Attack")
    public int spAttack;
    @SerializedName("Sp. Defense")
    public int spDefense;
    @SerializedName("Speed")
    public int speed;

    public Base(int hP, int attack, int defense, int spAttack, int spDefense, int speed){
        this.hP = hP;
        this.attack = attack;
        this.defense = defense;
        this.spAttack = spAttack;
        this.spDefense = spDefense;
        this.speed = speed;
    }

}
