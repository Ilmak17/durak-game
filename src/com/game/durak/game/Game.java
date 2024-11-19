package com.game.durak.game;

import com.game.durak.cards.Card;
import com.game.durak.cards.Deck;
import com.game.durak.cards.enums.Suit;
import com.game.durak.player.Player;

import java.util.List;
import java.util.Scanner;

public class Game {
    private final Deck deck = new Deck();
    private final List<Player> players;
    private final Table table;
    private final Suit trumpSuit;
    private int currentPlayerIndex = 0;

    public Game(List<Player> players) {
        this.players = players;
        this.deck.shuffle();
        Card trumpCard = deck.dealCard();
        this.trumpSuit = trumpCard.getSuit();
        System.out.println("Trump suit: " + trumpSuit);
        this.table = new Table(trumpSuit);
        dealInitialCards();
    }

    private void dealInitialCards() {
        for (int i = 0; i < 6; i++) {
            for (Player player : players) {
                player.addCard(deck.dealCard());
            }
        }
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Player attacker = players.get(currentPlayerIndex);
            Player defender = players.get((currentPlayerIndex + 1) % players.size());

            System.out.println("\n" + attacker.getName() + "'s turn to attack.");
            System.out.println(attacker);

            System.out.print("Choose card to attack: ");
            int attackIndex = scanner.nextInt();
            Card attackCard = attacker.playCard(attackIndex);
            table.addAttackCard(attackCard);
            System.out.println(attacker.getName() + " attacks with " + attackCard);

            System.out.println("\n" + defender.getName() + "'s turn to defend.");
            System.out.println(defender);

            System.out.print("Choose card to defend or -1 to take all cards: ");
            int defenseIndex = scanner.nextInt();
            if (defenseIndex == -1) {
                defender.getHand().addAll(table.getAllCards());
                table.clear();
                nextTurn();
                continue;
            }
            Card defenseCard = defender.playCard(defenseIndex);

            if (!table.canDefend(attackCard, defenseCard)) {
                System.out.println("Invalid defense! " + defender.getName() + " takes all cards.");
                defender.getHand().addAll(table.getAllCards());
                table.clear();
            } else {
                table.addDefenseCard(defenseCard);
                System.out.println(defender.getName() + " defends with " + defenseCard);
            }

            if (table.isDefended()) {
                System.out.println(defender.getName() + " successfully defended.");
                table.clear();
            }

            nextTurn();

            if (isGameOver()) {
                System.out.println("\nGame over!");
                System.out.println(attacker.getName() + " wins!");
                break;
            }
        }

        scanner.close();
    }

    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private boolean isGameOver() {
        return players.stream().anyMatch(Player::hasNoCards);
    }
}