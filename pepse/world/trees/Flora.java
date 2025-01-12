package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flora {
    private final List<Tree> trees;
    private static final double TREE_PROBABILITY = 0.1;
    private Random rand = new Random();

    public Flora() {
        this.trees = new ArrayList<>();
    }

    public void createInRange(int minX, int maxX) {
        // todo change return val in sign
        minX = (int) (Math.floor(minX / Block.SIZE) * Block.SIZE);
        maxX = (int) (Math.ceil(maxX / Block.SIZE) * Block.SIZE);
        int numOfCols = (maxX - minX) / Block.SIZE;
        for (int i = 0; i < numOfCols; i++) {
            int curX = minX + i * Block.SIZE;
            int curY = (int)
                    (Math.floor(groundHeightAt(curX) / Block.SIZE) * Block.SIZE);
            if (rand.nextDouble() <= TREE_PROBABILITY) {
                // TODO: create tree
                Vector2 groundTopLeftCorner = new Vector2(curX, curY);
//                Tree tree = new Tree(groundTopLeftCorner, ..., ...)
            }

        }
    }
}
