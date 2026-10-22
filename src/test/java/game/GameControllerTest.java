package game;

import core.entities.base.Cookie;
import core.stages.Stage;
import core.stages.StageList;
import game.cookies.implementations.HeroCookie;
import game.cookies.implementations.BlueberryCookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameController.
 * Tests game loop logic, scoring, state management, and cookie integration.
 */
public class GameControllerTest {

    private GameController controller;
    private Cookie testCookie;
    private Stage testStage;

    @BeforeEach
    public void setUp() {
        testCookie = new HeroCookie();
        testStage = StageList.ALL.get(0);
        controller = new GameController(testCookie, testStage);
    }

    // =========================
    // INITIALIZATION TESTS
    // =========================

    @Test
    public void testConstructor_InitializesController() {
        assertNotNull(controller, "GameController should be created");
        assertSame(testCookie, controller.getCookie(),
                "Cookie reference should be preserved");
    }

    @Test
    public void testConstructor_CookieAtMaxHpInitially() {
        assertEquals(testCookie.getMaxHpValue(), testCookie.getHp(),
                "Cookie should be at max HP at game start");
    }

    @Test
    public void testSingleton_ReturnsCurrentController() {
        GameController instance = GameController.getInstance();
        assertSame(controller, instance,
                "Singleton should match the most recently created instance");
    }

    @Test
    public void testConstructor_LoadsMatchingCookieType() {
        // When a HeroCookie is passed in, the loaded cookie should be HeroCookie too
        Cookie loaded = controller.getCookieManager().getLoadedCookie();
        assertNotNull(loaded);
        assertEquals(testCookie.getCookieName(), loaded.getCookieName(),
                "CookieManager should load a cookie matching the passed-in cookie's type");
    }

    @Test
    public void testConstructor_DifferentCookieTypes() {
        Cookie blueberry = new BlueberryCookie();
        GameController newCtrl = new GameController(blueberry, testStage);
        Cookie loaded = newCtrl.getCookieManager().getLoadedCookie();
        assertEquals("BlueberryCookie", loaded.getCookieName());
    }

    // =========================
    // GAME STATE TESTS
    // =========================

    @Test
    public void testInitialGameState_NotOverNotPaused() {
        assertFalse(controller.isGameOver());
        assertFalse(controller.isPaused());
    }

    @Test
    public void testTogglePause() {
        assertFalse(controller.isPaused());
        controller.togglePause();
        assertTrue(controller.isPaused());
        controller.togglePause();
        assertFalse(controller.isPaused());
    }

    @Test
    public void testTogglePause_BlockedAfterGameOver() {
        // Force HP to zero to trigger game-over
        testCookie.setHp(0);
        controller.update(0.016);
        boolean wasPaused = controller.isPaused();
        controller.togglePause();
        assertEquals(wasPaused, controller.isPaused(),
                "Pause should not toggle once the game is over");
    }

    // =========================
    // HP DRAIN TESTS
    // =========================

    @Test
    public void testHpDrains_OverTime() {
        double startHp = testCookie.getHp();
        controller.update(0.5);  // half a second
        assertTrue(testCookie.getHp() < startHp,
                "HP should drain naturally over time");
    }

    @Test
    public void testGameOver_WhenHpHitsZero() {
        testCookie.setHp(1);
        // Run enough simulated time to ensure drain takes HP to zero
        for (int i = 0; i < 60; i++) {
            controller.update(0.1);
            if (controller.isGameOver()) break;
        }
        assertTrue(controller.isGameOver() || testCookie.getHp() <= 0,
                "Game should end once HP reaches zero");
    }

    @Test
    public void testHpDoesNotDrain_WhenPaused() {
        controller.togglePause();
        double startHp = testCookie.getHp();
        controller.update(1.0);
        assertEquals(startHp, testCookie.getHp(),
                "HP should not drain while paused");
    }

    // =========================
    // SCORE & COINS TESTS
    // =========================

    @Test
    public void testInitialScoreAndCoins_Zero() {
        assertEquals(0, controller.getScore());
        assertEquals(0, controller.getCoins());
    }

