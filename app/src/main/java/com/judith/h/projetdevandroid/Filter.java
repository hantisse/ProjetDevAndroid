package com.judith.h.projetdevandroid;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private String name;
    private String[] cardList;
    private boolean isExpanded = false;

    public Filter(String name, String[] cards) {
        this.name = name;
        this.cardList = cards;
    }


    public String[] getFilterCardList(){
        return cardList;
    }


    public String getFilterName(){
        return name;
    }

    public boolean isExpanded(){ return isExpanded; }

    public void setExpanded(boolean b){ isExpanded = b ; }
}
