package com.judith.h.projetdevandroid.editor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.Deck;
import com.judith.h.projetdevandroid.DecksDataBaseHelper;
import com.judith.h.projetdevandroid.R;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class DeckEditor extends FragmentActivity {
    public static final int ADD_CARD_REQUEST_CODE = 4;
    public static final int ADD_CARD_RESULT_CODE = 1;
    public static final int CHANGE_CARD_MULT_REQUEST_CODE = 5;
    public static final int CHANGE_CARD_MULT_RESULT_CODE = 2;
    public static final int NO_CHANGE_RESULT_CODE = 3;
    public static final int EDIT_DECK_REQUEST = 20;
    public static final int EDIT_DECK_RESULT = 21;


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
                setResult(EDIT_DECK_RESULT);
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
                handler.updateModificationDate(deck);
                setResult(EDIT_DECK_RESULT);
            }
        });

        Button exportButton = findViewById(R.id.export_deck_button);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDeck();
            }
        });

        deckName.setText(deck_name);


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
            HashMap<Card, Integer> addedCards;
            if((bundle != null)){
                String deckPart = data.getStringExtra("deck_part");
                addedCards = (HashMap<Card, Integer>) bundle.get("added_cards");
                if(deckPart.equals("main")){
                    for(Card card : addedCards.keySet()){
                        boolean inDeck = false;
                        //On vérifie si la carte est déja dans le deck et on récupère l référence de la carte
                        for(Card card1 : deck.getMainMultiplicities().keySet()){
                            if(card1.getCardId() == card.getCardId()){
                                inDeck = true;
                                addedCards.put(card1, addedCards.get(card));
                                addedCards.remove(card);
                                card = card1;
                            }
                        }
                        //si elle n'est pas dans le deck on l'ajoute
                        if(!inDeck){
                            deck.getMainMultiplicities().put(card,addedCards.get(card));
                            deck.getMain().add(card);
                            cardAddedMain.add(card);
                            Log.i("JH", "card nor in deck");
                        //sinon, on incrémente la multiplicité de la carte référencée dans le deck
                        } else {
                            int count = deck.getMainMultiplicities().get(card);
                            deck.getMainMultiplicities().put(card, count + addedCards.get(card));
                            cardAddedMain.add(card);
                        }
                    }
                    updateFilterAfterCardAdded(addedCards, "main");
                    adapter.getMain().getmAdapter().notifyDataSetChanged();
                } else if(deckPart.equals("side")){
                    for(Card card : addedCards.keySet()){
                        boolean inDeck = false;
                        for(Card card1 : deck.getSideMultiplicities().keySet()){
                            if(card1.getCardId() == card.getCardId()){
                                inDeck = true;
                                addedCards.put(card1, addedCards.get(card));
                                addedCards.remove(card);
                                card = card1;
                            }
                        }
                        if(!inDeck){
                            deck.getSideMultiplicities().put(card,addedCards.get(card));
                            deck.getSide().add(card);
                            cardAddedSide.add(card);
                        } else {
                            int count = deck.getSideMultiplicities().get(card);
                            deck.getSideMultiplicities().put(card, count + addedCards.get(card));
                            cardAddedSide.add(card);
                        }
                    }
                    updateFilterAfterCardAdded(addedCards, "side");
                    adapter.getSide().getmAdapter().notifyDataSetChanged();
                }

            }
        } else if(resultCode == CHANGE_CARD_MULT_RESULT_CODE){
            Bundle bundle = data.getExtras();
            Card card = (Card) bundle.get("card");
            String deckPart = (String) bundle.get("deck_part");
            int multiplicity = (int) bundle.get("card_multiplicity");
            Log.i("JH", "nouvelle mult : " + multiplicity);
            boolean inDeck = false;

            HashMap<Card, Integer> multiplicities;
            ArrayList<Card> deckCards;
            if(bundle.get("deck_part").equals("side")){
                multiplicities = deck.getSideMultiplicities();
                deckCards = deck.getSide();
            } else {
                multiplicities = deck.getMainMultiplicities();
                deckCards = deck.getMain();
            }
            for(Card card1 : multiplicities.keySet()){
                if(card1.getCardId() == card.getCardId()){
                    inDeck = true;
                    card = card1;
                }
            }
            if(!inDeck){
                deckCards.add(card);
            }
            multiplicities.put(card, multiplicity);
            if(deckPart.equals("main")){
                cardAddedMain.add(card);
            } else if(deckPart.equals("side")){
                cardAddedSide.add(card);
            }

            HashMap<Card, Integer> hashmap = new HashMap<>();
            hashmap.put(card, multiplicities.get(card));
            updateFilterAfterCardAdded(hashmap, deckPart);
            if(deckPart.equals("side")){
                adapter.getSide().getmAdapter().notifyDataSetChanged();
            } else {
                adapter.getMain().getmAdapter().notifyDataSetChanged();
            }

        }
    }

    private void updateFilterAfterCardAdded(HashMap addedMult, String deckPart){
        EditorFragment fragment;
        if(deckPart.equals("side")){
            fragment = adapter.getSide();
        } else {
            fragment = adapter.getMain();
        }
        int selectedItemId = -1;
        if(navView.getCheckedItem() != null ){
            selectedItemId = navView.getCheckedItem().getItemId();
        }
        switch(selectedItemId){
            case R.id.nav_filter_cmc :
                fragment.updateFilterAfterCardsAdded(addedMult.keySet(), "cmc");
                break;
            case R.id.nav_filter_type :
                fragment.updateFilterAfterCardsAdded(addedMult.keySet(), "type");
                break;
            case R.id.nav_filter_color :
                fragment.updateFilterAfterCardsAdded(addedMult.keySet(), "colorIdentity");
                break;
            default:
                fragment.setDefaultFilter();
                break;
        }
        adapter.getStatFragment().updateManaCurveTv();
        adapter.getStatFragment().updateDeckPriceTv();
    }

    public void exportDeck(){
        String filename = deck.getDeckName() + ".txt";
        ArrayList<Card> main = deck.getMain();
        ArrayList<Card> side = deck.getSide();
        try {
            String dirName = "MTG DeckBuilder";
            File directory = new File(Environment.getExternalStorageDirectory(), dirName);

            /*if directory doesn't exist, create it*/
            if(!directory.exists())
                directory.mkdirs();

            File file = new File(directory , filename);
            Log.i("CARIBOU", directory.getAbsolutePath());
            FileWriter fileWriter = new FileWriter(file, true);
            for (Card card:main) {
                fileWriter.write("" + deck.getMainMultiplicities().get(card) + " " + card.getName());
                fileWriter.write( "\n");
            }
            fileWriter.write("Sideboard");
            fileWriter.write( "\n");
            for (Card card:side) {
                fileWriter.write("" + deck.getSideMultiplicities().get(card) + " " + card.getName());
                fileWriter.write( "\n");
            }
            fileWriter.flush();
            fileWriter.close();
            Context context = getApplicationContext();
            CharSequence text = "Deck exporté !";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } catch (java.io.IOException e){
            Log.d("CARIBOU", Log.getStackTraceString(e));
            Context context = getApplicationContext();
            CharSequence text = "Le deck ne peut pas être exporté, veuillez accorder les droits correspondants à l'application";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.quit_alert, null);
        builder.setView(promptView);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
                DeckEditor.super.onBackPressed();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

