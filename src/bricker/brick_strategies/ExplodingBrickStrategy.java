package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * A collision strategy that causes the hit brick to explode.
 * The explosion also destroys its four immediate neighbor bricks.
 */
public class ExplodingBrickStrategy extends BasicCollisionStrategy {

    private static final int ROW_INDEX = 0;
    private static final int COLUMN_INDEX = 1;
    //===========================================FIELDS=================================================
    private final BrickerGameManager gameManager;
    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;

    //===========================================CONSTRUCTOR============================================
    /**
     * Constructs the Exploding Brick Strategy.
     * This strategy is responsible for triggering a chain reaction that breaks adjacent bricks.
     * It holds local references to the collection and counter to perform manual,
     * removal and prevent recursion
     *
     * @param gameObjects The global collection of game objects.
     * @param bricksCounter The global counter tracking remaining bricks.
     * @param gameManager The main game manager, used to query the game grid for neighboring bricks.
     */
    public ExplodingBrickStrategy(GameObjectCollection gameObjects,
                                  Counter bricksCounter,
                                  BrickerGameManager gameManager) {
        super(gameObjects, bricksCounter);
        this.gameManager = gameManager;
        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
    }

    //===========================================PUBLIC METHODS=========================================
    /**
     * Executes the explosion behavior upon collision.
     * It manually attempts to remove the hit brick. If the removal is successful it decrements the 
     * counter and triggers the collision strategy of the four adjacent neighbors.
     *
     * @param object1 The brick that was hit.
     * @param object2 The object that caused the collision
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        boolean wasRemoved = gameObjects.removeGameObject(object1, Layer.STATIC_OBJECTS);

        if (wasRemoved) {
            bricksCounter.decrement();

            if (object1 instanceof Brick) {
                Brick source = (Brick) object1;
                int row = source.getRow();
                int col = source.getColumn();
                int[][] neighbors = new int[][] {
                        {row + 1, col}, {row - 1, col},
                        {row, col - 1}, {row, col + 1}
                };
                for (int[] neighborPos : neighbors) {
                    Brick neighbor = gameManager.getBrick(neighborPos[ROW_INDEX], neighborPos[COLUMN_INDEX]);
                    if (neighbor != null) {
                        neighbor.onCollisionEnter(source, null);
                    }
                }
            }
        }
    }
}