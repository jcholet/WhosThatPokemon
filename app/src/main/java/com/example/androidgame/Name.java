package com.example.androidgame;

import com.google.gson.annotations.SerializedName;

public class Name {

    @SerializedName("english")
    private String englishName;
    @SerializedName("japanese")
    private String japaneseName;
    @SerializedName("chinese")
    private String chineseName;
    @SerializedName("french")
    private String frenchName

    public Name(String englishName, String japaneseName, String chineseName, String frenchName){
        this.englishName = englishName;
        this.japaneseName = japaneseName;
        this.chineseName = chineseName;
        this.frenchName = frenchName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getJapaneseName() {
        return japaneseName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getFrenchName() {
        return frenchName;
    }
}
