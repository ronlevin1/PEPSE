package pepse.world;


import danogl.*;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cloud extends GameObject {
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final List<GameObject> cloudObjects = new ArrayList<>();

    private Cloud(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);

    }

    public static GameObject create(Vector2 topLeftCorner, int intitialeX,
                                    int finalX) {
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
                    Vector2 pos = new Vector2(x00 + j * Block.SIZE,
                            y00 + i * Block.SIZE);
                    Vector2 dims = new Vector2(Block.SIZE, Block.SIZE);
                    GameObject cloudBlock = new GameObject(pos, dims,
                            new RectangleRenderable(ColorSupplier.approximateMonoColor(
                                    BASE_CLOUD_COLOR)));
                    cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                    //todo: fix transition values. maybe use windowDimensions?
                    new Transition<>(
                            cloudBlock, // the game object being changed
                            (x) -> cloudBlock.transform().setTopLeftCorner(new Vector2(x, cloudBlock.getTopLeftCorner().y())), // the method to call
                            cloudBlock.getTopLeftCorner().x(), // initial
                            // transition value
                            cloudBlock.getTopLeftCorner().x() + finalX * 2, //
                            // final
                            // transition value
                            Transition.LINEAR_INTERPOLATOR_FLOAT, // use a
                            // linear interpolator
                            30, // transition duration
                            Transition.TransitionType.TRANSITION_LOOP, //
                            // loop the transition
                            null // nothing further to execute upon reaching
                            // final value
                    );
                    cloudObjects.add(cloudBlock);
//                    new Transition<>(cloudBlock,
//                            (Float x) -> cloudBlock.setTopLeftCorner(
//                                    Vector2.of(basePos.x() + Block.SIZE *
//                                    j + x,
//                                            basePos.y() + Block.SIZE * i)),
//                            0f,
//                            windowDimensions.x() + Block.SIZE *
//                            CLOUD_SHAPE.get(0).size(),
//                            Transition.LINEAR_INTERPOLATOR_FLOAT,
//                            CLOUD_SPEED,
//                            Transition.TransitionType.TRANSITION_LOOP,
//                            null);
                }
            }
        }
        return new Cloud(topLeftCorner, new Vector2(dimX, dimY), renderable);
    }

    public static List<GameObject> getCloudObjects() {
        return cloudObjects;
    }
}
