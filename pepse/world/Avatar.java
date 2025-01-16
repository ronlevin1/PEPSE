package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.Constants;
import pepse.util.interfaces.AvatarListener;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The main character of the game. This class is responsible for the avatar's
 * movement and energy level. It also contains the main method to run the game.
 */
public class Avatar extends GameObject {
    // movement constants
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    // energy level constants
    private static final float MAX_ENERGY = 100;
    private static final float MIN_ENERGY = 0;
    private static final float REST_ENERGY = 1;
    private static final float RUN_ENERGY = 0.5F;
    private static final float JUMP_ENERGY = 10;
    // state constants
    private static final String RUN_STATE = "run";
    private static final String JUMP_STATE = "jump";
    private static final String IDLE_STATE = "idle";
    public static final double TIME_BETWEEN_CLIPS = 0.2;
    // state variables
    private float avatarEnergy = MAX_ENERGY;
    private String avatarState;
    private static final Renderable[] idlePics = new Renderable[4];
    private static final Renderable[] jumpPics = new Renderable[4];
    private static final Renderable[] runPics = new Renderable[6];
    // Observers
    private List<AvatarListener> avatarListeners = new ArrayList<>();
    // Others
    private UserInputListener inputListener;

    /**
     * This method is called once at the beginning of the game. It is used to
     * initialize all the game objects and set up the game.
     *
     * @param pos           The position of the avatar.
     * @param inputListener Contains a single method: isKeyPressed, which
     *                      returns whether
     *                      a given key is currently pressed by the user or
     *                      not. See its
     *                      documentation.
     * @param imageReader   Contains a single method: readImage, which reads
     *                      an image from disk.
     *                      See its documentation for help.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener,
                  ImageReader imageReader) {
        super(pos, Vector2.ONES.mult(50), null);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        int i = 0;
        for (String path : List.of("assets/idle_0.png", "assets/idle_1.png",
                "assets/idle_2.png", "assets/idle_3.png")) {
            idlePics[i] =
                    new ImageRenderable(imageReader.readImage(path, true).getImage());
            i++;
        }
        i = 0;
        for (String path : List.of("assets/jump_0.png", "assets/jump_1.png",
                "assets/jump_2.png", "assets/jump_3.png")) {
            jumpPics[i] =
                    new ImageRenderable(imageReader.readImage(path, true).getImage());
            i++;
        }
        i = 0;
        for (String path : List.of("assets/run_0.png", "assets/run_1.png",
                "assets/run_2.png", "assets/run_3.png", "assets/run_4.png",
                "assets/run_5.png")) {
            runPics[i] =
                    new ImageRenderable(imageReader.readImage(path, true).getImage());
            i++;
        }
        avatarState = IDLE_STATE;
        changeAnimation();
    }

    /**
     * This method is called once per frame. It is used to update the avatar's
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by
     *                  multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        boolean runIsPossible = avatarEnergy >= RUN_ENERGY;
        boolean jumpIsPossible = avatarEnergy >= JUMP_ENERGY;
        boolean isNotMoving = getVelocity().equals(Vector2.ZERO);

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && runIsPossible) {
            xVel -= VELOCITY_X;
            renderer().setIsFlippedHorizontally(true);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && runIsPossible) {
            xVel += VELOCITY_X;
            renderer().setIsFlippedHorizontally(false);
        }
        transform().setVelocityX(xVel);

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0 && jumpIsPossible) {
            transform().setVelocityY(VELOCITY_Y);
            updateState(JUMP_STATE);
        } else if (isNotMoving) {
            updateState(IDLE_STATE);
        } else {
            updateState(RUN_STATE);
        }
    }

    private void updateState(String state) {
        if (state.equals(RUN_STATE) && getVelocity().y() == 0) {
            avatarEnergy -= RUN_ENERGY;
        } else if (state.equals(JUMP_STATE)) {
            avatarEnergy -= JUMP_ENERGY;
            notifyListeners();
        } else if (state.equals(IDLE_STATE)) {
            avatarEnergy = Math.min(avatarEnergy + REST_ENERGY, MAX_ENERGY);
        }
        if (!avatarState.equals(state)) {
            avatarState = state;
            changeAnimation();
        }
    }

    private void changeAnimation() {
        Renderable[] renderables;
        switch (avatarState) {
            case IDLE_STATE:
                renderables = idlePics;
                break;
            case JUMP_STATE:
                renderables = jumpPics;
                break;
            case RUN_STATE:
                renderables = runPics;
                break;
            default:
                renderables = idlePics;
        }
        AnimationRenderable animationRenderable =
                new AnimationRenderable(renderables, TIME_BETWEEN_CLIPS);
        renderer().setRenderable(animationRenderable);
    }

    /**
     * add Energy From Other Object
     *
     * @param energy energy to add
     */
    public void addEnergyFromOtherObject(float energy) {
        avatarEnergy = Math.min(avatarEnergy + energy, MAX_ENERGY);
    }

