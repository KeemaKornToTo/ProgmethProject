package game.obstacles;

import core.Physics;
import core.entities.base.Obstacle;

/**
 * Abstract base class for ground obstacles — player must JUMP to avoid.
 * Subclasses: CandyWall, Spike, Block
 */
public abstract class GroundObstacle extends Obstacle {

    public GroundObstacle(double x, double width, double height, double speed) {
        super(x, Physics.GROUND_Y - height, width, height, speed);
    }
}
