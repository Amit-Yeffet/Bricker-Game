package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a single brick in the Bricker game.
 * Each brick delegates its collision behavior to a collision strategy.
 */
public class Brick extends GameObject {
    //===========================================FIELDS=================================================
    private final int row;
    private final int column;
    private final CollisionStrategy collisionStrategy;

    //===========================================CONSTRUCTOR============================================
    /**
     * Construct a new Brick GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     * Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * the GameObject will not be rendered.
     * @param row           The row index of the brick in the game grid.
     * @param column        The column index of the brick in the game grid.
     * @param collisionStrategy The strategy to execute when this brick collides with another object.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, int row, int column,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.row = row;
        this.column = column;
        this.collisionStrategy = collisionStrategy;
    }

    //===========================================PUBLIC METHODS=========================================
    /**
     * Called when this brick collides with another GameObject.
     * Delegates the collision handling to the attached CollisionStrategy.
     *
     * @param other The other GameObject involved in the collision.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        this.collisionStrategy.onCollision(this, other);
    }

    /**
     * Retrieves the row index of this brick within the game grid.
     *
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Retrieves the column index of this brick within the game grid.
     *
     * @return The column index.
     */
    public int getColumn() {
        return column;
    }
}