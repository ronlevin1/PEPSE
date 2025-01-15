package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the terrain in the game.
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR =
            new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private float groundHeightAtX0;
    private float yMultFactor = (float) 2 / 3;
    private NoiseGenerator noiseGenerator;

    /**
     * Creates a new Terrain object.
     *
     * @param windowDimensions the dimensions of the window
     * @param seed             the seed for the noise generator
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        groundHeightAtX0 = windowDimensions.y() * yMultFactor;
        noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    /**
     * Returns the height of the ground at a given x-coordinate.
     *
     * @param x the x-coordinate
     * @return the height of the ground at the given x-coordinate
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);
        return groundHeightAtX0 + noise;
    }

    /**
     * Creates a list of blocks in the given range.
     *
     * @param minX the minimum x-coordinate
     * @param maxX the maximum x-coordinate
     * @return the list of blocks in the given range
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> lst = new ArrayList<>();
        minX = (int) (Math.floor(minX / Block.SIZE) * Block.SIZE);
        maxX = (int) (Math.ceil(maxX / Block.SIZE) * Block.SIZE);
        int numOfCols = (maxX - minX) / Block.SIZE;
        for (int i = 0; i < numOfCols; i++) {
            int curX = minX + i * Block.SIZE;
            int topBlockY =
                    (int) (Math.floor(groundHeightAt(curX) / Block.SIZE) * Block.SIZE);
            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                Renderable rend =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                int curY = topBlockY + j * Block.SIZE;
                Block blk = new Block(new Vector2(curX, curY), rend);
                blk.setTag(Constants.BLOCK);
                lst.add(blk);
            }
        }
        return lst;
    }
}
