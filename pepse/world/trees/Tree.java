package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree extends GameObject {
    private TreeTrunk trunk;
    private List<GameObject> leavesAndFruits;

    private static final double LEAF_PROBABILITY = 0.7;
    private static final double FRUIT_PROBABILITY = 0.1;

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
        this.leavesAndFruits = new ArrayList<>();
    }


    public void create(int x, int groundHeightAtX) {
        // create trunk
        // create 2D mtx of leaves and fruits
        // store them efficiently with some Collections DS
        // mtx size should be proportional to trunk height - between 1/3 and
        // 2/3 of trunk height
        // each coordinate in the mtx should have a chance to be a leaf (0
        // .7) or a fruit (0.1)
        // or nothing (0.2)
        // Create trunk
        trunk = (TreeTrunk) TreeTrunk.create(new Vector2(x, groundHeightAtX));
        GameObject trunkObject = trunk.create(new Vector2(x, groundHeightAtX));
        leavesAndFruits.add(trunkObject);

        // Calculate matrix size proportional to trunk height
        int trunkHeight = trunk.getHeight();
        int matrixSize =
                (int) (trunkHeight * (1.0 / 3.0 + Math.random() * (2.0 / 3.0 - 1.0 / 3.0)));

        Random random = new Random();

        // Generate leaves and fruits
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                double chance = random.nextDouble();
                Vector2 topLeftCorner = new Vector2(x + i * Block.SIZE,
                        groundHeightAtX - trunkHeight - j * Block.SIZE);
                if (chance < LEAF_PROBABILITY) {
                    GameObject leaf = Leaf.create(topLeftCorner);
                    leavesAndFruits.add(leaf);
                } else if (chance < LEAF_PROBABILITY + FRUIT_PROBABILITY) {
                    GameObject fruit = Fruit.create(topLeftCorner);
                    leavesAndFruits.add(fruit);
                }
            }
        }
        //todo: add to gameObjects() the trunk and all leaves and fruits
    }
}
