package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Physics class.
 * Verifies gravity, jumping, ground collision, and reset behaviour.
 */
public class PhysicsTest {

    private Physics physics;

    @BeforeEach
    public void setUp() {
        // Start the body resting on the ground
        physics = new Physics(80, Physics.GROUND_Y - Physics.NORMAL_H, 70, Physics.NORMAL_H);
    }

    // =========================
    // INITIAL STATE
    // =========================

    @Test
    public void testInitialPosition() {
        assertEquals(80, physics.getX());
        assertEquals(Physics.GROUND_Y - Physics.NORMAL_H, physics.getY(), 1e-9);
    }

    @Test
    public void testInitialVelocity_Zero() {
        assertEquals(0, physics.getVelocityY(), 1e-9);
    }

    @Test
    public void testInitialState_OnGround() {
        assertTrue(physics.isOnGround());
        assertFalse(physics.isInAir());
    }

    @Test
    public void testDimensions() {
        assertEquals(70, physics.getWidth());
        assertEquals(Physics.NORMAL_H, physics.getHeight(), 1e-9);
    }

    // =========================
    // JUMP TESTS
    // =========================

    @Test
    public void testJump_FromGround_SetsNegativeVelocity() {
        physics.jump();
        assertEquals(Physics.JUMP_VELOCITY, physics.getVelocityY(), 1e-9,
                "Jumping should give an upward (negative) initial velocity");
        assertTrue(physics.getVelocityY() < 0);
    }

    @Test
    public void testJump_WhileInAir_Ignored() {
        physics.jump();           // first jump
        physics.update(0.05);     // start ascending
        double velAfterFirst = physics.getVelocityY();
        physics.jump();           // should be a no-op
        assertEquals(velAfterFirst, physics.getVelocityY(), 1e-9,
                "Jump should not re-trigger mid-air (no double-jump)");
    }

    // =========================
    // GRAVITY TESTS
    // =========================

    @Test
    public void testGravity_AppliedWhileInAir() {
        physics.jump();
        physics.update(0.05);
        // After gravity has been applied for 50ms, velocity should be more positive (less upward)
        double afterFirstFrame = physics.getVelocityY();
        physics.update(0.05);
        assertTrue(physics.getVelocityY() > afterFirstFrame,
                "Gravity should pull velocity toward positive (downward) each frame");
    }

    @Test
    public void testJump_PeaksThenFalls() {
        physics.jump();
        // Simulate a few frames; eventually the cookie should land back on ground
        for (int i = 0; i < 200; i++) {
            physics.update(0.016);
            if (physics.isOnGround() && i > 5) break;
        }
        assertTrue(physics.isOnGround(),
                "Cookie should land back on the ground after jumping");
    }

    // =========================
    // GROUND COLLISION
    // =========================

    @Test
    public void testLanding_ResetsVelocityToZero() {
        physics.jump();
        for (int i = 0; i < 200; i++) {
            physics.update(0.016);
            if (physics.isOnGround() && i > 5) break;
        }
        assertEquals(0, physics.getVelocityY(), 1e-9,
                "Velocity should reset to zero on landing");
    }

    @Test
    public void testLanding_ClampsToGroundY() {
        physics.jump();
        for (int i = 0; i < 200; i++) {
            physics.update(0.016);
        }
        assertEquals(Physics.GROUND_Y - Physics.NORMAL_H, physics.getY(), 1e-6);
    }

    // =========================
    // SETTERS
    // =========================

    @Test
    public void testSetVelocityY() {
        physics.setVelocityY(-500);
        assertEquals(-500, physics.getVelocityY(), 1e-9);
    }

    @Test
    public void testSetY_PutsCookieInAir() {
        physics.setY(50);
        assertTrue(physics.isInAir(),
                "Manually placing Y above ground should report in-air");
    }

    @Test
    public void testSetHeight() {
        physics.setHeight(Physics.SLIDE_H);
        assertEquals(Physics.SLIDE_H, physics.getHeight(), 1e-9);
    }

    // =========================
    // RESET
    // =========================

    @Test
    public void testResetToStart_RestoresInitialPosition() {
        physics.jump();
        physics.update(0.05);
        physics.setX(999);
        physics.resetToStart();
        assertEquals(80, physics.getX(), 1e-9);
        assertEquals(Physics.GROUND_Y - Physics.NORMAL_H, physics.getY(), 1e-9);
        assertEquals(0, physics.getVelocityY(), 1e-9);
    }

    // =========================
    // CONSTANTS
    // =========================

    @Test
    public void testJumpVelocity_IsNegative() {
        assertTrue(Physics.JUMP_VELOCITY < 0,
                "Jump velocity should be negative since Y grows downward");
    }

    @Test
    public void testGravity_IsPositive() {
        assertTrue(Physics.GRAVITY > 0);
    }

    @Test
    public void testNormalHeightGreaterThanSlideHeight() {
        assertTrue(Physics.NORMAL_H > Physics.SLIDE_H,
                "Normal hitbox should be taller than slide hitbox");
    }
}
