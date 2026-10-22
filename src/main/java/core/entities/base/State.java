package core.entities.base;

/**
 * Movement / lifecycle state of a Cookie during gameplay.
 *
 * <ul>
 *   <li>{@link #RUNNING} - default state on the ground.</li>
 *   <li>{@link #JUMPING} - airborne after a jump input.</li>
 *   <li>{@link #SLIDING} - crouching to fit under air obstacles.</li>
 *   <li>{@link #DEAD}    - cookie has been defeated; game over.</li>
 * </ul>
 */
public enum State {
    /** On the ground, running automatically. */
    RUNNING,
    /** Airborne after a jump. */
    JUMPING,
    /** Crouching low to pass under air obstacles. */
    SLIDING,
    /** Defeated - game over. */
    DEAD
}
