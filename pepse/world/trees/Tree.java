package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class Tree extends GameObject {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final int WIDTH = Block.SIZE;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates
     *                      (pixels).
     *                      Note that (0,0) is the top-left corner of the
     *                      window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be
     *                      null, in which case
     *                      the GameObject will not be rendered.
     */
    private Tree(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    public static GameObject create(Vector2 topLeftCorner, int height) {
        Renderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
        return new Tree(topLeftCorner, new Vector2(WIDTH, height),
                renderable);
    }
}
