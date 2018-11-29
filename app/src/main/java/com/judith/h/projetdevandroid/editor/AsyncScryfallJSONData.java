package com.judith.h.projetdevandroid.editor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.R;
import com.judith.h.projetdevandroid.editor.AddCardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AsyncScryfallJSONData extends AsyncScryfall implements View.OnClickListener {
    private AddCardActivity activity;
    private HashMap<Card,Integer> addedCards = new HashMap<>();
    private HashMap<String, Card> cardNames = new HashMap<>();

    public AsyncScryfallJSONData(AddCardActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(JSONObject j) {
        String scryfallID = "";
        String name = "Not Found";
        String type_line = "";
        int cmc = -1;
        String manaCost = "";
        String imgURL = "";
        String descr = "";
        String color = "";
        Log.i("JH", "json : " + j);
        try {
            name = j.getString("name");
            scryfallID = j.getString("id");
            type_line = j.getString("type_line");
            cmc = j.getInt("cmc");
            color =  j.getString("color_identity");
            Log.i("JH", "string : " + color.toString());
            manaCost = j.getString("mana_cost");
            JSONObject imgUris = j.getJSONObject("image_uris");
            imgURL = imgUris.getString("border_crop");
            descr = j.getString("oracle_text");

        } catch(NullPointerException e){
            Log.i("JH", "Null pointer");
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.i("JH", "PAS BON");
        }

        if(addedCards.containsKey(name)) {
            Integer count = addedCards.get(cardNames.get(name));
            if (count != null) {
                addedCards.put(cardNames.get(name),count + 1);
                Log.i("JH", name + count );
            } else {
                addedCards.put(cardNames.get(name),1);
            }
        } else {
            Card card = new Card(name, scryfallID, cmc, manaCost, color, readTypeLine(type_line));
            card.setImgUrl(imgURL);
            cardNames.put(name, card);
            addedCards.put(card, 1);
        }


    }

    @Override
    public void onClick(View v){

    }

    private ArrayList<String> readTypeLine(String typeLine){
        ArrayList<String> types = new ArrayList<>();
        String[] typesArray = typeLine.split(" ");
        for(String s : typesArray){
            if (s !="—"){
                types.add(s);
            }
        }
        return types;
    }

    public HashMap<Card,Integer> getAddedCards() {
        return addedCards;
    }
}
