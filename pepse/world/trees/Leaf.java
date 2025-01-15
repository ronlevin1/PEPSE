package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;
import pepse.world.Block;

import java.awt.*;

/**
 * Represents a leaf object in the game.
 */
public class Leaf extends GameObject {
    private static final Color LEAF_BASE_COLOR = new Color(50, 200, 30);

    private static final int SIZE = Constants.LEAF_OR_FRUIT_SIZE;
    private static final float ANGLE_STARTING_VAL = -3 * (float) Math.PI;
    private static final float ANGLE_ENDING_VAL = 3 * (float) Math.PI;
    private static final float SIZE_STARTING_VAL = 1;
    private static final float SIZE_ENDING_VAL = 0.7f;
    private static final float TRANSITIONS_CYCLE_LENGTH = 3;

    private Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        float waitTime = (float) (Math.random() * TRANSITIONS_CYCLE_LENGTH);
        new ScheduledTask(this, waitTime, true, this::moveInWind);
    }

    /**
     * Creates a leaf object at the given position.
     * @param topLeftCorner The top left corner of the leaf.
     * @return The created leaf object.
     */
    public static GameObject create(Vector2 topLeftCorner) {
        Renderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor(LEAF_BASE_COLOR, Constants.COLOR_DELTA));
        return new Leaf(topLeftCorner, renderable);
    }

    private void moveInWind() {
        new Transition<>(
                this,
                this.renderer()::setRenderableAngle,
                ANGLE_STARTING_VAL,
                ANGLE_ENDING_VAL,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                TRANSITIONS_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        new Transition<>(
                this,
                (factor) -> this.setDimensions(new Vector2(Block.SIZE,
                        Block.SIZE).mult(factor)),
                SIZE_STARTING_VAL,
                SIZE_ENDING_VAL,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                TRANSITIONS_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
    }
}
