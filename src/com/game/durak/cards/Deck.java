package com.game.durak.cards;

import com.game.durak.cards.enums.Rank;
import com.game.durak.cards.enums.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        cards.addAll(
                Stream.of(Suit.values())
                        .flatMap(suit -> Stream.of(Rank.values()).map(rank -> new Card(suit, rank)))
                        .toList()
        );
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Optional<Card> draw() {
        return cards.isEmpty() ? Optional.empty() : Optional.of(cards.remove(0));
    }
}