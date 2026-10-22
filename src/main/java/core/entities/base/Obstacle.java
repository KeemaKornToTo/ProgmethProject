package core.entities.base;

import javafx.scene.canvas.GraphicsContext;

/**
 * Base class for all obstacles.
 *
 * Responsibilities:
 * - Move toward the player
 * - Handle obstacle lifetime
 * - Store damage value
 * - Support custom obstacle behavior
 *
 * Subclasses:
 * - Spike
 * - Bat
 * - Fireball
 * - CandyWall
 */
public abstract class Obstacle
        extends GameObject {

    // Horizontal movement speed
    protected double speed;

    // Damage dealt to player
    protected double baseDamage = 20.0;
    /**
     * Create obstacle with position,
     * size, and movement speed.
     */
    public Obstacle(
            double x,
            double y,
            double width,
            double height,
            double speed
    ) {
        super(x, y, width, height);
        this.speed = speed;
    }

    /**
     * Main obstacle update loop.
     *
     * Handles:
     * - Movement
     * - Auto removal outside screen
     * - Special obstacle behavior
     */
    @Override
    public void update(double delta) {
        x -= speed * delta;
        if (x + width < -20) alive = false;
        updateBehavior(delta);
    }

    /**
     * Extra per-frame behavior hook.
     *
     * Subclasses can override this
     * for floating, animation, etc.
     */
    protected void updateBehavior(
            double delta
    ) {}

    @Override
    public abstract void render(GraphicsContext gc);
    public void  setSpeed(double s) { speed = s; }
    public double getSpeed()        { return speed; }
    public double getBaseDamage()   { return baseDamage; }
    public void setBaseDamage(double damage) { baseDamage = damage; }
}
