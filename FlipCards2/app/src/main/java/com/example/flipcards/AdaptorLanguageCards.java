package com.example.flipcards;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class AdaptorLanguageCards extends RecyclerView.Adapter<AdaptorLanguageCards.MyViewHolder> implements Filterable{
    private ArrayList<CardDeckItem> cardDecksList;
    private ArrayList<CardDeckItem> cardDecksListFull;

    public static OnCardDeckClickListener cardDeckClickListener;

    public AdaptorLanguageCards(ArrayList<CardDeckItem> cardDecksList, OnCardDeckClickListener listener) {
        this.cardDecksList = cardDecksList;
        this.cardDecksListFull = new ArrayList<>(cardDecksList);
        cardDeckClickListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView cardDeckTitle;
        private final ImageView cardDeckImage;
        private final CardView cardDeckContainer;
        private final ConstraintLayout  cardDeckConstraintContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardDeckTitle = itemView.findViewById(R.id.cardDeckTitle);
            cardDeckImage = itemView.findViewById(R.id.cardDeckImage);
            cardDeckContainer = itemView.findViewById(R.id.cardDeckContainer);
            cardDeckConstraintContainer = itemView.findViewById(R.id.cardDeckConstraintContainer);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language_card_deck, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CardDeckItem cardDeckItem = cardDecksList.get(position);
        holder.cardDeckTitle.setText(cardDeckItem.getCardDeckTitle());
        holder.cardDeckImage.setImageResource(cardDeckItem.getCardDeckImage());
        if((position+1)%4 >= 2){
            holder.cardDeckConstraintContainer.setBackgroundResource(R.color.dusty_purple);
            holder.cardDeckTitle.setTextColor(Color.WHITE);
        }
        holder.cardDeckContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardDeckClickListener.onCardDeckClick(cardDeckItem);
            }
        });
    }
    @Override
    public int getItemCount() {
        return cardDecksList.size();
    }

    @Override
    public Filter getFilter() {
        return cardDecksFilter;
    }

    private Filter cardDecksFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<CardDeckItem> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(cardDecksListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(CardDeckItem item: cardDecksListFull){
                    if(item.getCardDeckTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            cardDecksList.clear();
            cardDecksList.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

}
