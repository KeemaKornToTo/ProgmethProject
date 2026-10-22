package gamelogic.events;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * EventBus - Singleton event publisher/subscriber system.
 * Managers post events, listeners subscribe and react.
 * Thread-safe implementation using CopyOnWriteArrayList.
 */
public class EventBus {
    private static EventBus instance;
    private final Map<String, List<EventListener>> listeners = new HashMap<>();

    private EventBus() {}

    /**
     * Get singleton instance of EventBus.
     */
    public static synchronized EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    /**
     * Subscribe a listener to a specific event type.
     * @param eventType The type of event to listen for
     * @param listener The listener to notify when event occurs
     */
    public void subscribe(String eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                 .add(listener);
    }

    /**
     * Unsubscribe a listener from an event type.
     */
    public void unsubscribe(String eventType, EventListener listener) {
        List<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    /**
     * Publish an event to all subscribers.
     * @param event The event to publish
     */
    public void publish(GameEvent event) {
        List<EventListener> eventListeners = listeners.get(event.getEventType());
        if (eventListeners != null) {
            for (EventListener listener : eventListeners) {
                listener.onEvent(event);
            }
        }
    }

    /**
     * Clear all listeners (useful for testing or cleanup).
     */
    public void clearAllListeners() {
        listeners.clear();
    }

    /**
     * Get number of listeners for a specific event type.
     */
    public int getListenerCount(String eventType) {
        List<EventListener> eventListeners = listeners.get(eventType);
        return eventListeners != null ? eventListeners.size() : 0;
    }
}
