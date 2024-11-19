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
        if (defenseCard.getSuit() == attackCard.getSuit() && defenseCard.getRank().ordinal() > attackCard.getRank().ordinal()) {
            return true;
        }
        return defenseCard.getSuit() == trumpSuit && attackCard.getSuit() != trumpSuit;
    }

    public boolean isDefended() {
        return attackCards.size() == defenseCards.size();
    }

    public void clear() {
        attackCards.clear();
        defenseCards.clear();
    }

    public List<Card> getAllCards() {
        List<Card> all = new ArrayList<>(attackCards);
        all.addAll(defenseCards);
        return all;
    }
}
