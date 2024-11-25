package com.game.durak.game;

import com.game.durak.cards.Card;
import com.game.durak.cards.Deck;
import com.game.durak.cards.enums.Suit;
import com.game.durak.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Game {
    private final Deck deck = new Deck();
    private final List<Player> players = new ArrayList<>();
    private Suit trumpSuit;
    private final Scanner scanner = new Scanner(System.in);

    public void setupGame() {
        System.out.println("=== Welcome to Durak ===");
        System.out.print("Enter the number of players (2-6): ");
        int numPlayers = scanner.nextInt();
        while (numPlayers < 2 || numPlayers > 6) {
            System.out.print("Invalid input. Enter a number between 2 and 6: ");
            numPlayers = scanner.nextInt();
        }

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for player " + i + ": ");
            String name = scanner.next();
            players.add(new Player(name, true));
        }

        deck.shuffle();
        determineTrumpSuit();
        dealInitialCards();
    }

    private void determineTrumpSuit() {
        deck.draw().ifPresent(card -> trumpSuit = card.suit());
        System.out.println("Trump suit: " + trumpSuit.getSymbol());
    }

    private void dealInitialCards() {
        for (int i = 0; i < 6; i++) {
            players.forEach(player -> deck.draw().ifPresent(player::addCard));
        }
    }

    public void startGame() {
        System.out.println("\n=== Game Starts! ===");
        while (!isGameOver()) {
            executeTurn();
        }
        System.out.println("\n=== Game Over ===");
        players.stream()
                .filter(player -> !player.getHand().isEmpty())
                .forEach(player -> System.out.println(player.getName() + " is the Durak!"));
    }

    private boolean isGameOver() {
        return players.stream().filter(player -> !player.getHand().isEmpty()).count() <= 1;
    }

    private void executeTurn() {
        Player attacker = players.get(0);
        Player defender = players.get(1);

        System.out.println("\n" + attacker.getName() + " attacks " + defender.getName());
        attacker.printHand();

        Card attackCard = chooseAttackCard(attacker);
        if (attackCard == null) return;

        System.out.println(attacker.getName() + " plays " + attackCard);
        attacker.removeCard(attackCard);

        Card defenseCard = chooseDefenseCard(defender, attackCard);

        if (defenseCard != null) {
            System.out.println(defender.getName() + " defends with " + defenseCard);
            defender.removeCard(defenseCard);
        } else {
            System.out.println(defender.getName() + " cannot defend and takes the card.");
            defender.addCard(attackCard);
        }

        players.add(players.remove(0));
    }

    private Card chooseAttackCard(Player attacker) {
        if (attacker.isHuman()) {
            System.out.print("Choose a card to attack with (1-" + attacker.getHand().size() + "): ");
            int attackIndex = scanner.nextInt() - 1;
            while (attackIndex < 0 || attackIndex >= attacker.getHand().size()) {
                System.out.print("Invalid choice. Choose again (1-" + attacker.getHand().size() + "): ");
                attackIndex = scanner.nextInt() - 1;
            }
            return attacker.getHand().get(attackIndex);
        }
        return attacker.chooseCardToAttack();
    }

    private Card chooseDefenseCard(Player defender, Card attackCard) {
        if (defender.isHuman()) {
            defender.printHand();
            System.out.print("Choose a card to defend (1-" + defender.getHand().size() + " or 0 to give up): ");
            int defenseIndex = scanner.nextInt() - 1;
            if (defenseIndex == -1) return null;
            while (defenseIndex < 0 || defenseIndex >= defender.getHand().size() ||
                    !canDefend(defender.getHand().get(defenseIndex), attackCard)) {
                System.out.print("Invalid choice. Choose again (1-" + defender.getHand().size() + "): ");
                defenseIndex = scanner.nextInt() - 1;
                if (defenseIndex == -1) return null;
            }
            return defender.getHand().get(defenseIndex);
        }
        return defender.chooseCardToDefend(attackCard, trumpSuit);
    }

    private boolean canDefend(Card defenderCard, Card attackCard) {
        if (defenderCard.suit() == attackCard.suit()) {
            return defenderCard.rank().ordinal() > attackCard.rank().ordinal();
        }
        return defenderCard.suit() == trumpSuit;
    }
}