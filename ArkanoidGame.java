
import game.Game;

/**
 *  main function game class.
 */
public class ArkanoidGame {

    public static void main(String[] args) {
        Game game = new Game();
        game.initializeNewGame();
        game.run();
    }
}