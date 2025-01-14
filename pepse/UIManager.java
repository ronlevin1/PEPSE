package pepse;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.EmptyFloatProvider;

public class UIManager extends GameObject {
    private static UIManager instance;
    private TextRenderable textRenderable;
    private EmptyFloatProvider energyLevelProvider;

    private UIManager(Vector2 topLeftCorner, Vector2 dimensions,
                      TextRenderable textRenderable) {
        super(topLeftCorner, dimensions, textRenderable);
    }

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

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float energyLevel = energyLevelProvider.getFloat();
        textRenderable.setString(energyLevel + "%");
    }
}
