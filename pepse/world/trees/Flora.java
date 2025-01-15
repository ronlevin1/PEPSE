package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Flora {
    private final List<List<GameObject>> trees;
    private static final double TREE_PROBABILITY = 0.1;
    private static final double LEAF_PROBABILITY = 0.75;
    private static final double FRUIT_PROBABILITY = 0.1;
    private static final int MAX_HEIGHT = Block.SIZE * 8;
    private static final int MIN_HEIGHT = Block.SIZE * 4;
    private final HeightProvider heightProvider;
    private final Consumer<Float> avatarEnergyConsumer;
    private Random rand = new Random();

    public Flora(HeightProvider heightProvider,
                 Consumer<Float> avatarEnergyConsumer) {
        this.trees = new ArrayList<>();
        this.heightProvider = heightProvider;
        this.avatarEnergyConsumer = avatarEnergyConsumer;
    }

    public List<List<GameObject>> createInRange(int minX, int maxX) {
        // todo change return val in sign?
        minX = (int) (Math.floor((double) minX / Block.SIZE) * Block.SIZE);
        maxX = (int) (Math.ceil((double) maxX / Block.SIZE) * Block.SIZE);
        int numOfCols = (maxX - minX) / Block.SIZE;
        for (int i = 0; i < numOfCols; i++) {
            int curX = minX + i * Block.SIZE;
            int groundHeightAtX = (int)
                    (Math.floor(heightProvider.getFloat(curX) / Block.SIZE) * Block.SIZE);
            if (rand.nextDouble() <= TREE_PROBABILITY) {
                List<GameObject> tree = createSingleTree(curX,
                        groundHeightAtX);
                trees.add(tree);
            }
        }
        return this.trees;
    }

    private List<GameObject> createSingleTree(int x, int groundHeightAtX) {
        List<GameObject> treeObjects = new ArrayList<>();
        // Create trunk
        // Calculate trunk height and round to nearest multiple of Block.SIZE
        // Ensure trunkHeight is within the range of MAX_HEIGHT and MIN_HEIGHT
        int trunkHeight =
                (int) (rand.nextDouble() * (MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT);
        trunkHeight =
                (int) (Math.round(trunkHeight / (double) Block.SIZE) * Block.SIZE);
        trunkHeight = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, trunkHeight));

        Vector2 trunkTopLeft = new Vector2(x, groundHeightAtX - trunkHeight);
        Tree trunk = (Tree) Tree.create(trunkTopLeft,
                trunkHeight);
        trunk.setTag(Constants.TREE_TRUNK);
        treeObjects.add(trunk);

        // Calculate matrix size proportional to trunk height
        int matrixSize =
                (int) (trunkHeight * ((double) Constants.N_2 / Constants.N_3));
        int xAt00 = x - matrixSize / Constants.N_2; // shift left
        int yAt00 = groundHeightAtX - trunkHeight - matrixSize / Constants.N_2;
        Random random = new Random();

        // Generate leaves and fruits
        for (int i = 0; i < matrixSize; i += Block.SIZE) {
            for (int j = 0; j < matrixSize; j += Block.SIZE) {
                double chance = random.nextDouble();
                Vector2 topLeftCorner = new Vector2(xAt00 + i,
                        yAt00 + j);
                if (FRUIT_PROBABILITY < chance && chance <=
                        LEAF_PROBABILITY) {
                    GameObject leaf = Leaf.create(topLeftCorner);
                    leaf.setTag(Constants.LEAF);
                    treeObjects.add(leaf);
                } else if (chance <= FRUIT_PROBABILITY) {
                    GameObject fruit = Fruit.create(topLeftCorner,
                            avatarEnergyConsumer);
                    fruit.setTag(Constants.FRUIT);
                    treeObjects.add(fruit);
                }
                // otherwise - add nothing
            }
        }
        return treeObjects;
    }
}
