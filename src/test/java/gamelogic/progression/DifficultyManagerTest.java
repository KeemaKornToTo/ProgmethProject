package gamelogic.progression;

import core.stages.Stage;
import core.stages.StageList;
import game.config.GameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DifficultyManager.
 * The manager is a singleton, so each test resets state in @BeforeEach.
 */
public class DifficultyManagerTest {

    private DifficultyManager manager;

    @BeforeEach
    public void setUp() {
        manager = DifficultyManager.getInstance();
        manager.reset();
    }

    @Test
    public void testSingleton_ReturnsSameInstance() {
        assertSame(DifficultyManager.getInstance(), DifficultyManager.getInstance());
    }

    @Test
    public void testInitialState_NoCurrentStage() {
        assertNull(manager.getCurrentStage(),
                "After reset, current stage should be null");
    }

    @Test
    public void testGetCurrentGameSpeed_DefaultBeforeInit() {
        assertEquals(GameConfig.BASE_GAME_SPEED, manager.getCurrentGameSpeed(), 1e-9,
                "With no stage set, speed falls back to BASE_GAME_SPEED");
    }

    @Test
    public void testGetSpawnRateMultiplier_DefaultBeforeInit() {
        assertEquals(1.0, manager.getSpawnRateMultiplier(), 1e-9);
    }

    @Test
    public void testInitializeStage_StoresStage() {
        Stage stage = StageList.getStage(2);
        manager.initializeStage(stage);
        assertSame(stage, manager.getCurrentStage());
    }

    @Test
    public void testGameSpeed_ScalesByStageMultiplier() {
        Stage stage = StageList.getStage(4); // Hardest stage
        manager.initializeStage(stage);
        double expected = GameConfig.BASE_GAME_SPEED * stage.getSpeedMultiplier();
        assertEquals(expected, manager.getCurrentGameSpeed(), 1e-9);
    }

    @Test
    public void testSpawnRateMultiplier_MatchesStage() {
        Stage stage = StageList.getStage(3);
        manager.initializeStage(stage);
        assertEquals(stage.getSpawnMultiplier(), manager.getSpawnRateMultiplier(), 1e-9);
    }

    @Test
    public void testUpdate_IsNoOp() {
        Stage stage = StageList.getStage(0);
        manager.initializeStage(stage);
        double speedBefore = manager.getCurrentGameSpeed();
        manager.update();
        manager.update();
        manager.update();
        assertEquals(speedBefore, manager.getCurrentGameSpeed(), 1e-9,
                "Stage-based difficulty should not change just from update() calls");
    }

    @Test
    public void testReset_ClearsCurrentStage() {
        manager.initializeStage(StageList.getStage(2));
        manager.reset();
        assertNull(manager.getCurrentStage());
        assertEquals(GameConfig.BASE_GAME_SPEED, manager.getCurrentGameSpeed(), 1e-9);
    }
}
