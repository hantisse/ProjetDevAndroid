package com.judith.h.projetdevandroid;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class Filter {
    private String name;
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
//
//        for(Card card : cards){
//            lvAdapter.add(card.getName());
//        }
    }

}
