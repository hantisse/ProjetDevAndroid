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
    NavigationView navView;

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
        }

        filter_drawer = findViewById(R.id.drawer_layout);

        navView = (NavigationView)findViewById(R.id.nav_filter);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        switch(menuItem.getItemId()){
                            case R.id.nav_filter_cmc :
                                adapter.calculateCMCFilters();
                                break;
                            case R.id.nav_filter_type :
                                adapter.calculateTypeFilters();
                                break;
                            case R.id.nav_filter_color :
                                adapter.calculateColorIdentityFilters();
                                break;
                            case R.id.nav_filter_default :
                                adapter.setDefaultFilter();
                                break;
                        }

                        filter_drawer.closeDrawers();
                        adapter.getMain().getmAdapter().notifyDataSetChanged();
                        adapter.getSide().getmAdapter().notifyDataSetChanged();
                        return true;
                    }
                });

        adapter = new EditorAdapter(getSupportFragmentManager(), deck);
        pager = (ViewPager)findViewById(R.id.pager);
        //pour que le pager cache les 2 fragments non visibles
        pager.setOffscreenPageLimit(2);
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
                    int selectedItemId = -1;
                    if(navView.getCheckedItem() != null ){
                        navView.getCheckedItem().getItemId();
                    }
                    switch(selectedItemId){
                        case R.id.nav_filter_cmc :
                            adapter.getMain().updateFilterAfterCardsAdded(deckCards.keySet(), "cmc");
                            break;
                        case R.id.nav_filter_type :
                            adapter.getMain().updateFilterAfterCardsAdded(deckCards.keySet(), "type");
                            break;
                        case R.id.nav_filter_color :
                            adapter.getMain().updateFilterAfterCardsAdded(deckCards.keySet(), "colorIdentity");
                            break;
                        default:
                            Log.i("JH", "pas de filtre sélzctionné");
                            break;
                    }
                    adapter.getMain().getmAdapter().notifyDataSetChanged();
                } else if(deckPart.equals("side")){
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
                    int selectedItemId = -1;
                    if(navView.getCheckedItem() != null ){
                        navView.getCheckedItem().getItemId();
                    }
                    switch(selectedItemId){
                        case R.id.nav_filter_cmc :
                            adapter.getSide().updateFilterAfterCardsAdded(deckCards.keySet(), "cmc");
                            break;
                        case R.id.nav_filter_type :
                            adapter.getSide().updateFilterAfterCardsAdded(deckCards.keySet(), "type");
                            break;
                        case R.id.nav_filter_color :
                            adapter.getSide().updateFilterAfterCardsAdded(deckCards.keySet(), "colorIdentity");
                            break;
                        default:
                            Log.i("JH", "pas de filtre sélectionné");
                            break;
                    }
                    adapter.getSide().getmAdapter().notifyDataSetChanged();
                }

            }
        }
    }
}

