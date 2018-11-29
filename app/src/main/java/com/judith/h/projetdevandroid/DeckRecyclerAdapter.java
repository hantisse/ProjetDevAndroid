package com.judith.h.projetdevandroid;

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DeckRecyclerAdapter extends RecyclerView.Adapter<DeckRecyclerAdapter.MyViewHolder> {
    private String[] deckDataset;

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
    public DeckRecyclerAdapter(String[] Dataset) {
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Button UpdateDeckButton = holder.mView.findViewById(R.id.DeckUpdateButton);
        final Button DeleteDeckButton = holder.mView.findViewById(R.id.DeckDeleteButton);

        //Penser à mettre un on click listener pour les boutons UpdateDeckButton et DeleteDeckButton

        TextView tv =  holder.mView.findViewById(R.id.deckTitle);
        tv.setText(deckDataset[position]);
        tv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

               if (UpdateDeckButton.getVisibility() == View.GONE){
                   UpdateDeckButton.setVisibility(View.VISIBLE);
                   DeleteDeckButton.setVisibility(View.VISIBLE);
               }
               else {
                   UpdateDeckButton.setVisibility(View.GONE);
                   DeleteDeckButton.setVisibility(View.GONE);
               }
           }
       });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return deckDataset.length;
    }

    public String[] getDeckDataset() {
        return deckDataset;
    }
}
