package bricker.brick_strategies;

import danogl.GameObject;

/**
 * A collision strategy that combines two other collision strategies.
 * When triggered, both strategies execute in sequence.
 */
public class DoubleBehaviorStrategy implements CollisionStrategy {

    //===========================================FIELDS==================================================
    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    //===========================================CONSTRUCTOR=============================================
    /**
     * Constructs a new Double Behavior Strategy.
     * This class acts as a wrapper that allows a single brick to execute two different
     * collision behaviors simultaneously. This is an example of the Composite Design Pattern.
     *
     * @param strategy1 The first collision strategy to execute upon collision.
     * @param strategy2 The second collision strategy to execute upon collision.
     */
    public DoubleBehaviorStrategy(CollisionStrategy strategy1, CollisionStrategy strategy2) {
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    //===========================================PUBLIC METHODS==========================================
    /**
     * Executes both wrapped collision strategies sequentially.
     * This method applies the collision
     * event to both stored strategies, ensuring two effects are triggered from a single brick impact.
     *
     * @param object1 The brick that was hit.
     * @param object2 The object that caused the collision (usually the ball).
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        strategy1.onCollision(object1, object2);
        strategy2.onCollision(object1, object2);
    }
}