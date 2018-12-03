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

    public EditorAdapter(FragmentManager fm, Deck deck) {
        super(fm);
        this.deck = deck;
    }

    @Override
    public Fragment getItem(int i) {

        if(i==2){
            Fragment fragment = new StatsFragment();
            Bundle args = new Bundle();
            // Our object is just an integer
            args.putInt(StatsFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
        }
        else if(i==0){
            main = new EditorFragment(deck, "main");
            Log.i("JH", main.toString());
            return main;
        }
        else{
            side = new EditorFragment(deck, "side");
            Log.i("JH", side.toString());
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


    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public EditorFragment getMain() {
        return main;
    }

    public void setMain(EditorFragment main) {
        this.main = main;
    }

    public EditorFragment getSide() {
        return side;
    }

    public void setSide(EditorFragment side) {
        this.side = side;
    }

}

