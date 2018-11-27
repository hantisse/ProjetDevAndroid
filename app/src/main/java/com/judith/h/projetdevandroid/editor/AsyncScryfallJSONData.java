package com.judith.h.projetdevandroid.editor;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.judith.h.projetdevandroid.R;
import com.judith.h.projetdevandroid.editor.AddCardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncScryfallJSONData extends AsyncTask<String, Void, JSONObject> {
    private AddCardActivity activity;

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
        String name = "Not Found";
        Log.i("JH", "json : " + j);
        try {
            name = j.getString("name");
        } catch(NullPointerException e){
            Log.i("JH", "Null pointer");
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.i("JH", "PAS BON**");
        }
        TextView cardNameFound = (TextView) activity.findViewById(R.id.card_name_found);
        cardNameFound.setText(name);
        /*
        cardNameFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                activity.setResult(4, intent);

            }
        });
        */

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


}
