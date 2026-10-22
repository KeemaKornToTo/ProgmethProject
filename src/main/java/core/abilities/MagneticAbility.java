package core.abilities;

import core.entities.base.Cookie;
import game.collectibles.Collectible;

import game.GameController;

/**
 * Magnetic ability for Blueberry Cookie.
 *
 * Effect:
 * - Automatically pulls collectibles
 * - Always active
 */
public class MagneticAbility implements CookieAbility {

    // Magnetic range
    private final double magneticRadius;

    // Pull strength
    private final double attractionStrength;

    /**
     * Default constructor.
     */
    public MagneticAbility() {

        this(170.0, 850.0);
    }

    /**
     * Custom constructor.
     */
    public MagneticAbility(
            double magneticRadius,
            double attractionStrength
    ) {

        this.magneticRadius =
                magneticRadius;

        this.attractionStrength =
                attractionStrength;
    }

    /**
     * Update magnetic effect every frame.
     */
    @Override
    public void update(
            GameController gc,
            Cookie cookie,
            double delta
    ) {

        // Passive ability:
        // continuously attract collectibles
        gc.applyMagneticForceToCollectibles(this);
    }

    /**
     * Passive skill does not activate manually.
     */
    @Override
    public void activate() {

        // No activation needed
    }

    /**
     * Passive ability is always active.
     */
    @Override
    public boolean isActive() {

        return true;
    }

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
     * Pull collectible toward the cookie.
     */
    public void applyForceToCollectible(
            Collectible collectible,
            Cookie cookie
    ) {

        double targetX =
                cookie.getX()
                        + cookie.getWidth() / 2;

        double targetY =
                cookie.getY()
                        + cookie.getHeight() / 2;

        collectible.applyMagneticForce(
                targetX,
                targetY,
                magneticRadius,
                attractionStrength
        );
    }

    public double getMagneticRadius() {
        return magneticRadius;
    }
}