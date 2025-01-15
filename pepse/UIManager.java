package pepse;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.EmptyFloatProvider;

/**
 * This class is responsible for managing the UI of the game. It displays
 * the energy level of the
 * avatar.
 */
public class UIManager extends GameObject {
    private static UIManager instance;
    private TextRenderable textRenderable;
    private EmptyFloatProvider energyLevelProvider;

    private UIManager(Vector2 topLeftCorner, Vector2 dimensions,
                      TextRenderable textRenderable) {
        super(topLeftCorner, dimensions, textRenderable);
    }

    /**
     * Returns the instance of the UIManager. If the instance does not
     * exist, it creates a new one.
     *
     * @param topLeftCorner       The top left corner of the UI.
     * @param dimensions          The dimensions of the UI.
     * @param energyLevelProvider The provider of the energy level of the
     *                            avatar.
     * @return The instance of the UIManager.
     */
    public static UIManager getInstance(Vector2 topLeftCorner,
                                        Vector2 dimensions,
                                        EmptyFloatProvider energyLevelProvider) {
        if (instance == null) {
            TextRenderable textRenderable = new TextRenderable("100%");
            instance = new UIManager(topLeftCorner, dimensions,
                    textRenderable);
            instance.textRenderable = textRenderable;
            instance.energyLevelProvider = energyLevelProvider;
            instance.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        }
        return instance;
    }

    /**
     * Updates the UI with the current energy level of the avatar.
     *
     * @param deltaTime The time passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float energyLevel = energyLevelProvider.getFloat();
        textRenderable.setString(energyLevel + "%");
    }
}
