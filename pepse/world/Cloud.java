package pepse.world;


import danogl.*;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import org.w3c.dom.ls.LSOutput;
import pepse.util.ColorSupplier;
import pepse.util.Constants;

import javax.security.auth.callback.Callback;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Cloud extends GameObject implements AvatarListener {
    private static final float RAINDROP_DURATION = 5;
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final List<GameObject> cloudObjects = new ArrayList<>();
    Consumer<GameObject> addGameObjectCallback;
    Consumer<GameObject> removeGameObjectCallback;

    private Cloud(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable,
                  Consumer<GameObject> addGameObjectCallback,
                  Consumer<GameObject> removeGameObjectCallback) {
        super(topLeftCorner, dimensions, renderable);
        this.addGameObjectCallback = addGameObjectCallback;
        this.removeGameObjectCallback = removeGameObjectCallback;
    }

    public static GameObject create(Vector2 topLeftCorner, int intitialX,
                                    int finalX,
                                    Consumer<GameObject> addGameObjectCallback,
                                    Consumer<GameObject> removeGameObjectCallback) {

        Renderable renderable =
                new RectangleRenderable(ColorSupplier.approximateMonoColor(
                        BASE_CLOUD_COLOR));
        List<List<Integer>> cloud = List.of(
                List.of(0, 1, 1, 0, 0, 0),
                List.of(1, 1, 1, 0, 1, 0),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(0, 1, 1, 1, 0, 0),
                List.of(0, 0, 0, 0, 0, 0)
        );
        int x00 = (int) topLeftCorner.x();
        int y00 = (int) topLeftCorner.y();
        int dimX = cloud.get(0).size();
        int dimY = cloud.size();
        for (int i = 0; i < dimY; i++) {
            for (int j = 0; j < dimX; j++) {
                if (cloud.get(j).get(i) == 1) {
                    createSingleCloudBlock(intitialX, finalX, x00, y00, i, j);
                }
            }
        }
        return new Cloud(topLeftCorner, new Vector2(dimX, dimY), renderable,
                addGameObjectCallback, removeGameObjectCallback);
    }

    private static void createSingleCloudBlock(int intitialX, int finalX,
                                               int x00,
                                               int y00,
                                               int i, int j) {
        Vector2 pos = new Vector2(x00 + j * Block.SIZE,
                y00 + i * Block.SIZE);
        Vector2 dims = new Vector2(Block.SIZE, Block.SIZE);
        GameObject cloudBlock = new GameObject(pos, dims,
                new RectangleRenderable(ColorSupplier.approximateMonoColor(
                        BASE_CLOUD_COLOR)));
        cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        new Transition<Float>(
                cloudBlock, // the game object being changed
                (x) -> cloudBlock.transform().setTopLeftCorner(
                        new Vector2(x, pos.y())),
                (float) intitialX + pos.x(),
                (float) finalX + pos.x(),
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                30, // transition duration
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );
        cloudObjects.add(cloudBlock);
    }
    // old transition values
//        new Transition<>(
//                cloudBlock, // the game object being changed
//                (x) -> cloudBlock.transform().setTopLeftCorner(
//                        new Vector2(x,
//                                cloudBlock.getTopLeftCorner().y())),
//                cloudBlock.getTopLeftCorner().x(),
//                cloudBlock.getTopLeftCorner().x() + finalX * 2,
//                Transition.LINEAR_INTERPOLATOR_FLOAT,
//                30, // transition duration
//                Transition.TransitionType.TRANSITION_LOOP,
//                null
//        );


    public static List<GameObject> getCloudObjects() {
        return cloudObjects;
    }

    @Override
    public void onAvatarJump() {
        for (GameObject cloudBlock : cloudObjects) {
            if (Math.random() < 0.3) {
                createSingleRaindrop(cloudBlock.getTopLeftCorner());
                createSingleRaindrop(cloudBlock.getTopLeftCorner());
            }
        }
    }

    private void createSingleRaindrop(Vector2 topLeftCorner) {
        // values
        float size = (float) Block.SIZE / Constants.N_3;
        Vector2 dims = new Vector2(size, size);
        Color rainDropColor = ColorSupplier.approximateMonoColor(
                new Color(141, 229, 248, 100));
        Renderable renderable = new OvalRenderable(rainDropColor);
        // object
        GameObject raindrop = new GameObject(topLeftCorner, dims, renderable);
        raindrop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        raindrop.transform().setAccelerationY(Constants.GRAVITY / Constants.N_4);
        new Transition<>(
                raindrop, // the game object being changed
                raindrop.renderer()::fadeOut, // the method to call
                1f, // initial transition value
                0f, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                RAINDROP_DURATION,
                Transition.TransitionType.TRANSITION_ONCE,
                () -> this.removeGameObjectCallback.accept(raindrop)
        );// nothing further to execute upon reaching final value
        this.addGameObjectCallback.accept(raindrop);
    }
}
