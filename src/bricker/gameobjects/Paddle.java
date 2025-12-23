package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents the main user-controlled paddle in the Bricker game.
 * The paddle moves horizontally according to keyboard input and reflects balls.
 */
public class Paddle extends GameObject {
    //============================================CONSTANTS============================================
    private static final float MOVEMENT_SPEED = 300;
    private static final float WALL_WIDTH = 25f;

    //============================================FIELDS================================================
    private final UserInputListener inputListener;
    private final float WIDTH;
    private final float rightBound;

    //============================================CONSTRUCTOR===========================================
    /**
     * Constructs a new Paddle object which the player controls.
     *
     * @param topLeftCorner The initial top-left position of the paddle.
     * @param dimensions    The width and height of the paddle.
     * @param renderable    The image used to render the paddle.
     * @param inputListener The user input mechanism, needed to read key presses.
     * @param rightBound    The maximum X coordinate the paddle can reach before hitting the right wall.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable,
                  UserInputListener inputListener, float rightBound) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.WIDTH = dimensions.x();
        this.rightBound = rightBound;
    }

    //============================================PUBLIC METHODS========================================
    /**
     * Called every frame to update the paddle's position based on user input.
     * This method reads the left/right arrow keys, calculates the new velocity, and checks boundaries.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementdir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementdir = movementdir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementdir = movementdir.add(Vector2.RIGHT);
        }
        setVelocity(movementdir.mult(MOVEMENT_SPEED));
        handlePaddleOutBound();
    }

    //============================================PRIVATE HELPER METHODS================================
    /**
     * Calculates the X coordinate of the paddle's top-right corner.
     * This is used internally for boundary checking against the right wall.
     *
     * @return The X coordinate of the right edge.
     */
    public float getRightTopCorner(){
        return getTopLeftCorner().x()+this.WIDTH;
    }

    /**
     * Ensures the paddle stays within the horizontal bounds of the game window.
     * If the paddle tries to move past the left wall (X=20) or the right wall,
     * its position is corrected to sit flush against the boundary.
     */
    private void handlePaddleOutBound(){
        boolean overToTheLeft = (getTopLeftCorner().x() <= WALL_WIDTH);
        if(overToTheLeft){
            setTopLeftCorner(new Vector2(WALL_WIDTH,getTopLeftCorner().y()));
        }
        boolean overToTheRight = (getRightTopCorner() >= this.rightBound);
        if(overToTheRight){
            setTopLeftCorner(new Vector2(this.rightBound - this.WIDTH,getTopLeftCorner().y()));
        }
    }
}