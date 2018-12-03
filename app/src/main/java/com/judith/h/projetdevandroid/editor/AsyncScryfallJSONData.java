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
import com.judith.h.projetdevandroid.DecksDataBaseHelper;
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
    private DecksDataBaseHelper handler;

    public AsyncScryfallJSONData(AddCardActivity activity){
        this.activity = activity;
        handler = new DecksDataBaseHelper(activity);
    }

    @Override
    protected void onPostExecute(JSONObject j) {
        String scryfallID = "";
        String name = "Not Found";
        String type_line = "";
        int cmc = -1;
        String manaCost = "";
        String imgURL = "";
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

        } catch(NullPointerException e){
            Log.i("JH", "Null pointer");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        Card card = new Card(name, scryfallID, cmc, manaCost, color, readTypeLine(type_line));
        card.setImgUrl(imgURL);
        handler.createCard(card);
        if(!activity.getAddedCards().contains(card)){
            activity.getAddedCards().add(card);
        };

        //handler.addCardInDeck(activity.getDeck(), card, "main");

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

}
