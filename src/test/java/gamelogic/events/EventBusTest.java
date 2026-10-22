package gamelogic.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the EventBus pub/sub system.
 */
public class EventBusTest {

    private EventBus bus;

    @BeforeEach
    public void setUp() {
        bus = EventBus.getInstance();
        bus.clearAllListeners();
    }

    // =========================
    // SINGLETON
    // =========================

    @Test
    public void testGetInstance_Singleton() {
        assertSame(EventBus.getInstance(), EventBus.getInstance());
    }

    // =========================
    // SUBSCRIBE / PUBLISH
    // =========================

    @Test
    public void testPublish_NotifiesSubscriber() {
        AtomicInteger received = new AtomicInteger();
        bus.subscribe("COIN_COLLECTED",
                event -> received.incrementAndGet());

        bus.publish(new CoinCollectedEvent(10, 0, 0));
        assertEquals(1, received.get());
    }

    @Test
    public void testPublish_MultipleSubscribersAllNotified() {
        AtomicInteger a = new AtomicInteger();
        AtomicInteger b = new AtomicInteger();
        bus.subscribe("COIN_COLLECTED", e -> a.incrementAndGet());
        bus.subscribe("COIN_COLLECTED", e -> b.incrementAndGet());

        bus.publish(new CoinCollectedEvent(10, 0, 0));
        assertEquals(1, a.get());
        assertEquals(1, b.get());
    }

    @Test
    public void testPublish_DifferentEventType_Ignored() {
        AtomicInteger received = new AtomicInteger();
        bus.subscribe("COIN_COLLECTED", e -> received.incrementAndGet());

        bus.publish(new GameOverEvent(100, 5, 1));
        assertEquals(0, received.get(),
                "Subscribers of one type should not see events of another type");
    }

    @Test
    public void testPublish_NoSubscribers_NoError() {
        assertDoesNotThrow(() ->
                bus.publish(new CoinCollectedEvent(10, 0, 0)));
    }

    // =========================
    // UNSUBSCRIBE
    // =========================

    @Test
    public void testUnsubscribe_StopsNotifications() {
        AtomicInteger received = new AtomicInteger();
        EventListener listener = e -> received.incrementAndGet();
        bus.subscribe("COIN_COLLECTED", listener);
        bus.unsubscribe("COIN_COLLECTED", listener);

        bus.publish(new CoinCollectedEvent(10, 0, 0));
        assertEquals(0, received.get());
    }

    @Test
    public void testUnsubscribe_UnknownType_NoError() {
        EventListener listener = e -> { };
        assertDoesNotThrow(() ->
                bus.unsubscribe("NO_SUCH_EVENT", listener));
    }

    // =========================
    // LISTENER COUNT
    // =========================

    @Test
    public void testGetListenerCount() {
        bus.subscribe("HEALTH_CHANGED", e -> { });
        bus.subscribe("HEALTH_CHANGED", e -> { });
        assertEquals(2, bus.getListenerCount("HEALTH_CHANGED"));
    }

    @Test
    public void testGetListenerCount_NoneRegistered() {
        assertEquals(0, bus.getListenerCount("NOT_A_REAL_EVENT"));
    }

    // =========================
    // CLEAR
    // =========================

    @Test
    public void testClearAllListeners() {
        bus.subscribe("COIN_COLLECTED", e -> { });
        bus.subscribe("HEALTH_CHANGED", e -> { });
        bus.clearAllListeners();
        assertEquals(0, bus.getListenerCount("COIN_COLLECTED"));
        assertEquals(0, bus.getListenerCount("HEALTH_CHANGED"));
    }

    // =========================
    // EVENT PAYLOAD INTEGRITY
    // =========================

    @Test
    public void testEventPayload_DeliveredIntact() {
        final int[] coinValue = {-1};
        bus.subscribe("COIN_COLLECTED", event -> {
            coinValue[0] = ((CoinCollectedEvent) event).getCoinValue();
        });
        bus.publish(new CoinCollectedEvent(42, 100, 200));
        assertEquals(42, coinValue[0]);
    }
}
