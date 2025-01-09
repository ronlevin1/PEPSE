package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    // energy level constants
    private static final float MAX_ENERGY = 100;
    private static final float MIN_ENERGY = 0;
    private static final float REST_ENERGY = 1;
    private static final float RUN_ENERGY = 0.5F;
    private static final float JUMP_ENERGY = 10;
    public static final String RUN_STATE = "run";
    public static final String JUMP_STATE = "jump";
    private float avatarEnergy = MAX_ENERGY;

    private UserInputListener inputListener;

    public Avatar(Vector2 pos, UserInputListener inputListener,
                  ImageReader imageReader) {
        super(pos, Vector2.ONES.mult(50),
                new ImageRenderable(imageReader.readImage(
                        "assets/idle_0.png", true).getImage()));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        boolean runIsPossible = avatarEnergy >= RUN_ENERGY;
        boolean jumpIsPossible = avatarEnergy >= JUMP_ENERGY;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && runIsPossible)
            xVel -= VELOCITY_X;
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && runIsPossible)
            xVel += VELOCITY_X;
        transform().setVelocityX(xVel);
        updateEnergy(RUN_STATE);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)
                && getVelocity().y() == 0 && jumpIsPossible) {
            transform().setVelocityY(VELOCITY_Y);
            updateEnergy(JUMP_STATE);
        }
    }

    private void updateEnergy(String action) {
        boolean isNotRunning = getVelocity().x() == 0;
        boolean isNotJumping = getVelocity().y() == 0;
        if (action.equals(RUN_STATE) && isNotJumping)
            avatarEnergy -= RUN_ENERGY;
        // jump
        if (action.equals(JUMP_STATE) && isNotJumping)
            avatarEnergy -= JUMP_ENERGY;
        // rest
        if (isNotRunning && isNotJumping)
            avatarEnergy = Math.min(avatarEnergy + REST_ENERGY, MAX_ENERGY);
    }

    public void addEnergyFromOtherObject(float energy) {
        avatarEnergy = Math.min(avatarEnergy + energy, MAX_ENERGY);
    }
}
