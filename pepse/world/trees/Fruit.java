package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Constants;

import java.awt.*;
import java.util.function.Consumer;

public class Fruit extends GameObject {
    private static final Color FRUIT_COLOR = new Color(174, 28, 28);
    private static final int RADIUS = Constants.LEAF_OR_FRUIT_SIZE;
    private static final int REGENERATION_CYCLE = Constants.CYCLE_LENGTH;
    private static final float FRUIT_ENERGY = Constants.N_10;
    private final Renderable renderable;
    private Consumer<Float> avatarEnergyConsumer;
    private boolean isCollisionEnabed = true;

    private Fruit(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable,
                  Consumer<Float> avatarEnergyConsumer) {
        super(topLeftCorner, dimensions, renderable);
        this.avatarEnergyConsumer = avatarEnergyConsumer;
        this.renderable = renderable;
        this.setCollisionEnabled(true);
    }

    public static GameObject create(Vector2 topLeftCorner,
                                    Consumer<Float> avatarEnergyConsumer) {
        Renderable renderable =
                new OvalRenderable(ColorSupplier.approximateColor(FRUIT_COLOR, Constants.COLOR_DELTA));
        return new Fruit(topLeftCorner, new Vector2(RADIUS, RADIUS),
                renderable, avatarEnergyConsumer);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (isCollisionEnabed && other.getTag().equals(Constants.AVATAR)) {
            avatarEnergyConsumer.accept(FRUIT_ENERGY);
            // schedule task to disappear and reappear
            this.renderer().setRenderable(null); // Make the fruit disappear
            this.setCollisionEnabled(false); // Disable collision
            new ScheduledTask(this, REGENERATION_CYCLE, false,
                    this::regenerateFruit);
        }
    }

    private void regenerateFruit() {
        // Make the fruit reappear
        this.renderer().setRenderable(this.renderable); //
        this.setCollisionEnabled(true);
    }

    private void setCollisionEnabled(boolean bool) {
        this.isCollisionEnabed = bool;
    }
}
