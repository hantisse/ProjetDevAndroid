package com.judith.h.projetdevandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DecksDataBaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DecksDatabaseHelper";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "CardsDataManager";

    // Table Names
    private static final String TABLE_DECKS = "Decks";
    private static final String TABLE_CARDS = "Cards";
    private static final String TABLE_CARD_DECK = "Card_Decks";

    // Common Column Names
    private static final String KEY_CARD_ID = "CardId";
    private static final String KEY_DECK_ID = "DeckId";

    // CARDS Table - Column Names
    private static final String KEY_CARD_NAME = "CardName";
    private static final String KEY_CARD_SCRYFALL_ID = "CardScryfallId";
    private static final String KEY_CARD_CMC = "CardCMC";
    private static final String KEY_CARD_TYPES = "CardTypes";
    private static final String KEY_CARD_IMAGE_URL = "CardImageURL";
    private static final String KEY_CARD_MANA_COST = "CardManaCost";
    private static final String KEY_CARD_COLOR_IDENTITY = "CardColorIdentity";

    // DECKS Table - Column Names
    private static final String KEY_DECK_NAME = "DeckName";
    private static final String KEY_DECK_MAIN = "DeckMain";
    private static final String KEY_DECK_SIDE = "DeckSide";
    private static final String KEY_DECK_CREATED_AT = "DeckCreationDate";

    // CARD_DECKS Table - Column Names
    private static final String KEY_CARD_DECK_ID = "CardDeckId";
    private static final String KEY_CARD_MULTIPLICITY = "CardMultiplicity";
    private static final String KEY_DECK_PART = "DeckPart";

    // Table Create Statements
    // CARDS Table Create Statement
    private static final String CREATE_TABLE_CARDS = "CREATE TABLE "
            + TABLE_CARDS + "(" + KEY_CARD_ID + " INTEGER PRIMARY KEY," + KEY_CARD_NAME
            + " TEXT," + KEY_CARD_SCRYFALL_ID + " TEXT, " + KEY_CARD_CMC + " INTEGER,"
            + KEY_CARD_TYPES + " TEXT, " + KEY_CARD_IMAGE_URL + " TEXT, "
            + KEY_CARD_MANA_COST + " TEXT, " + KEY_CARD_COLOR_IDENTITY + " TEXT " + ")";

    // DECKS Table Create Statement
    private static final String CREATE_TABLE_DECKS = "CREATE TABLE "
            + TABLE_DECKS + "(" + KEY_DECK_ID + " INTEGER PRIMARY KEY," + KEY_DECK_NAME
            + " TEXT, " + KEY_DECK_MAIN + " TEXT, " + KEY_DECK_SIDE + " TEXT, "
            + KEY_DECK_CREATED_AT + " DATETIME " + ")";

    // CARD_DECKS Table Create Statement
    private static final String CREATE_TABLE_CARD_DECK = "CREATE TABLE "
            + TABLE_CARD_DECK + "(" + KEY_CARD_DECK_ID + " INTEGER PRIMARY KEY," + KEY_DECK_ID
            + " INTEGER, " + KEY_CARD_ID + " INTEGER, " + KEY_CARD_MULTIPLICITY + " INTEGER, "
            + KEY_DECK_PART + " TEXT " + ")";

    DecksDataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creating Required Tables
        db.execSQL(CREATE_TABLE_CARDS);
        db.execSQL(CREATE_TABLE_DECKS);
        db.execSQL(CREATE_TABLE_CARD_DECK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // On Upgrade Drop Older Tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD_DECK);

        // Create New Tables
        onCreate(db);

    }

    /*
     * Creating a Card
     */
    public long createCard(Card card, long[] deck_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_ID, card.getCardId());
        values.put(KEY_CARD_NAME, card.getName());
        values.put(KEY_CARD_SCRYFALL_ID, card.getScryfallID());
        values.put(KEY_CARD_CMC, card.getCmc());
        ArrayList<String> cardTypes = card.getCardTypes();
        String cardTypesToString = "";
        for (String cardtype:cardTypes){
            cardTypesToString += cardtype + ";";
        }
        values.put(KEY_CARD_TYPES, cardTypesToString);
        values.put(KEY_CARD_IMAGE_URL, card.getImgUrl());
        values.put(KEY_CARD_MANA_COST, card.getManaCost());
        values.put(KEY_CARD_COLOR_IDENTITY, card.getColorIdentity());
        Date currentTime = Calendar.getInstance().getTime();
        values.put(KEY_DECK_CREATED_AT, currentTime.getTime());

        // insert row
        long card_id = db.insert(TABLE_CARDS, null, values);

        // assigning decks to cards
        for (long deck_id : deck_ids) {
            createCardDeck(card_id, deck_id);
        }

        return card_id;
    }

    /*
     * get single card
     */
    public Card getCard(long card_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CARDS + " WHERE "
                + KEY_CARD_ID + " = " + card_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Card card = new Card();
        card.setCardId(c.getInt(c.getColumnIndex(KEY_CARD_ID)));
        card.setName((c.getString(c.getColumnIndex(KEY_CARD_NAME))));
        card.setScryfallID(c.getString(c.getColumnIndex(KEY_CARD_SCRYFALL_ID)));
        card.setCmc((c.getInt(c.getColumnIndex(KEY_CARD_CMC))));
        String cardTypesToString = c.getString(c.getColumnIndex(KEY_CARD_NAME));
        ArrayList<String> cardTypes = new ArrayList<>();
        for (String s:cardTypesToString.split(";")){
            cardTypes.add(s);
        }
        card.setCardTypes(cardTypes);
        card.setImgUrl((c.getString(c.getColumnIndex(KEY_CARD_IMAGE_URL))));
        card.setManaCost((c.getString(c.getColumnIndex(KEY_CARD_MANA_COST))));
        card.setColorIdentity((c.getString(c.getColumnIndex(KEY_CARD_COLOR_IDENTITY))));


        return card;
    }

    /*
     * getting all cards
     * */
    public List<Card> getAllCards() {
        List<Card> todos = new ArrayList<Card>();
        String selectQuery = "SELECT  * FROM " + TABLE_CARDS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Card card = new Card();
                card.setCardId(c.getInt(c.getColumnIndex(KEY_CARD_ID)));
                card.setName((c.getString(c.getColumnIndex(KEY_CARD_NAME))));
                card.setScryfallID(c.getString(c.getColumnIndex(KEY_CARD_SCRYFALL_ID)));
                card.setCmc((c.getInt(c.getColumnIndex(KEY_CARD_CMC))));
                String cardTypesToString = c.getString(c.getColumnIndex(KEY_CARD_NAME));
                ArrayList<Sgdtring> cardTypes = new ArrayList<>();
                for (String s:cardTypesToString.split(";")){
                    cardTypes.add(s);
                }
                card.setCardTypes(cardTypes);
                card.setImgUrl((c.getString(c.getColumnIndex(KEY_CARD_IMAGE_URL))));
                card.setManaCost((c.getString(c.getColumnIndex(KEY_CARD_MANA_COST))));
                card.setColorIdentity((c.getString(c.getColumnIndex(KEY_CARD_COLOR_IDENTITY))));

                // adding to card list
                todos.add(card);
            } while (c.moveToNext());
        }

        return todos;
    }


}
