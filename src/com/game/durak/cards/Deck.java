package com.game.durak.cards;

import com.game.durak.cards.enums.Rank;
import com.game.durak.cards.enums.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck.");
        }
        return cards.remove(cards.size() - 1);
    }

    public void addCardToBottom(Card card) {
        cards.add(0, card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}