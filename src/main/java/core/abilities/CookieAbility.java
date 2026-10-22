package core.abilities;

import core.entities.base.Cookie;
import game.GameController;

/**
 * Base interface for all cookie abilities.
 *
 * Ability system uses the Strategy Pattern:
 * each cookie can have different ability behavior
 * without changing the Cookie class itself.
 */
public interface CookieAbility {

    /**
     * Update ability every frame.
     *
     * @param gc     game controller
     * @param cookie owner cookie
     * @param delta  frame delta time
     */
    void update(
            GameController gc,
            Cookie cookie,
            double delta
    );

    /**
     * Activate ability manually.
     * Used by active skills only.
     */
    void activate();

    /**
     * Check if ability is active.
     */
    boolean isActive();

    /**
     * Get remaining cooldown time.
     */
    double getCooldownRemaining();

    /**
     * Get cooldown progress.
     * 1.0 = ready
     * 0.0 = just used
     */
    double getCooldownPercent();

    /**
     * Check if ability is passive.
     * Passive abilities:
     * - always active
     * - no key press needed
     */
    boolean isPassive();
}