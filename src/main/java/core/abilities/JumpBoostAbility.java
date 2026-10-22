package core.abilities;

import core.entities.base.Cookie;

import game.GameController;

/**
 *
 * Effect:
 * - Increases jump speed
 * - Passive ability
 */
public class JumpBoostAbility implements CookieAbility {

    // Jump boost multiplier
    private static final double
            JUMP_MULTIPLIER = 1.2;

    /**
     * Passive ability update.
     */
    @Override
    public void update(
            GameController gc,
            Cookie cookie,
            double delta
    ) {
    }
    @Override
    public void activate() {}
    @Override
    public boolean isActive() {return true;}

    @Override
    public double getCooldownRemaining() {

        return 0;
    }

    @Override
    public double getCooldownPercent() {

        return 1.0;
    }

    @Override
    public boolean isPassive() {

        return true;
    }

    /**
     * Get jump speed multiplier.
     */
    public double getJumpVelocityMultiplier() {

        return JUMP_MULTIPLIER;
    }
}