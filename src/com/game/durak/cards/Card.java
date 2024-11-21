package com.game.durak.cards;

import com.game.durak.cards.enums.Rank;
import com.game.durak.cards.enums.Suit;

public record Card(Suit suit, Rank rank) {

    public boolean beats(Card other, Suit trumpSuit) {
        if (this.suit == other.suit && this.rank.ordinal() > other.rank.ordinal()) {
            return true;
        }
        return this.suit == trumpSuit && other.suit != trumpSuit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
