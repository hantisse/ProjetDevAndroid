package com.judith.h.projetdevandroid.editor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.Deck;
import com.judith.h.projetdevandroid.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AddCardActivity extends Activity implements View.OnClickListener {
    private ArrayList<Card> addedCards;
    private Deck deck;
    private String deckPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            deck = (Deck)intent.getExtras().get("deck_add");
            deckPart = intent.getStringExtra("deck_part");
        }

        addedCards = new ArrayList<>();

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.INTERNET  }, 48 );
        }

        Button addButton = findViewById(R.id.add_card_button_ac);
        addButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        AsyncScryfallJSONSearch task = new AsyncScryfallJSONSearch(this);
        EditText cardSearched = findViewById(R.id.search_bar);
        String url = "https://api.scryfall.com/cards/autocomplete?q=" + cardSearched.getText();
        task.execute(url);
    }

    @Override
    public void onBackPressed(){

        Intent intent = new Intent();
        if(!addedCards.isEmpty()) {
            intent.putExtra("added_cards", addedCards);
            intent.putExtra("deck_part", deckPart);
            setResult(1, intent);
            finish();
        } else{ setResult(0, intent);}

        finish();
        super.onBackPressed();
    }

    public ArrayList<Card> getAddedCards() {
        return addedCards;
    }

    public Deck getDeck(){
        return deck;
    }

}
