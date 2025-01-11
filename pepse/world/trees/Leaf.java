package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Leaf extends GameObject{
    private Color leafColor = new Color(50, 200, 30);

    private static final int SIZE = 30;

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
