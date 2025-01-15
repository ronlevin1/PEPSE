package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.Component;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;

import java.awt.*;

/**
 * Creates a sun halo around the sun
 */
public class SunHalo {

    /**
     * Creates a sun halo around the sun
     *
     * @param sun
     * @return the sun halo
     */
    public static GameObject create(GameObject sun) {
        Color haloColor = new Color(255, 255, 0, 20);
        Vector2 position = sun.getTopLeftCorner();
        Vector2 sunSize = sun.getDimensions().mult(3f);
        Renderable renderable = new OvalRenderable(haloColor);

        GameObject sunHalo = new GameObject(position, sunSize, renderable);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        Component updateHaloPosCallback =
                (deltaTime -> sunHalo.setCenter(sun.getCenter()));
        sunHalo.addComponent(updateHaloPosCallback);
        sunHalo.setTag("sunHalo");
        return sunHalo;
    }
}
