package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;

public class TreeTrunk extends GameObject {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final int WIDTH = Block.SIZE;
    private static final int MAX_HEIGHT = Block.SIZE * 10;
    private static final int MIN_HEIGHT = Block.SIZE * 2;
    private int height;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    private TreeTrunk(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        //todo: set tag to "treeTrunk"
    }

    public static GameObject create(Vector2 topLeftCorner) {
        int height = (int) (Math.random() * (MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT);
        Renderable renderable = new RectangleRenderable(TRUNK_COLOR);
        return new TreeTrunk(topLeftCorner, new Vector2(WIDTH, height), renderable);
    }

    public int getHeight() {
        return height;
    }
}