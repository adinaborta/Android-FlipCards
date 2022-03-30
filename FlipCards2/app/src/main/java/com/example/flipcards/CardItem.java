package com.example.flipcards;

public class CardItem {
    private String cardTitleEnglish;
    private String cardTitleGerman;
    private int cardImage;

    public CardItem(String cardTitleEnglish, String cardTitleGerman, int cardImage) {
        this.cardTitleEnglish = cardTitleEnglish;
        this.cardTitleGerman = cardTitleGerman;
        this.cardImage = cardImage;
    }

    public CardItem() {
    }

    public String getCardTitleEnglish() {
        return cardTitleEnglish;
    }

    public void setCardTitleEnglish(String cardTitleEnglish) {
        this.cardTitleEnglish = cardTitleEnglish;
    }

    public String getCardTitleGerman() {
        return cardTitleGerman;
    }

    public void setCardTitleGerman(String cardTitleGerman) {
        this.cardTitleGerman = cardTitleGerman;
    }

    public int getCardImage() {
        return cardImage;
    }

    public void setCardImage(int cardImage) {
        this.cardImage = cardImage;
    }
}
