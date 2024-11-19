package com.game.durak.player;

import com.game.durak.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public Card playCard(int index) {
        return hand.remove(index);
    }

    public boolean hasNoCards() {
        return hand.isEmpty();
    }

    @Override
    public String toString() {
        return name + "'s hand: " + hand;
    }
}
