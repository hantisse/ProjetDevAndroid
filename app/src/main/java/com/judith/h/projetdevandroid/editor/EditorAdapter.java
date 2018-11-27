package com.judith.h.projetdevandroid.editor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class EditorAdapter extends FragmentStatePagerAdapter {

    public EditorAdapter(FragmentManager fm) {
        super(fm);
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
            Fragment fragment = new EditorFragment();
            Bundle args = new Bundle();
            // Our object is just an integer
            args.putInt(EditorFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
        }
        else{
            Fragment fragment = new EditorFragment();
            Bundle args = new Bundle();
            // Our object is just an integer
            args.putInt(EditorFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
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
}

