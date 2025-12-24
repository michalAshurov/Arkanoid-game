package geometry;

import biuoop.DrawSurface;
import collision.HitListener;
import collision.HitNotifier;
import game.Block;
import game.Game;
import game.GameEnvironment;
import graphics.Sprite;
import collision.CollisionInfo;
import physics.Velocity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * This class to represent a ball in the GUI.
 */
public class Ball implements Sprite, HitNotifier {
    private Point center;
    private int radius;
    private Color color;
    private Velocity velocity;
    private GameEnvironment environment;
    private List<HitListener> hitListeners = new ArrayList<>();

    // Boundary settings (no Constants usage)
    private int guiWidth;
    private int guiHeight;
    private int boundsWidth;
    private int boundsHeight;

    /**
     * Constructor for moving balls.
     * @param center - the center point of the ball
     * @param radius - the radius of the ball.
     * @param color - the color of the ball to be filled by.
     * @param velocity - starting velocity of the ball.
     */
    public Ball(Point center, int radius, Color color, Velocity velocity) {
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.velocity = velocity;
    }

    /**
     * Optional setter for GUI boundaries (call once during initialization).
     * @param guiWidth - GUI width.
     * @param guiHeight - GUI height.
     * @param boundsWidth - left/right boundary thickness.
     * @param boundsHeight - top boundary thickness.
     */
    public void setBoundaries(int guiWidth, int guiHeight, int boundsWidth, int boundsHeight) {
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.boundsWidth = boundsWidth;
        this.boundsHeight = boundsHeight;
    }

    /**
     * Get the x value of the center of this ball.
     * @return - x integer value of the center point.
     */
    public int getX() {

        return (int) this.center.getX();
    }

    /**
     * Get the y value of the center of this ball.
     * @return - y integer value of the center point.
     */
    public int getY() {

        return (int) this.center.getY();
    }

    /**
     * Get the radius size of this ball.
     * @return - integer value of this ball's radius.
     */
    public int getSize() {

        return this.radius;
    }

    /**
     * Get the color of the ball.
     * @return - color of the ball.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Setter for the ball's color.
     * @param color - new color for the ball.
     */
    public void setColor(Color color) {

        this.color = color;
    }

    /**
     * Get the ball's velocity variable.
     * @return - the ball's velocity.
     */
    public Velocity getVelocity() {

        return this.velocity;
    }

    /**
     * Get the game environment used by the ball.
     * @return - game environment of the ball.
     */
    public GameEnvironment getGameEnvironment() {

        return environment;
    }

    /**
     * Change the game environment variable of the ball.
     * @param gameEnvironment - new game environment for the ball.
     */
    public void setGameEnvironment(GameEnvironment gameEnvironment) {

        this.environment = gameEnvironment;
    }

    /**
     * Change the center point of the ball according to the current velocity.
     */
    public void moveOneStep() {
        //Make sure we have a game environment set for the ball.
        if (environment == null) {
            return;
        }
        //Calculate the trajectory of the ball and get info for potential collision.
        Line path = new Line(center, this.velocity.applyToPoint(center));
        CollisionInfo hitInfo = this.environment.getClosestCollision(path);
        if (hitInfo == null) {
            //No collision was detected, keep moving.
            this.center = this.getVelocity().applyToPoint(this.center);
        } else {
            //Check if we hit a death block.
            if (hitInfo.collisionObject().getCollisionBlock().isBlockRemoved()) {
                notifyExit(hitInfo.collisionObject().getCollisionBlock(), this);
            }
            // Calculate the new velocity after hitting an object.
            Velocity newVelocity = hitInfo.collisionObject().hit(this, hitInfo.collisionPoint(), this.velocity);
            // Adjust the position to be slightly away from the collision point.
            this.center = moveToCollision(hitInfo.collisionPoint(), this.velocity);
            this.velocity = newVelocity;
        }
        checkBoundaryCollision();
    }

    /**
     * Move the ball to collision point and adjust to prevent sticking to blocks.
     * @param collisionPoint - point of collision.
     * @param velocity - current velocity of the ball.
     * @return - the new center point of the ball.
     */
    public Point moveToCollision(Point collisionPoint, Velocity velocity) {
        double adjustedX = collisionPoint.getX();
        double adjustedY = collisionPoint.getY();
        if (velocity.getDx() < 0) {
            adjustedX += this.radius;
        } else if (velocity.getDx() > 0) {
            adjustedX -= this.radius;
        }
        if (velocity.getDy() < 0) {
            adjustedY += this.radius;
        } else if (velocity.getDy() > 0) {
            adjustedY -= this.radius;
        }
        return new Point(adjustedX, adjustedY);
    }

    /**
     * Check collision on GUI boundaries and adjust accordingly the center and velocity.
     */
    private void checkBoundaryCollision() {
        // If boundaries were not set, do nothing (prevents wrong behavior / zeros).
        if (guiWidth <= 0 || guiHeight <= 0) {
            return;
        }

        //Check top boundary.
        if (this.center.getY() - this.radius <= boundsHeight) {
            this.velocity.setDy(-this.velocity.getDy());
            this.center.setY(this.radius + boundsHeight);
        }
        //Check right boundary.
        if (this.center.getX() + this.radius + boundsWidth >= guiWidth) {
            this.velocity.setDx(-this.velocity.getDx());
            this.center.setX(guiWidth - this.radius - boundsWidth);
        }
        //Check left boundary.
        if (this.center.getX() - this.radius <= boundsWidth) {
            this.velocity.setDx(-this.velocity.getDx());
            this.center.setX(this.radius + boundsWidth);
        }
    }

    /**
     * Add the ball to the game as a sprite.
     * @param g - instance of a game.
     */
    public void addToGame(Game g) {
        g.addSprite(this);
    }

    /**
     * Function to update all hit listeners upon a ball's GUI exit.
     *
     * @param beingHit - the death block that the ball hit.
     * @param exitBall - the ball that exist the GUI.
     */
    private void notifyExit(Block beingHit, Ball exitBall) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(beingHit, exitBall);
        }
    }

    /**
     * Function to remove this ball from the game.
     * @param game - game reference to remove the ball from.
     */
    public void removeFromGame(Game game) {
        if (game != null) {
            game.removeSprite(this);
            for (int i = 0; i < this.hitListeners.size(); i++) {
                removeHitListener(this.hitListeners.get(i));
            }
        }
    }

    @Override
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(this.getX(), this.getY(), this.getSize());
        surface.setColor(Color.BLACK);
        surface.drawCircle(this.getX(), this.getY(), this.getSize());
    }

    @Override
    public void timePassed() {
        moveOneStep();
    }

    @Override
    public void addHitListener(HitListener hl) {
        if (hl != null) {
            this.hitListeners.add(hl);
        }
    }

    @Override
    public void removeHitListener(HitListener hl) {
        if (hl != null) {
            this.hitListeners.remove(hl);
        }
    }

    @Override
    public String toString() {
        return "center: " + center + ", r: " + radius + ", color: " + color + ", velocity: " + velocity;
    }
}
