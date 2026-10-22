package core.pooling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the generic ObjectPool.
 */
public class ObjectPoolTest {

    /** Tiny dummy class used as the pooled type. */
    static class Bullet {
        int hits = 0;
    }

    private ObjectPool<Bullet> pool;

    @BeforeEach
    public void setUp() {
        pool = new ObjectPool<>(Bullet::new, 3, 5);
    }

    // =========================
    // INITIAL STATE
    // =========================

    @Test
    public void testInitialAvailableCount_MatchesInitialSize() {
        assertEquals(3, pool.getAvailableCount());
        assertEquals(0, pool.getInUseCount());
    }

    // =========================
    // ACQUIRE
    // =========================

    @Test
    public void testAcquire_DecreasesAvailable() {
        Bullet b = pool.acquire();
        assertNotNull(b);
        assertEquals(2, pool.getAvailableCount());
        assertEquals(1, pool.getInUseCount());
    }

    @Test
    public void testAcquire_GrowsWhenEmpty_UpToMax() {
        // Drain the initial three
        pool.acquire();
        pool.acquire();
        pool.acquire();
        // Force two creations on demand
        assertNotNull(pool.acquire());
        assertNotNull(pool.acquire());
        assertEquals(5, pool.getInUseCount());
    }

    @Test
    public void testAcquire_ReturnsNullWhenMaxExceeded() {
        // Acquire up to the max (5)
        for (int i = 0; i < 5; i++) {
            assertNotNull(pool.acquire());
        }
        assertNull(pool.acquire(), "Pool should return null once max capacity is reached");
    }


    // =========================
    // RELEASE
    // =========================

    @Test
    public void testRelease_ReturnsObjectToAvailable() {
        Bullet b = pool.acquire();
        pool.release(b);
        assertEquals(3, pool.getAvailableCount());
        assertEquals(0, pool.getInUseCount());
    }

    @Test
    public void testRelease_IgnoresUntrackedObject() {
        Bullet stray = new Bullet();
        int availableBefore = pool.getAvailableCount();
        pool.release(stray);
        assertEquals(availableBefore, pool.getAvailableCount(),
                "Releasing an object that wasn't acquired from the pool should be a no-op");
    }

    // =========================
    // CLEAR
    // =========================

    @Test
    public void testClear_EmptiesBothQueues() {
        pool.acquire();
        pool.clear();
        assertEquals(0, pool.getAvailableCount());
        assertEquals(0, pool.getInUseCount());
    }

    // =========================
    // CONCURRENCY-LIKE BEHAVIOUR
    // =========================

    @Test
    public void testRepeatedAcquireRelease_StaysConsistent() {
        for (int i = 0; i < 50; i++) {
            Bullet b = pool.acquire();
            pool.release(b);
        }
        assertEquals(0, pool.getInUseCount());
        assertTrue(pool.getAvailableCount() <= 5);
    }
}
