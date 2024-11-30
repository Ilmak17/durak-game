package src.com.game.smartcomp.durak.game;

import com.game.dumbcomp.durak.cards.Card;
import com.game.dumbcomp.durak.cards.Deck;
import com.game.dumbcomp.durak.cards.enums.Suit;
import com.game.dumbcomp.durak.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Deck deck = new Deck();
    private final List<Player> players = new ArrayList<>();
    private final List<Card> table = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();
    private Suit trumpSuit;
    private final Scanner scanner = new Scanner(System.in);

    public void setupGame() {
        System.out.println("=== Welcome to Durak ===");

        System.out.print("Enter your name: ");
        String name = scanner.next();
        players.add(new Player(name, true));

        players.add(new Player("Bot", false));

        deck.shuffle();
        determineTrumpSuit();
        dealInitialCards();
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
        Player defender = players.get(1);

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
        } else {
            System.out.println(defender.getName() + " successfully defended!");
            discardPile.addAll(attackCards);
            discardPile.addAll(table);
        }

        table.clear();
        drawCards();

        rotatePlayers();
    }

    private List<Card> chooseAttackCards(Player attacker, int maxCards) {
        if (!attacker.isHuman()) {
            return chooseRandomAttackCards(attacker, maxCards);
        }

        System.out.print("Enter card positions to attack (comma-separated, e.g., 1,2): ");
        String input = scanner.next();
        return Arrays.stream(input.split(","))
                .map(pos -> Integer.parseInt(pos.trim()) - 1)
                .filter(index -> index >= 0 && index < attacker.getHand().size())
                .limit(maxCards)
                .map(attacker.getHand()::get)
                .peek(table::add)
                .peek(attacker::removeCard)
                .toList();
    }

    private List<Card> chooseRandomAttackCards(Player attacker, int maxCards) {
        Random random = new Random();
        int numCards = Math.min(maxCards, attacker.getHand().size());
        List<Card> attackCards = new ArrayList<>();

        while (attackCards.size() < numCards) {
            int index = random.nextInt(attacker.getHand().size());
            Card card = attacker.getHand().get(index);
            if (!attackCards.contains(card)) {
                attackCards.add(card);
                table.add(card);
                attacker.removeCard(card);
            }
        }

        return attackCards;
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
            return chooseRandomDefense(defender, attackCard);
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

    private boolean chooseRandomDefense(Player defender, Card attackCard) {
        Optional<Card> validDefense = defender.getHand().stream()
                .filter(card -> canDefend(card, attackCard))
                .findFirst();

        if (validDefense.isPresent()) {
            Card defenseCard = validDefense.get();
            defender.removeCard(defenseCard);
            table.add(defenseCard);
            System.out.println(defender.getName() + " defends with " + defenseCard);
            return true;
        }

        System.out.println(defender.getName() + " cannot defend against " + attackCard);
        return false;
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