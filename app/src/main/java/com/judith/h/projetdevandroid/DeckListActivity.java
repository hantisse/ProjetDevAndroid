package com.judith.h.projetdevandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.judith.h.projetdevandroid.editor.DeckEditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class DeckListActivity extends Activity implements View.OnClickListener {

    private RecyclerView deckList;
    private RecyclerView.Adapter deckAdapter;
    private RecyclerView.LayoutManager deckLayoutManager;
    private ArrayList<Deck> decks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        deckList = (RecyclerView) findViewById(R.id.deckList);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        deckList.addItemDecoration(decoration);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        deckList.setHasFixedSize(true);

        // use a linear layout manager
        deckLayoutManager = new LinearLayoutManager(this);
        deckList.setLayoutManager(deckLayoutManager);

        DecksDataBaseHelper handler = new DecksDataBaseHelper(this);
        decks = (ArrayList<Deck>) handler.getAllDecks();
        // Sorting
        Collections.sort(decks, new Comparator<Deck>() {
            @Override
            public int compare(Deck deck2, Deck deck1)
            {
                return  deck1.getCreationDate().compareTo(deck2.getCreationDate());
            }
        });

        deckAdapter = new DeckRecyclerAdapter(decks, this);
        deckList.setAdapter(deckAdapter);

        Button newDeck_button = (Button)findViewById(R.id.newDeckButton2);

        newDeck_button.setOnClickListener(this);

    }

    @Override
    public void onClick(final View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
        View promptView = layoutInflater.inflate(R.layout.text_input, null);

        builder.setView(promptView);

        final EditText input = (EditText)promptView.findViewById(R.id.decknameinput);

        // Set up the buttons
        builder.setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
                String deckName = input.getText().toString();
                Intent intent = new Intent(v.getContext(),DeckEditor.class);
                intent.putExtra("deck_name", deckName);
                startActivityForResult(intent, DeckEditor.EDIT_DECK_REQUEST);


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(resultCode){
            case DeckEditor.EDIT_DECK_RESULT:
                recreate();
                break;
        }
    }
}
