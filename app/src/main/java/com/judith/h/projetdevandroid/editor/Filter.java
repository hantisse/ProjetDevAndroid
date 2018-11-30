package com.judith.h.projetdevandroid.editor;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.judith.h.projetdevandroid.Card;

import java.util.ArrayList;

public class Filter {
    private String name;

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
        lvAdapter.clear();
        for(Card card : cards){
            lvAdapter.add(card.getName());
        }
    }

    private ArrayList<Card> cards;
    private ArrayAdapter<String> lvAdapter = null;
    private boolean isExpanded = false;

    public Filter(String name){
        this.name = name;
    }

    public Filter(String name, ArrayList<Card> cards) {
        this.name = name;
        this.cards = cards;
    }

    public String getFilterName(){
        return name;
    }

    public ArrayList<String> getFilterCardNames(){
        ArrayList<String> s = new ArrayList<>();
        for (Card card : cards){
            s.add(card.getName());
        }
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

    public String getCardIdByCardName(String name){
        String id = "";
        for(Card card : cards){
            if (card.getName() == name){
                id = String.valueOf(card.getCardId());
                Log.i("JH", id);
            }
        }
        return id;
    }

}
