package gamelogic.events;

/**
 * Base interface for all game events.
 * All game events must implement this interface to be published through EventBus.
 */
public interface GameEvent {
    /**
     * Get the type/name of this event.
     */
    String getEventType();

    /**
     * Get the timestamp when this event occurred.
     */
    long getTimestamp();
}
