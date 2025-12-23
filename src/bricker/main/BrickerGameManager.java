package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.LivesManager;
import bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The main game manager for the Bricker game.
 * Responsible for initializing the game objects and updating the game loop.
 */
public class BrickerGameManager extends GameManager {
    //============================================CONSTANTS============================================
    private static final int WALL_WIDTH = 25;
    private static final float BALL_SPEED = 200;
    private static final int DEFAULT_BRICKS_ROWS = 7;
    private static final int DEFAULT_BRICKS_COLS = 8;
    private static final float BRICK_PADDING = 0.75f;
    private static final int BALL_RADIUS = 20;
    private static final Vector2 PADDLE_DIMENSION = new Vector2(100, 15);
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final String BLOP_SOUND_PATH = "assets/blop.wav";
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final float MID_SCREEN_MULTIPLIER = 0.5F;
    private static final float BRICK_HEIGHT = 15f;
    private static final float TOP_SCREEN_OFFSET = 30f;
    private static final String MAIN_PADDLE_TAG = "MAIN_PADDLE";
    private static final int COLUMNS_INDEX = 1;
    private static final int ROWS_INDEX = 0;
    //============================================FIELDS============================================
    private int bricksRows = DEFAULT_BRICKS_ROWS;
    private int bricksCols = DEFAULT_BRICKS_COLS;
    private final Vector2 windowDimensions;
    private UserInputListener inputListener;
    private WindowController windowController;
    private Ball ball;
    private Paddle paddle;
    private LivesManager livesManager;
    private Counter bricksCounter;
    private Brick[][] brickGrid;

    //============================================CONSTRUCTORS============================================
    /**
     * Creates a new BrickerGameManager instance.
     *
     * @param windowTitle the title of the game window
     * @param windowDimensions the width and height of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
    }

    /**
     * Creates a BrickerGameManager with a custom bricks grid size.
     *
     * @param windowTitle the title of the game window
     * @param windowDimensions the dimensions of the game window
     * @param bricksRows number of brick rows to generate
     * @param bricksCols number of brick columns to generate
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksRows, int bricksCols) {
        this(windowTitle, windowDimensions);
        this.bricksRows = bricksRows;
        this.bricksCols = bricksCols;
    }

    //============================================PUBLIC METHODS============================================
    /**
     * Initializes all game objects, assets, and game state for Bricker.
     *
     * @param imageReader used for loading images
     * @param soundReader used for loading sounds
     * @param inputListener handles user keyboard input
     * @param windowController controls window display and closing
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.inputListener = inputListener;
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.livesManager = new LivesManager(imageReader, gameObjects(),
                new Vector2(WALL_WIDTH, windowDimensions.y()));
        this.bricksCounter = new Counter(bricksCols * bricksRows);
        createBackground(imageReader);
        createBall(imageReader, soundReader);
        addWallsToGame();
        createPaddle(imageReader);
        CollisionStrategyFactory collisionStrategyFactory = new CollisionStrategyFactory(
                gameObjects(),
                this,
                imageReader,
                soundReader,
                bricksCounter,
                inputListener,
                WALL_WIDTH,
                windowDimensions,
                livesManager
        );
        buildBrickGrid(imageReader, collisionStrategyFactory);
    }

    /**
     * Updates the Bricker game on each frame.
     *
     * @param deltaTime time passed since last update, in seconds
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        String prompt = "";

        if (noBricksLeft() || WkeyPressed()) {
            prompt = "You Win! Play again?";
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }

        if (ballPassedPaddle()) {
            if (noLivesLeft()) {
                prompt = "You lose, Play again?";
                if (windowController.openYesNoDialog(prompt)) {
                    windowController.resetGame();
                } else {
                    windowController.closeWindow();
                }
            } else {
                livesManager.decrementLives();
                resetBallPosition();
                randomizeBallVelocity();
            }
        }
    }

    /**
     * Returns the brick located at the given row and column in the bricks grid.
     *
     * @param row the row index of the brick
     * @param col the column index of the brick
     * @return the brick at the specified position, or null if no brick exists there
     */
    public Brick getBrick(int row, int col) {
        if (row < 0 || row >= bricksRows || col < 0 || col >= bricksCols) {
            return null;
        }
        return brickGrid[row][col];
    }

    //============================================PRIVATE HELPER METHODS=====================================

