package game;

import javafx.scene.canvas.GraphicsContext;
import core.entities.base.Cookie;
import core.Physics;
import core.entities.base.GameObject;
import game.collectibles.Collectible;
import core.abilities.MagneticAbility;
import game.managers.CookieManager;
import game.managers.ObstacleManager;
import game.managers.CollectibleManager;
import game.managers.HealthManager;
import game.config.GameConfig;
import game.config.PhysicsConfig;
import gamelogic.events.EventBus;
import gamelogic.events.GameOverEvent;
import gamelogic.progression.DifficultyManager;
import utils.Renderable;
import utils.Updatable;
import audio.SoundManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Central game controller - unified game management system.
 *
 * Responsibilities:
 * - Handle game loop and real-time updates
 * - Manage all game systems (cookies, obstacles, collectibles, health)
 * - Handle user input
 * - Manage collisions and game state
 * - Provide centralized access to game managers
 * - Control score/coins/difficulty
 *
 * Uses manager pattern to separate concerns:
 * - CookieManager: Cookie data &amp; access
 * - ObstacleManager: Obstacle spawning &amp; collisions
 * - CollectibleManager: Collectible spawning &amp; collisions
 * - HealthManager: Health item spawning &amp; collisions
 */
public class GameController implements Renderable, Updatable {

    // Singleton instance for global game access
    private static GameController instance;

    // Game system managers
    private final CookieManager cookieManager;
    private final DifficultyManager difficultyManager;
    private final EventBus eventBus;

    private double cameraShake = 0;

    public static final double GROUND_Y   = Physics.GROUND_Y;
    public static final double GAME_WIDTH = GameConfig.WINDOW_WIDTH;

    private final Cookie cookie;
    private final core.stages.Stage stage;
    private final double difficultyMultiplier;

    /**
     * Manager systems.
     *
     * Each manager handles its own logic
     * to keep GameController cleaner.
     */
    private final ObstacleManager obstacleManager =
            new ObstacleManager();

    private final CollectibleManager collectibleManager =
            new CollectibleManager();

    private final HealthManager healthManager =
            new HealthManager();

    /**
     * All active game objects.
     *
     * Includes:
     * - Obstacles
     * - Collectibles
     * - Effects
     */
    private final List<GameObject> gameObjects =
            new ArrayList<>();

    private int score = 0;
    private int coins = 0;

    private double gameTime = 0;
    private double currentSpeed = GameConfig.BASE_GAME_SPEED;

    private boolean gameOver = false;
    private boolean paused = false;

    /**
     * Red hit effect timer.
     *
     * Used when player gets damaged.
     */
    private double redOverlayTimer = 0;
    private static final double RED_OVERLAY_TOTAL = 0.7;  // 0.2s in + 0.2s stay + 0.3s out

    /**
     * Create game controller.
     *
     * Also resets cookie state and initializes game systems.
     * Sets this instance as the global singleton.
     */
    public GameController(
            Cookie cookie,
            core.stages.Stage stage
    ) {
        this.cookie = cookie;
        this.stage = stage;
        this.difficultyMultiplier = stage.getDifficultyMultiplier();
        this.cookieManager = CookieManager.getInstance();
        this.difficultyManager = DifficultyManager.getInstance();
        this.eventBus = EventBus.getInstance();

        // Set as singleton instance
        instance = this;

        // Initialize cookie gameplay state - load the cookie that matches
        // the cookie passed in (so sprites/frames render correctly).
        int cookieIdx = 0;
        String cookieName = cookie.getCookieName();
        for (int i = 0; i < game.cookies.CookieList.size(); i++) {
            Cookie c = game.cookies.CookieList.get(i);
            if (c.getCookieName().equals(cookieName)) {
                cookieIdx = i;
                break;
            }
        }
        cookieManager.loadCookie(cookieIdx);  // Load correct cookie for gameplay
        cookieManager.initializeGameplayState();  // Set up gameplay state

        // Initialize stage difficulty
        difficultyManager.initializeStage(stage);

        // Play background music for the stage
        int stageNum = stage.getStageIndex();
        SoundManager.getInstance().playStageMusic(stageNum);
    }

