package com.judith.h.projetdevandroid.editor;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.Deck;
import com.judith.h.projetdevandroid.DecksDataBaseHelper;
import com.judith.h.projetdevandroid.R;

import java.util.ArrayList;
import java.util.HashMap;


public class DeckEditor extends FragmentActivity implements EditorFragment.OnEditorFragmentUpdatedListener {

    EditorAdapter adapter;
    ViewPager pager;
    Deck deck;
    DecksDataBaseHelper handler;

    private DrawerLayout filter_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_editor);

        Intent intent = getIntent();
        String deck_name = intent.getStringExtra("deck_name");
        deck = new Deck(deck_name);

        handler = new DecksDataBaseHelper(this);
        handler.createDeck(deck);

        TextView deckName = (TextView)findViewById(R.id.deckName);
        deckName.setText(deck_name);


        filter_drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        NavigationView navView = (NavigationView)findViewById(R.id.nav_filter);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        filter_drawer.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        adapter = new EditorAdapter(getSupportFragmentManager(), deck);
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);



    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof EditorFragment) {
            EditorFragment editorFragment = (EditorFragment) fragment;
            editorFragment.setOnEditorFragmentUpdatedListener(this);
            Log.i("JH", "attach fragment");
        }
    }

    @Override
    public void onCardAdded() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == 1){
            Bundle bundle = data.getExtras();
            ArrayList<String> addedCards;
            if(bundle != null){
                addedCards = (ArrayList<String>) bundle.get("added_cards");
                Log.i("JH", "added_card OK" + addedCards);

                for(String card : addedCards){

                    adapter.getMain().getmAdapter().notifyDataSetChanged();

                }

            }




        }

    }
}

