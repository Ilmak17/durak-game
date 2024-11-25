package com.game.durak.player;

import com.game.durak.cards.Card;
import com.game.durak.cards.enums.Suit;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final boolean isHuman;
    private final List<Card> hand = new ArrayList<>();

    public Player(String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
    }

    public String getName() {
        return name;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public Card chooseCardToAttack() {
        return isHuman ? hand.get(0) : hand.stream().findFirst().orElse(null);
    }

    public Card chooseCardToDefend(Card attackCard, Suit trumpSuit) {
        return hand.stream()
                .filter(card -> canDefend(card, attackCard, trumpSuit))
                .findFirst()
                .orElse(null);
    }

    private boolean canDefend(Card defenderCard, Card attackCard, Suit trumpSuit) {
        if (defenderCard.suit() == attackCard.suit()) {
            return Integer.valueOf(defenderCard.rank().getSymbol()) > Integer.valueOf(attackCard.rank().getSymbol());
        }
        return defenderCard.suit() == trumpSuit;
    }

    public void printHand() {
        System.out.println(name + "'s Hand: ");
        hand.forEach(card -> System.out.print("[" + card + "] "));
        System.out.println();
    }

    @Override
    public String toString() {
        return name + " (Human: " + isHuman + ")";
    }
}