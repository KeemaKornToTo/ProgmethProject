package game.obstacles;

import core.entities.base.Obstacle;

/**
 * Abstract base class for air obstacles — player must SLIDE to avoid.
 * Includes bobbing animation behavior.
 * Subclasses: Fireball, Bat, CloudSpike
 */
public abstract class AirObstacle extends Obstacle {

    protected double baseY;

    public AirObstacle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height, speed);
        this.baseY = this.y;
    }

    /** Bobbing animation for air obstacles */
    @Override
    protected void updateBehavior(double delta) {
        y = baseY + Math.sin(System.currentTimeMillis() / 333.3) * 8;
    }
}
