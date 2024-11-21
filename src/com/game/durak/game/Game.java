package com.game.durak.game;

import com.game.durak.cards.Card;
import com.game.durak.cards.Deck;
import com.game.durak.player.Player;

import java.util.List;
import java.util.Scanner;

public class Game {
    private final Deck deck;
    private final List<Player> players;
    private final Table table;
    private final Scanner scanner = new Scanner(System.in);
    private int currentPlayerIndex = 0;

    public Game(List<Player> players) {
        this.deck = new Deck();
        this.players = players;
        Card trumpCard = deck.dealCard();
        System.out.println("Trump suit: " + trumpCard.suit());
        this.table = new Table(trumpCard.suit());
        dealInitialCards(trumpCard);
    }

    private void dealInitialCards(Card trumpCard) {
        for (int i = 0; i < 6; i++) {
            for (Player player : players) {
                if (!deck.isEmpty()) {
                    player.addCard(deck.dealCard());
                }
            }
        }
        deck.shuffle();
        deck.addCardToBottom(trumpCard);
    }

    public void playGame() {
        while (!isGameOver()) {
            Player attacker = players.get(currentPlayerIndex);
            Player defender = getNextPlayer(currentPlayerIndex);

            System.out.println("\n" + attacker.getName() + "'s turn to attack.");
            attack(attacker, defender);

            if (!table.isDefended()) {
                System.out.println(defender.getName() + " takes all cards.");
                defender.addCards(table.getAllCards());
                table.clear();
                refillHand(attacker);
                continue;
            }

            System.out.println(defender.getName() + " successfully defended.");
            table.clear();
            refillHands();

            currentPlayerIndex = players.indexOf(defender);
        }

        announceWinner();
    }

    private Player getNextPlayer(int currentIndex) {
        return players.get((currentIndex + 1) % players.size());
    }

    private void attack(Player attacker, Player defender) {
        System.out.println(attacker);
        int attackIndex = getInput("Choose card to attack: ", attacker.getHand().size());
        Card attackCard = attacker.playCard(attackIndex);
        table.addAttackCard(attackCard);
        System.out.println(attacker.getName() + " attacks with " + attackCard);

        // Подбрасывание карт
        for (int i = 1; i < players.size(); i++) {
            Player nextPlayer = players.get((currentPlayerIndex + i) % players.size());
            if (nextPlayer.equals(defender)) break;

            System.out.println(nextPlayer.getName() + "'s turn to throw cards.");
            System.out.println(nextPlayer);

            while (table.canThrowMoreCards(6)) {
                int throwIndex = getInput("Choose card to throw or -1 to skip: ", nextPlayer.getHand().size());
                if (throwIndex == -1) break;

                Card throwCard = nextPlayer.playCard(throwIndex);
                if (table.getAllCards().stream().anyMatch(card -> card.rank() == throwCard.rank())) {
                    table.addAttackCard(throwCard);
                    System.out.println(nextPlayer.getName() + " throws " + throwCard);
                } else {
                    System.out.println("Invalid card to throw. Try again.");
                    nextPlayer.addCard(throwCard);
                }
            }
        }

        defend(defender, attackCard);
    }

    private void defend(Player defender, Card attackCard) {
        System.out.println(defender);

        while (true) {
            int defenseIndex = getInput("Choose card to defend or -1 to take all cards: ", defender.getHand().size());
            if (defenseIndex == -1) {
                break; // Игрок решил взять карты
            }

            Card defenseCard = defender.playCard(defenseIndex);
            if (!table.canDefend(attackCard, defenseCard)) {
                System.out.println("Invalid defense! Try again.");
                defender.addCard(defenseCard);
            } else {
                table.addDefenseCard(defenseCard);
                System.out.println(defender.getName() + " defends with " + defenseCard);
                break;
            }
        }
    }

    private void refillHands() {
        players.stream()
                .filter(player -> !player.hasNoCards())
                .forEach(this::refillHand);
    }

    private void refillHand(Player player) {
        while (player.getHand().size() < 6 && !deck.isEmpty()) {
            player.addCard(deck.dealCard());
        }
    }

    private int getInput(String prompt, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input >= -1 && input < max) {
                    return input;
                }
            }
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine();
        }
    }

    private boolean isGameOver() {
        return players.stream().filter(player -> !player.hasNoCards()).count() == 1;
    }

    private void announceWinner() {
        players.stream()
                .filter(Player::hasNoCards)
                .findFirst()
                .ifPresent(winner -> System.out.println(winner.getName() + " wins!"));
    }
}