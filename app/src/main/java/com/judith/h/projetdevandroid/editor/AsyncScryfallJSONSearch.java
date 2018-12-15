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
//        final ListView lv = activity.findViewById(R.id.cards_found);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(lv.getContext(),
//                R.layout.card_list_item);
//        lv.setAdapter(adapter);
//        try {
//            JSONArray ja = j.getJSONArray("data");
//            for(int i = 0; i<ja.length();i++){
//                adapter.add(ja.getString(i));
//                }
//        } catch(NullPointerException e){
//            Log.i("JH", "Null pointer");
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Button search_button = activity.findViewById(R.id.add_card_button_ac);
//        final EditText search_bar = activity.findViewById(R.id.search_bar);
//
//        search_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://api.scryfall.com/cards/autocomplete?q=" + search_bar.getText();
//                new AsyncScryfallJSONSearch(activity).execute(url);
//            }
//        });
//
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AsyncScryfallJSONData task = new AsyncScryfallJSONData(activity);;
//                String url = "https://api.scryfall.com/cards/named?fuzzy=" + lv.getAdapter().getItem(position);;
//                task.execute(url);
//            }
//        });


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
        CardSearchRecyclerAdapter adapter = new CardSearchRecyclerAdapter(activity, cardNames);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

    }


}
