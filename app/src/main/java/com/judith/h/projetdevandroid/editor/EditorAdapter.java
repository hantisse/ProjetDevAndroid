package com.judith.h.projetdevandroid.editor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.judith.h.projetdevandroid.Deck;


public class EditorAdapter extends FragmentStatePagerAdapter {
    private Deck deck;

    private EditorFragment main;
    private EditorFragment side;
    private StatsFragment statFragment;

    public EditorAdapter(FragmentManager fm, Deck deck) {
        super(fm);
        this.deck = deck;
    }

    @Override
    public Fragment getItem(int i) {

        if(i==2){
            statFragment = new StatsFragment(deck);
            return statFragment;
        }
        else if(i==0){
            main = new EditorFragment(deck, "main");
            return main;
        }
        else{
            side = new EditorFragment(deck, "side");
            return side;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if(position==0){
            title = "Main";
        } else if(position==1){
            title = "Side";
        } else {
            title = "Stats";
        }

        return title;
    }

    public void calculateCMCFilters(){
        side.calculateCMCFilters(deck);
        main.calculateCMCFilters(deck);
    }

    public void calculateTypeFilters(){
        side.calculateTypeFilters(deck);
        main.calculateTypeFilters(deck);
    }

    public void calculateColorIdentityFilters(){
        side.calculateColorIdentityFilters(deck);
        main.calculateColorIdentityFilters(deck);
    }

    public void setDefaultFilter(){
        side.setDefaultFilter();
        main.setDefaultFilter();
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public EditorFragment getMain() {
        return main;
    }

    public EditorFragment getSide() {
        return side;
    }

    public StatsFragment getStatFragment(){
        return statFragment;
    }

}

