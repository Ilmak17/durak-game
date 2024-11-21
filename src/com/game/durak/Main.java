package com.game.durak;

import com.game.durak.game.Game;
import com.game.durak.player.Player;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        Player player3 = new Player("Player3");

        Game game = new Game(List.of(player1, player2, player3));
        game.playGame();
    }
}