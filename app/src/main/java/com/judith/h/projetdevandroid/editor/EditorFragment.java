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
import java.util.Set;

@SuppressLint("ValidFragment")
public class EditorFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Deck deck;
    private String deckPart;
    private HashMap<String, Filter> filterNames;
    private HashMap<Filter, Boolean> filters;
    private ArrayList<Filter> activeFilters;


    public EditorFragment(Deck deck, String deckPart){
        this.deck = deck;
        this.deckPart = deckPart;
        filters = new HashMap<>();
        activeFilters = new ArrayList<>();
        filterNames = new HashMap<>();
        Filter defaultFilter = new Filter("Default");
        filters.put(defaultFilter, true);
        filterNames.put("Default", defaultFilter);
        activeFilters.add(defaultFilter);
        if(deckPart.equals("side")){
                defaultFilter.setCards(deck.getSide());
            } else {
                defaultFilter.setCards(deck.getMain());
            }
        mAdapter = new EditorRecyclerAdapter(activeFilters);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_collection_object, container, false);

        rootView.findViewById(R.id.add_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddCardActivity.class);
                intent.putExtra("deck_add", deck);
                intent.putExtra("deck_part", deckPart);
                getActivity().startActivityForResult(intent, DeckEditor.ADD_CARD_REQUEST_CODE);

            }
        });

        mRecyclerView = rootView.findViewById(R.id.editor_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return rootView;
    }

    public void calculateTypeFilters(Deck deck){
        ArrayList<Card> cards;
        if(deckPart.equals("main")){
            cards = deck.getMain();
        }else {
            cards = deck.getSide();
        }
        resetActiveFilters();
        for(Card card : cards){
            String type = card.getCardTypes().get(0);
            if(!filterNames.containsKey(type)){
                Filter filter = new Filter(type);
                filter.addCard(card);
                filterNames.put(type,filter);
                filters.put(filter, true);
                addInOrder(activeFilters, filter);
            }else {
                filterNames.get(type).addCard(card);
                if(!filters.get(filterNames.get(type))){
                    filters.put(filterNames.get(type), true);
//                    activeFilters.add(filterNames.get(type));
                    addInOrder(activeFilters, filterNames.get(type));
                }
            }
        }
    }

    public void calculateCMCFilters(Deck deck){
        ArrayList<Card> cards;
        if(deckPart.equals("main")){
            cards = deck.getMain();
        }else {
            cards = deck.getSide();
        }
        resetActiveFilters();
        for(Card card : cards){
            String cmc = String.valueOf(card.getCmc());
            if(!filterNames.containsKey(cmc)){
                Filter filter = new Filter(cmc);
                filter.addCard(card);
                filterNames.put(cmc,filter);
                filters.put(filter, true);
                addInOrder(activeFilters, filter);
            }else {
                filterNames.get(cmc).addCard(card);
                if(!filters.get(filterNames.get(cmc))){
                    filters.put(filterNames.get(cmc), true);
                    addInOrder(activeFilters, filterNames.get(cmc));
                }
            }
        }

    }

    public void calculateColorIdentityFilters(Deck deck){
        ArrayList<Card> cards;
        if(deckPart.equals("main")){
            cards = deck.getMain();
        }else {
            cards = deck.getSide();
        }
        resetActiveFilters();
        for(Card card : cards){
            String colorIdentity = card.getColorIdentity();
            if(!filterNames.containsKey(colorIdentity)){
                Filter filter = new Filter(colorIdentity);
                filter.addCard(card);
                filterNames.put(colorIdentity,filter);
                filters.put(filter, true);
                addInOrder(activeFilters, filter);
            }else {
                filterNames.get(colorIdentity).addCard(card);
                if(!filters.get(filterNames.get(colorIdentity))){
                    filters.put(filterNames.get(colorIdentity), true);
                    addInOrder(activeFilters, filterNames.get(colorIdentity));
                }
            }
            Log.i("JH", "COLOR ID : "+ deckPart + " : " + filterNames.get(colorIdentity).getFilterName()+ " " + filterNames.get(colorIdentity).getCards().size());
        }

    }

    public void updateFilterAfterCardsAdded(Set<Card> cards, String filterType){
        for(Card card : cards){
            switch(filterType){
                case "cmc":
                   String cmc = String.valueOf(card.getCmc());
                   filterAddCard(cmc, card);
                   break;
                case "colorIdentity" :
                    String colorIdentity = card.getColorIdentity();
                    filterAddCard(colorIdentity, card);
                    break;
                case "type" :
                    String type = card.getCardTypes().get(0);
                    filterAddCard(type, card);

            }

        }
    }

    public void setDefaultFilter(){
        resetActiveFilters();
        activeFilters.add(filterNames.get("Default"));
        filters.put(filterNames.get("Default"), true);
    }

    private void filterAddCard(String filterName, Card card){
        if(!filterNames.containsKey(filterName)){
            Filter filter = new Filter(filterName);
            filter.addCard(card);
            filterNames.put(filterName,filter);
            filters.put(filter, true);
            addInOrder(activeFilters, filter);
        }else {
            filterNames.get(filterName).addCard(card);
            if(!filters.get(filterNames.get(filterName))){
                filters.put(filterNames.get(filterName), true);
                addInOrder(activeFilters, filterNames.get(filterName));
            }
        }
    }

    public void resetActiveFilters(){
        for(Filter filter : filters.keySet()){
            if(filters.get(filter)){
                if( !filter.getFilterName().equals("Default")){
                    filter.clearCards();
                }
                filters.put(filter,false);
            }
        }
        activeFilters.clear();
    }

    private void addInOrder(ArrayList<Filter> activeFilters, Filter filter){
        int k =0;
        while(k<activeFilters.size() && filter.getFilterName().compareTo(activeFilters.get(k).getFilterName())>0){ //>0 car filtres rangés dans l'ordre inverse dans l'adapter
            k++;
        }
        activeFilters.add(k,filter);
    }

    public RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }
}
