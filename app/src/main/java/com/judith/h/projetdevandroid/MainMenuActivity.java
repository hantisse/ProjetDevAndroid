package com.judith.h.projetdevandroid;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.judith.h.projetdevandroid.editor.DeckEditor;

public class MainMenuActivity extends Activity {
    public static final int OPT_CHANGE_LANG_REQUEST_CODE = 10;
    public static final int OPT_CHANGE_LANG_RESULT_CODE = 11;
    public static final int NO_LANG_CHANGE = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.INTERNET  }, 48 );
        }

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE  }, 48 );
        }

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.READ_EXTERNAL_STORAGE  }, 48 );
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);

        Button newDeck_button = (Button)findViewById(R.id.newdeckbutton);

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

        Button deckList_button = (Button)findViewById(R.id.decksbutton);
        deckList_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DeckListActivity.class);
                startActivity(intent);
            }
        });

        Button options_button = (Button) findViewById(R.id.optionsbutton);
        options_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),OptionsActivity.class);
                startActivityForResult(intent, OPT_CHANGE_LANG_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(resultCode){
            case OPT_CHANGE_LANG_RESULT_CODE:
                Log.i("JH", "changement de langue");
                recreate();
                break;
            case DeckEditor.EDIT_DECK_RESULT:
                break;
        }
    }

}
