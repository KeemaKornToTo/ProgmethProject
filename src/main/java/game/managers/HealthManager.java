package game.managers;

import core.entities.base.Cookie;
import core.entities.base.GameObject;
import core.Physics;
import game.items.HealthItem;
import gamelogic.events.EventBus;
import gamelogic.events.HealthChangedEvent;
import audio.SoundManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Handles:
 * - Health item spawning
 * - Healing collision
 */
public class HealthManager {

    // =========================
    // STATE
    // =========================

    private final List<HealthItem> healthItems =
            new ArrayList<>();

    private final Random rng =
            new Random();

    private double healthSpawnTimer = 0;

    private double nextSpawnTime = 4;

    // =========================
    // UPDATE
    // =========================

    /**
     * Update health system every frame.
     */
    public void update(
            double delta,
            List<GameObject> gameObjects
    ) {

        spawnHealthItems(
                delta,
                gameObjects
        );
    }

    // =========================
    // COLLISION
    // =========================

    /**
     * Heal cookie on pickup.
     * OPTIMIZATION: Skip off-screen health items before collision check.
     */
    public void checkCollision(
            Cookie cookie
    ) {

        Iterator<HealthItem> iterator =
                healthItems.iterator();
        EventBus eventBus = EventBus.getInstance();

        while (iterator.hasNext()) {

            HealthItem item =
                    iterator.next();

            // Skip items far off-screen (avoid collision check)
            if (item.getX() < -100) {
                continue;
            }

            if (!item.collides(cookie)) {
                continue;
            }

            // Record old health for event
            double oldHp = cookie.getHp();
            CookieManager.getInstance().heal(cookie, 25);
            double newHp = cookie.getHp();

            // Play healing sound
            SoundManager.getInstance().playClickSound();

            // Post health changed event
            eventBus.publish(new HealthChangedEvent(
                (int) oldHp,
                (int) newHp,
                "Health item pickup"
            ));

            item.destroy();

            iterator.remove();
        }
    }

    // =========================
    // SPAWNING
    // =========================

    /**
     * Spawn health item randomly.
     */
    private void spawnHealthItems(
            double delta,
            List<GameObject> gameObjects
    ) {

        healthSpawnTimer += delta;

        if (healthSpawnTimer
                < nextSpawnTime) {

            return;
        }

        healthSpawnTimer = 0;

        // Random next spawn
        nextSpawnTime =
                3 + rng.nextDouble() * 3;

        HealthItem item =
                new HealthItem(
                        810,
                        Physics.GROUND_Y - 120
                );

        healthItems.add(item);

        gameObjects.add(item);
    }

    // =========================
    // GETTERS
    // =========================

    public List<HealthItem> getHealthItems() {

        return healthItems;
    }
}