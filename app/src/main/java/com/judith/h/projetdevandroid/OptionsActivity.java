package com.judith.h.projetdevandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.judith.h.projetdevandroid.editor.DeckEditor;

import java.util.Locale;

public class OptionsActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private static final int ENG_LANG = 1;
    private static final int FR_LANG = 2;

    private SharedPreferences preferences;
    private LinearLayout mRelativeLayout;
    private Button mButton;
    private PopupWindow mPopupWindow;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        preferences = getApplication().getSharedPreferences(MainMenuActivity.PREF_NAME, MODE_PRIVATE);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mRelativeLayout = (LinearLayout) inflater.inflate(R.layout.credits_display, null);

        mButton = (Button) findViewById(R.id.creditsbutton);

        Log.i("CARIBOU","relative layout : " + mRelativeLayout);

        // Set a click listener for the text view
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(view.getContext());
                // Inflate the custom layout/view
                LinearLayout customView = (LinearLayout) inflater.inflate(R.layout.credits_display,null);

                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );


                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                Button closeButton = (Button) customView.findViewById(R.id.closecredits);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
            }
        });

        spinner = (Spinner) findViewById(R.id.lang_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void setLocale(String lang) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (!conf.locale.getLanguage().equals(lang)) {
            conf.locale = new Locale(lang);
            res.updateConfiguration(conf, dm);
            Intent intent = new Intent(this, OptionsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == ENG_LANG){
            setResult(MainMenuActivity.OPT_CHANGE_LANG_RESULT_CODE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("language","en");
            editor.apply();
            setLocale("en");
        } else if(position == FR_LANG){
            setResult(MainMenuActivity.OPT_CHANGE_LANG_RESULT_CODE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("language","fr");
            editor.apply();
            setLocale("fr");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
