package game.collectibles;

import core.entities.base.GameObject;
import javafx.scene.canvas.GraphicsContext;

public abstract class Collectible extends GameObject {

    protected final double speed;
    protected final int scoreValue;

    protected double magneticVelocityX = 0;
    protected double magneticVelocityY = 0;

    public Collectible(
            double x,
            double y,
            double size,
            double speed,
            int scoreValue
    ) {

        super(x, y, size, size);

        this.speed = speed;
        this.scoreValue = scoreValue;
    }

    @Override
    public void update(double delta) {

        x -= speed * delta;

        x += magneticVelocityX * delta;
        y += magneticVelocityY * delta;

        if (x + width < -10) {
            alive = false;
        }
    }

    @Override
    public abstract void render(GraphicsContext gc);

    /**
     * Applies magnetic force to the collectible, pulling it toward a target.
     * @param targetX The target x-coordinate.
     * @param targetY The target y-coordinate.
     * @param radius The radius within which the force is active.
     * @param strength The strength of the pull.
     */
    public void applyMagneticForce(
            double targetX,
            double targetY,
            double radius,
            double strength
    ) {

        if (!isMagnetic()) {
            return;
        }

        double dx = targetX - x;
        double dy = targetY - y;

        double distance =
                Math.sqrt(dx * dx + dy * dy);

        if (distance >= radius || distance <= 0) {

            resetMagneticVelocity();
            return;
        }

        double directionX = dx / distance;
        double directionY = dy / distance;

        double force =
                (1 - distance / radius)
                        * strength;

        magneticVelocityX = directionX * force;
        magneticVelocityY = directionY * force;
    }

    /**
     * Resets any magnetic pull velocity.
     */
    public void resetMagneticVelocity() {

        magneticVelocityX = 0;
        magneticVelocityY = 0;
    }

    /**
     * @return true if the collectible is affected by magnetic force.
     * Subclasses should override this to enable magnetism.
     */
    protected boolean isMagnetic() {

        return false;
    }

    /**
     * @return the score points awarded when collected.
     */
    public int getScoreValue() {

        return scoreValue;
    }
}