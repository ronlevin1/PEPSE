package pepse;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class UIManager extends GameObject {
    private static UIManager instance;
    private static int energyLevel;

    private UIManager(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        energyLevel = 100;
    }

    public static UIManager getInstance(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        if (instance == null) {
            instance = new UIManager(topLeftCorner, dimensions, renderable);
        }
        return instance;
    }

    public static void updateEnergyLevel(int energyLevel) {
        UIManager.energyLevel = energyLevel;
    }
    //todo: finish displaying energy level
}
