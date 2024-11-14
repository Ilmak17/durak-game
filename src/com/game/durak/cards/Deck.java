package com.game.durak.cards;

import com.game.durak.cards.enums.Rank;
import com.game.durak.cards.enums.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        Arrays.stream(Suit.values()).forEach(suit ->
                Arrays.stream(Rank.values())
                        .forEach(rank -> cards.add(new Card(suit, rank)))
        );
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
        }
        return cards.remove(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int getSizeOfCards() {
        return cards.size();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

}
