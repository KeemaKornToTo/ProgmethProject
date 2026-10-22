package gamelogic.progression;

import core.stages.Stage;
import game.config.GameConfig;

/**
 * DifficultyManager - Manages stage-based difficulty.
 * Difficulty is stored in the Stage class.
 * No time-based progression or waves.
 */
public class DifficultyManager {
    private static DifficultyManager instance;
    private Stage currentStage;

    private DifficultyManager() {
    }

    public static synchronized DifficultyManager getInstance() {
        if (instance == null) {
            instance = new DifficultyManager();
        }
        return instance;
    }

    /**
     * Initialize difficulty for a stage.
     */
    public void initializeStage(Stage stage) {
        this.currentStage = stage;
    }

    /**
     * Update difficulty (no-op for stage-based system).
     */
    public void update() {
        // Stage difficulty is fixed - no updates needed
    }

    /**
     * Get current game speed based on stage multiplier.
     */
    public double getCurrentGameSpeed() {
        if (currentStage == null) return GameConfig.BASE_GAME_SPEED;
        return GameConfig.BASE_GAME_SPEED * currentStage.getSpeedMultiplier();
    }

    /**
     * Get spawn rate multiplier from current stage.
     */
    public double getSpawnRateMultiplier() {
        if (currentStage == null) return 1.0;
        return currentStage.getSpawnMultiplier();
    }

    /**
     * Get current stage.
     */
    public Stage getCurrentStage() {
        return currentStage;
    }

    /**
     * Reset difficulty manager.
     */
    public void reset() {
        currentStage = null;
    }
}

