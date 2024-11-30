package src.com.game.smartcomp.durak;


import src.com.game.smartcomp.durak.game.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.setupGame();
        game.startGame();
    }
}