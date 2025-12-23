package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import bricker.gameobjects.LivesManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A collision strategy that creates a falling heart power-up.
 * Collecting the heart increases the player's lives.
 */
public class ExtraLifeStrategy extends BasicCollisionStrategy {

    //===========================================CONSTANTS==============================================
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final float HEART_SIZE = 20f;
    private static final String FALLING_OBJECT = "FALLING_OBJECT";

    //===========================================FIELDS=================================================
    private final ImageReader imageReader;
    private final GameObjectCollection gameObjects;
    private final LivesManager livesManager;
    private final Vector2 windowDimensions;

    //===========================================CONSTRUCTOR============================================
    /**
     * Constructs the Extra Life Strategy.
     * This strategy is responsible for dropping a heart power-up when the associated brick is destroyed.
     *
     * @param gameObjects The global collection of game objects.
     * @param bricksCounter The global counter tracking remaining bricks.
     * @param imageReader Tool for loading the heart image asset.
     * @param livesManager The manager that tracks and updates the player's life count.
     * @param windowDimensions The dimensions of the game window, needed to set the heart's bounds.
     */
    public ExtraLifeStrategy(GameObjectCollection gameObjects,
                             Counter bricksCounter,
                             ImageReader imageReader,
                             LivesManager livesManager,
                             Vector2 windowDimensions) {
        super(gameObjects, bricksCounter);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.livesManager = livesManager;
        this.windowDimensions = windowDimensions;
    }

    //===========================================PUBLIC METHODS=========================================
    /**
     * Executes the extra life behavior upon collision.
     * It first performs the basic action (breaks the brick and decrements the counter),
     * and then creates a falling {@link Heart} object at the brick's center.
     *
     * @param thisObj The brick that was hit.
     * @param otherObj The object that caused the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        Vector2 brickCenter = thisObj.getCenter();
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        Heart heart = new Heart(
                Vector2.ZERO,
                new Vector2(HEART_SIZE, HEART_SIZE),
                heartImage,
                livesManager,
                gameObjects,
                windowDimensions.y(),
                FALLING_OBJECT
        );
        heart.setCenter(brickCenter);
        gameObjects.addGameObject(heart, Layer.DEFAULT);
    }
}