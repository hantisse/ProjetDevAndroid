package com.judith.h.projetdevandroid.editor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.DecksDataBaseHelper;
import com.judith.h.projetdevandroid.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AsyncScryfallJSONData extends AsyncScryfall{
    private AddCardActivity activity;
    private DecksDataBaseHelper handler;
    private CardSearchRecyclerAdapter.CardSearchHolder holder;

    //savois si le bouton add a été pressé
    private boolean add;

    public AsyncScryfallJSONData(AddCardActivity activity, CardSearchRecyclerAdapter.CardSearchHolder holder, boolean add){
        this.activity = activity;
        handler = new DecksDataBaseHelper(activity);
        this.holder = holder;
        this.add = add;
        progDailog = new ProgressDialog(activity);
    }

    @Override
    protected void onPostExecute(JSONObject j) {
        //TODO mettre string not found dans string.xml
        String scryfallID = "Not Found";
        String name = "Not Found";
        String type_line = "Not found";
        int cmc = -1;
        String manaCost = "Not found";
        String imgURL = "Not found";
        String color = "Not found";
        float price = -1;
        String description = "Not found";
        String stats = "";
        try {
            name = j.getString("name");
            scryfallID = j.getString("id");
            type_line = j.getString("type_line");
            cmc = j.getInt("cmc");
            description = j.getString("oracle_text");
            color = j.getString("color_identity");
            manaCost = j.getString("mana_cost");
            JSONObject imgUris = j.getJSONObject("image_uris");
            imgURL = imgUris.getString("border_crop");
            price = Float.parseFloat(j.getString("eur"));
            stats = j.getString("power") + "/" + j.getString("toughness");


        } catch (NullPointerException e) {
            Log.i("JH", "Null pointer");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONExcetoption", e.toString());

        }
        holder.setDescriptionText(activity.getString(R.string.description, manaCost, type_line + " " + stats, description));

        final Card card = new Card(name, scryfallID, cmc, manaCost, color, readTypeLine(type_line), price);
        card.setImgUrl(imgURL);

        holder.getMoreButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getClicked()[holder.getAdapterPosition()] = holder.getDescription().getVisibility() == View.GONE;
                holder.getDescription().setVisibility(holder.getDescription().getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        holder.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCardToDeck(card);
            }
        });
        if (add) {
            addCardToDeck(card);
        }
        progDailog.dismiss();

    }


    public void addCardToDeck(Card card){
        Log.i("JH", "ajout API");

        handler.createCard(card);
        boolean inDeck = false;
        for(Card card1 : activity.getAddedCards().keySet()){
            if(card1.getCardId() == card.getCardId()){
                inDeck = true;
                int count = activity.getAddedCards().get(card1);
                activity.getAddedCards().put(card1, count + 1);
            }
        }
        if(!inDeck){
            Log.i("JH", "put card");
            activity.getAddedCards().put(card, 1);
        }
        Toast.makeText(activity, activity.getString(R.string.card_added_msg) + card.getName(), Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> readTypeLine(String typeLine){
        ArrayList<String> types = new ArrayList<>();
        String[] typesArray = typeLine.split(" — ");
        StringBuilder sb = new StringBuilder();

        String[] stringSplit = typesArray[0].split(" ");
        if (stringSplit[0].equals("Legendary")){
            for(int k = 1; k <stringSplit.length; k++){
                sb.append(stringSplit[k]);
            }
        } else {
            for(int k = 0; k < stringSplit.length; k++){
                sb.append(stringSplit[k]);
            }
        }

        types.add(sb.toString());
        if(typesArray.length == 2){
            types.add(typesArray[1]);
        }

        return types;
    }

}
