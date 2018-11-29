package com.judith.h.projetdevandroid.editor;


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

public class AsyncScryfallJSONSearch extends AsyncScryfall {

    private AddCardActivity activity;

    public AsyncScryfallJSONSearch(AddCardActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(JSONObject j) {
        final ListView lv = activity.findViewById(R.id.cards_found);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(lv.getContext(),
                R.layout.card_search_list_element);
        lv.setAdapter(adapter);
        Log.i("JH", "json : " + j);

        try {
            JSONArray ja = j.getJSONArray("data");
            Log.i("JH", "****");

            for(int i = 0; i<ja.length();i++){
                adapter.add(ja.getString(i));
                }

        } catch(NullPointerException e){
            Log.i("JH", "Null pointer");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Button search_button = activity.findViewById(R.id.add_card_button_ac);
        final EditText search_bar = activity.findViewById(R.id.search_bar);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.scryfall.com/cards/autocomplete?q=" + search_bar.getText();
                Log.i("JH", "clic sur search");
                new AsyncScryfallJSONSearch(activity).execute(url);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AsyncScryfallJSONData task = new AsyncScryfallJSONData(activity);;
                String url = "https://api.scryfall.com/cards/named?fuzzy=" + lv.getAdapter().getItem(position);;
                Log.i("JH", url);
                task.execute(url);
            }
        });

    }

}
