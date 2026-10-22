package core;

import game.config.PhysicsConfig;

/**
 * Physics system for game objects.
 *
 * Responsibilities:
 * - Gravity
 * - Jump physics
 * - Ground collision
 * - Position updates
 *
 * Used mainly by Cookie.
 */
public class Physics {

    // =========================
    // PHYSICS CONSTANTS (from PhysicsConfig)
    // =========================

    // Physics constants (optimized for snappy response)
    public static final double GRAVITY = PhysicsConfig.GRAVITY;
    public static final double JUMP_VELOCITY = PhysicsConfig.JUMP_VELOCITY;

    // Ground position
    public static final double GROUND_Y = PhysicsConfig.GROUND_Y;

    // Player hitbox sizes
    public static final double NORMAL_H = PhysicsConfig.NORMAL_H;
    public static final double SLIDE_H = PhysicsConfig.SLIDE_H;

    // =========================
    // STATE
    // =========================

    private double x;
    private double y;
    private double velocityY = 0;

    // Store initial position for reset
    private final double startX;
    private final double startY;

    private double width;
    private double height;

    // =========================
    // CONSTRUCTOR
    // =========================

    /**
     * Create physics object with
     * initial position and size.
     */
    public Physics(
            double x,
            double y,
            double width,
            double height
    ) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.width = width;
        this.height = height;
        this.velocityY = 0;
    }

    // =========================
    // PHYSICS UPDATE
    // =========================

    /**
     * Main physics update loop.
     *
     * Handles:
     * - Gravity
     * - Vertical movement
     * - Ground collision
     */
    public void update(double delta) {
        // Apply physics if in air OR if jumping (velocityY != 0)
        if (isInAir() || velocityY != 0) {
            velocityY += GRAVITY * delta;
            y += velocityY * delta;

            // Ground collision
            if (y >= GROUND_Y - NORMAL_H) {
                y = GROUND_Y - NORMAL_H;
                velocityY = 0;
            }
        }
    }

    // =========================
    // ACTIONS
    // =========================

    public void jump() {
        if (!isInAir()) {
            velocityY = JUMP_VELOCITY;
        }
    }

    // =========================
    // STATE CHECKS
    // =========================

    public boolean isInAir() {
        return y < GROUND_Y - NORMAL_H;
    }

    public boolean isOnGround() {
        return !isInAir();
    }

    // =========================
    // POSITION SETTERS
    // =========================

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    // =========================
    // GETTERS
    // =========================

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
    public double getVelocityY() {
        return velocityY;
    }
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    // =========================
    // RESET
    // =========================

    /**
     * Reset physics to initial state.
     *
     * Used when starting a new game.
     */
    public void resetToStart() {
        this.x = startX;
        this.y = startY;
        this.velocityY = 0;
    }
}