    /**
     * get Avatar Energy
     *
     * @return avatar energy
     */
    public float getAvatarEnergy() {
        return avatarEnergy;
    }

    /**
     * add Listener
     *
     * @param listener listener
     */
    public void addListener(AvatarListener listener) {
        avatarListeners.add(listener);
    }

    /**
     * remove Listener
     *
     * @param listener listener
     */
    public void removeListener(AvatarListener listener) {
        avatarListeners.remove(listener);
    }

    /**
     * notify Listeners
     */
    public void notifyListeners() {
        for (AvatarListener listener : avatarListeners) {
            listener.onAvatarJump();
        }
    }

    /**
     * on Collision Enter
     *
     * @param other     other game object
     * @param collision collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // NOTE: this doesnt solve the problem of avatar entering beneath
        // first block layer.
        if (other.getTag().equals(Constants.BLOCK)) {
            this.transform().setVelocityY(0);
        }
    }
}

//old code
//    @Override
//    public void update(float deltaTime) {
//        super.update(deltaTime);
//        float xVel = 0;
//        boolean runIsPossible = avatarEnergy >= RUN_ENERGY;
//        boolean jumpIsPossible = avatarEnergy >= JUMP_ENERGY;
//        boolean isNotMoving = getVelocity().equals(Vector2.ZERO);
//
//        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && runIsPossible)
//            xVel -= VELOCITY_X;
//        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && runIsPossible)
//            xVel += VELOCITY_X;
//        transform().setVelocityX(xVel);
//        updateState(RUN_STATE);
//        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)
//                && getVelocity().y() == 0 && jumpIsPossible) {
//            transform().setVelocityY(VELOCITY_Y);
//            updateState(JUMP_STATE);
//        }
//        if (isNotMoving) {
//            updateState(IDLE_STATE);
//        }
//    }
//
//    private void updateState(String state) {
//        boolean isNotRunning = getVelocity().x() == 0;
//        boolean isRunningLeft = getVelocity().x() < 0;
//        boolean isNotJumping = getVelocity().y() == 0;
//        // run
//        if (state.equals(RUN_STATE) && isNotJumping) {
//            avatarEnergy -= RUN_ENERGY;
/// /            avatarState = state;
/// /            changeAnimation();
/// /            if (isRunningLeft)
/// /                renderer().setIsFlippedHorizontally(true);
//        }
//        // jump
/// /        if (state.equals(JUMP_STATE) && isNotJumping) {
//        else if (state.equals(JUMP_STATE)) {
//            doJump();
/// /            avatarEnergy -= JUMP_ENERGY;
/// /            avatarState = state;
/// /            changeAnimation();
//        }
//        // rest
//        if (state.equals(IDLE_STATE)) {
//            avatarEnergy = Math.min(avatarEnergy + REST_ENERGY, MAX_ENERGY);
/// /            avatarState = state;
/// /            changeAnimation();
//        }
//        avatarState = state;
//        changeAnimation();
//    }
//
//    private void doJump() {
//        avatarEnergy -= JUMP_ENERGY;

/// /        avatarState = JUMP_STATE;
/// /        changeAnimation();
//        notifyListeners();
//    }
