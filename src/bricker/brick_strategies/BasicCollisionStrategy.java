package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * The default collision strategy used by bricks.
 * Removes the brick and updates the global bricks counter.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    //===========================================CONSTANTS==============================================
    private static final String BRICK_COLLISION_MSG = "collision with brick detected";

    //===========================================FIELDS=================================================
    private final GameObjectCollection gameObjectsCollection;
    private final Counter bricksCounter;

    //===========================================CONSTRUCTOR============================================
    /**
     * Constructs a new Basic Collision Strategy.
     * This strategy is responsible for the fundamental behavior of a standard brick:
     * removing the brick from the game and decreasing the global brick count.
     *
     * @param gameObjectsCollection A reference to the global game object collection,
     *                              used to remove the brick.
     * @param bricksCounter A reference to the counter tracking the total number of bricks remaining.
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjectsCollection, Counter bricksCounter) {
        this.gameObjectsCollection = gameObjectsCollection;
        this.bricksCounter = bricksCounter;
    }

    //===========================================PUBLIC METHODS=========================================
    /**
     * Executes the basic collision behavior.
     * This method removes the hit brick and decrements the global counter.
     *
     * @param object1 The brick that was hit (this object).
     * @param object2 The object that caused the collision (e.g., the ball or a puck).
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        if (this.gameObjectsCollection.removeGameObject(object1, Layer.STATIC_OBJECTS)){
            bricksCounter.decrement();
        }
    }
}