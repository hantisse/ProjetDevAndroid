package com.judith.h.projetdevandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.judith.h.projetdevandroid.editor.DeckEditor;

public class DeckListActivity extends Activity {

    private RecyclerView deckList;
    private RecyclerView.Adapter deckAdapter;
    private RecyclerView.LayoutManager deckLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deck_list);


        deckList = (RecyclerView) findViewById(R.id.deckList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        deckList.setHasFixedSize(true);

        // use a linear layout manager
        deckLayoutManager = new LinearLayoutManager(this);
        deckList.setLayoutManager(deckLayoutManager);

        // specify an adapter (see also next example)
        deckAdapter = new DeckAdapter(new String[]{"Judith", "est","une", "super", "patate"});
        deckList.setAdapter(deckAdapter);

        Button newDeck_button = (Button)findViewById(R.id.newDeckButton2);

        newDeck_button.setOnClickListener(new View.OnClickListener() {
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
                        startActivity(intent);



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
        });

    }
}
