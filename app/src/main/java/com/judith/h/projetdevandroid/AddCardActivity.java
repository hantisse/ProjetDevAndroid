package com.judith.h.projetdevandroid;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCardActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ){
            Log.i("JH", "Access to coarse location not granted");
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.INTERNET  }, 48 );
        }

        Button addButton = findViewById(R.id.add_card_button_ac);

        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText cardSearched = findViewById(R.id.search_bar);
        String url = "https://api.scryfall.com/cards/named?fuzzy=" + cardSearched.getText();

        AsyncScryfallJSONData task = new AsyncScryfallJSONData(this);
        task.execute(url);
    }
}
