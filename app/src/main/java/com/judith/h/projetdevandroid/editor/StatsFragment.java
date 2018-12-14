package com.judith.h.projetdevandroid.editor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.judith.h.projetdevandroid.Card;
import com.judith.h.projetdevandroid.Deck;
import com.judith.h.projetdevandroid.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class StatsFragment extends Fragment {

    private Deck deck;
    private TextView manaCurveTv;
    private TextView deckCostTv;

    public StatsFragment (Deck deck){
        this.deck = deck;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated properly.
        View rootView = inflater.inflate(R.layout.deck_stats, container, false);

        String manacurvevalue = getManaCurve();
        manaCurveTv = rootView.findViewById(R.id.manacurve);
        manaCurveTv.setText(manacurvevalue);

        deckCostTv = rootView.findViewById(R.id.deckcost);
        deckCostTv.setText(getContext().getString(R.string.deckcost, deck.getDeckPrice()));
        return rootView;
    }
    public String getManaCurve(){
        ArrayList<Integer> manacurve = new ArrayList<>();
        for (Card card:deck.getMain()){
            if (card.getCmc() >= manacurve.size()){
                while (manacurve.size() != card.getCmc()){
                    manacurve.add(0);
                }
                manacurve.add(deck.getMainMultiplicities().get(card));
            } else {
                manacurve.set(card.getCmc(),manacurve.get(card.getCmc()) + deck.getMainMultiplicities().get(card));
            }
        }
        String manacurvevalue = "";
        int i = 0;
        while (i<manacurve.size()) {
            if (i<10){
                manacurvevalue += "0";
            }
            manacurvevalue += i + "  ";
            int j=0;
            while (j<manacurve.get(i)){
                manacurvevalue += "| ";
                j += 1;
            }
            //manacurvevalue += "(" + j + ")";
            manacurvevalue += "\n";
            i += 1;
        }
        return manacurvevalue;
    }

    public void updateManaCurveTv(){
        manaCurveTv.setText(getManaCurve());
    }

    public void updateDeckPriceTv(){
        deckCostTv.setText(getContext().getString(R.string.deckcost, deck.getDeckPrice()));
    }

}
