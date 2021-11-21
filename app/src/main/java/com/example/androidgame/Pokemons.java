package com.example.androidgame;

import java.util.List;

public class Pokemons {

    private List<Pokemon> pokemons;

    public Pokemons(List<Pokemon> pokemons){
        this.pokemons = pokemons;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }
}
