package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;

import java.awt.*;

public class Leaf extends GameObject{
    private static Color leafColor = new Color(50, 200, 30);
    private static final int SIZE = Constants.LEAF_OR_FRUIT_SIZE;

    private Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
//        todo: set tag to "leaf"
    }

    public static GameObject create(Vector2 topLeftCorner) {
        Renderable renderable = new RectangleRenderable(leafColor);
        // todo: make move in wind.
        return new Leaf(topLeftCorner, renderable);
    }
}
