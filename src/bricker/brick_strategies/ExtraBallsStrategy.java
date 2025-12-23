package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A collision strategy that spawns additional small balls (pucks).
 * Triggered when a brick with this behavior is hit.
 */
public class ExtraBallsStrategy extends BasicCollisionStrategy {

    //===========================================CONSTANTS==============================================
    private static final float BALL_SPEED = 250;
    private static final float PUCK_RADIUS = 15;
    private static final String MOCK_BALL_IMAGE_PATH = "assets/mockBall.png";
    private static final String BLOP_SOUND_PATH = "assets/blop.wav";
    private static final int AMOUNT_OF_MOCKS = 2;

    //===========================================FIELDS=================================================
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final GameObjectCollection gameObjects;

    //===========================================CONSTRUCTOR============================================
    /**
     * Constructs the Extra Balls Strategy.
     * This strategy is responsible for spawning two smaller balls (pucks) when the associated brick is hit.
     *
     * @param gameObjects The global collection of game objects, used to add the new pucks.
     * @param bricksCounter The global counter tracking remaining bricks.
     * @param imageReader Tool for loading image assets (specifically the puck image).
     * @param soundReader Tool for loading sound assets.
     */
    public ExtraBallsStrategy(GameObjectCollection gameObjects,
                              Counter bricksCounter,
                              ImageReader imageReader,
                              SoundReader soundReader) {
        super(gameObjects, bricksCounter);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    //===========================================PUBLIC METHODS=========================================
    /**
     * Executes the extra balls behavior upon collision.
     * It first performs the basic action (breaks the brick and decrements the counter),
     * and then spawns the specified number of pucks at the center of the broken brick.
     *
     * @param object1 The brick that was hit.
     * @param object2 The object that caused the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);

        Vector2 brickCenter = object1.getCenter();
        Renderable puckImage = imageReader.readImage(MOCK_BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BLOP_SOUND_PATH);

        for (int i = 0; i < AMOUNT_OF_MOCKS; i++) {
            createPuck(brickCenter, puckImage, collisionSound);
        }
    }

    //===========================================PRIVATE METHODS========================================
    /**
     * Creates a single puck ball, sets its starting position and a randomized upward velocity,
     * and adds it to the game.
     *
     * @param location The center position where the puck should spawn (center of the broken brick).
     * @param image The puck image renderable.
     * @param sound The sound to play on collision.
     */
    private void createPuck(Vector2 location, Renderable image, Sound sound) {
        Ball puck = new Ball(
                Vector2.ZERO,
                new Vector2(PUCK_RADIUS, PUCK_RADIUS),
                image,
                sound
        );

        puck.setCenter(location);
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * BALL_SPEED;
        float velocityY = (float) Math.sin(angle) * BALL_SPEED;
        puck.setVelocity(new Vector2(velocityX, -Math.abs(velocityY)));
        gameObjects.addGameObject(puck);
    }
}