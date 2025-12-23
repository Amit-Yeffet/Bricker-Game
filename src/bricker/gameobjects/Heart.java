package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a heart object in the Bricker game.
 * A heart can be used as a static life indicator or as a falling extra-life pickup.
 */
public class Heart extends GameObject {
    //===========================================CONSTANTS==============================================
    private static final float HEART_FALL_SPEED = 100f;
    private static final String MAIN_PADDLE_TAG = "MAIN_PADDLE";
    private static final String LIVE_COUNTER_TAG = "LIVE_COUNTER";
    private static final String FALLING_OBJECT = "FALLING_OBJECT";

    //===========================================FIELDS=================================================
    private final LivesManager livesManager;
    private final GameObjectCollection gameObjects;
    private final float windowHeight;

    //===========================================CONSTRUCTOR============================================
    /**
     * Constructs a new Heart power-up object that falls down, allowing the player to regain a life.
     *
     * @param topLeftCorner Initial position of the heart (usually at the center of the broken brick).
     * @param dimensions    Width and height of the heart.
     * @param renderable    The image representing the heart.
     * @param livesManager  A reference to the LivesManager to increment the,
     *                      player's life count upon collection.
     * @param gameObjects   The game's collection of objects,
     *                      used to remove the heart once it's collected or falls off-screen.
     * @param windowHeight  The height of the game window,
     *                      used to check if the heart has fallen out of bounds.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 LivesManager livesManager, GameObjectCollection gameObjects, float windowHeight,
                 String isCounterOrObject) {
        super(topLeftCorner, dimensions, renderable);
        this.livesManager = livesManager;
        this.gameObjects = gameObjects;
        this.windowHeight = windowHeight;
        if (isCounterOrObject.equals(LIVE_COUNTER_TAG)) {
            this.setVelocity(Vector2.ZERO);
        }
        if (isCounterOrObject.equals(FALLING_OBJECT)) {
            this.setVelocity(new Vector2(Vector2.DOWN.mult(HEART_FALL_SPEED)));
        }

    }

    //===========================================PUBLIC METHODS=========================================
    /**
     * Determines which objects the falling heart is allowed to collide with.
     * We use the 'MAIN_PADDLE_TAG' to ensure the heart only interacts with the original paddle,
     * as required by the assignment rules (no 'instanceof').
     *
     * @param other The potential collision partner.
     * @return true if the other object is the main paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(MAIN_PADDLE_TAG);
    }

    /**
     * Handles the collision event for the Heart object.
     * If the heart successfully collides with the main paddle,
     * the player's life count is incremented, and the heart is removed from the game.
     *
     * @param other The object involved in the collision.
     * @param collision Information regarding this collision (unused here).
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (shouldCollideWith(other)) {
            livesManager.incrementLives();
            gameObjects.removeGameObject(this, Layer.DEFAULT);
        }
    }

    /**
     * Called every frame. Checks if the heart has fallen out of the bottom of the game window.
     * If it has, the heart is removed so it doesn't clutter the game engine.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getTopLeftCorner().y() > windowHeight) {
            gameObjects.removeGameObject(this, Layer.DEFAULT);
        }
    }
}