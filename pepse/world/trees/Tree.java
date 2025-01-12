package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree extends GameObject {
    private final List<GameObject> treeObjects;
    private static final double LEAF_PROBABILITY = 0.8;
    private static final double FRUIT_PROBABILITY = 0.1;

    /**
     * Construct a new GameObject instance.
     *
     * @param groundTopLeftCorner Position of the object, in window coordinates
     *                            (pixels).
     *                            Note that (0,0) is the top-left corner of the
     *                            window.
     * @param dimensions          Width and height in window coordinates.
     * @param renderable          The renderable representing the object.
     *                            Can be
     *                            null, in which case
     *                            the GameObject will not be rendered.
     */
    public Tree(Vector2 groundTopLeftCorner, Vector2 dimensions,
                Renderable renderable) {
        super(groundTopLeftCorner, dimensions, renderable);
        this.treeObjects = new ArrayList<>();
        create((int) groundTopLeftCorner.x(), (int) groundTopLeftCorner.y());
    }

    public List<GameObject> getTreeObjects() {
        return treeObjects;
    }

    private void create(int x, int groundHeightAtX) {
        // create trunk
        // create 2D mtx of leaves and fruits
        // store them efficiently with some Collections DS
        // mtx size should be proportional to trunk height - between 1/3 and
        // 2/3 of trunk height
        // each coordinate in the mtx should have a chance to be a leaf (0
        // .7) or a fruit (0.1)
        // or nothing (0.2)
        // Create trunk
        TreeTrunk trunk = (TreeTrunk) TreeTrunk.create(new Vector2(x,
                groundHeightAtX));
        trunk.setTag("treeTrunk");
        treeObjects.add(trunk);

        // Calculate matrix size proportional to trunk height
        int trunkHeight = trunk.getHeight();
        int matrixSize =
                (int) (trunkHeight * (1.0 / 3.0 + Math.random() * (2.0 / 3.0 - 1.0 / 3.0)));
        x -= matrixSize / Constants.N_2; // shift left
        int y = groundHeightAtX - trunkHeight;
        Random random = new Random();

        // Generate leaves and fruits
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                double chance = random.nextDouble();
                Vector2 topLeftCorner = new Vector2(x + i * Block.SIZE,
                        y + j * Block.SIZE);
                if (FRUIT_PROBABILITY < chance && chance <= LEAF_PROBABILITY) {
                    GameObject leaf = Leaf.create(topLeftCorner);
                    leaf.setTag("leaf");
                    treeObjects.add(leaf);
                } else if (chance <= FRUIT_PROBABILITY) {
                    GameObject fruit = Fruit.create(topLeftCorner);
                    fruit.setTag("fruit");
                    treeObjects.add(fruit);
                }
                // otherwise - add nothing
            }
        }
    }
}
