package com.game.dumbcomp.durak.cards;

import com.game.dumbcomp.durak.cards.enums.Rank;
import com.game.dumbcomp.durak.cards.enums.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Optional<Card> draw() {
        return cards.isEmpty() ? Optional.empty() : Optional.of(cards.remove(0));
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}