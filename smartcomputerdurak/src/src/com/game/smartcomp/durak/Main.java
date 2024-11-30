package src.com.game.smartcomp.durak;

import com.game.dumbcomp.durak.game.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.setupGame();
        game.startGame();
    }
}