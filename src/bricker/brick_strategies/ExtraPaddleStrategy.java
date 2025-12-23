package bricker.brick_strategies;

import bricker.gameobjects.MockPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A collision strategy that creates a temporary second paddle.
 * The extra paddle disappears after being hit a limited number of times.
 */
public class ExtraPaddleStrategy extends BasicCollisionStrategy{
    //===========================================CONSTANTS==============================================
    private static final Vector2 PADDLE_DIMENSION = new Vector2(100, 15);
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";

    //===========================================FIELDS=================================================
    private final Vector2 initPosition;
    private final Vector2 initVelocity;
    private final Vector2 windowsDimensions;
    private final UserInputListener userInputListener;
    private final int wallWidth;
    private final GameObjectCollection gameObjectsCollection;
    private final ImageReader imageReader;
    private MockPaddle mockPaddle;


    //===========================================CONSTRUCTOR============================================
    /**
     * Constructs the Extra Paddle Strategy.
     * This strategy is responsible for creating a second, temporary paddle at the center of the screen.
     * It holds all the necessary tools to build and configure the paddle.
     *
     * @param gameObjectsCollection A reference to the global game object collection.
     * @param bricksCounter The global counter tracking remaining bricks.
     * @param windowsDimensions The dimensions of the game window, used to calculate placement.
     * @param imageReader Tool for loading the paddle image asset.
     * @param userInputListener Tool for reading player input, needed for the paddle's movement.
     * @param wallWidth The width of the side walls, used for boundary calculations.
     */
    public ExtraPaddleStrategy(GameObjectCollection gameObjectsCollection, Counter bricksCounter,
                               Vector2 windowsDimensions, ImageReader imageReader,
                               UserInputListener userInputListener, int wallWidth) {
        super(gameObjectsCollection, bricksCounter);
        this.initPosition = new Vector2(windowsDimensions.x()/2, windowsDimensions.y()/2);
        this.windowsDimensions = windowsDimensions;
        this.userInputListener = userInputListener;
        this.wallWidth = wallWidth;
        this.initVelocity = Vector2.ZERO;
        this.gameObjectsCollection = gameObjectsCollection;
        this.imageReader = imageReader;
        this.mockPaddle = null;
    }

    //===========================================PUBLIC METHODS=========================================
    /**
     * Executes the extra paddle behavior upon collision.
     * It first performs the basic action (breaks the brick and decrements the counter).
     * It then checks the entire game environment to ensure that only one temporary paddle
     * exists before spawning a new one.
     *
     * @param thisObj The brick that was hit.
     * @param otherObj The object that caused the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        for (GameObject obj : gameObjectsCollection) {
            if (obj instanceof MockPaddle) {
                return;
            }
        }
        MockPaddle mockPaddle = getMockPaddle();
        gameObjectsCollection.addGameObject(mockPaddle, Layer.DEFAULT);
    }

    //===========================================PRIVATE METHODS========================================
    /**
     * Factory method to create a new MockPaddle object ready for deployment.
     *
     * @return A newly constructed {@link MockPaddle} instance, placed at the center of the screen.
     */
    private MockPaddle getMockPaddle(){
        Renderable mockBallImageRenderer = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        MockPaddle mockPaddle = new MockPaddle(initPosition,PADDLE_DIMENSION, mockBallImageRenderer,
                userInputListener, gameObjectsCollection, windowsDimensions.x() - wallWidth);
        return mockPaddle;
    }
}