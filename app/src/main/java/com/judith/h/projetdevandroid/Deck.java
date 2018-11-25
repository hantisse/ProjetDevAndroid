package com.judith.h.projetdevandroid;

import java.util.ArrayList;

public class Deck {
    private String deckName;
    private ArrayList<Card> main;
    private ArrayList<Card> side;

    public Deck(String deckName){
        this.deckName = deckName;
    }

    public Deck(String deckName, ArrayList<Card> main, ArrayList<Card> side){
        this.deckName = deckName;
        this.main = main;
        this.side = side;
    }
    public ArrayList<Card> getMain() {
        return main;
    }

    public ArrayList<Card> getSide() {
        return side;
    }

    public void addMain(Card card){
        main.add(card);
    }

    public void addSide(Card card){
        side.add(card);
    }
}
