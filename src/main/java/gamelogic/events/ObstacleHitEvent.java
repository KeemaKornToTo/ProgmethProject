package gamelogic.events;

/**
 * Event fired when an obstacle hits the player.
 */
public class ObstacleHitEvent implements GameEvent {
    private final String obstacleType;
    private final int damageAmount;
    private final double posX;
    private final double posY;
    private final long timestamp;

    public ObstacleHitEvent(String obstacleType, int damageAmount, double posX, double posY) {
        this.obstacleType = obstacleType;
        this.damageAmount = damageAmount;
        this.posX = posX;
        this.posY = posY;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String getEventType() {
        return "OBSTACLE_HIT";
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public String getObstacleType() {
        return obstacleType;
    }

    public int getDamageAmount() {
        return damageAmount;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}
