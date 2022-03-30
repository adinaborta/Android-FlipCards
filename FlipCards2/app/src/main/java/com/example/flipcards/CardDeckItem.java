package com.example.flipcards;

import java.util.ArrayList;

public class CardDeckItem {
    private String cardDeckTitle;
    private String cardDeckDescription;
    private int cardDeckImage;
    private ArrayList<CardItem> cardItems;

    public CardDeckItem(String cardDeckTitle, String cardDeckDescription, int cardDeckImage) {
        this.cardDeckTitle = cardDeckTitle;
        this.cardDeckDescription = cardDeckDescription;
        this.cardDeckImage = cardDeckImage;
    }

    public String getCardDeckTitle() {
        return cardDeckTitle;
    }

    public String getCardDeckDescription() {
        return cardDeckDescription;
    }

    public int getCardDeckImage() {
        return cardDeckImage;
    }

    public void setCardImage(int cardImage) {
        this.cardDeckImage = cardImage;
    }

    public void setCardDeckDescription(String cardDeckDescription) {
        this.cardDeckDescription = cardDeckDescription;
    }

    public void setCardDeckTitle(String cardDeckTitle) {
        this.cardDeckTitle = cardDeckTitle;
    }
}
