package com.judith.h.projetdevandroid;

import android.app.AlertDialog;
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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public MyViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DeckRecyclerAdapter(ArrayList<Deck> Dataset) {
        deckDataset = Dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DeckRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_deck, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

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
                v.getContext().startActivity(intent);

            }
        });
        final Button deleteDeckButton = holder.mView.findViewById(R.id.deckDeleteButton);
        deleteDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deckId = deckDataset.get(holder.getAdapterPosition()).getDeckId();
                DecksDataBaseHelper decksDataBaseHelper = new DecksDataBaseHelper(v.getContext());
                decksDataBaseHelper.deleteDeck(deckId);
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

    public View getLastClickedDeck(){
        return this.lastClickedDeck;
    }

    public void setLastClickedDeck(View lastClickedDeck){
        this.lastClickedDeck = lastClickedDeck;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return deckDataset.size();
    }

    public ArrayList<Deck> getDeckDataset() {
        return deckDataset;
    }
}
