package com.judith.h.projetdevandroid;

import android.widget.ListView;

import java.util.List;

public class Filter {
    private String name;
    private List<String> cardList;
    private ListView listView;
    private boolean isExpanded;

    public Filter(String title, List<String> cards, ListView listView) {
        this.name = name;
        this.cardList = cards;
        this.listView = listView;

    }

    public List<String> getFilterCardList(){
        return cardList;
    }

    public String getFilterName(){
        return name;
    }
}
