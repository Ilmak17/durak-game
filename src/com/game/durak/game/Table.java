package com.game.durak.game;

import com.game.durak.cards.Card;
import com.game.durak.cards.enums.Suit;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final List<Card> attackCards = new ArrayList<>();
    private final List<Card> defenseCards = new ArrayList<>();
    private final Suit trumpSuit;

    public Table(Suit trumpSuit) {
        this.trumpSuit = trumpSuit;
    }

    public void addAttackCard(Card card) {
        attackCards.add(card);
    }

    public void addDefenseCard(Card card) {
        defenseCards.add(card);
    }

    public boolean canDefend(Card attackCard, Card defenseCard) {
        return defenseCard.beats(attackCard, trumpSuit);
    }

    public boolean isDefended() {
        return attackCards.size() == defenseCards.size();
    }

    public List<Card> getAllCards() {
        List<Card> all = new ArrayList<>(attackCards);
        all.addAll(defenseCards);
        return all;
    }

    public void clear() {
        attackCards.clear();
        defenseCards.clear();
    }

    public boolean canThrowMoreCards(int maxCards) {
        return attackCards.size() + defenseCards.size() < maxCards;
    }
}
