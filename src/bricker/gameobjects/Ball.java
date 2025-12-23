package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents the main ball in the Bricker game.
 * The ball bounces off objects and plays a sound on each collision.
 */
public class Ball extends GameObject {
    //============================================FIELDS================================================
    private final Sound collisionSound;

    //============================================CONSTRUCTOR===========================================

    /**
     * Constructs a new Ball instance for the game.
     * The Ball is the main object responsible for breaking bricks and bounces off surfaces.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     * @param dimensions     Width and height of the ball.
     * @param renderable     The image used to render the ball.
     * @param collisionSound The sound to play when the ball hits something.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    //============================================PUBLIC METHODS========================================

    /**
     * Handles the behavior of the ball upon colliding with another object.
     * The ball's velocity is flipped based on the collision normal (providing an elastic bounce),
     * and the collision sound is played.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision Information regarding the collision,
     *                  including the normal vector used to calculate the bounce.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }
}