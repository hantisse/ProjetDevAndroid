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
                    Log.i("JH", "DeckEditor : "  + c.getName());
                }
                Intent intent = new Intent(v.getContext(),DeckEditor.class);
                intent.putExtra("deck", deckDataset.get(holder.getAdapterPosition()));
                v.getContext().startActivity(intent);

            }
        });
        final Button deleteDeckButton = holder.mView.findViewById(R.id.deckDeleteButton);

        //TODO Penser à mettre un on click listener pour les boutons updateButton et deleteDeckButton

        TextView tv =  holder.mView.findViewById(R.id.deckTitle);
        tv.setText(deckDataset.get(position).getDeckName());
        tv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return deckDataset.size();
    }

    public ArrayList<Deck> getDeckDataset() {
        return deckDataset;
    }
}
