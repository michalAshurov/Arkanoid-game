package game;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
import graphics.BallRemover;
import graphics.BlockRemover;
import geometry.Ball;
import geometry.Point;
import geometry.Rectangle;
import graphics.Sprite;
import graphics.SpriteCollection;
import collision.Collidable;
import physics.Velocity;
import score.ScoreIndicator;
import score.ScoreTrackingListener;
import util.Counter;

import java.awt.Color;

/**
 * This class handle the game's sprites animation and GUI creation.
 */
public class Game {

    private SpriteCollection sprites;
    private GameEnvironment environment;
    private GUI gui;
    private BlockRemover blockRemover;
    private BallRemover ballRemover;
    private ScoreTrackingListener scoreTrackingListener;
    private ScoreIndicator scoreIndicator;

    /**
     * Constructor for the game, will create a new sprite collection and environment and set the GUI size.
     */
    public Game() {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.gui = new GUI("Arkanoid", 800, 600);
    }

    /**
     * Add a new collidable object to the game's environment.
     * @param c - collidable to be added.
     */
    public void addCollidable(Collidable c) {

        this.environment.addCollidable(c);
    }

    /**
     * Add a new sprite object to the game's environment.
     * @param s - sprite to be added.
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Remove a collidable object from the game environment.
     * @param c - collidable to be removed.
     */
    public void removeCollidable(Collidable c) {

        this.environment.removeCollidable(c);
    }

    /**
     * Remove a sprite object from the game environment.
     * @param s - sprite object to be removed.
     */
    public void removeSprite(Sprite s) {

        this.sprites.removeSprite(s);
    }

    /**
     * Initializes the game objects.
     * Creates listeners, boundaries, blocks, paddle, balls, and score indicator.
     */
    /**
     * Function to initialize the game's objects.
     * Will create balls, paddle, blocks etc.
     */
    public void initializeNewGame() {
        this.blockRemover = new BlockRemover(this, new Counter());
        this.ballRemover = new BallRemover(this, new Counter());
        this.scoreIndicator = new ScoreIndicator(new Counter());
        this.scoreTrackingListener =
                new ScoreTrackingListener(this.scoreIndicator.getScoreCounter());

        generateBounds();

        int numOfRows = 6;
        int blockWidth = 50;
        int blockHeight = 20;
        int boundsWidth = 10;
        int boundsHeight = 10;
        int guiWidth = 800;

        generateBlocks(
                numOfRows,
                blockWidth,
                blockHeight,
                boundsWidth,
                boundsHeight,
                guiWidth
        );

        generatePaddle();
        generateBalls();

        this.scoreIndicator.addToGame(this);
    }


    private void generateBounds() {

        int guiWidth = 800;
        int guiHeight = 600;

        int boundsWidth = 10;
        int boundsHeight = 10;

        int scoreFontSize = 20;

        Color backgroundColor = new Color(255, 245, 245);
        Color boundsColor = Color.LIGHT_GRAY;

        Rectangle backgroundRect = new Rectangle(
                new Point(boundsWidth, scoreFontSize + boundsHeight),
                guiWidth - 2 * boundsWidth,
                guiHeight - boundsHeight
        );
        Block backgroundBlock = new Block(backgroundRect, backgroundColor);
        backgroundBlock.addBackground(this);

        Rectangle leftRec = new Rectangle(
                new Point(0, scoreFontSize),
                boundsWidth,
                guiHeight
        );
        Block leftBound = new Block(leftRec, boundsColor);
        leftBound.addToGame(this);

        Rectangle rightRec = new Rectangle(
                new Point(guiWidth - boundsWidth, scoreFontSize),
                boundsWidth,
                guiHeight
        );
        Block rightBound = new Block(rightRec, boundsColor);
        rightBound.addToGame(this);

        Rectangle topRec = new Rectangle(
                new Point(0, scoreFontSize),
                guiWidth,
                boundsHeight
        );
        Block topBound = new Block(topRec, boundsColor);
        topBound.addToGame(this);

        Rectangle bottomRec = new Rectangle(
                new Point(0, guiHeight + boundsHeight),
                guiWidth,
                boundsHeight
        );
        Block bottomBound = new Block(bottomRec, boundsColor);
        bottomBound.setBlockRemoved(true);
        bottomBound.addToGame(this);
    }




