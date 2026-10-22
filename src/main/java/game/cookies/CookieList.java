package game.cookies;

import core.entities.base.Cookie;
import game.cookies.implementations.*;

import java.util.List;
import java.util.function.Supplier;

/**
 * Registry of every playable cookie in the game.
 *
 * <p>{@link #ALL} stores {@link Supplier}s rather than concrete instances so
 * each call to {@link #get(int)} returns a fresh cookie. This lets gameplay
 * keep mutable state on the active cookie (HP, position) without polluting
 * the registry.</p>
 *
 * <p>Order in {@link #ALL} defines the cookie index used throughout the game
 * (selection screen, saved data, asset folders).</p>
 */
public class CookieList {

    /**
     * All cookies registered for play, in display order.
     * Each entry is a no-arg constructor reference that builds a fresh cookie.
     */
    public static final List<Supplier<Cookie>> ALL =
            List.of(
                    BlueberryCookie::new,
                    BraveGingerbreadCookie::new,
                    PirateCookie::new,
                    ZombieCookie::new,
                    HeroCookie::new
            );

    /**
     * Build a fresh cookie at the given index.
     *
     * @param index 0-based position in {@link #ALL}
     * @return a new cookie instance
     * @throws IndexOutOfBoundsException if {@code index} is outside [0, {@link #size()})
     */
    public static Cookie get(int index) {

        if (index < 0 || index >= ALL.size()) {

            throw new IndexOutOfBoundsException(
                    "No cookie at index " + index
            );
        }

        return ALL.get(index).get();
    }

    /**
     * @return the number of cookies registered.
     */
    public static int size() {

        return ALL.size();
    }

    /** Utility class - not instantiable. */
    private CookieList() {
    }
}