    @Test
    public void testScoreIncreases_OverTime() {
        controller.update(0.5);
        assertTrue(controller.getScore() > 0,
                "Score should accumulate from time-based scoring");
    }

    @Test
    public void testScore_ZeroDelta_NoChange() {
        controller.update(0.016); // build a little
        int before = controller.getScore();
        controller.update(0);
        assertEquals(before, controller.getScore(),
                "Zero delta should not change the score");
    }

    @Test
    public void testScore_MonotonicallyIncreases() {
        controller.update(0.1);
        int s1 = controller.getScore();
        controller.update(0.1);
        int s2 = controller.getScore();
        assertTrue(s2 >= s1, "Score should never decrease during normal play");
    }

    // =========================
    // GAME TIME TESTS
    // =========================

    @Test
    public void testGameTime_InitiallyZero() {
        assertEquals(0.0, controller.getGameTime(), 1e-9);
    }

    @Test
    public void testGameTime_IncreasesWithUpdate() {
        controller.update(0.25);
        assertEquals(0.25, controller.getGameTime(), 1e-6);
    }

    @Test
    public void testGameTime_DoesNotAdvance_WhenPaused() {
        controller.togglePause();
        double before = controller.getGameTime();
        controller.update(1.0);
        assertEquals(before, controller.getGameTime(), 1e-9);
    }

    // =========================
    // INPUT TESTS
    // =========================

    @Test
    public void testOnJump_DoesNotCrash() {
        assertDoesNotThrow(() -> controller.onJump());
    }

    @Test
    public void testOnSlide_DoesNotCrash() {
        assertDoesNotThrow(() -> controller.onSlide());
    }

    @Test
    public void testOnReleaseSlide_DoesNotCrash() {
        assertDoesNotThrow(() -> controller.onReleaseSlide());
    }

    @Test
    public void testOnAbility_DoesNotCrash() {
        assertDoesNotThrow(() -> controller.onAbility());
    }

    @Test
    public void testInput_IgnoredWhenPaused() {
        controller.togglePause();
        // These should be safe no-ops, not exceptions
        assertDoesNotThrow(() -> {
            controller.onJump();
            controller.onSlide();
            controller.onAbility();
        });
    }

    // =========================
    // CAMERA SHAKE TESTS
    // =========================

    @Test
    public void testCameraShake_InitiallyZero() {
        assertEquals(0, controller.getCameraShake(),
                "Should start with no camera shake");
    }

    @Test
    public void testShakeCamera_Triggered() {
        controller.shakeCamera(5.0);
        assertTrue(controller.getCameraShake() > 0,
                "Camera shake should be positive after trigger");
    }

    @Test
    public void testCameraShake_DecaysOverTime() {
        controller.shakeCamera(10.0);
        double before = controller.getCameraShake();
        controller.update(0.1);
        double after = controller.getCameraShake();
        assertTrue(after < before, "Camera shake should decay between frames");
    }

    @Test
    public void testCameraShake_DoesNotGoNegative() {
        controller.shakeCamera(1.0);
        // Run a long update so shake fully decays
        controller.update(5.0);
        assertTrue(controller.getCameraShake() >= 0,
                "Shake should clamp at zero, not go negative");
    }

    // =========================
    // RED OVERLAY TESTS
    // =========================

    @Test
    public void testRedOverlayOpacity_InitiallyZero() {
        assertEquals(0.0, controller.getRedOverlayOpacity(), 1e-9);
    }

    // =========================
    // MANAGER ACCESS TESTS
    // =========================

    @Test
    public void testGetCookieManager_NotNull() {
        assertNotNull(controller.getCookieManager());
    }

    @Test
    public void testResetAll_PreservesManager() {
        controller.update(0.1);
        controller.resetAll();
        assertNotNull(controller.getCookieManager(),
                "Manager should still be accessible after reset");
    }

    @Test
    public void testGetGameInfo_ContainsKeyFields() {
        String info = controller.getGameInfo();
        assertNotNull(info);
        assertTrue(info.contains("Score"));
        assertTrue(info.contains("Coins"));
        assertTrue(info.contains("Game Time"));
    }
}
