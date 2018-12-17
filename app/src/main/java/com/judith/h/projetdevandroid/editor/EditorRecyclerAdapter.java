package com.judith.h.projetdevandroid.editor;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.Deck;
import com.judith.h.projetdevandroid.R;

import java.util.ArrayList;


public class EditorRecyclerAdapter extends RecyclerView.Adapter<EditorRecyclerAdapter.FilterViewHolder>{
    private ArrayList<Filter> activeFilters;
    private Deck deck;
    private String deckPart;
    private EditorFragment fragment;

     class FilterViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView filterName;
        private ListView filterListView;

        private FilterViewHolder(View v) {
            super(v);
            mView = v;
            filterName = v.findViewById(R.id.filter_name);
            filterListView = v.findViewById(R.id.filtered_list);
        }

        private void bind(final Filter filter){
            boolean isExpanded = filter.isExpanded();

            filterListView.setVisibility(isExpanded ? View.VISIBLE: View.GONE);

            if(filter.getLvAdapter() == null){
                filter.setLvAdapter( new ArrayAdapter<String>(
                        filterListView.getContext(), R.layout.card_list_item ));
            }
            if(deckPart.equals("side")){
                filter.setCardsInAdapter(deck.getSideMultiplicities());
            } else {
                filter.setCardsInAdapter(deck.getMainMultiplicities());
            }

            filterListView.setAdapter(filter.getLvAdapter());

            updateListElementsTotalHeight(filter.getLvAdapter(), filterListView);

            filterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(view.getContext(),"Pressed " + filterListView.getAdapter().getItem(position),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), CardActivity.class);
                    intent.putExtra("card",filter.getCardByCardName((String)filterListView.getAdapter().getItem(position)));
                    if(deckPart.equals("side")){
                        //recupère la multiplicité de la carte
                        Log.i("JH", "mult : " + deck.getSideMultiplicities().get(filter.getCardByCardName((String)filterListView.getAdapter().getItem(position))));
                        intent.putExtra("card_multiplicity", deck.getSideMultiplicities().get(filter.getCardByCardName((String)filterListView.getAdapter().getItem(position))));
                    } else {
                        Card card = filter.getCardByCardName((String)filterListView.getAdapter().getItem(position));
                        intent.putExtra("card_multiplicity", deck.getMainMultiplicities().get(card));

                    }
                    intent.putExtra("deck_part", deckPart);
                    fragment.getActivity().startActivityForResult(intent, DeckEditor.CHANGE_CARD_MULT_REQUEST_CODE);
                }
            });
        }

        //Calcule la taille de la liste de carte dans un filtre pour l'afficher
        private void updateListElementsTotalHeight(ArrayAdapter tableau, ListView lv){
            int totalHeight = 0;
            for (int i = 0; i < tableau.getCount(); i++) {
                View mView = tableau.getView(i, null, lv);

                mView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                totalHeight += mView.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = lv.getLayoutParams();
            params.height = totalHeight
                    + (filterListView.getDividerHeight() * (tableau.getCount() - 1));


            filterListView.setLayoutParams(params);
            filterListView.requestLayout();

        }
    }

    EditorRecyclerAdapter(EditorFragment fragment, Deck deck,String deckPart, ArrayList<Filter> activeFilters) {

         this.fragment = fragment;
         this.deck = deck;
         this.deckPart = deckPart;
         this.activeFilters = activeFilters;

    }


    // appelé par le layout manager (crée les vues)
    @Override
    public EditorRecyclerAdapter.FilterViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtered_cards, parent, false);
        FilterViewHolder vh = new FilterViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final FilterViewHolder holder, int position) {
        Filter filter = activeFilters.get(position);
        holder.bind(filter);
        holder.filterName.setText(holder.mView.getContext().getString(R.string.filter_text, filter.getFilterName(), filter.getFilterSize()));
        holder.filterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = activeFilters.get(holder.getAdapterPosition()).isExpanded();
                activeFilters.get(holder.getAdapterPosition()).setExpanded(!expanded);
                notifyItemChanged(holder.getAdapterPosition());

            }
        });


    }

    @Override
    public int getItemCount() {
        return activeFilters.size();
    }


}
