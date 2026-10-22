package game.cookies.implementations;

import core.entities.base.Cookie;
import javafx.scene.paint.Color;

/**
 * Zombie Cookie - B-tier cookie that trades special powers for raw bulk.
 *
 * <p>No active or passive ability instance is assigned; instead it has the
 * highest HP pool of any cookie in the roster (150 HP), letting it shrug off
 * extra hits before going down.</p>
 */
public class ZombieCookie extends Cookie {

    public ZombieCookie() {
        super("ZombieCookie", Color.web("#556B2F"));
        setDisplayName("Zombie Cookie");
        setTier("B");
        setHex("#556B2F");
        setIconPath("/CookieSprite/ZombieCookie/ZombieCookie1.png");
        setCookieAbilityDescription("More Max Hp");
        setMaxHpValue(150);
        setHp(150);

    }
}
