package gamelogic.events;

/**
 * EventListener interface.
 * Implement this to listen for game events.
 */
public interface EventListener {
    /**
     * Called when an event is published that this listener is subscribed to.
     * @param event The event that occurred
     */
    void onEvent(GameEvent event);
}
