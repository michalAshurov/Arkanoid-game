package graphics;

import collision.HitListener;
import game.Block;
import geometry.Ball;
import util.Counter;
import game.Game;

/**
 * This class removing blocks from the game.
 */
public class BlockRemover implements HitListener {
    private Game game;
    private Counter remainingBlocks;

    /**
     * Constructor for the class.
     * @param game - game reference the block is in.
     * @param remainingBlocks - number of remaining blocks in the game.
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    /**
     * Getter for the counter.
     * @return - counter instance of remaining blocks.
     */
    public Counter getRemainingBlocks() {

        return remainingBlocks;
    }

    /**
     * Handles a hit event on a block.
     * Updates the ball's color to match the block's color,
     * removes the block from the game,
     * and decreases the remaining blocks counter.
     *
     * @param beingHit the block that was hit
     * @param hitter the ball that hit the block
     */
    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.setColor(beingHit.getColor());
        beingHit.removeFromGame(this.game);
        this.remainingBlocks.decrease(1);
    }
}
