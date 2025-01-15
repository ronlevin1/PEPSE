package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

/**
 * Represents a tree in the game world.
 */
public class Tree extends GameObject {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final int WIDTH = Block.SIZE;

    private Tree(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * Create a new tree GameObject.
     *
     * @param topLeftCorner Position of the object, in window coordinates
     *                      (pixels).
     *                      Note that (0,0) is the top-left corner of the
     *                      window.
     * @param height        Height of the tree in window coordinates.
     * @return A new tree GameObject.
     */
    public static GameObject create(Vector2 topLeftCorner, int height) {
        Renderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
        return new Tree(topLeftCorner, new Vector2(WIDTH, height),
                renderable);
    }
}
