package core.entities.base;

import game.items.HealthItem;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GameObject base class.
 * HealthItem is used as a concrete implementation since GameObject is abstract.
 */
public class GameObjectTest {

    // =========================
    // POSITION / SIZE
    // =========================

    @Test
    public void testConstructor_StoresPosition() {
        HealthItem item = new HealthItem(100, 200);
        assertEquals(100, item.getX());
        assertEquals(200, item.getY());
    }

    @Test
    public void testConstructor_StoresSize() {
        HealthItem item = new HealthItem(0, 0);
        assertTrue(item.getWidth() > 0);
        assertTrue(item.getHeight() > 0);
    }

    // =========================
    // COLLISION
    // =========================

    @Test
    public void testCollides_Overlapping() {
        HealthItem a = new HealthItem(100, 100);
        HealthItem b = new HealthItem(110, 110);
        assertTrue(a.collides(b));
    }

    @Test
    public void testCollides_NonOverlapping() {
        HealthItem a = new HealthItem(0, 0);
        HealthItem b = new HealthItem(500, 500);
        assertFalse(a.collides(b));
    }

    @Test
    public void testCollides_TouchingEdges_DoesNotCollide() {
        // AABB uses strict <, so exactly-touching edges do not count
        HealthItem a = new HealthItem(0, 0);
        HealthItem b = new HealthItem(a.getWidth(), 0);
        assertFalse(a.collides(b));
    }

    @Test
    public void testIntersects_SameAsCollides() {
        HealthItem a = new HealthItem(100, 100);
        HealthItem b = new HealthItem(105, 105);
        assertEquals(a.collides(b), a.intersects(b));
    }

    @Test
    public void testGetBounds_MatchesPositionAndSize() {
        HealthItem item = new HealthItem(50, 75);
        Rectangle2D bounds = item.getBounds();
        assertEquals(50, bounds.getMinX(), 1e-9);
        assertEquals(75, bounds.getMinY(), 1e-9);
        assertEquals(item.getWidth(), bounds.getWidth(), 1e-9);
        assertEquals(item.getHeight(), bounds.getHeight(), 1e-9);
    }

    // =========================
    // LIFECYCLE
    // =========================

    @Test
    public void testIsAlive_DefaultTrue() {
        HealthItem item = new HealthItem(0, 0);
        assertTrue(item.isAlive());
    }

    @Test
    public void testDestroy_MarksDead() {
        HealthItem item = new HealthItem(0, 0);
        item.destroy();
        assertFalse(item.isAlive());
    }

    @Test
    public void testSetAlive_ChangesState() {
        HealthItem item = new HealthItem(0, 0);
        item.setAlive(false);
        assertFalse(item.isAlive());
        item.setAlive(true);
        assertTrue(item.isAlive());
    }

    // =========================
    // OFF-SCREEN DETECTION
    // =========================

    @Test
    public void testIsOutOfScreen_FarRight_False() {
        HealthItem item = new HealthItem(1000, 100);
        assertFalse(item.isOutOfScreen());
    }

    @Test
    public void testIsOutOfScreen_OnLeftEdge_False() {
        HealthItem item = new HealthItem(0, 100);
        assertFalse(item.isOutOfScreen());
    }

    @Test
    public void testIsOutOfScreen_FarLeft_True() {
        HealthItem item = new HealthItem(-500, 100);
        assertTrue(item.isOutOfScreen());
    }

    // =========================
    // HEALTH ITEM SPECIFIC
    // =========================

    @Test
    public void testHealthItem_MovesLeftEachFrame() {
        HealthItem item = new HealthItem(500, 100);
        double startX = item.getX();
        item.update(0.1);
        assertTrue(item.getX() < startX,
                "HealthItem should scroll left with the world");
    }

    @Test
    public void testHealthItem_DiesOffScreen() {
        HealthItem item = new HealthItem(500, 100);
        for (int i = 0; i < 100; i++) {
            item.update(0.1);
        }
        assertFalse(item.isAlive(),
                "HealthItem should become inactive after leaving the screen");
    }
}
