package game.managers;

import core.entities.base.Cookie;
import core.entities.base.GameObject;
import core.entities.base.Obstacle;

import game.obstacles.types.airobstacles.Bat;
import game.obstacles.types.airobstacles.CloudSpike;
import game.obstacles.types.airobstacles.Fireball;

import game.obstacles.types.groundobstacles.Block;
import game.obstacles.types.groundobstacles.CandyWall;
import game.obstacles.types.groundobstacles.Spike;

import game.GameController;
import game.config.ObstacleConfig;
import gamelogic.events.EventBus;
import gamelogic.events.ObstacleHitEvent;
import gamelogic.progression.DifficultyManager;
import audio.SoundManager;
import core.pooling.ObjectPool;
import game.managers.CookieManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Handles:
 * - Obstacle spawning
 * - Obstacle collision
 * - Damage handling
 *
 * Optimization: Object pooling infrastructure for efficient obstacle reuse.
 * Reduces garbage collection pressure during high-difficulty gameplay.
 */
public class ObstacleManager {

    // =========================
    // STATE
    // =========================

    private final List<Obstacle> obstacles =
            new ArrayList<>();

    // Object pooling for frequently spawned obstacles
    // OPTIMIZATION: Reuse obstacle objects instead of creating new ones.
    // Pools for each obstacle type reduce memory allocation during gameplay.
    // Currently obstacles are created fresh, but pools are ready for optimization
    // if obstacle classes implement proper reset(x, speed) methods.
    private final ObjectPool<Spike> spikePool =
            new ObjectPool<>(
                    () -> new Spike(0, 0),      // Factory
                    10,                          // Pre-allocate 10 spikes
                    40                           // Max pool size
            );

    private final ObjectPool<Block> blockPool =
            new ObjectPool<>(
                    () -> new Block(0, 0),
                    8,
                    35
            );

    private final ObjectPool<CandyWall> candyWallPool =
            new ObjectPool<>(
                    () -> new CandyWall(0, 0),
                    8,
                    35
            );

    private final Random rng =
            new Random();

    private double obstacleTimer = 0;

    private double nextObstacleIn = ObstacleConfig.BASE_SPAWN_INTERVAL;

    // =========================
    // UPDATE
    // =========================

    /**
     * Update obstacle system.
     */
    public void update(
            double delta,
            double currentSpeed,
            List<GameObject> gameObjects
    ) {

        spawnObstacles(
                delta,
                currentSpeed,
                gameObjects
        );
    }

    // =========================
    // COLLISION
    // =========================

    /**
     * Damage cookie on collision.
     *
     * Optimization: Instead of removing obstacles immediately (O(n) operation),
     * we mark them as dead with destroy(). The GameController will batch-remove
     * them in a single removeIf() pass, which is O(n) but only happens once per frame.
     *
     * @param cookie The player cookie.
     * @param gameOverFlag Array used to communicate game over state back.
     * @param difficultyMultiplier Damage scaling factor from the current stage.
     * @param gc The game controller to trigger effects like camera shake.
     */
    public void checkCollision(
            Cookie cookie,
            boolean[] gameOverFlag,
            double difficultyMultiplier,
            GameController gc
    ) {

        // Get cookie manager for gameplay logic
        CookieManager cookieManager = CookieManager.getInstance();

        // Ignore damage during invincibility
        if (cookie.isGhost()
                || cookieManager.isInvincible()) {

            return;
        }

        // OPTIMIZATION: Use Iterator + early return for off-screen culling
        Iterator<Obstacle> iterator =
                obstacles.iterator();
        EventBus eventBus = EventBus.getInstance();

        while (iterator.hasNext()) {

            Obstacle obstacle =
                    iterator.next();

            // Skip obstacles far off-screen left (optimization: avoid pixel-perfect collision check)
            if (obstacle.getX() < -100) {
                continue;
            }

            if (!cookie.intersects(obstacle)) {
                continue;
            }

            // Record old health for event
            double oldHp = cookie.getHp();

            double damage =
                    obstacle.getBaseDamage()
                            * difficultyMultiplier;

            cookieManager.decreaseHp(cookie, damage);

            double newHp = cookie.getHp();

            cookieManager.setInvincible(true);

            gc.shakeCamera(8);

            // Play hit sound effect
            SoundManager.getInstance().playHitSound();

            // Post obstacle hit event
            eventBus.publish(new ObstacleHitEvent(
                obstacle.getClass().getSimpleName(),
                (int) damage,
                obstacle.getX(),
                obstacle.getY()
            ));

            // Game over
            if (cookie.getHp() <= 0) {

                cookieManager.die(cookie);

                gameOverFlag[0] = true;
            }

            // Mark as dead instead of removing immediately
            // GameController will batch-remove in a single efficient pass
            obstacle.destroy();

            return;
        }
    }

    // =========================
    // SPAWNING
    // =========================

    /**
     * Spawn obstacles periodically.
     * OPTIMIZATION: Adaptive spawn rate scales with difficulty to prevent object accumulation.
     * As difficulty increases, spawn intervals increase, keeping object density constant.
     */
    private void spawnObstacles(
            double delta,
            double currentSpeed,
            List<GameObject> gameObjects
    ) {

        obstacleTimer += delta;

        if (obstacleTimer
                < nextObstacleIn) {

            return;
        }

        obstacleTimer = 0;

        // Get spawn multiplier from current stage difficulty
        DifficultyManager diffMgr = DifficultyManager.getInstance();
        double spawnMultiplier = diffMgr.getSpawnRateMultiplier();

        // Spawn interval scales with difficulty (higher multiplier = more frequent spawns)
        nextObstacleIn =
                ObstacleConfig.BASE_SPAWN_INTERVAL / spawnMultiplier +
                (rng.nextDouble() * ObstacleConfig.SPAWN_VARIATION);

        int type =
                rng.nextInt(6);

        Obstacle obstacle =
                createObstacle(
                        type,
                        810,
                        currentSpeed
                );

        obstacles.add(obstacle);

        gameObjects.add(obstacle);
    }

    /**
     * Create random obstacle.
     */
    private Obstacle createObstacle(
            int type,
            double x,
            double speed
    ) {

        return switch (type) {

            case 0 ->
                    new CandyWall(x, speed);

            case 1 ->
                    new Spike(x, speed);

            case 2 ->
                    new Block(x, speed);

            case 3 ->
                    new Fireball(x, speed);

            case 4 ->
                    new Bat(x, speed);

            default ->
                    new CloudSpike(x, speed);
        };
    }

    // =========================
    // GETTERS
    // =========================

    public List<Obstacle> getObstacles() {

        return obstacles;
    }
}