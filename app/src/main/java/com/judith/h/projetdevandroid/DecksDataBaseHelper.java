package com.judith.h.projetdevandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DecksDataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE_NAME = "DecksTable";
    private static final String PKEY_DECK = "pkey_deck";
    private static final String NAME = "name";
    private static final String MAIN = "main";
    private static final String SIDE = "side";



    DecksDataBaseHelper(Context context){
        super(context, DATABASE_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                PKEY_DECK + " INTEGER PRIMARY KEY," +
                NAME + " TEXT," + MAIN + " TEXT, " + SIDE + " TEXT);" ;

        db.execSQL(DATABASE_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
