package com.judith.h.projetdevandroid.editor;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.judith.h.projetdevandroid.Deck;
import com.judith.h.projetdevandroid.R;


public class DeckEditor extends FragmentActivity implements EditorFragment.OnEditorFragmentUpdatedListener {

    EditorAdapter adapter;
    ViewPager pager;
    Deck deck;

    private DrawerLayout filter_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_editor);

        Intent intent = getIntent();
        String deck_name = intent.getStringExtra("deck_name");

        TextView deckName = (TextView)findViewById(R.id.deckName);
        deckName.setText(deck_name);


        filter_drawer = (DrawerLayout)findViewById(R.id.drawer_layout);

        NavigationView navView = (NavigationView)findViewById(R.id.nav_filter);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        filter_drawer.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        adapter = new EditorAdapter(getSupportFragmentManager());
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);



    }

    @Override
    public void onCardAdded() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i("JH", "toast");
        Bundle bundle = data.getExtras();
        StringBuilder sb = new StringBuilder();
        if(bundle != null){
            for(String key : bundle.keySet()){
                sb.append(bundle.get(key));
            }

            Log.i("JH", sb.toString());

        }

        Log.i("JH", "***");


    }
}

