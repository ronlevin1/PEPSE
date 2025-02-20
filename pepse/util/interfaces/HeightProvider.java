package pepse.util.interfaces;

/**
 * This interface is used to provide the height of the terrain at a given
 * x-coordinate.
 */
@FunctionalInterface
public interface HeightProvider {
    /**
     * get the value of some function executed on input x.
     */
    float getFloat(float x);
}