    private void createBackground(ImageReader imageReader) {
        Renderable backGround = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject backGroundObject = new GameObject(Vector2.ZERO, windowDimensions, backGround);
        gameObjects().addGameObject(backGroundObject, Layer.BACKGROUND);
    }

    private GameObject[] buildWalls(WindowController windowController) {
        GameObject[] walls = new GameObject[3];
        Vector2[] coordinates = new Vector2[]{
                new Vector2(0, 0),
                new Vector2(0, 0),
                new Vector2(windowController.getWindowDimensions().x() - WALL_WIDTH + 1, 0)};

        Vector2[] dimensions = new Vector2[]{
                new Vector2(WALL_WIDTH, windowController.getWindowDimensions().y()),
                new Vector2(windowController.getWindowDimensions().x(), WALL_WIDTH),
                new Vector2(WALL_WIDTH, windowController.getWindowDimensions().y())};

        for (int i = 0; i < walls.length; i++) {
            walls[i] = new GameObject(coordinates[i], dimensions[i], new RectangleRenderable(null));
        }
        return walls;
    }

    private void addWallsToGame() {
        GameObject[] walls = buildWalls(windowController);
        for (GameObject wall : walls) {
            gameObjects().addGameObject(wall, Layer.STATIC_OBJECTS);
        }
    }

    private void buildBrickGrid(ImageReader imageReader, CollisionStrategyFactory collisionStrategyFactory) {
        float brickLength = (windowDimensions.x() - 2 * WALL_WIDTH) / (float) bricksCols - BRICK_PADDING;
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        this.brickGrid = new Brick[bricksRows][bricksCols];
        float currentHorizontalInsertPosition;
        float currentVerticalInsertPosition = TOP_SCREEN_OFFSET + BRICK_PADDING;
        for (int i = 0; i < bricksRows; i++) {
            currentHorizontalInsertPosition = WALL_WIDTH + BRICK_PADDING;

            for (int j = 0; j < bricksCols; j++) {
                this.brickGrid[i][j] = new Brick(new Vector2(currentHorizontalInsertPosition,
                        currentVerticalInsertPosition), new Vector2(brickLength, BRICK_HEIGHT),
                        brickImage, i, j,
                        collisionStrategyFactory.getStrategy());
                gameObjects().addGameObject(this.brickGrid[i][j], Layer.STATIC_OBJECTS);
                currentHorizontalInsertPosition += (brickLength + BRICK_PADDING);
            }
            currentVerticalInsertPosition += (BRICK_HEIGHT + BRICK_PADDING);
        }
    }
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        randomizeBallVelocity();
        resetBallPosition();
        gameObjects().addGameObject(ball);
    }

    private void createPaddle(ImageReader imageReader) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        paddle = new Paddle(Vector2.ZERO, PADDLE_DIMENSION, paddleImage,
                inputListener, windowDimensions.x() - WALL_WIDTH);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
        paddle.setTag(MAIN_PADDLE_TAG);
        gameObjects().addGameObject(paddle);
    }

    private void resetBallPosition() {
        ball.setCenter(windowDimensions.mult(MID_SCREEN_MULTIPLIER));
    }

    private void randomizeBallVelocity() {
        float ballXVel = BALL_SPEED;
        float ballYVel = BALL_SPEED;
        Random random = new Random();
        if (random.nextBoolean()) ballXVel *= -1;
        if (random.nextBoolean()) ballYVel *= -1;
        ball.setVelocity(new Vector2(ballXVel, ballYVel));
    }

    private boolean ballPassedPaddle() {
        return ball.getCenter().y() > windowDimensions.y();
    }

    private boolean noLivesLeft() {
        return livesManager.getCurrentLives() == 1;
    }

    private boolean noBricksLeft() {
        return bricksCounter.value() <= 0;
    }

    private boolean WkeyPressed() {
        return inputListener.isKeyPressed(KeyEvent.VK_W);
    }

    //============================================MAIN============================================
    /**
     * The entry point of the Bricker game application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        BrickerGameManager manager = null;
        if (args.length == 0) {
            manager = new BrickerGameManager("Bricker", new Vector2(700, 500));
        }
        else{
            manager = new BrickerGameManager("Bricker", new Vector2(700, 500), 
                    Integer.parseInt(args[COLUMNS_INDEX]),
                    Integer.parseInt(args[ROWS_INDEX]));
        }
        manager.run();
    }
}