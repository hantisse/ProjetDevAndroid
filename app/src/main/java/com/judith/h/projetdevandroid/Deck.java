package com.judith.h.projetdevandroid;

import java.util.ArrayList;
import java.util.HashMap;

public class Deck {
    private String deckName;
    private ArrayList<Card> main;
    private ArrayList<Card> side;
    private HashMap<Card,Integer> cardMultiplicities;

    public Deck(String deckName){
        this.deckName = deckName;
    }

    public Deck(String deckName, ArrayList<Card> main, ArrayList<Card> side){
        this.deckName = deckName;
        this.main = main;
        this.side = side;
        cardMultiplicities = new HashMap<>();
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
