package game.config;

/**
 * Configuration constants for obstacle spawning.
 *
 * <p>Used by {@link game.managers.ObstacleManager} to decide when to drop
 * the next obstacle into the world. Actual interval per spawn is jittered
 * by {@link #SPAWN_VARIATION} around {@link #BASE_SPAWN_INTERVAL} so the
 * pattern doesn't feel mechanical.</p>
 *
 * <p>This class cannot be instantiated.</p>
 */
public class ObstacleConfig {

    /** Mean seconds between obstacle spawns at base speed. */
    public static final double BASE_SPAWN_INTERVAL = 0.8;
    /** Random variation (in seconds) added/subtracted from the base interval. */
    public static final double SPAWN_VARIATION = 0.3;

    /** Utility class - not instantiable. */
    private ObstacleConfig() {}
}
