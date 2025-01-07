package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private static final Color BASE_GROUND_COLOR =
            new Color(212, 123, 74);
    private float groundHeightAtX0;
    private float yMultFactor = (float) 2 / 3;

    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = windowDimensions.y() * yMultFactor;
    }

    public float groundHeightAt(float x) {
        // todo: use NoiseGenerator for Noise Perlin.
        return groundHeightAtX0;
    }

    public List<Block> createInRange(int minX, int maxX) {
        Renderable rend =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        List<Block> lst = new ArrayList<>();
        Block blk = new Block(new Vector2(0, groundHeightAtX0), rend);
        blk.setTag("ground");
        lst.add(blk);
        return lst;
    }
}
