package com.judith.h.projetdevandroid.editor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class AsyncScryfallJSONData extends AsyncTask<String, Void, JSONObject> implements View.OnClickListener {
    private AddCardActivity activity;
    private ArrayList<Card> addedCards = new ArrayList<Card>();

    public AsyncScryfallJSONData(AddCardActivity activity){
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        JSONObject json = null;
        URL url = null;
        HttpURLConnection urlConnection = null;
        String result = null;
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection(); // Open
            InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream
            result = readStream(in); // Read stream
        } catch (MalformedURLException e) {
            Log.i("JH", "exception1");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("JH", "exception2");
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                Log.i("JH", "result : " + result);
            }
            try {
                json = new JSONObject(result);
            } catch(NullPointerException e){
                Log.i("JH", "Null pointer");
            } catch (JSONException e) {
                Log.i("JH", "exception");
                e.printStackTrace();
            }


            return json; // returns the result

        }

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
        TextView cardNameFound = (TextView) activity.findViewById(R.id.card_name_found);
        cardNameFound.setText(name);

        //Modifier :
        final Card card = new Card(name, scryfallID, cmc, manaCost, color, readTypeLine(type_line));
        card.setImgUrl(imgURL);

        cardNameFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedCards.add(card);
            }
        });


    }

    protected String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
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

    public ArrayList<Card> getAddedCards() {
        return addedCards;
    }
}
