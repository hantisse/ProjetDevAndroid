package com.judith.h.projetdevandroid.editor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Intent intent = getIntent();
        String cardId = intent.getStringExtra("card_id");
        Log.i("CARIBOU", "****" + cardId);

        DecksDataBaseHelper dataBaseHelper = new DecksDataBaseHelper(this);
        card = dataBaseHelper.getCard(Long.parseLong(cardId));

        String name = card.getName();
        TextView title = findViewById(R.id.cardtitle);
        title.setText(name);

        final TextView cardmultiplicity = findViewById(R.id.cardmultiplicity);
        cardmultiplicity.setText("0");

        Button lesscard = findViewById(R.id.lesscard);
        lesscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int multiplicity = Integer.parseInt(cardmultiplicity.getText().toString());
                if (multiplicity != 0){
                    multiplicity = multiplicity - 1;
                }
                cardmultiplicity.setText(Integer.toString(multiplicity));
            }
        });

        Button morecard = findViewById(R.id.morecard);
        morecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int multiplicity = Integer.parseInt(cardmultiplicity.getText().toString()) + 1;
                cardmultiplicity.setText(Integer.toString(multiplicity));
            }
        });

        String url = card.getImgUrl();

        AsyncBitmapDownloader task = new AsyncBitmapDownloader(this);
        task.execute(url);
    }
}
