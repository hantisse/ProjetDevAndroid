package com.judith.h.projetdevandroid.editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.DecksDataBaseHelper;
import com.judith.h.projetdevandroid.R;

public class CardActivity extends Activity {

    Card card;
    int multiplicity;
    String deckPart;
    DecksDataBaseHelper handler;
    TextView cardmultiplicityTv;
    boolean hasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        handler = new DecksDataBaseHelper(this);

        Intent intent = getIntent();
        card = (Card) intent.getExtras().get("card");
        multiplicity = (int) intent.getExtras().get("card_multiplicity");

        deckPart = intent.getStringExtra("deck_part");

        String name = card.getName();
        TextView title = findViewById(R.id.cardtitle);
        title.setText(name);

        cardmultiplicityTv = findViewById(R.id.cardmultiplicity);
        Log.i("JH", "tv : " + cardmultiplicityTv.toString());

        cardmultiplicityTv.setText(String.valueOf(multiplicity));

        Button lesscard = findViewById(R.id.lesscard);
        lesscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasChanged){
                    hasChanged = true;
                }
                if (multiplicity > 0){
                    multiplicity -= 1;
                }
                cardmultiplicityTv.setText(String.valueOf(multiplicity));
            }
        });

        Button morecard = findViewById(R.id.morecard);
        morecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasChanged){
                    hasChanged = true;
                }
                multiplicity += 1;
                cardmultiplicityTv.setText(String.valueOf(multiplicity));

            }
        });

        String url = card.getImgUrl();

        AsyncBitmapDownloader task = new AsyncBitmapDownloader(this);
        task.execute(url);
    }

    @Override
    public void onBackPressed(){
        Intent resultIntent = new Intent();
        if(hasChanged){
            resultIntent.putExtra("deck_part", deckPart);
            resultIntent.putExtra("card", card);
            resultIntent.putExtra("card_multiplicity", multiplicity);
            setResult(DeckEditor.CHANGE_CARD_MULT_RESULT_CODE, resultIntent);
        } else {
            setResult(DeckEditor.NO_CHANGE_RESULT_CODE, resultIntent);
        }

        finish();
        super.onBackPressed();
    }
}
