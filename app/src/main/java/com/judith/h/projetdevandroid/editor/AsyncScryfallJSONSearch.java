package com.judith.h.projetdevandroid.editor;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.judith.h.projetdevandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AsyncScryfallJSONSearch extends AsyncScryfall {

    private AddCardActivity activity;

    public AsyncScryfallJSONSearch(AddCardActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(JSONObject j) {


        ArrayList<String> cardNames = new ArrayList<>();
        try {
            JSONArray ja = j.getJSONArray("data");
            for(int i = 0; i<ja.length();i++){
                cardNames.add(ja.getString(i));
            }
        } catch(NullPointerException e){
            Log.i("JH", "Null pointer");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = activity.findViewById(R.id.cards_found);
        TextView tv = activity.findViewById(R.id.no_cards_found);
        if(!cardNames.isEmpty()){
            tv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            CardSearchRecyclerAdapter adapter = new CardSearchRecyclerAdapter(activity, cardNames);
            recyclerView.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(layoutManager);
        } else{
            recyclerView.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);

        }

    }


}
