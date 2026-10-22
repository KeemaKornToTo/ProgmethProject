package core.stages;

/**
 * Stage entity representing a game level/world.
 * Contains stage properties and resource paths.
 */
public class Stage {

    private final int id;
    private final String displayName;
    private final String description;
    private final String difficulty;
    private final double difficultyMultiplier;
    private final double speedMultiplier;
    private final double spawnMultiplier;
    private final String playBackgroundPath;
    private final String jellyLogoPath;

    /**
     * Create a stage with description, difficulty, and multipliers.
     */
    public Stage(int id, String displayName, String worldName, String description, String difficulty,
                 double difficultyMultiplier, double speedMultiplier, double spawnMultiplier) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.difficulty = difficulty;
        this.difficultyMultiplier = difficultyMultiplier;
        this.speedMultiplier = speedMultiplier;
        this.spawnMultiplier = spawnMultiplier;
        this.playBackgroundPath = String.format("/Stages/%s/%sPlayBg.png", worldName, worldName);
        this.jellyLogoPath = String.format("/Stages/%s/%sJelly.png", worldName, worldName);
    }

    public int getStageIndex() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPlayBackgroundPath() {
        return playBackgroundPath;
    }

    public String getJellyLogoPath() {
        return jellyLogoPath;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public double getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getSpawnMultiplier() {
        return spawnMultiplier;
    }
}