    /**
     * Generate balls for the game.
     */
    private void generateBalls() {
        int ballsAmount = 3;
        int defaultRadius = 8;

        int guiWidth = 800;
        int guiHeight = 600;
        int boundsWidth = 10;
        int boundsHeight = 10;
        int paddleHeight = 7;
        int blockHeight = 20;
        int numOfRows = 6;

        int minX = boundsWidth + defaultRadius;
        int maxX = guiWidth - boundsWidth - defaultRadius;
        int minY = boundsHeight + defaultRadius + (numOfRows + 3) * blockHeight;
        int maxY = guiHeight - boundsHeight - defaultRadius - paddleHeight;

        this.ballRemover.getRemainingBalls().increase(ballsAmount);

        Ball[] ballsArray = new Ball[ballsAmount];
        for (int i = 0; i < ballsArray.length; i++) {
            Point startPos = Point.randomPoint(minX, maxX, minY, maxY);
            Ball ball = new Ball(startPos, defaultRadius, new Color(255, 182, 193), Velocity.randVelocity());
            ball.setGameEnvironment(this.environment);
            ball.addToGame(this);
            ball.addHitListener(this.ballRemover);
        }
    }


    /**
     * Generate the paddle for the game.
     * Location will be based on the size of the paddle and the size of the bounds.
     */
    public void generatePaddle() {
        int guiWidth = 800;
        int guiHeight = 600;

        int paddleWidth = 80;
        int paddleHeight = 7;

        int boundsHeight = 10;

        Color paddleColor = Color.ORANGE;

        Point topLeft = new Point(
                (double) (guiWidth - paddleWidth) / 2,
                guiHeight - boundsHeight - paddleHeight
        );

        Rectangle paddleRec = new Rectangle(topLeft, paddleWidth, paddleHeight);
        Paddle paddle = new Paddle(
                new Block(paddleRec, paddleColor),
                this.gui,
                this.environment
        );
        paddle.addToGame(this);
    }


    private Color pinkByRow(int row) {
        Color[] pinks = {
                new Color(255, 230, 240), // שורה 0 – ורוד כמעט לבן
                new Color(255, 190, 210), // שורה 1 – ורוד בהיר
                new Color(255, 150, 180), // שורה 2 – ורוד בינוני
                new Color(235, 110, 160), // שורה 3 – ורוד חזק
                new Color(200, 70, 130),  // שורה 4 – ורוד כהה
                new Color(160, 30, 100)   // שורה 5 – ורוד עמוק מאוד
        };
        return pinks[row];
    }

    /**
     * Generate blocks to be on the top part of the gui.
     *
     * @param rowsAmount - amount of rows of blocks to create.
     * @param blockWidth - width of the blocks.
     * @param blockHeight - height of the blocks.
     * @param boundWidth     - width of the GUI boundaries.
     * @param boundHeight     - height of the GUI boundaries.
     * @param guiWidth   - total width of the gui.
     */
    public void generateBlocks(int rowsAmount, int blockWidth, int blockHeight,
                               int boundWidth, int boundHeight, int guiWidth) {

        for (int j = 0; j < rowsAmount; j++) {
            // One unique pink color per row
            Color color = pinkByRow(j);

            for (int i = 0; i < rowsAmount * 2 - j; i++) {
                double xValue = guiWidth - ((i + 1) * blockWidth + boundWidth);
                double yValue = (j + 3) * blockHeight + boundHeight + 1;

                Rectangle rec = new Rectangle(new Point(xValue, yValue), blockWidth, blockHeight);
                Block block = new Block(rec, color);

                block.addHitListener(this.blockRemover);
                block.addHitListener(this.scoreTrackingListener);
                block.addToGame(this);
            }
        }

        this.blockRemover.getRemainingBlocks()
                .increase((3 * rowsAmount * rowsAmount + rowsAmount) / 2);
    }



    /**
     * Function to start the animation of the game.
     */
    public void run() {
        Sleeper sleeper = new Sleeper();
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;
        while (blockRemover.getRemainingBlocks().getValue() > 0 && ballRemover.getRemainingBalls().getValue() > 0) {
            long startTime = System.currentTimeMillis(); // timing
            DrawSurface d = gui.getDrawSurface();
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
        if (blockRemover.getRemainingBlocks().getValue() <= 0) {
            this.scoreTrackingListener.levelCleared();
        }
        if (ballRemover.getRemainingBalls().getValue() <= 0) {
            System.out.println("Player lost. " + scoreTrackingListener.toString());
        }
        if (blockRemover.getRemainingBlocks().getValue() <= 0) {
            System.out.println("Player won! " + scoreTrackingListener.toString());
        }
        this.gui.close();
    }

}
