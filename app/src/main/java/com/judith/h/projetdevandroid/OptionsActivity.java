package com.judith.h.projetdevandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class OptionsActivity extends AppCompatActivity {

    private String LanguageCode = "fr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Change French to English when user clicked the button.
        findViewById(R.id.switchlanguagebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Change Application level locale
                LocaleHelper.setLocale(OptionsActivity.this, LanguageCode);

                //It is required to recreate the activity to reflect the change in UI.
                recreate();
            }
        });
    }
}
