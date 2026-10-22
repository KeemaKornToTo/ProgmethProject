package game.managers;

import java.util.*;
import core.abilities.CookieAbility;

/**
 * AbilityManager - Manages cookie abilities, cooldowns, and effects.
 */
public class AbilityManager {
    private Map<String, AbilityCooldown> cooldowns = new HashMap<>();
    private CookieAbility activeAbility;
    private long abilityStartTime = 0;

    public void activateAbility(CookieAbility ability) {
        String abilityName = ability.getClass().getSimpleName();

        // Check if ability is on cooldown
        if (isOnCooldown(abilityName)) {
            return;
        }

        this.activeAbility = ability;
        this.abilityStartTime = System.currentTimeMillis();
        ability.activate();

        // Start cooldown - default 5 seconds
        AbilityCooldown cooldown = new AbilityCooldown(abilityName, 5.0);
        cooldowns.put(abilityName, cooldown);
    }

    public void update(double delta) {
        // Update cooldowns
        for (AbilityCooldown cooldown : cooldowns.values()) {
            cooldown.update(delta);
        }
    }

    public boolean isOnCooldown(String abilityName) {
        AbilityCooldown cooldown = cooldowns.get(abilityName);
        if (cooldown == null) return false;
        return cooldown.isActive();
    }

    public double getCooldownRemaining(String abilityName) {
        AbilityCooldown cooldown = cooldowns.get(abilityName);
        return cooldown != null ? cooldown.getTimeRemaining() : 0;
    }

    public CookieAbility getActiveAbility() {
        return activeAbility;
    }

    public void reset() {
        activeAbility = null;
        cooldowns.clear();
    }
}

class AbilityCooldown {
    private String abilityName;
    private double timeRemaining;
    private double totalCooldown;

    public AbilityCooldown(String name, double cooldownSeconds) {
        this.abilityName = name;
        this.totalCooldown = cooldownSeconds;
        this.timeRemaining = cooldownSeconds;
    }

    public void update(double delta) {
        timeRemaining = Math.max(0, timeRemaining - delta);
    }

    public boolean isActive() {
        return timeRemaining > 0;
    }

    public double getTimeRemaining() {
        return timeRemaining;
    }

    public double getProgress() {
        return 1.0 - (timeRemaining / totalCooldown);
    }
}
