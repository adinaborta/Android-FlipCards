package com.example.flipcards;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptorLanguageCards extends RecyclerView.Adapter<AdaptorLanguageCards.MyViewHolder>{
    private ArrayList<CardDeckItem> cardDecksList;

    public static OnCardDeckClickListener cardDeckClickListener;

    public AdaptorLanguageCards(ArrayList<CardDeckItem> cardDecksList, OnCardDeckClickListener listener) {
        this.cardDecksList = cardDecksList;
        cardDeckClickListener = listener;
    }

    public void filterList(ArrayList<CardDeckItem> filterList) {
        cardDecksList = filterList;
        notifyDataSetChanged();
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

}
