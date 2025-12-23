package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A temporary paddle created by the extra-paddle power-up.
 * It behaves like the main paddle but disappears after a limited number of hits.
 */
public class MockPaddle extends Paddle {

    //===========================================CONSTANTS==============================================
    private static final int MAX_COLLISIONS_FOR_REMOVAL = 4;

    //===========================================FIELDS=================================================
    private final GameObjectCollection gameObjects;
    private int collisionCounter;

    //===========================================CONSTRUCTOR============================================
    /**
     * Constructs a new Mock Paddle (the temporary secondary paddle).
     * This paddle behaves identically to the main paddle but tracks collisions and
     * removes itself after reaching a maximum hit limit.
     *
     * @param topLeftCorner The initial top-left position of the paddle.
     * @param dimensions The width and height of the paddle.
     * @param renderable The image used to render the paddle.
     * @param inputListener The user input listener, needed for movement.
     * @param gameObjects The game's main object collection, used for self-removal.
     * @param rightBound The maximum X coordinate the paddle can reach (window edge - wall width).
     */
    public MockPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      UserInputListener inputListener,
                      GameObjectCollection gameObjects, float rightBound) {
        super(topLeftCorner, dimensions, renderable, inputListener, rightBound);
        this.gameObjects = gameObjects;
        this.collisionCounter = 0;
    }

    //===========================================PUBLIC METHODS=========================================
    /**
     * Called when the mock paddle collides with another object (e.g., the ball or a puck).
     * It increments the internal collision counter and removes the paddle if the counter
     * reaches {@link #MAX_COLLISIONS_FOR_REMOVAL}.
     *
     * @param other The object that collided with this paddle.
     * @param collision Information regarding the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        collisionCounter++;

        if (collisionCounter >= MAX_COLLISIONS_FOR_REMOVAL) {
            gameObjects.removeGameObject(this, Layer.DEFAULT);
        }
    }

}