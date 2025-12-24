package score;

import biuoop.DrawSurface;
import game.Game;
import graphics.Sprite;
import util.Counter;

import java.awt.*;

/**
 * This class to represent the score shown .
 */
public class ScoreIndicator implements Sprite {
    private Counter scoreCounter;

    /**
     * Constructor for the class.
     * @param scoreCounter - instance of the score counter.
     */
    public ScoreIndicator(Counter scoreCounter) {
        this.scoreCounter = scoreCounter;
    }

    /**
     * Getter for the score counter.
     * @return counter instance of the score.
     */
    public Counter getScoreCounter() {
        return scoreCounter;
    }

    /**
     * Add the score indicator to the game as a sprite.
     * @param g - instance of a game.
     */
    public void addToGame(Game g) {
        g.addSprite(this);
    }

    /**
     * Draws the score indicator on the given drawing surface.
     * Displays a background rectangle and the current score text
     * at the top of the screen.
     *
     * @param d the drawing surface on which the score is drawn
     */
    @Override
    public void drawOn(DrawSurface d) {
        // Draw score background
        d.setColor(Color.LIGHT_GRAY);
        d.fillRectangle(0, 0, 800, 20);

        // Draw score text
        d.setColor(Color.BLACK);
        String scoreText = "Score: " + this.scoreCounter.toString();
        d.drawText(10, 15, scoreText, 16);
    }

    @Override
    public void timePassed() {
    }
}
