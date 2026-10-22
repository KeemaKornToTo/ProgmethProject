package gamelogic.events;

/**
 * Event fired when player health changes.
 */
public class HealthChangedEvent implements GameEvent {
    private final int oldHealth;
    private final int newHealth;
    private final String reason;
    private final long timestamp;

    public HealthChangedEvent(int oldHealth, int newHealth, String reason) {
        this.oldHealth = oldHealth;
        this.newHealth = newHealth;
        this.reason = reason;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String getEventType() {
        return "HEALTH_CHANGED";
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public int getOldHealth() {
        return oldHealth;
    }

    public int getNewHealth() {
        return newHealth;
    }

    public String getReason() {
        return reason;
    }
}
