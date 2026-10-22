package game.cookies.implementations;

import core.entities.base.Cookie;
import javafx.scene.paint.Color;

/**
 * Brave Gingerbread Cookie - C-tier cookie with no special ability.
 *
 * <p>The baseline cookie - solid HP (110), no perks. Useful as a difficulty
 * benchmark and as a starting choice for new players.</p>
 */
public class BraveGingerbreadCookie extends Cookie {

    public BraveGingerbreadCookie() {
        super("BraveGingerBread", Color.web("#D2691E"));
        setDisplayName("Brave Cookie");
        setTier("C");
        setHex("#D2691E");
        setIconPath("/CookieSprite/BraveGingerBread/BraveGingerBread1.png");
        setCookieAbilityDescription("Nothing, just bravery");
        setMaxHpValue(110);
        setHp(110);
    }
}
