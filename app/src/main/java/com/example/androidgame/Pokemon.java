package com.example.androidgame;

import java.util.List;

public class Pokemon {

//    {
//        "id": 1,
//            "name": {
//        "english": "Bulbasaur",
//                "japanese": "フシギダネ",
//                "chinese": "妙蛙种子",
//                "french": "Bulbizarre"
//    },
//        "type": [
//        "Grass",
//                "Poison"
//    ],
//        "base": {
//        "HP": 45,
//                "Attack": 49,
//                "Defense": 49,
//                "Sp. Attack": 65,
//                "Sp. Defense": 65,
//                "Speed": 45
//    }
//    }


    private int id;
    private Name name;
    private List<String> type;
    private Base base;

    public Pokemon(int id, Name name, List<String> type, Base base){
        this.id = id;
        this.name = name;
        this.type = type;
        this.base = base;
    }

    public int getId() {
        return id;
    }

    public Base getBase() {
        return base;
    }

    public List<String> getType() {
        return type;
    }

    public Name getName() {
        return name;
    }
}
