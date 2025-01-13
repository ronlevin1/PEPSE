package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Block;

import java.awt.*;

public class Fruit extends GameObject {
    private static final Color FRUIT_COLOR = new Color(174, 28, 28);
    private static final int RADIUS = Constants.LEAF_OR_FRUIT_SIZE;
    private static final int REGENERATION_CYCLE = Constants.CYCLE_LENGTH;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    private Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    public static GameObject create(Vector2 topLeftCorner) {
        Renderable renderable = new OvalRenderable(FRUIT_COLOR);
        return new Fruit(topLeftCorner, new Vector2(RADIUS, RADIUS), renderable);
    }
    //todo: regenerate fruit after REGENERATION_CYCLE
}
