package com.game.durak.player;

import com.game.durak.cards.Card;

import java.util.List;

public class Player {
    private final List<Card> cards;
    private final String name;


    public Player(List<Card> cards, String name) {
        this.cards = cards;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public Card playCard(Card card) {
        if (cards.contains(card)) {
            cards.remove(card);
            return card;
        }
        return null;
    }

    public boolean hasNoCards() {
        return cards.isEmpty();
    }

}
