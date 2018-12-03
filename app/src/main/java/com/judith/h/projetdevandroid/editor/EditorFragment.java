package com.judith.h.projetdevandroid.editor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.Deck;
import com.judith.h.projetdevandroid.R;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class EditorFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Deck deck;
    private String deckPart;
    private HashMap<String, Filter> FilterNames;
    private ArrayList<Filter> filters;


    public EditorFragment(Deck deck, String deckPart){
        this.deck = deck;
        this.deckPart = deckPart;
        filters = new ArrayList<>();
        mAdapter = new EditorRecyclerAdapter(deck,deckPart,filters);
        FilterNames = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_collection_object, container, false);

        rootView.findViewById(R.id.add_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("JH", this.toString());
                Intent intent = new Intent(v.getContext(), AddCardActivity.class);
                intent.putExtra("deck_add", (Serializable) deck);
                intent.putExtra("deck_part", deckPart);
                getActivity().startActivityForResult(intent, 4); //request code 4 : cartes à ajouter au deck

            }
        });

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.editor_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private void calculateFilters(Deck deck){
        ArrayList<Card> cards;
        if(deckPart.equals("main")){
            cards = deck.getMain();
        }else {
            cards = deck.getSide();
        }
        for(Card card : cards){
            String type = card.getCardTypes().get(0);
            if(!FilterNames.containsKey(type)){
                Filter filter = new Filter(type);
                filter.addCard(card);
                FilterNames.put(type,filter);
                filters.add(filter);
            }else {
                FilterNames.get(type).addCard(card);
            }
            String colorIdentity = card.getColorIdentity();
            if(!FilterNames.containsKey(colorIdentity)){
                Filter filter = new Filter(colorIdentity);
                filter.addCard(card);
                FilterNames.put(colorIdentity,filter);
                filters.add(filter);
            }else {
                FilterNames.get(colorIdentity).addCard(card);
            }
            String cmc = String.valueOf(card.getCmc());
            if(!FilterNames.containsKey(cmc)){
                Filter filter = new Filter(cmc);
                filter.addCard(card);
                FilterNames.put(cmc,filter);
                filters.add(filter);
            }else {
                FilterNames.get(cmc).addCard(card);
            }
        }

    }

    public RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }
}
