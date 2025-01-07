package pepse;

import danogl.*;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;

import java.util.List;

public class PepseGameManager extends GameManager {

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        Terrain terr = new Terrain(windowController.getWindowDimensions(), 0);
        List<Block> blockList = terr.createInRange(0, 0);
        for (Block b : blockList) {
            gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