    /**
     * Get singleton instance of GameController.
     * Available during gameplay.
     */
    public static GameController getInstance() {
        return instance;
    }

    // INPUT
    /**
     * Handles jump input.
     */
    public void onJump() {
        if (!paused && !gameOver)
            cookieManager.jump(cookie);
    }

    /**
     * Triggers a camera shake effect with easing.
     * @param intensity The magnitude of the shake.
     */
    public void shakeCamera(double intensity) {
        cameraShake = Math.min(intensity, cameraShake + 3);  // Smooth accumulation
    }

    /**
     * Handles slide input.
     */
    public void onSlide() {
        if (!paused && !gameOver)
            cookieManager.slideDown(cookie);
    }

    /**
     * Handles releasing slide input.
     */
    public void onReleaseSlide() {
        cookieManager.releaseSlide(cookie);
    }

    /**
     * Handles special ability activation input.
     */
    public void onAbility() {
        if (!paused && !gameOver && cookie.getAbility() != null) {
            cookie.getAbility().activate();
        }
    }

    /**
     * Toggles the pause state.
     */
    public void togglePause() {
        if (!gameOver)
            paused = !paused;
    }

    /**
     * Main game update loop.
     *
     * Handles:
     * - Speed scaling
     * - Score update
     * - Cookie update
     * - Manager updates
     * - Ability updates
     * - Collision detection
     * - Game over logic
     */
    public void update(double delta) {
        // Update red overlay timer (even when paused)
        if (redOverlayTimer > 0) {
            redOverlayTimer -= delta;
        }
        if (paused || gameOver)
            return;

        // Update stage difficulty
        difficultyManager.update();
        currentSpeed = difficultyManager.getCurrentGameSpeed();

        gameTime += delta;

        // Score increases based on current speed
        score += (int)(delta * currentSpeed * 0.05);

        // Update cookie gameplay state (also applies natural HP drain inside)
        cookieManager.update(cookie, delta);

        if(cookie.getHp() <= 0) {
            gameOver = true;
            // Publish game over event
            eventBus.publish(new GameOverEvent(score, coins, stage.getStageIndex()));
        }

        // Update managers
        obstacleManager.update(delta, currentSpeed, gameObjects);
        collectibleManager.update(delta, currentSpeed, gameObjects);
        healthManager.update(delta, gameObjects);

        // Update cookie ability
        if (cookie.getAbility() != null) {
            cookie.getAbility().update(this, cookie, delta);
            cookie.setGhost(cookie.getAbility().isActive() && !cookie.getAbility().isPassive());
        }

        // Update game objects
        // We update objects first, then do a single batch cleanup pass.
        // This is more efficient than removing items one-by-one during iteration.
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject obj = gameObjects.get(i);
            obj.update(delta);
        }

        /*
        * Single-pass cleanup: remove dead objects from all lists
        * removeIf is optimized in modern JVMs and creates a single pass through each list.
        * Must clean all lists to prevent unbounded memory growth.
        */
        gameObjects.removeIf(obj -> !obj.isAlive());
        obstacleManager.getObstacles().removeIf(obj -> !obj.isAlive());
        collectibleManager.getCollectibles().removeIf(obj -> !obj.isAlive());
        healthManager.getHealthItems().removeIf(obj -> !obj.isAlive());

        // Check collisions
        boolean[] gameOverFlag = {gameOver};
        double oldHp = cookie.getHp();
        obstacleManager.checkCollision(cookie, gameOverFlag, difficultyMultiplier,this);
        if (cookie.getHp() < oldHp) {
            // Cookie was hit - trigger red overlay
            redOverlayTimer = RED_OVERLAY_TOTAL;
        }
        gameOver = gameOverFlag[0];

        if (gameOver) return;

        int[] scoreCoins = {score, coins};
        collectibleManager.checkCollision(cookie, scoreCoins);
        score = scoreCoins[0];
        coins = scoreCoins[1];

        healthManager.checkCollision(cookie);

