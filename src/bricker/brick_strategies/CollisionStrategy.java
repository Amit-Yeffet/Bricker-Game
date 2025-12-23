package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Defines a collision behavior for bricks in the Bricker game.
 * Each strategy determines what happens when a brick is hit.
 */
public interface CollisionStrategy {
    /**
     * Handles a collision event between two game objects.
     *
     * @param object1 the first object participating in the collision
     * @param object2 the second object participating in the collision
     */
    void onCollision(GameObject object1, GameObject object2);
}
