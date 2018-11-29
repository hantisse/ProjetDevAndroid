package com.judith.h.projetdevandroid.editor;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.judith.h.projetdevandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AsyncScryfallJSONSearch extends AsyncScryfall {

    private AddCardActivity activity;
    private AsyncScryfallJSONData task;

    public AsyncScryfallJSONSearch(AddCardActivity activity){
        this.activity = activity;
        this.task = new AsyncScryfallJSONData(activity);
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
                Log.i("JH", "" +i);

            }

        } catch(NullPointerException e){
            Log.i("JH", "Null pointer");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "https://api.scryfall.com/cards/named?fuzzy=" + lv.getAdapter().getItem(position);;
                Log.i("JH", url);
                task.execute(url);
            }
        });

    }

    public AsyncScryfallJSONData getTask(){
        return task;
    }


}
