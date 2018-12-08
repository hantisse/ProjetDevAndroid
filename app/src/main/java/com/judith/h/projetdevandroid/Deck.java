package com.judith.h.projetdevandroid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Deck implements Serializable {
    //TODO supprimer les arraylists:  inutile;
    private int DeckId;
    private String deckName;
    private ArrayList<Card> main;
    private ArrayList<Card> side;
    private HashMap<Card,Integer> mainMultiplicities;
    private HashMap<Card,Integer> sideMultiplicities;

    public Deck(){ deckName = "sans_nom";}

    public Deck(String deckName){
        this.deckName = deckName;
        main = new ArrayList<>();
        side = new ArrayList<>();
        mainMultiplicities = new HashMap<>();
        sideMultiplicities = new HashMap<>();
    }

    public Deck(String deckName, ArrayList<Card> main, ArrayList<Card> side){
        this.deckName = deckName;
        this.main = main;
        this.side = side;
        mainMultiplicities = new HashMap<>();
        sideMultiplicities = new HashMap<>();
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

    public String getDeckName(){
        return deckName;
    }

    public void setDeckName(String name){
        deckName = name;
    }

    public int getDeckId() {
        return DeckId;
    }

    public void setDeckId(int deckId) {
        DeckId = deckId;
    }

    public HashMap<Card, Integer> getMainMultiplicities() {
        return mainMultiplicities;
    }

    public void setMainMultiplicities(HashMap<Card, Integer> mainMultiplicities) {
        this.mainMultiplicities = mainMultiplicities;
    }

    public void setMain(ArrayList<Card> main) {
        this.main = main;
    }

    public void setSide(ArrayList<Card> side) {
        this.side = side;
    }

    public HashMap<Card, Integer> getSideMultiplicities() {
        return sideMultiplicities;
    }

    public void setSideMultiplicities(HashMap<Card, Integer> sideMultiplicities) {
        this.sideMultiplicities = sideMultiplicities;
    }
}
