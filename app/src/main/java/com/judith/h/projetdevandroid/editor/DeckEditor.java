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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.Deck;
import com.judith.h.projetdevandroid.DecksDataBaseHelper;
import com.judith.h.projetdevandroid.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class DeckEditor extends FragmentActivity {
    public static final int ADD_CARD_REQUEST_CODE = 4;
    public static final int ADD_CARD_RESULT_CODE = 1;

    EditorAdapter adapter;
    ViewPager pager;
    Deck deck;
    DecksDataBaseHelper handler;
    ArrayList<Card> cardAddedMain;
    ArrayList<Card> cardAddedSide;
    private DrawerLayout filter_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_editor);
        Intent intent = getIntent();
        String deck_name = intent.getStringExtra("deck_name");
        handler = new DecksDataBaseHelper(this);
        if(intent.getExtras() != null){
            try{
                deck = (Deck)intent.getExtras().get("deck");
                deck_name = deck.getDeckName();

            } catch (NullPointerException e){
                deck = new Deck(deck_name);
                handler.createDeck(deck);
            }
        }

        cardAddedMain = new ArrayList<>();
        cardAddedSide = new ArrayList<>();

        TextView deckName = findViewById(R.id.deckName);
        Button saveButton = findViewById(R.id.save_deck_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Card card : cardAddedMain){
                    handler.addCardInDeck(deck, card,deck.getMainMultiplicities().get(card), "main");
                }
                for (Card card : cardAddedSide){
                    handler.addCardInDeck(deck, card, deck.getSideMultiplicities().get(card), "side");
                }
            }
        });

        deckName.setText(deck_name);

        for(Card c : deck.getMain()){
            Log.i("JH", "DeckEditor : "  + deck.getMain().size());
        }




        filter_drawer = findViewById(R.id.drawer_layout);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == ADD_CARD_RESULT_CODE){
            Bundle bundle = data.getExtras();
            HashMap<Card, Integer> deckCards;
            if((bundle != null)){
                String deckPart = data.getStringExtra("deck_part");
                deckCards = (HashMap<Card, Integer>) bundle.get("added_cards");
                if(deckPart.equals("main")){
                    for(Card card : deckCards.keySet()){
                        Card cardInDeck = null;
                        boolean inDeck = false;
                        for(Card card1 : deck.getMainMultiplicities().keySet()){
                            if(card1.getCardId() == card.getCardId()){
                                inDeck = true;
                                cardInDeck = card1;
                            }
                        }
                        if(!inDeck){
                            deck.getMainMultiplicities().put(card,deckCards.get(card));
                            deck.getMain().add(card);
                            cardAddedMain.add(card);
                        } else {
                            int count = deck.getMainMultiplicities().get(cardInDeck);
                            deck.getMainMultiplicities().put(cardInDeck, count + deckCards.get(card));
                            cardAddedMain.add(cardInDeck);
                        }
                    }
                    adapter.getMain().getmAdapter().notifyDataSetChanged();
                } else if(deckPart.equals("side")){
                    Log.i("JH", "ajout dans le side");

                    for(Card card : deckCards.keySet()){
                        Card cardInDeck = null;
                        boolean inDeck = false;
                        for(Card card1 : deck.getSideMultiplicities().keySet()){
                            if(card1.getCardId() == card.getCardId()){
                                inDeck = true;
                                cardInDeck = card1;
                            }
                        }
                        if(!inDeck){
                            deck.getSideMultiplicities().put(card,deckCards.get(card));
                            deck.getSide().add(card);
                            cardAddedSide.add(card);
                        } else {
                            int count = deck.getSideMultiplicities().get(cardInDeck);
                            deck.getSideMultiplicities().put(cardInDeck, count + deckCards.get(card));
                            cardAddedSide.add(cardInDeck);
                        }
                    }
                    adapter.getSide().getmAdapter().notifyDataSetChanged();
                }

            }
        }
    }
}

