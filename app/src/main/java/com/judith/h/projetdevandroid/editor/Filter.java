package com.judith.h.projetdevandroid.editor;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.judith.h.projetdevandroid.Card;

import java.util.ArrayList;

public class Filter {
    private String name;
    private ArrayList<Card> cards;
    private ArrayAdapter<String> lvAdapter = null;
    private boolean isExpanded = true;

    public Filter(String name){
        this.name = name;
        cards = new ArrayList<>();
    }

    public String getFilterName(){
        return name;
    }

    public ArrayList<String> getFilterCardNames(){
        ArrayList<String> s = new ArrayList<>();
        return s;
    }

    public boolean isExpanded(){ return isExpanded; }
    public void setExpanded(boolean b){ isExpanded = b ; }

    public ArrayAdapter<String> getLvAdapter() {
        return lvAdapter;
    }

    public void setLvAdapter(ArrayAdapter<String> adapter){
        lvAdapter = adapter;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Neceite que l'adapteur doit défini
     * @param cards cartes dans le filtre
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void setCardsInAdapter(){
        if(lvAdapter != null){
            lvAdapter.clear();
            for(Card card : cards){
                lvAdapter.add(card.getName());
            }
        }
    }
    public String getCardIdByCardName(String name){
        String id = "";
        for(Card card : cards){
            if (card.getName() == name){
                id = String.valueOf(card.getCardId());
            }
        }
        return id;
    }

    public void addCard(Card card){
        cards.add(card);
        if(lvAdapter != null) {
            lvAdapter.add(card.getName());
        }
    }

    public void clearCards(){
        cards.clear();
        if(lvAdapter != null){
            lvAdapter.clear();
        }
    }

}
