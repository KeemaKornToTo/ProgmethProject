package game.config;

/**
 * Game-wide configuration constants.
 *
 * <p>Central place for tunable settings shared across the whole game:
 * window dimensions, frame-rate target, base scroll speed, and audio volumes.
 * All fields are static finals so they can be inlined by the compiler and used
 * as compile-time constants in {@code switch} statements and array sizes.</p>
 *
 * <p>This class cannot be instantiated.</p>
 */
public class GameConfig {

    /** Game window width in pixels. */
    public static final int WINDOW_WIDTH = 800;
    /** Game window height in pixels. */
    public static final int WINDOW_HEIGHT = 450;
    /** Whether the user is allowed to resize the window. */
    public static final boolean RESIZABLE = false;

    /** Base horizontal scroll speed in pixels/second before stage multipliers. */
    public static final double BASE_GAME_SPEED = 300.0;

    /** Target frames-per-second for the game loop. */
    public static final int TARGET_FPS = 60;
    /** When true, draws the current FPS in the corner of the canvas. */
    public static final boolean SHOW_FPS = true;

    /** Master switch for all in-game audio. */
    public static final boolean AUDIO_ENABLED = true;
    /** Master volume multiplier (0.0 - 1.0). Applied on top of music/SFX volumes. */
    public static final double MASTER_VOLUME = 1.0;
    /** Background music volume (0.0 - 1.0). */
    public static final double MUSIC_VOLUME = 0.7;
    /** Sound-effect volume (0.0 - 1.0). */
    public static final double SFX_VOLUME = 0.8;

    /** Utility class - not instantiable. */
    private GameConfig() {}
}
