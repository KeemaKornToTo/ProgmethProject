package game.config;

/**
 * Configuration constants for collectible (coin, jelly) spawning.
 *
 * <p>Used by {@link game.managers.CollectibleManager}. The actual interval
 * between spawns is jittered by {@link #SPAWN_VARIATION} around
 * {@link #BASE_SPAWN_INTERVAL} so coins/jellies don't appear in a rigid pattern.</p>
 *
 * <p>This class cannot be instantiated.</p>
 */
public class CollectibleConfig {

    /** Mean seconds between collectible spawns. */
    public static final double BASE_SPAWN_INTERVAL = 0.9;
    /** Random variation (in seconds) added/subtracted from the base interval. */
    public static final double SPAWN_VARIATION = 0.7;

    /** Utility class - not instantiable. */
    private CollectibleConfig() {}
}
