package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.gameobjects.LivesManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A factory responsible for creating collision strategies for bricks.
 * Randomly selects strategies while enforcing the maximum behavior limit.
 */
public class CollisionStrategyFactory {
    //===========================================CONSTANTS===============================================
    private static final int MAX_BEHAVIORS_ALLOWED = 3;

    //===========================================FIELDS==================================================
    private final GameObjectCollection gameObjects;
    private final BrickerGameManager gameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Counter bricksCounter;
    private final UserInputListener userInputListener;
    private final int wallWidth;
    private final Vector2 windowDimensions;
    private final LivesManager livesManager;
    private final Random random;

    //===========================================CONSTRUCTOR=============================================
    /**
     * Constructs the factory responsible for randomly generating the appropriate collision strategy
     * for every brick, enforcing specific probabilities and recursion limits.
     *
     * @param gameObjects The global collection of game objects.
     * @param gameManager The main game manager, used to access grid data for the Exploding Strategy.
     * @param imageReader Tool for loading image assets.
     * @param soundReader Tool for loading sound assets.
     * @param bricksCounter The global counter tracking remaining bricks.
     * @param userInputListener Tool for reading player input (used by the Extra Paddle).
     * @param wallWidth The width of the walls (used by Extra Paddle for boundary checks).
     * @param windowDimensions The dimensions of the game window.
     * @param livesManager The manager for player lives (used by the Extra Life strategy).
     */
    public CollisionStrategyFactory(GameObjectCollection gameObjects,
                                    BrickerGameManager gameManager,
                                    ImageReader imageReader,
                                    SoundReader soundReader,
                                    Counter bricksCounter,
                                    UserInputListener userInputListener,
                                    int wallWidth,
                                    Vector2 windowDimensions,
                                    LivesManager livesManager) {
        this.gameObjects = gameObjects;
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.bricksCounter = bricksCounter;
        this.userInputListener = userInputListener;
        this.wallWidth = wallWidth;
        this.windowDimensions = windowDimensions;
        this.livesManager = livesManager;
        this.random = new Random();
    }

    //===========================================PUBLIC METHODS==========================================
    /**
     * Generates a collision strategy based on the defined probabilities.
     * This method starts the recursive generation process with the maximum allowed behaviors (3).
     *
     * @return The randomly selected and configured CollisionStrategy.
     */
    public CollisionStrategy getStrategy() {
        return buildStrategy(MAX_BEHAVIORS_ALLOWED);
    }

    //===========================================PRIVATE METHODS=========================================
    /**
     * Recursively builds the collision strategy, enforcing the maximum behavior limit.
     * The strategy is chosen based on a 1/10 chance for each special type, and 5/10 for Basic.
     *
     * @param behaviorCount The current budget of special behaviors allowed in this branch of recursion.
     * @return The resulting CollisionStrategy.
     */
    private CollisionStrategy buildStrategy(int behaviorCount) {
        if (behaviorCount <= 0) {
            return new BasicCollisionStrategy(gameObjects, bricksCounter);
        }

        int drawnStrategy = random.nextInt(10);

        switch (drawnStrategy) {
            case 0:
                return new ExtraBallsStrategy(gameObjects, bricksCounter, imageReader, soundReader);

            case 1:
                return new ExtraPaddleStrategy(gameObjects, bricksCounter, windowDimensions,
                        imageReader, userInputListener, wallWidth);

            case 2:
                return new ExplodingBrickStrategy(gameObjects, bricksCounter, gameManager);

            case 3:
                return new ExtraLifeStrategy(gameObjects, bricksCounter, imageReader, livesManager,
                        windowDimensions);

            case 4:
                if (behaviorCount > 1) {
                    int quantity1 = 1;
                    int quantity2 = behaviorCount - 1;

                    return new DoubleBehaviorStrategy(
                            buildStrategy(quantity1),
                            buildStrategy(quantity2)
                    );
                } else {
                    return new BasicCollisionStrategy(gameObjects, bricksCounter);
                }

            default:
                return new BasicCollisionStrategy(gameObjects, bricksCounter);
        }
    }
}