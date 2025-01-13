//package pepse.world.trees;
//
//import danogl.GameObject;
//import danogl.gui.rendering.Renderable;
//import danogl.util.Vector2;
//import pepse.util.Constants;
//import pepse.world.Block;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class Tree extends GameObject {
//    private final List<GameObject> treeObjects;
//    private static final double LEAF_PROBABILITY = 0.8;
//    private static final double FRUIT_PROBABILITY = 0.1;
//
//    /**
//     * Construct a new GameObject instance.
//     *
//     * @param groundTopLeftCorner Position of the object, in window coordinates
//     *                            (pixels).
//     *                            Note that (0,0) is the top-left corner of the
//     *                            window.
//     * @param dimensions          Width and height in window coordinates.
//     * @param renderable          The renderable representing the object.
//     *                            Can be
//     *                            null, in which case
//     *                            the GameObject will not be rendered.
//     */
//    public Tree(Vector2 groundTopLeftCorner, Vector2 dimensions,
//                Renderable renderable) {
//        super(groundTopLeftCorner, dimensions, renderable);
//        this.treeObjects = new ArrayList<>();
//        create((int) groundTopLeftCorner.x(), (int) groundTopLeftCorner.y());
//    }
//
//    public List<GameObject> getTreeObjects() {
//        return treeObjects;
//    }
//
//    private void create(int x, int groundHeightAtX) {
//        // create trunk
//        // create 2D mtx of leaves and fruits
//        // store them efficiently with some Collections DS
//        // mtx size should be proportional to trunk height - between 1/3 and
//        // 2/3 of trunk height
//        // each coordinate in the mtx should have a chance to be a leaf (0
//        // .7) or a fruit (0.1)
//        // or nothing (0.2)
//
//    }
//}
