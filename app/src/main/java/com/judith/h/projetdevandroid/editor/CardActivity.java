package com.judith.h.projetdevandroid.editor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        String url = card.getImgUrl();

        AsyncBitmapDownloader task = new AsyncBitmapDownloader(this);
        task.execute(url);
    }
}
