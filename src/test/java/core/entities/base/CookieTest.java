package core.entities.base;

import core.abilities.CookieAbility;
import core.abilities.JumpBoostAbility;
import core.abilities.MagneticAbility;
import game.cookies.implementations.BlueberryCookie;
import game.cookies.implementations.HeroCookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the lightweight Cookie data model.
 * Cookie is metadata + state holder; gameplay logic lives in CookieManager.
 */
public class CookieTest {

    private Cookie cookie;

    @BeforeEach
    public void setUp() {
        cookie = new HeroCookie();
    }

    // =========================
    // HP STATE TESTS
    // =========================

    @Test
    public void testInitialHP_StartsAtMax() {
        assertTrue(cookie.getHp() > 0, "HP should be positive on construction");
        assertEquals(cookie.getMaxHpValue(), cookie.getHp(),
                "Cookie should start at max HP");
    }

    @Test
    public void testSetHp_NormalValue() {
        cookie.setHp(42);
        assertEquals(42, cookie.getHp());
    }

    @Test
    public void testSetHp_NegativeClampedToZero() {
        cookie.setHp(-100);
        assertEquals(0, cookie.getHp(), "HP should never go below zero");
    }

    @Test
    public void testSetHp_Zero() {
        cookie.setHp(0);
        assertEquals(0, cookie.getHp());
    }

    @Test
    public void testSetMaxHpValue_PositiveValue() {
        cookie.setMaxHpValue(150);
        assertEquals(150, cookie.getMaxHpValue());
    }

    @Test
    public void testSetMaxHpValue_NegativeClampedToZero() {
        cookie.setMaxHpValue(-50);
        assertEquals(0, cookie.getMaxHpValue(),
                "Max HP should clamp at zero, not be negative");
    }

    // =========================
    // METADATA TESTS
    // =========================

    @Test
    public void testGetDisplayName_NonEmpty() {
        assertNotNull(cookie.getDisplayName());
        assertFalse(cookie.getDisplayName().isEmpty());
    }

    @Test
    public void testSetDisplayName() {
        cookie.setDisplayName("Custom Cookie");
        assertEquals("Custom Cookie", cookie.getDisplayName());
    }

    @Test
    public void testGetTier_SingleLetter() {
        assertNotNull(cookie.getTier());
        assertTrue(cookie.getTier().matches("[A-Z]"),
                "Tier should be a single uppercase letter");
    }

    @Test
    public void testSetTier() {
        cookie.setTier("A");
        assertEquals("A", cookie.getTier());
    }

    @Test
    public void testGetCookieName_Immutable() {
        String original = cookie.getCookieName();
        assertEquals("HeroCookie", original);
        // cookieName is final — no setter exists, so verifying immutability by type
    }

    @Test
    public void testGetIconPath_Set() {
        Cookie blueberry = new BlueberryCookie();
        assertTrue(blueberry.getIconPath().contains("BlueberryCookie"),
                "Icon path should reference cookie's sprite folder");
    }

    @Test
    public void testGetHex_SetByConstructor() {
        Cookie blueberry = new BlueberryCookie();
        assertEquals("#4169E1", blueberry.getHex());
    }

    @Test
    public void testGetCookieAbilityDescription_NonEmpty() {
        assertNotNull(cookie.getCookieAbilityDescription());
        assertFalse(cookie.getCookieAbilityDescription().isEmpty());
    }

    @Test
    public void testGetPlaceholderColor_NotNull() {
        assertNotNull(cookie.getPlaceholderColor());
    }

    // =========================
    // ABILITY TESTS
    // =========================

    @Test
    public void testHeroCookie_HasJumpBoostAbility() {
        assertNotNull(cookie.getAbility(),
                "HeroCookie should have an ability");
        assertInstanceOf(JumpBoostAbility.class, cookie.getAbility(),
                "HeroCookie's ability should be JumpBoost");
    }

    @Test
    public void testBlueberryCookie_HasMagneticAbility() {
        Cookie blueberry = new BlueberryCookie();
        assertInstanceOf(MagneticAbility.class, blueberry.getAbility());
    }

    @Test
    public void testSetAbility() {
        CookieAbility newAbility = new MagneticAbility();
        cookie.setAbility(newAbility);
        assertSame(newAbility, cookie.getAbility());
    }

    // =========================
    // GHOST STATE TESTS
    // =========================

    @Test
    public void testGhost_DefaultFalse() {
        assertFalse(cookie.isGhost(),
                "Cookie should not be in ghost mode by default");
    }

    @Test
    public void testSetGhost_TogglesValue() {
        cookie.setGhost(true);
        assertTrue(cookie.isGhost());
        cookie.setGhost(false);
        assertFalse(cookie.isGhost());
    }

    // =========================
    // POSITION & SIZE TESTS
    // =========================

    @Test
    public void testGetPosition_PositiveValues() {
        assertTrue(cookie.getX() >= 0);
        assertTrue(cookie.getY() >= 0);
    }

    @Test
    public void testGetDimensions_Positive() {
        assertTrue(cookie.getWidth() > 0);
        assertTrue(cookie.getHeight() > 0);
    }

    @Test
    public void testSetPosition() {
        cookie.setX(123);
        cookie.setY(456);
        assertEquals(123, cookie.getX());
        assertEquals(456, cookie.getY());
    }

    @Test
    public void testSetSize() {
        cookie.setWidth(80);
        cookie.setHeight(90);
        assertEquals(80, cookie.getWidth());
        assertEquals(90, cookie.getHeight());
    }

    // =========================
    // ALIVE STATE TESTS
    // =========================

    @Test
    public void testIsAlive_DefaultTrue() {
        assertTrue(cookie.isAlive(),
                "New cookie should be alive");
    }

    @Test
    public void testSetAlive_False() {
        cookie.setAlive(false);
        assertFalse(cookie.isAlive());
    }
}