        if (cameraShake > 0) {
            cameraShake = Math.max(0, cameraShake - delta * 20);
        }
    }

    public void render(GraphicsContext gc) {

        for (GameObject obj : gameObjects) {

            obj.render(gc);
        }

        // Render cookie through CookieManager
        cookieManager.render(cookie, gc);

/**
 * Draw magnetic aura effect
 * for MagneticAbility cookies.
 */
        if (cookie.getAbility()
                instanceof MagneticAbility) {

            MagneticAbility magnetic =
                    (MagneticAbility)
                            cookie.getAbility();

            double radius =
                    magnetic.getMagneticRadius() * 0.5;

            double cookieX =
                    cookie.getX()
                            + cookie.getWidth()
                            - 20;

            double cookieY =
                    cookie.getY()
                            + cookie.getHeight()
                            - 20;

            gc.setFill(
                    javafx.scene.paint.Color.web(
                            "#FFD700",
                            0.2
                    )
            );

            gc.fillOval(
                    cookieX - radius,
                    cookieY - radius,
                    120,
                    120
            );
        }

        /**
         * Render collectible particles/effects.
         */
        collectibleManager.renderParticles(gc);
    }

    // GETTERS
    public int getScore() { return score; }
    public int getCoins() { return coins; }
    public double getGameTime() { return gameTime; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPaused() { return paused; }
    public Cookie getCookie() { return cookie; }

    /**
     * Calculate red overlay opacity
     * for smooth damage feedback.
     */
    public double getRedOverlayOpacity() {
        if (redOverlayTimer <= 0) return 0.0;

        double elapsed = RED_OVERLAY_TOTAL - redOverlayTimer;

        if (elapsed < 0.2) {
            // Fade in: 0 to 1 over 0.2s
            return elapsed / 0.2;
        } else if (elapsed < 0.4) {
            // Stay: full opacity for 0.2s
            return 1.0;
        } else {
            // Fade out: 1 to 0 over 0.3s
            return 1.0 - ((elapsed - 0.4) / 0.3);
        }
    }

    /**
     * Apply magnetic force to collectibles within range.
     *
     * OPTIMIZATION: Skip off-screen collectibles before distance checks.
     * Only applies force to collectibles within the magnetic radius,
     * avoiding unnecessary physics calculations for distant items.
     *
     * Used by MagneticAbility.
     */
    public void applyMagneticForceToCollectibles(
            MagneticAbility ability
    ) {
        double magneticRadius = ability.getMagneticRadius();
        double cookieX = cookie.getX() + cookie.getWidth() / 2;
        double cookieY = cookie.getY() + cookie.getHeight() / 2;

        for (Collectible c : collectibleManager.getCollectibles()) {
            // Skip collectibles far off-screen left (avoid distance calculations)
            if (c.getX() < -magneticRadius) {
                continue;
            }

            double dx = c.getX() - cookieX;
            double dy = c.getY() - cookieY;
            double distanceSquared = dx * dx + dy * dy;
            double radiusSquared = magneticRadius * magneticRadius;

            // Only apply force if within range (using squared distance to avoid sqrt)
            if (distanceSquared <= radiusSquared) {
                ability.applyForceToCollectible(c, cookie);
            }
        }
    }
    public double getCameraShake() {

        return cameraShake;
    }

    // =========================
    // GAME SYSTEM MANAGEMENT
    // =========================

    /**
     * Get the cookie manager system.
     *
     * @return CookieManager instance for cookie operations
     */
    public CookieManager getCookieManager() {
        return cookieManager;
    }

    /**
     * Reset all game systems.
     * Call this when starting a new game or returning to menu.
     */
    public void resetAll() {
        cookieManager.clearCache();
    }

    /**
     * Get game statistics and debug info.
     *
     * @return String with game system statistics
     */
    public String getGameInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== GAME INFO ===\n");
        info.append("Score: ").append(score).append("\n");
        info.append("Coins: ").append(coins).append("\n");
        info.append("Game Time: ").append(String.format("%.1f", gameTime)).append("s\n");
        info.append("Current Speed: ").append(String.format("%.0f", currentSpeed)).append("\n");
        info.append("Cookie Manager: ").append(cookieManager.getStatistics()).append("\n");
        return info.toString();
    }
}
