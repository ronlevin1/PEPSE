package pepse;

import danogl.*;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.HeightProvider;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class PepseGameManager extends GameManager {
    private Random rand = new Random();
    private int leftMostX;
    private int rightMostX;

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        // todo: shorten method
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        leftMostX = (int) -windowDimensions.x() / Constants.N_2;
        rightMostX = (int) windowDimensions.x();
        // Initiate Objects
        initSky(windowDimensions);
        initSunWithHalo(windowDimensions);
        Terrain terr = initTerrainWithBlock(windowDimensions);
        Avatar avatar = initAvatar(imageReader, inputListener,
                windowController, terr);
        initCloud(windowDimensions, avatar,
                gameObjects()::addGameObject, gameObjects()::removeGameObject);
        initTrees(terr, avatar);
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
        Flora flora = new Flora(groundHeightProvider, avatarEnergyConsumer);
        List<List<GameObject>> trees = flora.createInRange(leftMostX,
                rightMostX);
        for (List<GameObject> tree : trees) {
            for (GameObject treeObject : tree) {
                int curLayer = Layer.DEFAULT;
                if (treeObject.getTag().equals(Constants.TREE_TRUNK))
                    curLayer = Layer.STATIC_OBJECTS;
                else if (treeObject.getTag().equals(Constants.LEAF))
                    curLayer = Layer.UI;
                gameObjects().addGameObject(treeObject, curLayer);
            }
        }
    }

    private Avatar initAvatar(ImageReader imageReader,
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
        return avatar;
    }

    private Terrain initTerrainWithBlock(Vector2 windowDimensions) {
        // Terrain with Blocks
        int seed = (int) (rand.nextGaussian() * Constants.N_10);
        Terrain terr = new Terrain(windowDimensions, seed);
        List<Block> blockList = terr.createInRange(leftMostX, rightMostX);
        for (Block block : blockList) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        return terr;
    }

    private void initCloud(Vector2 windowDimensions, Avatar avatar,
                           Consumer<GameObject> addGameObjectCallback,
                           Consumer<GameObject> removeGameObjectCallback) {
        // Cloud
        float cloudInitialX = -200;
        GameObject cloud = Cloud.create(new Vector2(cloudInitialX,
                        windowDimensions.y() / Constants.N_10), leftMostX,
                rightMostX, addGameObjectCallback, removeGameObjectCallback);
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

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
