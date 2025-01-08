package pepse.world.daynight;

import danogl.*;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;

import java.awt.*;

public class Night {
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final Float DAY_CYCLE = 30f;

    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength) {
        Renderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor(Color.BLACK));
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                renderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("basic_night");

        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
                Constants.CYCLE_LENGTH, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, //
                // Choose appropriate ENUM value
                null
        );// nothing further to execute upon reaching final value

        return night;
    }
}
