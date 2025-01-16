package pepse;

import danogl.*;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.HeightProvider;

import java.util.*;
import java.util.function.Consumer;

/**
 * The main class of the game. This class is responsible for initializing
 * the game
 * and setting up the game objects. It also contains the main method to run
 * the game.
 */
public class PepseGameManager extends GameManager {
    public static final float CLOUD_INITIAL_X = -200;
    private final Random rand = new Random();
    private int WINDOW_PADDING;
    private int leftMostX;
    private int rightMostX;
    private final int[] exitingObjectsRange = new int[2];
    Map<Float, List<GameObject>> existingBlocks = new HashMap<>();
    Map<Float, List<List<GameObject>>> existingTrees = new HashMap<>();
    Map<Float, Float> existingGroundHeight = new HashMap<>();
    private Avatar avatar;
    private Terrain terrain;
    private Flora flora;


    /**
     * This method is called once at the beginning of the game. It is used to
     * initialize all the game objects and set up the game.
     *
     * @param imageReader      Contains a single method: readImage, which
     *                         reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which
     *                         reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed, which
     *                         returns whether
     *                         a given key is currently pressed by the user
     *                         or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful, self
     *                         explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        WINDOW_PADDING = (int) windowDimensions.x() / Constants.N_2;
        leftMostX = -WINDOW_PADDING;
        rightMostX = (int) windowDimensions.x() + WINDOW_PADDING;
        exitingObjectsRange[0] = leftMostX;
        exitingObjectsRange[1] = rightMostX;
        // Initiate Objects
        initSky(windowDimensions);
        initSunWithHalo(windowDimensions);
        initTerrainWithBlocks(windowDimensions);
        initAvatar(imageReader, inputListener, windowController, terrain);
        initCloud(windowDimensions, avatar, gameObjects()::addGameObject,
                gameObjects()::removeGameObject);
        initTrees(terrain, avatar);
        initNight(windowDimensions);
        initUI(avatar);
    }

    private void initUI(Avatar avatar) {
        // UIManager
        UIManager uiManager = UIManager.getInstance(Vector2.ONES,
                Vector2.ONES.mult(50), avatar::getAvatarEnergy);
        gameObjects().addGameObject(uiManager, Layer.UI);
    }

    private void initNight(Vector2 windowDimensions) {
        // Night
        GameObject night = Night.create(windowDimensions,
                Constants.CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.UI);
    }

    private void initTrees(Terrain terr, Avatar avatar) {
        // Trees
        HeightProvider groundHeightProvider = terr::groundHeightAt;
        Consumer<Float> avatarEnergyConsumer =
                avatar::addEnergyFromOtherObject;
        this.flora = new Flora(groundHeightProvider, avatarEnergyConsumer);
        createFloraInRange(leftMostX, rightMostX);
    }

    private void createFloraInRange(int minX, int maxX) {
        List<List<GameObject>> trees = flora.createInRange(minX, maxX);
        float x;
        for (List<GameObject> tree : trees) {
            for (GameObject treeObject : tree) {
                int curLayer = Layer.DEFAULT;
                if (treeObject.getTag().equals(Constants.TREE_TRUNK)) {
                    curLayer = Layer.STATIC_OBJECTS;
                    // store existing trees
                    x = treeObject.getTopLeftCorner().x();
                    float groundHeightAtX = terrain.groundHeightAt(x);
                    existingTrees.put(x, trees);
                    existingGroundHeight.put(x, groundHeightAtX);
                } else if (treeObject.getTag().equals(Constants.LEAF))
                    curLayer = Layer.UI;
                gameObjects().addGameObject(treeObject, curLayer);
            }
        }
    }

    private void initAvatar(ImageReader imageReader,
                            UserInputListener inputListener,
                            WindowController windowController,
                            Terrain terr) {
        // Avatar
        float avatarX = 0; // top left corner dimensions
        float avatarY =
                terr.groundHeightAt(avatarX) - Constants.AVATAR_HEIGHT * Constants.N_2;
        Avatar avatar = new Avatar(new Vector2(avatarX, avatarY),
                inputListener,
                imageReader);
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        avatar.setTag(Constants.AVATAR);
        gameObjects().addGameObject(avatar);
        this.avatar = avatar;
    }

    private void initTerrainWithBlocks(Vector2 windowDimensions) {
        // Terrain with Blocks
        int seed = (int) (rand.nextGaussian() * Constants.N_10);
        this.terrain = new Terrain(windowDimensions, seed);
        createTerrainInRange(leftMostX, rightMostX);
    }

    private void createTerrainInRange(int minX, int maxX) {
        List<Block> blockList = terrain.createInRange(minX, maxX);
        for (Block block : blockList) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
            float x = block.getTopLeftCorner().x();
            float groundHeightAtX = terrain.groundHeightAt(x);
            if (existingBlocks.containsKey(x)) {
                existingBlocks.get(x).add(block);
            } else {
                List<GameObject> l = new ArrayList<>(List.of(block));
                existingBlocks.put(x, l);
            }
            existingGroundHeight.put(x, groundHeightAtX);
        }
    }

    private void initCloud(Vector2 windowDimensions, Avatar avatar,
                           Consumer<GameObject> addGameObjectCallback,
                           Consumer<GameObject> removeGameObjectCallback) {
        // Cloud
        GameObject cloud = Cloud.create(new Vector2(CLOUD_INITIAL_X,
                        windowDimensions.y() / Constants.N_10),
                (int) CLOUD_INITIAL_X, (int) (rightMostX),
                addGameObjectCallback, removeGameObjectCallback);
        for (GameObject cloudObject : Cloud.getCloudObjects()) {
            cloudObject.setTag(Constants.CLOUD);
            gameObjects().addGameObject(cloudObject, Layer.BACKGROUND);
        }
        if (cloud instanceof AvatarListener) {
            avatar.addListener((AvatarListener) cloud);
        }
    }

    private void initSunWithHalo(Vector2 windowDimensions) {
        // Sun + SunHalo
        GameObject sun = Sun.create(windowDimensions, Constants.CYCLE_LENGTH);
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
    }

    private void initSky(Vector2 windowDimensions) {
        // Sky
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    /**
     * This method is called once per frame. It is used to update the game
     *
     * @param deltaTime The time, in seconds, that passed since the last
     *                  invocation
     *                  of this method (i.e., since the last frame). This is
     *                  useful
     *                  for either accumulating the total time that passed
     *                  since some
     *                  event, or for physics integration (i.e., multiply
     *                  this by
     *                  the acceleration to get an estimate of the added
     *                  velocity or
     *                  by the velocity to get an estimate of the difference
     *                  in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
//        System.out.println(avatar.getCenter().x());
        infiniteWorldHandler();
    }

    // TODO: Infinite world
    private void infiniteWorldHandler() {
        /*
        idea notes.
        store all game object in 2 maps:
        - (x, groundHeightAtX) -> List<List<GameObject>> trees
        - (x, groundHeightAtX) -> List<GameObject> block
        use update to get avatar x position.
        compare avatar x position to some threshold for left and right:
        - if under threshold to the right,
            generate new blocks and trees to the right
            remove blocks and trees to the left
        - if under threshold to the left,
            generate new blocks and trees to the left
            remove blocks and trees to the right
        use the maps to get the blocks and trees to remove and add.
        use Terrain.createInRange and Flora.createInRange to generate new
        blocks and trees.
         */
        float threshold = (float) WINDOW_PADDING;
        boolean underRightThreshold =
                (Math.abs(avatar.getCenter().x() - rightMostX) < threshold);
        boolean underLeftThreshold =
                (Math.abs(avatar.getCenter().x() - leftMostX) < threshold);
        if (underRightThreshold) {
            // todo: first check if new range is within the old world bounds
            //  and needs to be recreated from old objects
            int newRightMostX = (int) (rightMostX + threshold);
            int newLeftMostX = (int) (leftMostX + threshold);
            // generate new blocks and tees to the right
            /*
            NOTE: to create a single column of block, send input param s.t.
            maxX = minX + Block.SIZE
             */
            createTerrainInRange(rightMostX, newRightMostX);
            // TODO: BUG: seems that the trees are not being generated
            // since groundHeightAtX is not being calculated correctly
            createFloraInRange(newLeftMostX, leftMostX);
            // remove blocks and trees to the left
            // ...
            //todo
            rightMostX = newRightMostX;
            leftMostX = newLeftMostX;
        }
        // left
        if (underLeftThreshold) {
            int newRightMostX = (int) (rightMostX - threshold);
            int newLeftMostX = (int) (leftMostX - threshold);
            // generate new blocks and trees to the left
//            for (Block block : terrain.createInRange(rightMostX,
//                    newRightMostX)) {
//                gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
//            }
//            for (List<GameObject> tree : flora.createInRange(newLeftMostX,
//                    leftMostX)) {
//                for (GameObject treeObject : tree) {
//                    int curLayer = Layer.DEFAULT;
//                    if (treeObject.getTag().equals(Constants.TREE_TRUNK))
//                        curLayer = Layer.STATIC_OBJECTS;
//                    else if (treeObject.getTag().equals(Constants.LEAF))
//                        curLayer = Layer.UI;
//                    gameObjects().addGameObject(treeObject, curLayer);
//                }
//            }
            // remove blocks and trees to the right
            //todo
            rightMostX = newRightMostX;
            leftMostX = newLeftMostX;
        }
        // update exiting objects range
        exitingObjectsRange[0] = Math.min(leftMostX, exitingObjectsRange[0]);
        exitingObjectsRange[1] = Math.max(rightMostX, exitingObjectsRange[1]);
    }


    /**
     * Main method to run the game.
     *
     * @param args
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
