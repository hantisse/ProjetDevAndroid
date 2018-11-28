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

import com.judith.h.projetdevandroid.R;

import java.io.Serializable;

public class AddCardActivity extends Activity implements View.OnClickListener {
    AsyncScryfallJSONData task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.INTERNET  }, 48 );
        }

        Button addButton = findViewById(R.id.add_card_button_ac);
        addButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        EditText cardSearched = findViewById(R.id.search_bar);
        String url = "https://api.scryfall.com/cards/named?fuzzy=" + cardSearched.getText();

        task = new AsyncScryfallJSONData(this);
        task.execute(url);
    }

    @Override
    public void onBackPressed(){
        Log.i("JH", "back");
        Intent intent = new Intent();
        intent.putExtra("added_cards", task.getAddedCards());
        Log.i("JH", "bug putextra");
        setResult(1, intent);
        Log.i("JH", "bug setResult");
        finish();
        Log.i("JH", "bug finish");

        super.onBackPressed();
    }
}
