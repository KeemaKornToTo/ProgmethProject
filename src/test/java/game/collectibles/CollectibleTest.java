package game.collectibles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Collectible subclasses (Coin, JellyBig, JellySmall).
 * Covers movement, magnetism, scoring, and lifecycle.
 */
public class CollectibleTest {

    // =========================
    // COIN
    // =========================

    @Test
    public void testCoin_ScoreValue() {
        Coin coin = new Coin(100, 100, 300);
        assertEquals(10, coin.getScoreValue());
    }

    @Test
    public void testCoin_MovesLeftEachFrame() {
        Coin coin = new Coin(100, 100, 300);
        double startX = coin.getX();
        coin.update(0.1);
        assertTrue(coin.getX() < startX,
                "Collectible should drift left as the world scrolls");
    }

    @Test
    public void testCoin_DeactivatesWhenOffScreen() {
        Coin coin = new Coin(100, 100, 300);
        // Push far enough left so x + width < -10
        for (int i = 0; i < 100; i++) {
            coin.update(0.1);
        }
        assertFalse(coin.isAlive(),
                "Coin should be dead once it moves off the left edge");
    }

    @Test
    public void testCoin_MagneticForce_PullsToward() {
        Coin coin = new Coin(500, 100, 300);
        coin.applyMagneticForce(100, 100, 600, 800);
        // applyMagneticForce sets internal velocities; one update should move x toward target
        double xBefore = coin.getX();
        coin.update(0.05);
        assertTrue(coin.getX() < xBefore,
                "With magnetic force toward x=100 from x=500, coin should move left (toward target)");
    }

    @Test
    public void testCoin_MagneticForce_OutsideRadius_NoPull() {
        Coin coin = new Coin(500, 100, 0); // speed=0 so movement comes only from magnetism
        coin.applyMagneticForce(0, 100, 50, 800); // target far away, small radius
        coin.update(0.05);
        // Coin should not move (or move very little since outside radius)
        assertEquals(500, coin.getX(), 1e-6,
                "Outside the magnetic radius the coin should not gain velocity");
    }

    @Test
    public void testCoin_ResetMagneticVelocity() {
        Coin coin = new Coin(500, 100, 0);
        coin.applyMagneticForce(100, 100, 800, 600);
        coin.resetMagneticVelocity();
        double xBefore = coin.getX();
        coin.update(0.1);
        assertEquals(xBefore, coin.getX(), 1e-9,
                "After resetMagneticVelocity, no magnetic motion should happen");
    }

    // =========================
    // JELLY BIG
    // =========================

    @Test
    public void testJellyBig_HasScoreValue() {
        JellyBig jelly = new JellyBig(100, 100, 300);
        assertTrue(jelly.getScoreValue() > 0);
    }

    @Test
    public void testJellyBig_ScoresMoreThanCoin() {
        JellyBig jelly = new JellyBig(0, 0, 300);
        Coin coin = new Coin(0, 0, 300);
        assertTrue(jelly.getScoreValue() > coin.getScoreValue(),
                "Big jelly is meant to be the high-value pickup");
    }

    @Test
    public void testJellyBig_IsNotMagnetic() {
        JellyBig jelly = new JellyBig(500, 100, 0);
        jelly.applyMagneticForce(0, 100, 800, 600);
        double xBefore = jelly.getX();
        jelly.update(0.05);
        assertEquals(xBefore, jelly.getX(), 1e-9,
                "JellyBig is not magnetic; magnetic force should be ignored");
    }

    @Test
    public void testJellyBig_MovesLeft() {
        JellyBig jelly = new JellyBig(100, 100, 300);
        double startX = jelly.getX();
        jelly.update(0.1);
        assertTrue(jelly.getX() < startX);
    }

    // =========================
    // JELLY SMALL
    // =========================

    @Test
    public void testJellySmall_HasScoreValue() {
        JellySmall jelly = new JellySmall(100, 100, 300);
        assertTrue(jelly.getScoreValue() > 0);
    }

    @Test
    public void testJellySmall_LowerScoreThanBig() {
        JellySmall small = new JellySmall(0, 0, 300);
        JellyBig big = new JellyBig(0, 0, 300);
        assertTrue(small.getScoreValue() <= big.getScoreValue());
    }

    @Test
    public void testJellySmall_MovesLeft() {
        JellySmall jelly = new JellySmall(100, 100, 300);
        double startX = jelly.getX();
        jelly.update(0.1);
        assertTrue(jelly.getX() < startX);
    }

    // =========================
    // BASE GAMEOBJECT BEHAVIOUR
    // =========================

    @Test
    public void testCollectible_CollidesWithSelfAtSamePosition() {
        Coin a = new Coin(100, 100, 0);
        Coin b = new Coin(100, 100, 0);
        assertTrue(a.collides(b),
                "Two overlapping collectibles should collide");
    }

    @Test
    public void testCollectible_DoesNotCollideWhenFarApart() {
        Coin a = new Coin(0, 0, 0);
        Coin b = new Coin(500, 500, 0);
        assertFalse(a.collides(b));
    }

    @Test
    public void testCollectible_DestroyMarksDead() {
        Coin c = new Coin(100, 100, 0);
        assertTrue(c.isAlive());
        c.destroy();
        assertFalse(c.isAlive());
    }

    @Test
    public void testCollectible_IsOutOfScreen_PositiveX_False() {
        Coin c = new Coin(100, 100, 0);
        assertFalse(c.isOutOfScreen());
    }

    @Test
    public void testCollectible_IsOutOfScreen_FarLeft_True() {
        Coin c = new Coin(-1000, 100, 0);
        assertTrue(c.isOutOfScreen());
    }
}
