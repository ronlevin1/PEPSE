package pepse.world.daynight;

import danogl.GameObject;
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
 * This class is responsible for creating the sun object in the game.
 */
public class Sun {

    private static final float N_360 = 360f;
    private static final int SUN_RADIUS = 120;
    private static final int CYCLE_RADIUS = 420;

    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength) {
        Renderable renderable =
                new OvalRenderable(ColorSupplier.approximateColor(Color.YELLOW));
        float x = windowDimensions.x() / Constants.N_2;
        float y = windowDimensions.y() / Constants.N_3;
        Vector2 position = new Vector2(x, y);
        Vector2 sunSize = new Vector2(SUN_RADIUS, SUN_RADIUS); // Adjust the
        // size as needed
        GameObject sun = new GameObject(position, sunSize, renderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");

        Vector2 initialSunCenter = sun.getCenter();
        Vector2 cycleCenter = position.add(new Vector2(0, CYCLE_RADIUS));
        new Transition<Float>(
                sun, // the game object being changed
                (Float angle) -> sun.setCenter
                        (initialSunCenter.subtract(cycleCenter)
                                .rotated(angle)
                                .add(cycleCenter)), // the method to call
                0f, // initial transition value
                N_360, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT,// use a cubic
                // interpolator
                Constants.N_2 * cycleLength, // transition fully over half a
                // day
                Transition.TransitionType.TRANSITION_LOOP, //
                // Choose appropriate ENUM value
                null
        );// nothing further to execute upon reaching final value

        return sun;
    }
}
