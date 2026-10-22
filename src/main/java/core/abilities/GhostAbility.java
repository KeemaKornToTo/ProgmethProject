package core.abilities;

import core.entities.base.Cookie;
import game.GameController;

/**
 * Ghost ability for Pirate Cookie.
 *
 * Effect:
 * - Become invisible
 * - Pass through obstacles
 *
 * Duration: 3 seconds
 * Cooldown: 5 seconds
 */
public class GhostAbility implements CookieAbility {

    // Active skill timer
    private double activeTimer = 0;

    // Cooldown timer
    private double cooldownTimer = 0;

    private static final double DURATION  = 3.0;
    private static final double COOLDOWN = 5.0;

    /**
     * Update ability every frame.
     */
    @Override
    public void update(
            GameController gc,
            Cookie cookie,
            double delta
    ) {

        updateActiveState(cookie, delta);

        updateCooldown(delta);
    }

    /**
     * Activate skill if available.
     */
    @Override
    public void activate() {

        // Prevent activation during cooldown
        if (cooldownTimer > 0 || activeTimer > 0) {
            return;
        }

        activeTimer = DURATION;
    }

    /**
     * Update ghost mode state.
     */
    private void updateActiveState(
            Cookie cookie,
            double delta
    ) {

        if (activeTimer <= 0) {
            return;
        }

        activeTimer -= delta;

        cookie.setGhost(true);

        // End skill
        if (activeTimer <= 0) {

            activeTimer = 0;

            cooldownTimer = COOLDOWN;

            cookie.setGhost(false);
        }
    }

    /**
     * Update cooldown timer.
     */
    private void updateCooldown(double delta) {

        if (cooldownTimer <= 0) {
            return;
        }

        cooldownTimer -= delta;

        if (cooldownTimer < 0) {
            cooldownTimer = 0;
        }
    }

    @Override
    public boolean isActive() {

        return activeTimer > 0;
    }

    @Override
    public double getCooldownRemaining() {

        return cooldownTimer;
    }

    @Override
    public double getCooldownPercent() {

        return 1.0 -
                (cooldownTimer / COOLDOWN);
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}