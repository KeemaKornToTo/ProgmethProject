package gamelogic.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the concrete game event types.
 * Verifies event type strings, payloads, and timestamps.
 */
public class GameEventsTest {

    // =========================
    // COIN COLLECTED
    // =========================

    @Test
    public void testCoinCollectedEvent_EventType() {
        CoinCollectedEvent e = new CoinCollectedEvent(10, 100, 200);
        assertEquals("COIN_COLLECTED", e.getEventType());
    }

    @Test
    public void testCoinCollectedEvent_PayloadPreserved() {
        CoinCollectedEvent e = new CoinCollectedEvent(25, 150.5, 75.2);
        assertEquals(25, e.getCoinValue());
        assertEquals(150.5, e.getPosX(), 1e-9);
        assertEquals(75.2, e.getPosY(), 1e-9);
    }

    @Test
    public void testCoinCollectedEvent_TimestampSet() {
        long before = System.currentTimeMillis();
        CoinCollectedEvent e = new CoinCollectedEvent(10, 0, 0);
        long after = System.currentTimeMillis();
        assertTrue(e.getTimestamp() >= before && e.getTimestamp() <= after);
    }

    // =========================
    // GAME OVER
    // =========================

    @Test
    public void testGameOverEvent_EventType() {
        GameOverEvent e = new GameOverEvent(1000, 50, 2);
        assertEquals("GAME_OVER", e.getEventType());
    }

    @Test
    public void testGameOverEvent_PayloadPreserved() {
        GameOverEvent e = new GameOverEvent(1234, 56, 3);
        assertEquals(1234, e.getFinalScore());
        assertEquals(56, e.getFinalCoins());
        assertEquals(3, e.getStageNumber());
    }

    // =========================
    // HEALTH CHANGED
    // =========================

    @Test
    public void testHealthChangedEvent_EventType() {
        HealthChangedEvent e = new HealthChangedEvent(100, 80, "obstacle");
        assertEquals("HEALTH_CHANGED", e.getEventType());
    }

    @Test
    public void testHealthChangedEvent_PayloadPreserved() {
        HealthChangedEvent e = new HealthChangedEvent(80, 60, "Spike hit");
        assertEquals(80, e.getOldHealth());
        assertEquals(60, e.getNewHealth());
        assertEquals("Spike hit", e.getReason());
    }

    @Test
    public void testHealthChangedEvent_HealingReportsIncrease() {
        HealthChangedEvent e = new HealthChangedEvent(50, 75, "Health item pickup");
        assertTrue(e.getNewHealth() > e.getOldHealth());
    }

    // =========================
    // OBSTACLE HIT
    // =========================

    @Test
    public void testObstacleHitEvent_EventType() {
        ObstacleHitEvent e = new ObstacleHitEvent("Spike", 10, 100, 200);
        assertEquals("OBSTACLE_HIT", e.getEventType());
    }

    @Test
    public void testObstacleHitEvent_PayloadPreserved() {
        ObstacleHitEvent e = new ObstacleHitEvent("Bat", 15, 250, 130);
        assertEquals("Bat", e.getObstacleType());
        assertEquals(15, e.getDamageAmount());
        assertEquals(250, e.getPosX(), 1e-9);
        assertEquals(130, e.getPosY(), 1e-9);
    }
}
