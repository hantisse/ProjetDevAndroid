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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;


public class DeckEditor extends FragmentActivity {
    public static final int ADD_CARD_REQUEST_CODE = 4;
    public static final int ADD_CARD_RESULT_CODE = 1;
    public static final int CHANGE_CARD_MULT_REQUEST_CODE = 5;
    public static final int CHANGE_CARD_MULT_RESULT_CODE = 2;
    public static final int NO_CHANGE_RESULT_CODE = 3;
    public static final int EDIT_DECK_REQUEST = 20;
    public static final int EDIT_DECK_RESULT = 21;


    private EditorAdapter adapter;
    private Deck deck;
    private DecksDataBaseHelper handler;
    private ArrayList<Card> cardAddedMain;
    private ArrayList<Card> cardAddedSide;
    private DrawerLayout filter_drawer;
    private NavigationView navView;
    private boolean deckSaved = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_editor);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        String deck_name = intent.getStringExtra("deck_name");
        handler = new DecksDataBaseHelper(this);

        //Soit créé un nouveau deck avec le nom entré, soit charge le eck depuis la base de données
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

        //sauvegarde dans la database
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
                Toast.makeText(v.getContext(), getString(R.string.deck_saved), Toast.LENGTH_SHORT).show();
                handler.updateDeckName(deck);
                handler.updateModificationDate(deck);
                setResult(EDIT_DECK_RESULT);
                deckSaved = true;
            }
        });

        Button exportButton = findViewById(R.id.export_deck_button);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDeck();
            }
        });

        //Poour changer le nom du deck
        final TextView deckName = findViewById(R.id.deckName);
        deckName.setText(deck_name);
        deckName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                View promptView = layoutInflater.inflate(R.layout.text_input, null);

                builder.setView(promptView);

                final EditText input = (EditText)promptView.findViewById(R.id.decknameinput);
                input.setText(deck.getDeckName());
                input.setSelection(input.getText().length());

                // Set up the buttons
                builder.setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                      if(!deck.getDeckName().contentEquals(input.getText())){
                          deck.setDeckName(String.valueOf(input.getText()));
                          deckName.setText(deck.getDeckName());
                          deckSaved = false;
                      }
                      dialog.dismiss();
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });

                AlertDialog alertDialog = builder.create();
                Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialog.show();
            }
        });



        filter_drawer = findViewById(R.id.drawer_layout);

        //mise à jour de ma vue lors dde la séléection d'un filtre
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
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        //pour que le pager cache les 2 fragments non visibles
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

    }

    //Récupère les cartes ajoutées soit par l'Ajout depuis une requête API, soit par ajout depuis le détail d'une carte
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == ADD_CARD_RESULT_CODE){
            Bundle bundle = data.getExtras();
            HashMap<Card, Integer> addedCards;
            if((bundle != null)){
                boolean deckModified = false;
                String deckPart = data.getStringExtra("deck_part");
                addedCards = (HashMap<Card, Integer>) bundle.get("added_cards");
                ArrayList<Card> toRemoveFromAddedCards = new ArrayList<>();
                if(deckPart.equals("main")){
                    for(Card card : addedCards.keySet()){
                        boolean inDeck = false;
                        int baseMult = 0;
                        //On vérifie si la carte est déja dans le deck et on récupère la référence de la carte
                        for(Card card1 : deck.getMainMultiplicities().keySet()){
                            if(card1.getCardId() == card.getCardId()){
                                inDeck = true;
                                addedCards.put(card1, addedCards.get(card));
                                toRemoveFromAddedCards.add(card);
                                card = card1;
                            }
                            baseMult = addedCards.get(card);
                        }
                        //si elle n'est pas dans le deck on l'ajoute
                        if (!inDeck) {
                            deckModified = true;
                            deck.getMainMultiplicities().put(card, addedCards.get(card));
                            deck.getMain().add(card);
                            cardAddedMain.add(card);
                            Log.i("JH", "card nor in deck");
                            //sinon, on incrémente la multiplicité de la carte référencée dans le deck
                        } else if(baseMult != 0){
                            deckModified = true;
                            int count = deck.getMainMultiplicities().get(card);
                            deck.getMainMultiplicities().put(card, count + addedCards.get(card));
                            cardAddedMain.add(card);
                        }

                    }
                    if(deckModified) {
                        addedCards.keySet().removeAll(toRemoveFromAddedCards);
                        updateFilterAfterCardAdded(addedCards, "main");
                        adapter.getMain().getmAdapter().notifyDataSetChanged();
                        deckSaved = false;
                    }

                } else if(deckPart.equals("side")){
                    for(Card card : addedCards.keySet()){
                        boolean inDeck = false;
                        int baseMult = 0;
                        for(Card card1 : deck.getSideMultiplicities().keySet()){
                            if(card1.getCardId() == card.getCardId()){
                                inDeck = true;
                                addedCards.put(card1, addedCards.get(card));
                                toRemoveFromAddedCards.add(card);
                                card = card1;
                            }
                            baseMult = addedCards.get(card);
                        }
                        if (!inDeck) {
                            deckModified = true;
                            deck.getSideMultiplicities().put(card, addedCards.get(card));
                            deck.getSide().add(card);
                            cardAddedSide.add(card);
                        } else if(baseMult != 0){
                            deckModified = true;
                            int count = deck.getSideMultiplicities().get(card);
                            deck.getSideMultiplicities().put(card, count + addedCards.get(card));
                            cardAddedSide.add(card);
                        }

                    }
                    if(deckModified) {
                        deckSaved = false;
                        addedCards.keySet().removeAll(toRemoveFromAddedCards);
                        updateFilterAfterCardAdded(addedCards, "side");
                        adapter.getSide().getmAdapter().notifyDataSetChanged();
                    }
                }

            }
        } else if(resultCode == CHANGE_CARD_MULT_RESULT_CODE){
            Bundle bundle = data.getExtras();
            Card card = (Card) bundle.get("card");
            String deckPart = (String) bundle.get("deck_part");
            int multiplicity = (int) bundle.get("card_multiplicity");

            HashMap<Card, Integer> multiplicities;
            ArrayList<Card> deckCards;

            //ajoute / supprime des exemplaires de la carte dans le deck
            if(deckPart != null && card != null) {
                if (deckPart.equals("side")) {
                    multiplicities = deck.getSideMultiplicities();
                    deckCards = deck.getSide();
                } else {
                    multiplicities = deck.getMainMultiplicities();
                    deckCards = deck.getMain();
                }

                for (Card card1 : multiplicities.keySet()) {
                    if (card1.getCardId() == card.getCardId()) {
                        card = card1;
                    }
                }

                if(multiplicity <= 0){
                    deckCards.remove(card);
                }
                if(multiplicities.get(card) != null && multiplicities.get(card) != multiplicity) {

                    multiplicities.put(card, multiplicity);
                    if (deckPart.equals("main")) {
                        cardAddedMain.add(card);
                    } else if (deckPart.equals("side")) {
                        cardAddedSide.add(card);
                    }
                    deckSaved = false;

                    HashMap<Card, Integer> hashmap = new HashMap<>();
                    hashmap.put(card, multiplicities.get(card));

                    updateFilterAfterCardAdded(hashmap, deckPart);

                    if (deckPart.equals("side")) {
                        adapter.getSide().getmAdapter().notifyDataSetChanged();
                    } else {
                        adapter.getMain().getmAdapter().notifyDataSetChanged();
                    }
                }
            }

        }
    }

    //Update le recycler view
    private void updateFilterAfterCardAdded(HashMap<Card, Integer> addedMult, String deckPart){
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

    //export du deck dans un Dossier "MTG DeckBuilder"
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
            Context context = getApplicationContext();
            CharSequence text = "Le deck ne peut pas être exporté, veuillez accorder les droits correspondants à l'application";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    //Affiche un message de confirmation si le deck a été modifié et non sauvegardé
    @Override
    public void onBackPressed(){
        if(!deckSaved) {
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
        } else {
            super.onBackPressed();
        }
    }
}

