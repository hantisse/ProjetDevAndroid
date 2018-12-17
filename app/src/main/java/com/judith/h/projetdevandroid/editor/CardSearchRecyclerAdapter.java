package com.judith.h.projetdevandroid.editor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.judith.h.projetdevandroid.R;

import java.util.ArrayList;

public class CardSearchRecyclerAdapter extends RecyclerView.Adapter<CardSearchRecyclerAdapter.CardSearchHolder> implements View.OnClickListener {

    private AddCardActivity activity;
    private ArrayList<String> cardNames;
    private boolean[] isClicked;

    public CardSearchRecyclerAdapter(AddCardActivity activity, ArrayList<String> cardNames){
        this.activity = activity;
        this.cardNames = cardNames;
        isClicked = new boolean[cardNames.size()];
    }

    @NonNull
    @Override
    public CardSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_item, parent, false);
        CardSearchHolder vh = new CardSearchHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CardSearchHolder holder, int i) {
        if(isClicked[i]){
            holder.getDescription().setVisibility(View.VISIBLE);
        }else{
            holder.getDescription().setVisibility(View.GONE);
        }
        holder.setCardNameTvText(cardNames.get(i));
        holder.getAddButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getDescription().getVisibility() == View.GONE){
                    AsyncScryfallJSONData task = new AsyncScryfallJSONData(activity, holder, true);
                    String url = "https://api.scryfall.com/cards/named?fuzzy=" + cardNames.get(holder.getAdapterPosition());
                    task.execute(url);
                }
            }
        });
        holder.getMoreButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked[holder.getAdapterPosition()] = holder.getDescription().getVisibility() == View.GONE;
                holder.getDescription().setVisibility(holder.getDescription().getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                if(holder.getDescription().getVisibility() == View.VISIBLE){
                    AsyncScryfallJSONData task = new AsyncScryfallJSONData(activity, holder, false);
                    String url = "https://api.scryfall.com/cards/named?fuzzy=" + cardNames.get(holder.getAdapterPosition());
                    task.execute(url);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardNames.size();
    }

    @Override
    public void onClick(View v) {

    }


    public class CardSearchHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView cardNameTv, description;
        private Button addButton, moreButton;
        private boolean isExpanded;

        public CardSearchHolder(View v){
            super(v);
            view = v;
            isExpanded = false;
            cardNameTv = v.findViewById(R.id.card_search);
            addButton = v.findViewById(R.id.card_search_add_button);
            moreButton = v.findViewById(R.id.card_search_more);
            description = v.findViewById(R.id.card_search_description);
            description.setVisibility(View.GONE);
        }

        public boolean isExpanded(){
            return isExpanded;
        }

        public TextView getDescription(){
            return description;
        }

        public void setExpanded(boolean b){
            isExpanded = b;
        }

        public void setDescriptionText(String description) {
            this.description.setText(description);
        }

        public void setCardNameTvText(String cardName) {
            this.cardNameTv.setText(cardName);
        }

        public Button getAddButton() {
            return addButton;
        }

        public Button getMoreButton() {
            return moreButton;
        }

        public boolean[] getClicked(){
            return isClicked;
        }

    }

}
