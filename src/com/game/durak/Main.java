package com.game.durak;

import com.game.durak.game.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.setupGame();
        game.startGame();
    }
}