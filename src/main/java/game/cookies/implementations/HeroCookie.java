package game.cookies.implementations;

import core.entities.base.Cookie;
import core.abilities.JumpBoostAbility;
import javafx.scene.paint.Color;

/**
 * Hero Cookie - S-tier cookie with a passive jump-boost ability.
 *
 * <p>Jumps higher than other cookies thanks to {@link JumpBoostAbility}.
 * Starts with 115 HP.</p>
 *
 * @see JumpBoostAbility
 */
public class HeroCookie extends Cookie {

    public HeroCookie() {
        super("HeroCookie", Color.web("#1A1A1A"));
        setDisplayName("Hero Cookie");
        setTier("S");
        setHex("#1A1A1A");
        setIconPath("/CookieSprite/HeroCookie/HeroCookie1.png");
        setCookieAbilityDescription("Jump higher");
        setMaxHpValue(115);
        setHp(115);

        // Grant jump boost ability - jumps higher than other cookies
        this.ability = new JumpBoostAbility();
    }
}
