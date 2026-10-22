package game.cookies.implementations;

import core.entities.base.Cookie;
import core.abilities.GhostAbility;
import javafx.scene.paint.Color;

/**
 * Pirate Cookie - A-tier cookie with the active {@link GhostAbility}.
 *
 * <p>Pressing the ability key turns the cookie intangible for 3 seconds,
 * letting it phase through obstacles. The ability has a 5-second cooldown
 * after the ghost phase ends. Starts with 105 HP.</p>
 *
 * @see GhostAbility
 */
public class PirateCookie extends Cookie {

    public PirateCookie() {
        super("PirateCookie", Color.web("#2C3E50"));
        setDisplayName("Pirate Cookie");
        setTier("A");
        setHex("#2C3E50");
        setIconPath("/CookieSprite/PirateCookie/PirateCookie1.png");
        setCookieAbilityDescription("Ghost: ignore obstacles for 3 seconds (5-second cooldown)");
        setMaxHpValue(105);
        setHp(105);

        // Grant ghost ability - pass through obstacles
        this.ability = new GhostAbility();
    }
}
