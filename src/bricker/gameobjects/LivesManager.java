package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Manages the player's lives in the Bricker game.
 * Responsible for both the numeric and graphical life indicators.
 */
public class LivesManager {
    //===========================================CONSTANTS===============================================
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final int MAX_LIVES = 4;
    private static final float HEART_SIZE = 20f;
    private static final int INITIAL_LIVES = 3;
    private static final float HEART_SPACING = HEART_SIZE + 5f;
    private static final String LIVE_COUNTER_TAG = "LIVE_COUNTER";

    //===========================================FIELD===================================================
    private TextRenderable livesTextRenderer;
    private final ImageReader heartImageReader;
    private final GameObject[] heartObjects = new GameObject[MAX_LIVES];
    private final GameObjectCollection gameObjects;
    private GameObject textLivesObject;
    private Vector2 bottomLeftCorner;
    private float currentHorizontalPos;
    private final float currentVerticalPos;
    private int currentLives = INITIAL_LIVES;

    //===========================================PRIVATE METHODS=========================================
    /**
     * Initializes the starting heart icons on the screen based on the INITIAL_LIVES constant.
     * It calls addHeartObject() in a loop to place the hearts and update the positioning variable.
     */
    private void initializeHeartObjects() {
        for (int i = 0; i < INITIAL_LIVES; i++) {
            addHeartObject(i);
        }
    }

    /**
     * Creates and adds the numeric text counter (the number '3' at start) to the game UI.
     * It sets the initial color and position.
     */
    private void initializeTextObjects() {
        Vector2 position = new Vector2(currentHorizontalPos, currentVerticalPos);
        livesTextRenderer.setColor(Color.GREEN);
        textLivesObject = new GameObject(position, new Vector2(20,20),
                livesTextRenderer);
        gameObjects.addGameObject(textLivesObject,Layer.UI);
    }

    /**
     * Updates the top-left position of the numeric lives counter object.
     * This is called after a life is lost or gained to make the number follow the last heart.
     */
    private void rePositionLivesText() {
        textLivesObject.setTopLeftCorner(new Vector2(currentHorizontalPos, currentVerticalPos));
    }

    /**
     * Orchestrates the visual update of the numeric counter after the life count changes.
     * It updates the text string, changes the color, and repositions the object.
     */
    private void handleLivesTextUpdate() {
        livesTextRenderer.setString(Integer.toString(currentLives));
        colorLivesText();
        rePositionLivesText();
    }

    /**
     * Changes the color of the numeric life counter based on the current life count.
     * Provides visual feedback to the player (e.g., green for full, red for low).
     */
    private void colorLivesText() {
        switch (currentLives) {
            case 2:
                livesTextRenderer.setColor(Color.YELLOW);
                break;
            case 1:
                livesTextRenderer.setColor(Color.RED);
                break;
            default:
                livesTextRenderer.setColor(Color.GREEN);
        }
    }

    /**
     * Creates a single heart GameObject, adds it to the game, and updates the horizontal
     * starting position for the next heart/number to be placed next to it.
     *
     * @param index The array index (0-3) for the heart being added.
     */
    private void addHeartObject(int index) {
        Vector2 position = new Vector2(currentHorizontalPos, currentVerticalPos);
        heartObjects[index] = new Heart(position, new Vector2(HEART_SIZE, HEART_SIZE),
                heartImageReader.readImage(HEART_IMAGE_PATH, true),
                this,gameObjects, currentVerticalPos, LIVE_COUNTER_TAG);
        gameObjects.addGameObject(heartObjects[index], Layer.UI);
        currentHorizontalPos += HEART_SPACING;
    }

    //===========================================PUBLIC METHODS==========================================

    /**
     * Constructs the LivesManager, handling the visual representation of player lives.
     * It sets up the initial positioning logic and creates both the heart icons and the numeric counter.
     *
     * @param heartImageReader Tool used to load the heart image asset.
     * @param gameObjects      The game's master object collection, used to add/remove UI elements.
     * @param bottomLeftCorner The desired anchor position for the UI elements.
     */
    public LivesManager(ImageReader heartImageReader,
                        GameObjectCollection gameObjects,
                        Vector2 bottomLeftCorner) {
        this.livesTextRenderer = new TextRenderable(Integer.toString(INITIAL_LIVES));
        this.heartImageReader = heartImageReader;
        this.gameObjects = gameObjects;
        this.bottomLeftCorner = bottomLeftCorner;
        // Calculate the actual starting position just above the bottom of the screen
        this.currentHorizontalPos = bottomLeftCorner.x();
        this.currentVerticalPos = bottomLeftCorner.y() - 2 * HEART_SIZE;
        initializeHeartObjects();
        initializeTextObjects();
    }

    /**
     * Increases the player's life count by one, up to MAX_LIVES (4).
     * This is typically called when the player collects a falling heart power-up.
     * It adds a new heart icon and updates the numeric counter display.
     */
    public void incrementLives() {
        if  (currentLives == MAX_LIVES) { return; }
        addHeartObject(currentLives);
        currentLives++;
        handleLivesTextUpdate();
    }

    /**
     * Decreases the player's life count by one.
     * This is typically called by the BrickerGameManager when the main ball falls off-screen.
     * It removes the rightmost heart icon and updates the numeric counter display.
     */
    public void decrementLives() {
        gameObjects.removeGameObject(heartObjects[currentLives-1], Layer.UI);
        heartObjects[currentLives-1] = null;

        currentLives--;
        currentHorizontalPos -= HEART_SPACING;

        handleLivesTextUpdate();
    }

    /**
     * Gets the current number of lives the player has remaining.
     * This method is crucial for the BrickerGameManager to check for the "Game Over" condition.
     *
     * @return The current life count.
     */
    public int getCurrentLives() {
        return currentLives;
    }
}