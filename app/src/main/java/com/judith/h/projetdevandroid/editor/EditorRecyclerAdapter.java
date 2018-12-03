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
import com.judith.h.projetdevandroid.DecksDataBaseHelper;
import com.judith.h.projetdevandroid.R;

import java.util.ArrayList;


public class EditorRecyclerAdapter extends RecyclerView.Adapter<EditorRecyclerAdapter.FilterViewHolder>{
    private Filter[] filters;
    private Deck deck;
    private String deckPart;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

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

            filterName.setText(filter.getFilterName());

            if(filter.getLvAdapter() == null){

                filter.setLvAdapter( new ArrayAdapter<String>(
                        filterListView.getContext(), R.layout.card_list_item ));
            }

            filter.setCards(deck.getMain());
            filterListView.setAdapter(filter.getLvAdapter());


            updateListElementsTotalHeight(filter.getLvAdapter(), filterListView);

            filterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(view.getContext(),"Pressed " + filterListView.getAdapter().getItem(position),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), CardActivity.class);
                    intent.putExtra("cardName",(String)filterListView.getAdapter().getItem(position));
                    intent.putExtra("card_id",filter.getCardIdByCardName((String)filterListView.getAdapter().getItem(position)));
                    Log.i("JH", "recycler : id : " + filter.getCardIdByCardName((String)filterListView.getAdapter().getItem(position)));
                    view.getContext().startActivity(intent);
                }
            });
        }

        private void updateListElementsTotalHeight(ArrayAdapter tableau, ListView lv){
            int totalHeight = 0;
            for (int i = 0; i < tableau.getCount(); i++) {
                View mView = tableau.getView(i, null, lv);

                mView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                totalHeight += mView.getMeasuredHeight();
                Log.w("HEIGHT" + i, String.valueOf(totalHeight));
            }

            ViewGroup.LayoutParams params = lv.getLayoutParams();
            params.height = totalHeight
                    + (filterListView.getDividerHeight() * (tableau.getCount() - 1));


            filterListView.setLayoutParams(params);
            filterListView.requestLayout();

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    EditorRecyclerAdapter(Deck deck, Filter[] filters, String deckPart) {
        this.filters = filters;
        this.deck = deck;
        this.deckPart = deckPart;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public EditorRecyclerAdapter.FilterViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtered_cards, parent, false);
        FilterViewHolder vh = new FilterViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final FilterViewHolder holder, int position) {

        Filter filter = filters[position];

       holder.bind(filter);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView tv = (TextView) holder.mView.findViewById(R.id.filter_name);
        tv.setText(filter.getFilterName());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = filters[holder.getAdapterPosition()].isExpanded();
                filters[holder.getAdapterPosition()].setExpanded(!expanded);
                notifyItemChanged(holder.getAdapterPosition());

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filters.length;
    }


}
