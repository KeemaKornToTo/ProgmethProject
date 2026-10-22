package utils;

/**
 * Interface for objects that have sprite-based frame animation.
 * Implemented by Cookie (and any future animated characters).
 */
public interface Animatable {
    /**
     * Advances the animation to the next frame.
     */
    void nextFrame();

    /**
     * Sets the current animation by name.
     * @param animationName The name of the animation (e.g., "RUN", "JUMP").
     */
    void setAnimation(String animationName);

    /**
     * @return the index of the currently active animation frame.
     */
    int  getCurrentFrame();
}
