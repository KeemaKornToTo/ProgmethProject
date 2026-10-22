package core.abilities;

import core.entities.base.Cookie;
import game.cookies.implementations.HeroCookie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for cookie abilities (Magnetic, JumpBoost, Ghost).
 */
public class AbilitiesTest {

    // =========================
    // JUMP BOOST (PASSIVE)
    // =========================

    @Test
    public void testJumpBoost_IsPassive() {
        JumpBoostAbility ability = new JumpBoostAbility();
        assertTrue(ability.isPassive());
    }

    @Test
    public void testJumpBoost_AlwaysActive() {
        JumpBoostAbility ability = new JumpBoostAbility();
        assertTrue(ability.isActive());
    }

    @Test
    public void testJumpBoost_NoCooldown() {
        JumpBoostAbility ability = new JumpBoostAbility();
        assertEquals(0, ability.getCooldownRemaining(), 1e-9);
        assertEquals(1.0, ability.getCooldownPercent(), 1e-9);
    }

    @Test
    public void testJumpBoost_MultiplierAboveOne() {
        JumpBoostAbility ability = new JumpBoostAbility();
        assertTrue(ability.getJumpVelocityMultiplier() > 1.0,
                "Jump boost should give a multiplier greater than 1.0");
    }

    @Test
    public void testJumpBoost_UpdateDoesNothing() {
        JumpBoostAbility ability = new JumpBoostAbility();
        Cookie cookie = new HeroCookie();
        // Passive update should be a safe no-op
        assertDoesNotThrow(() -> ability.update(null, cookie, 0.016));
    }

    // =========================
    // MAGNETIC (PASSIVE)
    // =========================

    @Test
    public void testMagnetic_IsPassive() {
        MagneticAbility ability = new MagneticAbility();
        assertTrue(ability.isPassive());
    }

    @Test
    public void testMagnetic_AlwaysActive() {
        MagneticAbility ability = new MagneticAbility();
        assertTrue(ability.isActive());
    }

    @Test
    public void testMagnetic_DefaultRadiusPositive() {
        MagneticAbility ability = new MagneticAbility();
        assertTrue(ability.getMagneticRadius() > 0);
    }

    @Test
    public void testMagnetic_CustomRadius() {
        MagneticAbility ability = new MagneticAbility(250.0, 500.0);
        assertEquals(250.0, ability.getMagneticRadius(), 1e-9);
    }

    @Test
    public void testMagnetic_NoCooldown() {
        MagneticAbility ability = new MagneticAbility();
        assertEquals(0, ability.getCooldownRemaining(), 1e-9);
        assertEquals(1.0, ability.getCooldownPercent(), 1e-9);
    }

    @Test
    public void testMagnetic_ActivateIsNoOp() {
        MagneticAbility ability = new MagneticAbility();
        assertDoesNotThrow(ability::activate);
    }

    // =========================
    // GHOST (ACTIVE)
    // =========================

    @Test
    public void testGhost_IsActiveSkill() {
        GhostAbility ability = new GhostAbility();
        assertFalse(ability.isPassive(),
                "Ghost is an active ability, not passive");
    }

    @Test
    public void testGhost_InitiallyInactive() {
        GhostAbility ability = new GhostAbility();
        assertFalse(ability.isActive());
        assertEquals(0, ability.getCooldownRemaining(), 1e-9);
    }

    @Test
    public void testGhost_ActivateTurnsOnAfterFirstFrame() {
        GhostAbility ability = new GhostAbility();
        Cookie cookie = new HeroCookie();
        ability.activate();
        ability.update(null, cookie, 0.016);
        assertTrue(ability.isActive(),
                "Ghost should be active immediately after activation");
        assertTrue(cookie.isGhost(),
                "Cookie should be marked ghost while ability is active");
    }

    @Test
    public void testGhost_ExpiresAfterDuration() {
        GhostAbility ability = new GhostAbility();
        Cookie cookie = new HeroCookie();
        ability.activate();
        // Drive the timer past the 3-second duration
        for (int i = 0; i < 100; i++) {
            ability.update(null, cookie, 0.05);
            if (!ability.isActive()) break;
        }
        assertFalse(ability.isActive(),
                "Ghost should turn off after its duration elapses");
        assertFalse(cookie.isGhost(),
                "Cookie ghost flag should clear when ability ends");
    }

    @Test
    public void testGhost_EntersCooldownAfterExpiry() {
        GhostAbility ability = new GhostAbility();
        Cookie cookie = new HeroCookie();
        ability.activate();
        for (int i = 0; i < 100; i++) {
            ability.update(null, cookie, 0.05);
            if (!ability.isActive()) break;
        }
        assertTrue(ability.getCooldownRemaining() > 0,
                "Cooldown should start once the ability ends");
    }

    @Test
    public void testGhost_CannotActivateDuringCooldown() {
        GhostAbility ability = new GhostAbility();
        Cookie cookie = new HeroCookie();
        ability.activate();
        // Run past duration to enter cooldown
        for (int i = 0; i < 100; i++) {
            ability.update(null, cookie, 0.05);
            if (!ability.isActive()) break;
        }
        double cdBefore = ability.getCooldownRemaining();
        ability.activate(); // should be ignored
        assertFalse(ability.isActive(),
                "Ghost should not reactivate while still on cooldown");
        assertEquals(cdBefore, ability.getCooldownRemaining(), 1e-9);
    }

    @Test
    public void testGhost_CooldownPercent_Progresses() {
        GhostAbility ability = new GhostAbility();
        Cookie cookie = new HeroCookie();
        ability.activate();
        // Get into cooldown
        for (int i = 0; i < 100; i++) {
            ability.update(null, cookie, 0.05);
            if (!ability.isActive()) break;
        }
        double first = ability.getCooldownPercent();
        ability.update(null, cookie, 1.0);
        double second = ability.getCooldownPercent();
        assertTrue(second > first,
                "Cooldown percent should rise toward 1.0 as time passes");
    }
}
