package com.judith.h.projetdevandroid.editor;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.Deck;

import java.util.ArrayList;
import java.util.HashMap;

public class Filter {
    private String name;
    private ArrayList<Card> cards;
    private ArrayAdapter<String> lvAdapter = null;
    private int filterSize = 0;
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

    public void setCardsInAdapter(HashMap<Card, Integer> multiplicities){
        if(lvAdapter != null){
            lvAdapter.clear();
            filterSize = 0;
            for(Card card : cards){
                lvAdapter.add(card.getName() + " x " + multiplicities.get(card) );
                filterSize += multiplicities.get(card);
            }
        }
    }
    public Card getCardByCardName(String name){
        Card card = null;
        for(Card c : cards){
            if (c.getName().equals(name.split(" x ")[0])){
                card = c;
            }
        }
        return card;
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

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    public int getFilterSize(){
        return filterSize;
    }

}
