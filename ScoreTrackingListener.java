package score;

import collision.HitListener;
import game.Block;
import geometry.Ball;
import util.Counter;

/**
 * This class to keep track of the user's score.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * Constructor for the listener.
     *
     * @param scoreCounter - a counter for the user's score.
     */
    public ScoreTrackingListener(Counter scoreCounter) {

        this.currentScore = scoreCounter;
    }

    /**
     * Add points to the score for clearing all the blocks.
     */
    public void levelCleared() {

        this.currentScore.increase(100);
    }

    /**
     * Handles a hit event on a block.
     * Increases the current score when the block is hit,
     * as long as the block is not marked for removal.
     *
     * @param beingHit the block that was hit
     * @param hitter the ball that hit the block
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        if (!beingHit.isBlockRemoved()) {
            this.currentScore.increase(5);
        }
    }

    /**
     * Returns a string representation of the current score
     * @return a formatted string representing the score
     */
    @Override
    public String toString() {

        return "Score: " + this.currentScore.toString();
    }
}
