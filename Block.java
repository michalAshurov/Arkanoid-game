package game;

import biuoop.DrawSurface;
import geometry.Ball;
import geometry.Point;
import geometry.Rectangle;
import graphics.Sprite;
import collision.Collidable;
import collision.HitListener;
import collision.HitNotifier;
import physics.Velocity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represent a block on the game.
 */
public class Block implements Collidable, Sprite, HitNotifier {

    private Rectangle rectangle;
    private Color color;
    private List<HitListener> hitListeners = new ArrayList<>();
    private boolean blockRemoved;

    /**
     * Constructor for the block class.
     * @param rectangle - rectangle that the block is made of.
     * @param color - color that the block will be filled by.
     */
    public Block(Rectangle rectangle, Color color) {
        this.rectangle = rectangle;
        this.color = color;
        this.blockRemoved = false;
    }

    /**
     * Check if this block already removed from the game.
     * @return - true if this block already removed from the game.
     */
    public boolean isBlockRemoved() {

        return this.blockRemoved;
    }

    /**
     * Set this block as a block that removed from the game.
     * @param isBlockRemoved - true if this block should be removed.
     */
    public void setBlockRemoved(boolean isBlockRemoved) {

        this.blockRemoved = isBlockRemoved;
    }

    /**
     * Add the block to the game a sprite and a collidable object.
     * @param g - the game reference we add to.
     */
    public void addToGame(Game g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    /**
     * Add the background as only a sprite to the game.
     * @param g - the game reference we add to.
     */
    public void addBackground(Game g) {

        g.addSprite(this);
    }

    /**
     * Check if this block and the provided ball have the same color.
     * @param ball - ball to check it's color.
     * @return - true if the block and ball has the same color, otherwise false.
     */
    public boolean ballColorMatch(Ball ball) {

        return this.getColor().equals(ball.getColor());
    }

    /**
     * Function to remove this block from the game.
     * @param game - game reference to remove the block from.
     */
    public void removeFromGame(Game game) {
        if (game != null) {
            game.removeCollidable(this);
            game.removeSprite(this);
            for (int i = 0; i < this.hitListeners.size(); i++) {
                removeHitListener(this.hitListeners.get(i));
            }
        }
    }

    /**
     * Function to update all hit listeners upon a hit.
     * @param hitter - the ball that hit the block.
     */
    private void notifyHit(Ball hitter) {
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * @return the collision rectangle of the block
     */
    @Override
    public Rectangle getCollisionRectangle() {

        return rectangle;
    }

    /**
     * Returns the block involved in the collision.
     * @return this block
     */
    @Override
    public Block getCollisionBlock() {

        return this;
    }

    /**
     * Getter for the block's color.
     * @return - color of the block.
     */
    public Color getColor() {
        return color;
    }


    /**
     * Handles a collision between a ball and this block.
     * Determines which edge of the block was hit using the collision point,
     * updates the ball's velocity accordingly by reversing its direction,
     * and notifies listeners if the ball color does not match the block color.
     *
     * @param hitter the ball that hit the block
     * @param collisionPoint the point where the collision occurred
     * @param currentVelocity the ball's velocity before the collision
     * @return the new velocity of the ball after the collision
     * @throws IllegalArgumentException if collisionPoint or currentVelocity is null
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        if ((collisionPoint == null) || (currentVelocity == null)) {
            throw new IllegalArgumentException("Null exception hit function");
        }
        double dx = currentVelocity.getDx(), dy = currentVelocity.getDy();
        //Check horizontal lines collision.
        if (this.rectangle.getTopLine().isOnLine(collisionPoint)
                || this.rectangle.getBottomLine().isOnLine(collisionPoint)) {
            dy *= -1;
        }
        //Check vertical lines collision.
        if (this.rectangle.getLeftLine().isOnLine(collisionPoint)
                || this.rectangle.getRightLine().isOnLine(collisionPoint)) {
            dx *= -1;
        }
        //Remove the ball if the color of the ball is different from the block.
        if (!ballColorMatch(hitter)) {
            this.notifyHit(hitter);
        }
        return new Velocity(dx, dy);
    }

    /**
     * Draws the block on the given drawing surface.
     * @param d the drawing surface on which the block is drawn
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.fillRectangle((int) rectangle.getUpperLeft().getX(), (int) rectangle.getUpperLeft().getY(),
                (int) rectangle.getWidth(), (int) rectangle.getHeight());
        d.setColor(Color.black);
        d.drawRectangle((int) rectangle.getUpperLeft().getX(), (int) rectangle.getUpperLeft().getY(),
                (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }

    /**
     * Updates the block state over time.
     */
    @Override
    public void timePassed() {

    }

    /**
     * Returns a string representation of the block,
     * including its rectangle and color.
     *
     * @return a string describing the block
     */
    @Override
    public String toString() {

        return "Block [rectangle=" + rectangle + ", color=" + color + "]";
    }

    /**
     * Adds a hit listener to this block.
     * The listener will be notified when the block is hit.
     *
     * @param hl the hit listener to add
     */
    @Override
    public void addHitListener(HitListener hl) {
        if (hl != null) {
            this.hitListeners.add(hl);
        }
    }

    /**
     * Removes a hit listener from this block.
     * The listener will no longer be notified when the block is hit.
     *
     * @param hl the hit listener to remove
     */
    @Override
    public void removeHitListener(HitListener hl) {
        if (hl != null) {
            this.hitListeners.remove(hl);
        }
    }
}