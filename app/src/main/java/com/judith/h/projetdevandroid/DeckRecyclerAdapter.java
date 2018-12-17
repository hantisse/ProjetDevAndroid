package com.judith.h.projetdevandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.judith.h.projetdevandroid.editor.DeckEditor;

import java.util.ArrayList;

public class DeckRecyclerAdapter extends RecyclerView.Adapter<DeckRecyclerAdapter.MyViewHolder> {
    private ArrayList<Deck> deckDataset;
    private View lastClickedDeck;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public MyViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DeckRecyclerAdapter(ArrayList<Deck> Dataset, Context context) {
        this.context = context;
        deckDataset = Dataset;
    }

    @Override
    public DeckRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_deck, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Button updateButton = holder.mView.findViewById(R.id.deckUpdateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Card c : deckDataset.get(holder.getAdapterPosition()).getMain()){
                }
                Intent intent = new Intent(v.getContext(),DeckEditor.class);
                intent.putExtra("deck", deckDataset.get(holder.getAdapterPosition()));
                DeckListActivity activity = (DeckListActivity)context;
                activity.startActivityForResult(intent, DeckEditor.EDIT_DECK_REQUEST);

            }
        });

        //Bouton qui supprime le deck séléctionné
        final Button deleteDeckButton = holder.mView.findViewById(R.id.deckDeleteButton);
        deleteDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deckId = deckDataset.get(holder.getAdapterPosition()).getDeckId();
                DecksDataBaseHelper decksDataBaseHelper = new DecksDataBaseHelper(v.getContext());
                decksDataBaseHelper.deleteDeck(deckId);
                lastClickedDeck.findViewById(R.id.deckDeleteButton).setVisibility(View.GONE);
                lastClickedDeck.findViewById(R.id.deckUpdateButton).setVisibility(View.GONE);
                deckDataset.remove(holder.getAdapterPosition());
                notifyDataSetChanged();


            }
        });

        TextView tv =  holder.mView.findViewById(R.id.deckTitle);
        tv.setText(deckDataset.get(position).getDeckName());
        tv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if ((lastClickedDeck != null) && (holder.mView != lastClickedDeck)){
                   lastClickedDeck.findViewById(R.id.deckDeleteButton).setVisibility(View.GONE);
                   lastClickedDeck.findViewById(R.id.deckUpdateButton).setVisibility(View.GONE);
               }
               setLastClickedDeck(holder.mView);
               if (updateButton.getVisibility() == View.GONE){
                   updateButton.setVisibility(View.VISIBLE);
                   deleteDeckButton.setVisibility(View.VISIBLE);
               }
               else {
                   updateButton.setVisibility(View.GONE);
                   deleteDeckButton.setVisibility(View.GONE);
               }
           }
       });

    }


    public void setLastClickedDeck(View lastClickedDeck){
        this.lastClickedDeck = lastClickedDeck;
    }

    @Override
    public int getItemCount() {
        return deckDataset.size();
    }

}
