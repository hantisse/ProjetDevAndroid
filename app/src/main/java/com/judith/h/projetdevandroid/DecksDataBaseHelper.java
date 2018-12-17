package com.judith.h.projetdevandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.provider.Contacts.SettingsColumns.KEY;

public class DecksDataBaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DecksDatabaseHelper";
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "CardsDataManager";

    // Table Names
    private static final String TABLE_DECKS = "decks";
    private static final String TABLE_CARDS = "cards";
    private static final String TABLE_CARD_DECK = "cards_decks";

    // Common Column Names
    private static final String KEY_CARD_ID = "card_id";
    private static final String KEY_DECK_ID = "deck_id";

    // CARDS Table - Column Names
    private static final String KEY_CARD_NAME = "card_name";
    private static final String KEY_CARD_SCRYFALL_ID = "card_scryfall_id";
    private static final String KEY_CARD_CMC = "card_cmc";
    private static final String KEY_CARD_TYPES = "card_types";
    private static final String KEY_CARD_IMAGE_URL = "card_img_url";
    private static final String KEY_CARD_MANA_COST = "card_mana_cost";
    private static final String KEY_CARD_COLOR_IDENTITY = "card_color_identity";
    private static final String KEY_CARD_PRICE = "card_price";

    // DECKS Table - Column Names
    private static final String KEY_DECK_NAME = "deck_name";
    private static final String KEY_DECK_CREATED_AT = "deck_creation_date";

    // CARD_DECKS Table - Column Names
    private static final String KEY_CARD_DECK_ID = "card_deck_id";
    private static final String KEY_CARD_MULTIPLICITY = "card_multiplicity";
    private static final String KEY_DECK_PART = "deck_part";

    // Table Create Statements
    // CARDS Table Create Statement
    private static final String CREATE_TABLE_CARDS = "CREATE TABLE "
            + TABLE_CARDS + "(" + KEY_CARD_ID + " INTEGER PRIMARY KEY," + KEY_CARD_NAME
            + " TEXT," + KEY_CARD_SCRYFALL_ID + " TEXT, " + KEY_CARD_CMC + " INTEGER,"
            + KEY_CARD_TYPES + " TEXT, " + KEY_CARD_IMAGE_URL + " TEXT, "
            + KEY_CARD_MANA_COST + " TEXT, " + KEY_CARD_COLOR_IDENTITY + " TEXT, " + KEY_CARD_PRICE + " REAL " + ")";

    // DECKS Table Create Statement
    private static final String CREATE_TABLE_DECKS = "CREATE TABLE "
            + TABLE_DECKS + "(" + KEY_DECK_ID + " INTEGER PRIMARY KEY," + KEY_DECK_NAME
            + " TEXT, " + KEY_DECK_CREATED_AT + " DATETIME " + ")";

    // CARD_DECKS Table Create Statement
    private static final String CREATE_TABLE_CARD_DECK = "CREATE TABLE "
            + TABLE_CARD_DECK + "(" + KEY_CARD_DECK_ID + " INTEGER PRIMARY KEY," + KEY_DECK_ID
            + " INTEGER, " + KEY_CARD_ID + " INTEGER, " + KEY_CARD_MULTIPLICITY + " INTEGER, "
            + KEY_DECK_PART + " TEXT " + ")";

    public DecksDataBaseHelper(Context context){
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
        //Ajouter les modifications à chaque changement de version selon les lodifications faites
        // en faisant un if(oldVersion < [prochaine version]
        //permet le fait que l'utilisateur peut sauter des versions
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_CARDS + " ADD COLUMN " + KEY_CARD_PRICE + " REAL");
        }


        // Create New Tables
        onCreate(db);

    }

    /**
     * Vérifie si la carte n'existe pas déja dans la ddb, sinon la crée
     * @param card qui n'a pas encore d'id
     * @return id dans la db de la carte ajouter (à associer à la carte)
     */
    public long createCard(Card card) {
        long card_id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_CARDS, new String[]{KEY_CARD_ID}, KEY_CARD_SCRYFALL_ID + " = ?",new String[]{card.getScryfallID()},null, null,null);

        if(c.moveToFirst()){
            card_id =  c.getLong(c.getColumnIndex(KEY_CARD_ID));
            this.updateCard(card);
            card.setCardId((int)card_id);
        } else{
            ContentValues values = new ContentValues();
            values.put(KEY_CARD_NAME, card.getName());
            values.put(KEY_CARD_SCRYFALL_ID, card.getScryfallID());
            values.put(KEY_CARD_CMC, card.getCmc());
            ArrayList<String> cardTypes = card.getCardTypes();
            String cardTypesToString = "";
            for (String cardtype : cardTypes){
                cardTypesToString += cardtype + ";";
            }
            values.put(KEY_CARD_TYPES, cardTypesToString);
            values.put(KEY_CARD_IMAGE_URL, card.getImgUrl());
            values.put(KEY_CARD_MANA_COST, card.getManaCost());
            values.put(KEY_CARD_COLOR_IDENTITY, card.getColorIdentity());
            values.put(KEY_CARD_PRICE, card.getPrice());

            // insert row
            card_id = db.insert(TABLE_CARDS, null, values);
            card.setCardId((int)card_id);
        }

        c.close();
        db.close();
        return card_id;

    }

    /**
     *
     * @param card_id
     * @return renvoi null si pas de carte avec cet id dans la database
     */
    public Card getCard(long card_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CARDS + " WHERE "
                + KEY_CARD_ID + " = " + card_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        Card card = null;
        if (c.moveToFirst()){
            card = new Card();
            card.setCardId(c.getInt(c.getColumnIndex(KEY_CARD_ID)));
            card.setName((c.getString(c.getColumnIndex(KEY_CARD_NAME))));
            card.setScryfallID(c.getString(c.getColumnIndex(KEY_CARD_SCRYFALL_ID)));
            card.setCmc((c.getInt(c.getColumnIndex(KEY_CARD_CMC))));
            String cardTypesToString = c.getString(c.getColumnIndex(KEY_CARD_TYPES));
            ArrayList<String> cardTypes = new ArrayList<>(Arrays.asList(cardTypesToString.split(";")));
            card.setCardTypes(cardTypes);
            card.setImgUrl((c.getString(c.getColumnIndex(KEY_CARD_IMAGE_URL))));
            card.setManaCost((c.getString(c.getColumnIndex(KEY_CARD_MANA_COST))));
            card.setColorIdentity((c.getString(c.getColumnIndex(KEY_CARD_COLOR_IDENTITY))));
            card.setPrice(c.getFloat(c.getColumnIndex(KEY_CARD_PRICE)));
        }
        c.close();
        db.close();
        return card;
    }

    /**
     *
     * @return liste de toutes les cartes
     */
    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
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
                String cardTypesToString = c.getString(c.getColumnIndex(KEY_CARD_TYPES));
                ArrayList<String> cardTypes = new ArrayList<>(Arrays.asList(cardTypesToString.split(";")));
                card.setCardTypes(cardTypes);
                card.setImgUrl((c.getString(c.getColumnIndex(KEY_CARD_IMAGE_URL))));
                card.setManaCost((c.getString(c.getColumnIndex(KEY_CARD_MANA_COST))));
                card.setColorIdentity((c.getString(c.getColumnIndex(KEY_CARD_COLOR_IDENTITY))));
                card.setPrice(c.getFloat(c.getColumnIndex(KEY_CARD_PRICE)));

                // adding to card list
                cards.add(card);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return cards;
    }

    /**
     *
     * @param deck_id id du deck à chercher
     * @return liste vide si il n'y a pas de cartes dans le main
     */
    public List<Card> getAllMainCardsByDeck(long deck_id) {
        List<Card> cards = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CARDS + " tc, "
                + TABLE_CARD_DECK + " tcd WHERE tcd."
                + KEY_DECK_PART + " = 'main'" + " AND tcd." + KEY_DECK_ID
                + " = " + deck_id + " AND tcd." + KEY_CARD_ID + " = "
                + "tc." + KEY_CARD_ID;

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
                String cardTypesToString = c.getString(c.getColumnIndex(KEY_CARD_TYPES));
                ArrayList<String> cardTypes = new ArrayList<>(Arrays.asList(cardTypesToString.split(";")));
                card.setCardTypes(cardTypes);
                card.setImgUrl((c.getString(c.getColumnIndex(KEY_CARD_IMAGE_URL))));
                card.setManaCost((c.getString(c.getColumnIndex(KEY_CARD_MANA_COST))));
                card.setColorIdentity((c.getString(c.getColumnIndex(KEY_CARD_COLOR_IDENTITY))));
                card.setPrice(c.getFloat(c.getColumnIndex(KEY_CARD_PRICE)));

                // adding to card list
                cards.add(card);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return cards;
    }

    /**
     *
     * @param deck_id id du deck
     * @return liste vide si il n'y a pas de cartes dans le side
     */
    public List<Card> getAllSideCardsByDeck(long deck_id) {
        List<Card> cards = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CARDS + " tc, "
                + TABLE_CARD_DECK + " tcd WHERE tcd."
                + KEY_DECK_PART + " = 'side'" + " AND tcd." + KEY_DECK_ID
                + " = " + deck_id + " AND tcd." + KEY_CARD_ID + " = "
                + "tc." + KEY_CARD_ID;


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
                String cardTypesToString = c.getString(c.getColumnIndex(KEY_CARD_TYPES));
                ArrayList<String> cardTypes = new ArrayList<>(Arrays.asList(cardTypesToString.split(";")));
                card.setCardTypes(cardTypes);
                card.setImgUrl((c.getString(c.getColumnIndex(KEY_CARD_IMAGE_URL))));
                card.setManaCost((c.getString(c.getColumnIndex(KEY_CARD_MANA_COST))));
                card.setColorIdentity((c.getString(c.getColumnIndex(KEY_CARD_COLOR_IDENTITY))));
                card.setPrice(c.getFloat(c.getColumnIndex(KEY_CARD_PRICE)));

                // adding to card list
                cards.add(card);
            } while (c.moveToNext());

        }
        c.close();
        db.close();
        return cards;
    }

    /**
     *
     * @param card carte à mettre à jour, carte doit avoir un id dans la ddb
     * @return 0 si il y a un pb
     */
    public int updateCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_NAME, card.getName());
        values.put(KEY_CARD_SCRYFALL_ID, card.getScryfallID());
        values.put(KEY_CARD_CMC, card.getCmc());
        ArrayList<String> cardTypes = card.getCardTypes();
        String cardTypesToString = "";
        for (String cardtype : cardTypes){
            cardTypesToString += cardtype + ";";
        }
        values.put(KEY_CARD_TYPES, cardTypesToString);
        values.put(KEY_CARD_IMAGE_URL, card.getImgUrl());
        values.put(KEY_CARD_MANA_COST, card.getManaCost());
        values.put(KEY_CARD_COLOR_IDENTITY, card.getColorIdentity());
        values.put(KEY_CARD_PRICE, card.getPrice());

        // updating row
        int result = db.update(TABLE_CARDS, values, KEY_CARD_ID + " = ?",
                new String[] { String.valueOf(card.getCardId()) });

        db.close();
        return result;
    }

    /*
     * Deleting a card
     */
    public void deleteCard(long card_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARDS, KEY_CARD_ID + " = ?",
                new String[] { String.valueOf(card_id) });
        db.delete(TABLE_CARD_DECK, KEY_CARD_ID + " =?", new String[]{String.valueOf(card_id)});
        db.close();
    }

    /**
     *
     * @param deck deck avec un nom
     * @return l'id du deck créé dans la ddb
     */
    public long createDeck(Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DECK_NAME, deck.getDeckName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        String currentTimeFormatted = format.format(currentTime);
        values.put(KEY_DECK_CREATED_AT, "" + currentTimeFormatted);

        // insert row
        long deck_id = db.insert(TABLE_DECKS, null, values);
        db.close();

        deck.setDeckId((int)deck_id);
        return deck_id;
    }

    /**
     *
     * @return liste de decks
     */
    public List<Deck> getAllDecks() {
        List<Deck> decks = new ArrayList<Deck>();
        String selectQuery = "SELECT * FROM " + TABLE_DECKS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Deck deck = new Deck();
                deck.setDeckId(c.getInt(c.getColumnIndex(KEY_DECK_ID)));
                deck.setDeckName(c.getString(c.getColumnIndex(KEY_DECK_NAME)));
                deck.setCreationDate(c.getString(c.getColumnIndex(KEY_DECK_CREATED_AT)));
                this.deckUpdateContent(deck);
                decks.add(deck);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return decks;
    }

    /**
     *
     * @param deck deck quelconque
     * @return 0 si pb;
     */
    public int updateDeckName(Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DECK_NAME, deck.getDeckName());

        // updating row
        int result = db.update(TABLE_DECKS, values, KEY_DECK_ID + " = ?",
                new String[] { String.valueOf(deck.getDeckId()) });

        db.close();
        return result;
    }

    /**
     *
     * @param deck deck qui est déjà dans la ddb
     * @param card carte qui est déjà dans la ddb
     * @param deckPart la partie du deck ("main" ou "side")
     */
    public void addCardInDeck(Deck deck, Card card,int nbr, String deckPart){
        SQLiteDatabase db = this.getWritableDatabase();
        if(nbr > 0){

            Cursor c = db.query(TABLE_CARD_DECK, new String[]{KEY_CARD_MULTIPLICITY},
                    KEY_CARD_ID + " = ? AND " + KEY_DECK_ID + " = ? AND " + KEY_DECK_PART + " = ?" ,
                    new String[]{String.valueOf(card.getCardId()), String.valueOf(deck.getDeckId()), deckPart},
                    null, null, null );
            if (c.moveToFirst()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_CARD_MULTIPLICITY, nbr);
                db.update(TABLE_CARD_DECK, contentValues, KEY_CARD_ID + " = ? AND " + KEY_DECK_ID + " = ?", new String[]{String.valueOf(card.getCardId()), String.valueOf(deck.getDeckId())});
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_DECK_ID, deck.getDeckId());
                contentValues.put(KEY_CARD_ID, card.getCardId());
                contentValues.put(KEY_CARD_MULTIPLICITY, nbr);
                contentValues.put(KEY_DECK_PART, deckPart);
                db.insert(TABLE_CARD_DECK, null ,contentValues );
            }
            c.close();
            db.close();
        } else if(nbr == 0){
            db.delete(TABLE_CARD_DECK, KEY_CARD_ID + " = ? AND " + KEY_DECK_ID + " = ?", new String[]{String.valueOf(card.getCardId()), String.valueOf(deck.getDeckId())});
        }

    }

    /**
     *
     * @param deck qui est dans la database (donc a déjà un id de ddb)
     */
    public void deckUpdateContent(Deck deck){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Card> main = new ArrayList<>();
        ArrayList<Card> side = new ArrayList<>();
        HashMap<Card, Integer> mainMult = new HashMap<>();
        HashMap<Card, Integer> sideMult = new HashMap<>();

        String query = "SELECT " + "tc." + KEY_CARD_ID + ", " + KEY_CARD_MULTIPLICITY + ", " +  KEY_DECK_PART
                + " FROM " + TABLE_CARD_DECK + " tcd, " + TABLE_CARDS + " tc"
                + " WHERE tc." + KEY_CARD_ID + " = tcd." + KEY_CARD_ID +" AND tcd." + KEY_DECK_ID + " = ?";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(deck.getDeckId())});
        if(c.moveToFirst()){
            do {
                //si carte dans le main
               if(c.getString(c.getColumnIndex(KEY_DECK_PART)).equals("main")){
                   Card card = this.getCard(c.getLong(c.getColumnIndex(KEY_CARD_ID)));
                   //on regarde si la carte est dans le main deck
                   for(Card card1 : main){
                       if(card1.getName().equals(card.getName())){
                           card = card1;
                       }
                   }
                   main.add(card) ;

                   int mult = c.getInt(c.getColumnIndex(KEY_CARD_MULTIPLICITY));
                   mainMult.put(card, mult);
               }else if(c.getString(c.getColumnIndex(KEY_DECK_PART)).equals("side")){
                   Card card = this.getCard(c.getLong(c.getColumnIndex(KEY_CARD_ID)));
                   for(Card card1 : side){
                       if(card1.getName().equals(card.getName())){
                           card = card1;
                       }
                   }
                   side.add(card) ;
                   int mult = c.getInt(c.getColumnIndex(KEY_CARD_MULTIPLICITY));
                   sideMult.put(card, mult);
               }

            } while(c.moveToNext());
        }
        db.close();
        c.close();
        deck.setMain(main);
        deck.setSide(side);
        deck.setMainMultiplicities(mainMult);
        deck.setSideMultiplicities(sideMult);

    }


    /*
     * Deleting a deck
     */
    public void deleteDeck(long deck_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DECKS, KEY_DECK_ID + " = ?",
                new String[] { String.valueOf(deck_id) });
        db.delete(TABLE_CARD_DECK, KEY_DECK_ID + " = ?",
                new String[] { String.valueOf(deck_id) });
        db.close();
    }

    /**
     *
     * @param deck deck quelconque
     * @return 0 si pb;
     */
    public int updateModificationDate(Deck deck){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        String currentTimeFormatted = format.format(currentTime);
        values.put(KEY_DECK_CREATED_AT, "" + currentTimeFormatted);

        // updating row
        int result = db.update(TABLE_DECKS, values, KEY_DECK_ID + " = ?",
                new String[] { String.valueOf(deck.getDeckId()) });

        db.close();
        return result;
    }
}
