package com.judith.h.projetdevandroid.editor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.judith.h.projetdevandroid.R;

import java.util.ArrayList;

public class CardSearchAdapter extends ArrayAdapter<String> {

    private AddCardActivity activity;
    private ArrayList<String> cardNames;

    public CardSearchAdapter(AddCardActivity activity, int resource, ArrayList<String> cardNames) {
        super(activity, resource, cardNames);
        this.activity = activity;
        this.cardNames = cardNames;

    }

    @Override
    public int getCount(){
        return cardNames.size();
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View listItem = convertView;
        CardSearchHolder holder;
        if(listItem == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItem = inflater.inflate(R.layout.card_search_item, null);
            holder = new CardSearchHolder(listItem, position);
            holder.setCardNameTvText(cardNames.get(position));
            holder.getMoreButton().setOnClickListener(holder);
            holder.getAddButton().setOnClickListener(holder);
            listItem.setTag(holder);
        }


        return listItem;
    }

    public class CardSearchHolder implements View.OnClickListener {
        private View row;
        private TextView cardNameTv, description;
        private Button addButton, moreButton;

        public CardSearchHolder(View listItem, int position) {
            this.row = row;
            cardNameTv = listItem.findViewById(R.id.card_search);
            addButton = listItem.findViewById(R.id.card_search_add_button);
            moreButton = listItem.findViewById(R.id.card_search_more);
            description = listItem.findViewById(R.id.card_search_description);
            description.setVisibility(View.GONE);

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



        @Override
        public void onClick(View v) {
            boolean b = false;
            boolean b2 = (description.getVisibility() == View.GONE);
            if(v == addButton){
                b = true;
            }
            if(!b){
                if(description.getVisibility() == View.GONE){
                    description.setVisibility(View.VISIBLE);
                } else {
                    description.setVisibility(View.GONE);
                }
            }

            if(b2){
                AsyncScryfallJSONData task = new AsyncScryfallJSONData(activity, this, b);
                String url = "https://api.scryfall.com/cards/named?fuzzy=" + cardNameTv.getText();
                task.execute(url);
            }

        }


    }

}





