package src.com.game.durak52.game;

import src.com.game.durak52.player.Player;
import src.com.game.durak52.cards.Card;
import src.com.game.durak52.cards.Deck;
import src.com.game.durak52.cards.enums.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {
    private final Deck deck = new Deck();
    private final List<Player> players = new ArrayList<>();
    private final List<Card> table = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();
    private final Set<Player> skippedPlayers = new HashSet<>();
    private Suit trumpSuit;
    private final Scanner scanner = new Scanner(System.in);

    public void setupGame() {
        System.out.println("=== Welcome to Durak ===");
        int numPlayers = getInput("Enter the number of players (2-6): ");

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for player " + i + ": ");
            String name = scanner.next();
            players.add(new Player(name, true));
        }

        deck.shuffle();
        determineTrumpSuit();
        dealInitialCards();
    }

    private int getInput(String prompt) {
        int input;
        do {
            System.out.print(prompt);
            input = scanner.nextInt();
        } while (input < 2 || input > 6);
        return input;
    }

    private void determineTrumpSuit() {
        deck.draw().ifPresent(card -> trumpSuit = card.getSuit());
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
        endGame();
    }

    private void endGame() {
        System.out.println("\n=== Game Over ===");
        players.stream()
                .filter(player -> !player.hasNoCards())
                .forEach(player -> System.out.println(player.getName() + " is the Durak!"));
    }

    private boolean isGameOver() {
        long activePlayers = players.stream().filter(player -> !player.hasNoCards()).count();
        return activePlayers <= 1 && deck.isEmpty();
    }

    private void executeTurn() {
        Player attacker = players.get(0);
        Player defender = findNextDefender();

        System.out.println("\n" + attacker.getName() + " attacks " + defender.getName());
        attacker.printHand();

        List<Card> attackCards = chooseAttackCards(attacker, defender.getHand().size());
        System.out.println(attacker.getName() + " attacks with " + attackCards);

        boolean defenderTakesCards = !defendCards(defender, attackCards);

        if (defenderTakesCards) {
            System.out.println(defender.getName() + " cannot defend and takes the cards.");

            if (defender.getHand().size() >= 6) {
                defender.getHand().addAll(attackCards);
            } else {
                defender.getHand().addAll(attackCards);
                defender.getHand().addAll(table);
            }

            skippedPlayers.add(defender);
        } else {
            System.out.println(defender.getName() + " successfully defended!");
            discardPile.addAll(attackCards);
            discardPile.addAll(table);
        }

        table.clear();
        drawCards();

        if (!defenderTakesCards) {
            rotatePlayers();
        }
    }

    private Player findNextDefender() {
        for (int i = 1; i < players.size(); i++) {
            Player potentialDefender = players.get(i);
            if (!skippedPlayers.contains(potentialDefender)) {
                return potentialDefender;
            }
        }
        skippedPlayers.clear();
        return players.get(1);
    }

    private List<Card> chooseAttackCards(Player attacker, int maxCards) {
        if (!attacker.isHuman()) {
            return List.of(attacker.getHand().get(0));
        }

        System.out.print("Enter card positions to attack (comma-separated, e.g., 1,2): ");
        String input = scanner.next();
        return Arrays.stream(input.split(","))
                .map(pos -> Integer.parseInt(pos.trim()) - 1)
                .filter(index -> index >= 0 && index < attacker.getHand().size())
                .limit(maxCards)
                .map(attacker.getHand()::get)
                .peek(card -> table.add(card))
                .peek(attacker::removeCard)
                .collect(Collectors.toList());
    }

    private boolean defendCards(Player defender, List<Card> attackCards) {
        for (Card attackCard : attackCards) {
            if (!defendSingleCard(defender, attackCard)) {
                return false;
            }
        }
        return true;
    }

    private boolean defendSingleCard(Player defender, Card attackCard) {
        if (!defender.isHuman()) {
            return defender.getHand().stream()
                    .filter(card -> canDefend(card, attackCard))
                    .findFirst()
                    .map(card -> {
                        defender.removeCard(card);
                        table.add(card);
                        return true;
                    }).orElse(false);
        }

        defender.printHand();
        System.out.print("Choose a card to defend against " + attackCard + " (or 0 to give up): ");
        int index = scanner.nextInt() - 1;

        if (index == -1) return false;

        Card defenseCard = defender.getHand().get(index);
        if (canDefend(defenseCard, attackCard)) {
            defender.removeCard(defenseCard);
            table.add(defenseCard);
            return true;
        }

        System.out.println("Invalid defense! Try again.");
        return defendSingleCard(defender, attackCard);
    }

    private boolean canDefend(Card defenderCard, Card attackCard) {
        if (defenderCard.getSuit() == attackCard.getSuit()) {
            return defenderCard.getRank().getValue() > attackCard.getRank().getValue();
        }
        return defenderCard.getSuit() == trumpSuit;
    }

    private void drawCards() {
        players.forEach(player -> {
            if (player.getHand().size() >= 6) {
                return;
            }

            while (player.getHand().size() < 6 && !deck.isEmpty()) {
                deck.draw().ifPresent(player::addCard);
            }
        });
    }

    private void rotatePlayers() {
        players.add(players.remove(0));
    }
}