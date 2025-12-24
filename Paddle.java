package game;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import geometry.Ball;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import graphics.Sprite;
import collision.Collidable;
import physics.Velocity;

import java.awt.Color;
import java.util.List;

public class Paddle implements Sprite, Collidable {

    private KeyboardSensor keyboard;
    private Block block;
    private final Rectangle shape;
    private GUI gui;
    private GameEnvironment environment;

    public Paddle(Block block, GUI gui, GameEnvironment environment) {
        this.block = block;
        this.shape = block.getCollisionRectangle();
        this.gui = gui;
        this.keyboard = gui.getKeyboardSensor();
        this.environment = environment;
    }

    public void moveLeft() {
        // Values identical to Constants
        int movementSensitivity = 8;
        int boundsWidth = 10;

        Point topLeft;
        double guiWidth = this.gui.getDrawSurface().getWidth();

        if (getCollisionRectangle().getUpperLeft().getX() > 0) {
            topLeft = new Point(
                    getCollisionRectangle().getUpperLeft().getX() - movementSensitivity,
                    getCollisionRectangle().getUpperLeft().getY()
            );
        } else {
            topLeft = new Point(
                    guiWidth - boundsWidth - this.shape.getWidth(),
                    this.getCollisionRectangle().getUpperLeft().getY()
            );
        }
        this.shape.setUpperLeft(topLeft);
    }

    public void moveRight() {
        // Values identical to Constants
        int movementSensitivity = 8;

        Point topLeft;
        double guiWidth = this.gui.getDrawSurface().getWidth();

        if (this.getCollisionRectangle().getUpperLeft().getX() + this.shape.getWidth() < guiWidth) {
            topLeft = new Point(
                    this.getCollisionRectangle().getUpperLeft().getX() + movementSensitivity,
                    this.getCollisionRectangle().getUpperLeft().getY()
            );
        } else {
            topLeft = new Point(0, this.getCollisionRectangle().getUpperLeft().getY());
        }
        this.shape.setUpperLeft(topLeft);
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(block.getColor());
        Rectangle thisRec = this.shape;
        d.fillRectangle(
                (int) thisRec.getUpperLeft().getX(),
                (int) thisRec.getUpperLeft().getY(),
                (int) thisRec.getWidth(),
                (int) thisRec.getHeight()
        );
        d.setColor(Color.black);
        d.drawRectangle(
                (int) thisRec.getUpperLeft().getX(),
                (int) thisRec.getUpperLeft().getY(),
                (int) thisRec.getWidth(),
                (int) thisRec.getHeight()
        );
    }

    @Override
    public void timePassed() {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)
                || this.keyboard.isPressed("a")
                || this.keyboard.isPressed("A")) {
            moveLeft();
        }
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)
                || this.keyboard.isPressed("d")
                || this.keyboard.isPressed("D")) {
            moveRight();
        }
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return this.shape;
    }

    @Override
    public Block getCollisionBlock() {
        return new Block(shape, Color.ORANGE);
    }

    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        if (collisionPoint == null) {
            return currentVelocity;
        }

        List<Line> divide = this.shape.getTopLine().divideTo5();

        if (this.shape.getTopLine().isOnLine(collisionPoint) && currentVelocity.getDy() > 0) {
            for (int j = 1; j < 6; j++) {
                if (divide.get(j - 1).isOnLine(collisionPoint)) {
                    switch (j) {
                        case 1:
                            return Velocity.fromAngleAndSpeed(300, currentVelocity.getSpeed());
                        case 2:
                            return Velocity.fromAngleAndSpeed(330, currentVelocity.getSpeed());
                        case 3:
                            return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
                        case 4:
                            return Velocity.fromAngleAndSpeed(30, currentVelocity.getSpeed());
                        case 5:
                            return Velocity.fromAngleAndSpeed(60, currentVelocity.getSpeed());
                        default:
                    }
                }
            }
        } else if (this.shape.getRightLine().isOnLine(collisionPoint)
                || this.shape.getLeftLine().isOnLine(collisionPoint)) {
            return new Velocity(-currentVelocity.getDx(), currentVelocity.getDy());
        }

        return currentVelocity;
    }

    public void addToGame(Game g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    @Override
    public String toString() {
        return "paddle: [" + this.shape.toString() + "]";
    }
}
