package com.judith.h.projetdevandroid.editor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.judith.h.projetdevandroid.R;

@SuppressLint("ValidFragment")
public class EditorFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnEditorFragmentUpdatedListener mCallback;

    public EditorFragment(){
    }

    public interface OnEditorFragmentUpdatedListener {
        public void onCardAdded();
    }

    public void setOnEditorFragmentUpdatedListener(Activity activity) {
        mCallback = (OnEditorFragmentUpdatedListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setOnEditorFragmentUpdatedListener((DeckEditor)getActivity());

        View rootView = inflater.inflate(
                R.layout.fragment_collection_object, container, false);

        rootView.findViewById(R.id.add_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddCardActivity.class);
                getActivity().startActivityForResult(intent, 4); //request code 4 : cartes à ajouter au deck

            }
        });

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.editor_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        Filter filter1 = new Filter("FILTRE1");
        Filter filter2 = new Filter("FILTRE2");
        Filter[] filters = new Filter[]{filter1, filter2};
        mAdapter = new EditorRecyclerAdapter(filters);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public void updateCardList(){
        //faire qqchose avec mAdapter
    }

}
