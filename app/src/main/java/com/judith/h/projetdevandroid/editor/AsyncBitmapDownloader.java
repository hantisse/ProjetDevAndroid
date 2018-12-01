package com.judith.h.projetdevandroid.editor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.DecksDataBaseHelper;
import com.judith.h.projetdevandroid.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncBitmapDownloader extends AsyncTask<String, Void, Bitmap> {

    CardActivity cardActivity;

    AsyncBitmapDownloader(CardActivity cardActivity){
        this.cardActivity = cardActivity;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        Bitmap bm = null;
        try {
            url = new URL(strings[0]);
            Log.i("CIO", "Downloading " + url);
            urlConnection = (HttpURLConnection) url.openConnection(); // Open
            InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream
            bm = BitmapFactory.decodeStream(in);
            in.close();
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return bm;
    }

    protected void onPostExecute(Bitmap bitmap){
        ImageView image = cardActivity.findViewById(R.id.CardImage);
        image.setImageBitmap(bitmap);
    }
}