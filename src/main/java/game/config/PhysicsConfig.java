package game.config;

/**
 * Physics tuning constants used by {@link core.Physics}.
 *
 * <p>Y grows downward in JavaFX canvas coordinates, so jump velocity is negative
 * and gravity is positive. The ground line and hitbox heights here drive both
 * collision math and the cookie's vertical position on screen.</p>
 *
 * <p>This class cannot be instantiated.</p>
 */
public class PhysicsConfig {

    /** Gravitational acceleration in pixels/second^2 (positive = pulls down). */
    public static final double GRAVITY = 2000.0;

    /** Initial upward velocity applied on jump (negative because Y grows downward). */
    public static final double JUMP_VELOCITY = -750.0;

    /** Y-coordinate of the ground line. Cookie's feet rest here. */
    public static final double GROUND_Y = 290.0;

    /** Cookie hitbox height while running / jumping. */
    public static final double NORMAL_H = 70.0;
    /** Cookie hitbox height while sliding (used so the player can fit under air obstacles). */
    public static final double SLIDE_H = 40.0;

    /** Utility class - not instantiable. */
    private PhysicsConfig() {}
}
