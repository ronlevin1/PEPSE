package pepse.util;

/**
 * Constants for the game
 */
public class Constants {
    /**
     * Constants for the game
     */
    public static final int CYCLE_LENGTH = 30;
    public static final int AVATAR_HEIGHT = 50;
    public static final int COLOR_DELTA = 40;
    public static final float GRAVITY = 600;
    /**
     * sometimes u just need to mult/div by some number
     */
    public static final int N_2 = 2;
    public static final int N_3 = 3;
    public static final int N_4 = 4;
    public static final int N_10 = 10;
    // More Layers
    /**
     * between BACKGROUND (Sun) and STATIC_OBJECTS (Blocks).
     * Avatar is in front of Blocks on default layer.
     */
    public static final int LEAF_OR_FRUIT_SIZE = 28;

    /**
     * Object tags
     */
    public static final String BLOCK = "block";
    public static final String TREE_TRUNK = "treeTrunk";
    public static final String LEAF = "leaf";
    public static final String FRUIT = "fruit";
    public static final String AVATAR = "avatar";
    public static final String CLOUD = "cloud";
}
