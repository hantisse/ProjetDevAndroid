package com.judith.h.projetdevandroid;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class EditorRecyclerAdapter extends RecyclerView.Adapter<EditorRecyclerAdapter.FilterViewHolder>{
    private Filter[] filters;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class FilterViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public FilterViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EditorRecyclerAdapter(Filter[] filters) {
        this.filters = filters;
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
    public void onBindViewHolder(FilterViewHolder holder, final int position) {

        final Filter filter = filters[position];

        final ListView lv = (ListView)holder.mView.findViewById(R.id.filtered_list);
        boolean expanded = filter.isExpanded();

        lv.setVisibility(expanded ? View.VISIBLE : View.GONE);

        ArrayAdapter<String> tableau = new ArrayAdapter<String>(
                lv.getContext(), R.layout.card_list_item);

        for(String cardName: filter.getFilterCardList()) {
            for (int k = 0; k<40; k++){
                tableau.add(cardName + " " + k);
            }
        }

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
                + (lv.getDividerHeight() * (tableau.getCount() - 1));
        lv.setLayoutParams(params);
        lv.requestLayout();

        lv.setAdapter(tableau);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView tv = (TextView) holder.mView.findViewById(R.id.filter_name);
        tv.setText(filter.getFilterName());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = filter.isExpanded();
                filter.setExpanded(!expanded);
                Log.i("JH", filter.getFilterName());

                notifyItemChanged(position);

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filters.length;
    }
}
