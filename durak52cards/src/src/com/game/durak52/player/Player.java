package src.com.game.durak52.player;

import src.com.game.durak52.cards.Card;

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
        return hand;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public void printHand() {
        System.out.println(name + "'s Hand: ");
        hand.forEach(card -> System.out.print("[" + card + "] "));
        System.out.println();
    }

    public boolean hasNoCards() {
        return hand.isEmpty();
    }
}