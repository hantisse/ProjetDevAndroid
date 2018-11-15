package com.judith.h.projetdevandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DeckEditor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_editor);

        Intent intent = getIntent();
        String deck_name = intent.getStringExtra("deck_name");

        TextView deckName = (TextView)findViewById(R.id.deckName);
        deckName.setText(deck_name);
    }
}
