package core.entities.base;

import core.abilities.CookieAbility;
import javafx.scene.paint.Color;

/**
 * Lightweight Cookie data model - metadata only.
 *
 * ALL game logic is handled by CookieManager.
 * This class is purely for data storage and retrieval.
 */
public abstract class Cookie extends GameObject {

    // =====================
    // METADATA
    // =====================
    protected String displayName = "Cookie";
    protected String tier = "C";
    protected String hex = "#CCCCCC";
    protected String iconPath = "";
    protected int maxHpValue = 100;
    protected String cookieAbilityDescription = "";
    protected final String cookieName;
    protected final Color placeholderColor;

    // =====================
    // CURRENT STATE
    // =====================
    protected double hp = 100;
    protected CookieAbility ability = null;
    protected boolean isGhost = false;

    public Cookie(String cookieName, Color placeholderColor) {
        super(80, 398 - 70, 70, 70);
        this.cookieName = cookieName;
        this.placeholderColor = placeholderColor;
        this.hp = maxHpValue;
    }

    // =====================
    // METADATA GETTERS
    // =====================
    public String getDisplayName() { return displayName; }
    public String getTier() { return tier; }
    public String getHex() { return hex; }
    public String getIconPath() { return iconPath; }
    public int getMaxHpValue() { return maxHpValue; }
    public String getCookieAbilityDescription() { return cookieAbilityDescription; }
    public String getCookieName() { return cookieName; }
    public Color getPlaceholderColor() { return placeholderColor; }

    // =====================
    // POSITION/SIZE GETTERS
    // =====================
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    // =====================
    // STATE GETTERS
    // =====================
    public double getHp() { return hp; }
    public CookieAbility getAbility() { return ability; }
    public boolean isGhost() { return isGhost; }

    // =====================
    // METADATA SETTERS
    // =====================
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setTier(String tier) { this.tier = tier; }
    public void setHex(String hex) { this.hex = hex; }
    public void setIconPath(String iconPath) { this.iconPath = iconPath; }
    public void setMaxHpValue(int maxHpValue) { this.maxHpValue = Math.max(0, maxHpValue); }
    public void setCookieAbilityDescription(String desc) { this.cookieAbilityDescription = desc; }

    // =====================
    // STATE SETTERS (for CookieManager)
    // =====================
    public void setHp(double hp) { this.hp = Math.max(0, hp); }
    public void setAbility(CookieAbility ability) { this.ability = ability; }
    public void setGhost(boolean ghost) { this.isGhost = ghost; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
    public void setAlive(boolean alive) { this.alive = alive; }

    // =====================
    // UTILITY (Not overridden - CookieManager handles update/render)
    // =====================
    @Override
    public void update(double delta) {
        // CookieManager handles all updates
    }

    @Override
    public void render(javafx.scene.canvas.GraphicsContext gc) {
        // CookieManager handles all rendering
    }
}
