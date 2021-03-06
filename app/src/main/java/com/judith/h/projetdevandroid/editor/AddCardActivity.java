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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.Deck;
import com.judith.h.projetdevandroid.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddCardActivity extends Activity implements View.OnClickListener {
    private HashMap<Card, Integer> addedCards;
    private Deck deck;
    private String deckPart;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            deck = (Deck)intent.getExtras().get("deck_add");
            deckPart = intent.getStringExtra("deck_part");
        }

        addedCards = new HashMap<>();

        Button addButton = findViewById(R.id.add_card_button_ac);
        addButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        AsyncScryfallJSONSearch task = new AsyncScryfallJSONSearch(this);
        EditText cardSearched = findViewById(R.id.search_bar);
        String url = "https://api.scryfall.com/cards/autocomplete?q=" + cardSearched.getText();
        task.execute(url);
        hideKeyboard(this);
    }

    @Override
    public void onBackPressed(){

        //Envoie le résultat à l'activité DeckEditor
        Intent intent = new Intent();
        if(!addedCards.isEmpty()) {
            intent.putExtra("added_cards", addedCards);
            intent.putExtra("deck_part", deckPart);
            setResult(DeckEditor.ADD_CARD_RESULT_CODE, intent);
            finish();
        } else{ setResult(0, intent);}

        finish();
        super.onBackPressed();
    }

    public HashMap<Card, Integer> getAddedCards() {
        return addedCards;
    }

    public Deck getDeck(){
        return deck;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView lv){
        listView = lv;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
