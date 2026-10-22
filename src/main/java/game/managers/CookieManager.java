package game.managers;

import core.entities.base.Cookie;
import core.Physics;
import core.abilities.JumpBoostAbility;
import game.cookies.CookieList;
import audio.SoundManager;
import utils.Animatable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Game-level cookie manager.
 *
 * Responsibilities:
 * - Load and manage cookies for gameplay
 * - Handle cookie selection and UI interactions
 * - Provide access to cookies by index, name, or tier
 * - Manage cookie state during selection/loading
 * - Cache cookie instances efficiently
 * - Manage sprite-based animation via Animatable interface
 *
 * Uses CookieList from game.cookies as the data source.
 * Cookie class contains basic data (HP, ability, stats).
 */
public class CookieManager implements Animatable {

    // Singleton instance
    private static CookieManager instance;
    // Cookie cache for frequently accessed instances
    private final Cookie[] cookieCache = new Cookie[CookieList.size()];
    // Game state
    private int selectedCookieIndex = 0;
    private Cookie loadedCookie = null;

    private CookieManager() {
    }
    /**
     * Get singleton instance of CookieManager.
     */
    public static CookieManager getInstance() {
        if (instance == null) {
            instance = new CookieManager();
        }
        return instance;
    }

    // =========================
    // COOKIE LOADING & SELECTION
    // =========================

    /**
     * Load a cookie for gameplay.
     * Creates a fresh instance, prepares frames and resources for the game.
     *
     * @param index The cookie index to load
     * @return The loaded cookie instance (fully prepared)
     */
    public Cookie loadCookie(int index) {
        if (index < 0 || index >= CookieList.size()) {
            throw new IndexOutOfBoundsException(
                    "No cookie at index " + index
            );
        }

        this.selectedCookieIndex = index;
        this.loadedCookie = getFreshCookie(index);

        // Ensure all animation frames are loaded
        loadCookieFrames(this.loadedCookie);

        return loadedCookie;
    }

    /**
     * Load all animation frames for a cookie.
     * Prepares RUN, JUMP, SLIDE, and DEAD animation frames.
     *
     * @param cookie The cookie to load frames for
     */
    private void loadCookieFrames(Cookie cookie) {
        // Cookie class handles frame loading internally via loadAnimationFrames()
        // This is called during Cookie construction, so frames are already loaded
        // This method can be extended for pre-loading or resource verification
    }
    /**
     * Get the currently loaded cookie.
     *
     * @return The loaded cookie, or null if none loaded
     */
    public Cookie getLoadedCookie() {
        return loadedCookie;
    }

    /**
     * Preload a cookie's resources without loading it for gameplay.
     * Useful for pre-caching during selection menu.
     *
     * @param index The cookie index to preload
     */
    public void preloadCookieResources(int index) {
        if (index >= 0 && index < CookieList.size()) {
            Cookie cookie = getCookie(index);
            // Cookie resources are loaded automatically
            // This ensures the instance is cached for quick access
        }
    }

    /**
     * Handle cookie button press from UI.
     * Updates selection and prepares cookie for preview.
     *
     * @param index The index of the button pressed
     */
    public void onCookieButtonPressed(int index) {
        if (index < 0 || index >= CookieList.size()) {
            return;
        }
        this.selectedCookieIndex = index;

        // Preload resources for smooth selection
        preloadCookieResources(index);
    }

    /**
     * Get the currently selected cookie index.
     *
     * @return The selected cookie index
     */
    public int getSelectedCookieIndex() {
        return selectedCookieIndex;
    }

    /**
     * Set the selected cookie index.
     *
     * @param index The index to select
     */
    public void setSelectedCookieIndex(int index) {
        if (index >= 0 && index < CookieList.size()) {
            this.selectedCookieIndex = index;
            preloadCookieResources(index);
        }
    }

    /**
     * Get preview of a cookie for selection UI.
     * Returns cached instance with all resources loaded.
     *
     * @param index The cookie index to preview
     * @return The cookie instance for preview (fully prepared)
     */
    public Cookie previewCookie(int index) {
        Cookie cookie = getCookie(index);
        // Ensure frames are loaded
        loadCookieFrames(cookie);
        return cookie;
    }

    /**
     * Unload cookie resources and clear loaded state.
     * Call when returning to menu or changing scenes.
     */
    public void unloadCookie() {
        this.loadedCookie = null;
        this.selectedCookieIndex = 0;
    }

    // =========================
    // COOKIE ACCESS & QUERIES
    // =========================

    /**
     * Get a cookie by index with caching.
     * Uses cache to avoid recreating instances if accessed multiple times.
     * Use for UI preview and data access.
     *
     * @param index The cookie index (0-based)
     * @return The cookie instance
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Cookie getCookie(int index) {
        if (index < 0 || index >= CookieList.size()) {
            throw new IndexOutOfBoundsException(
                    "No cookie at index " + index
            );
        }

        // Return cached instance if available
        if (cookieCache[index] != null) {
            return cookieCache[index];
        }

        // Create new instance and cache it
        Cookie cookie = CookieList.get(index);
        cookieCache[index] = cookie;
        return cookie;
    }

    /**
     * Get a fresh cookie instance (uncached).
     * Use this when you need a new independent instance for gameplay.
     *
     * @param index The cookie index (0-based)
     * @return A new cookie instance
     */
    public Cookie getFreshCookie(int index) {
        return CookieList.get(index);
    }

    /**
     * Get cookie by display name.
     *
     * @param displayName The display name of the cookie
     * @return The cookie with matching name, or null if not found
     */
    public Cookie getCookieByName(String displayName) {
        for (int i = 0; i < CookieList.size(); i++) {
            Cookie cookie = getCookie(i);
            if (cookie.getDisplayName().equalsIgnoreCase(displayName)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * Get the index of a cookie by display name.
     *
     * @param displayName The display name of the cookie
     * @return The index, or -1 if not found
     */
    public int getCookieIndexByName(String displayName) {
        for (int i = 0; i < CookieList.size(); i++) {
            Cookie cookie = getCookie(i);
            if (cookie.getDisplayName().equalsIgnoreCase(displayName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get all cookies of a specific tier.
     *
     * @param tier The tier letter (e.g., "S", "A", "B", "C")
     * @return List of cookies matching the tier
     */
    public List<Cookie> getCookiesByTier(String tier) {
        List<Cookie> result = new ArrayList<>();
        for (int i = 0; i < CookieList.size(); i++) {
            Cookie cookie = getCookie(i);
            if (cookie.getTier().equals(tier)) {
                result.add(cookie);
            }
        }
        return result;
    }

    /**
     * Get all available cookies.
     *
     * @return List of all cookies in order
     */
    public List<Cookie> getAllCookies() {
        List<Cookie> result = new ArrayList<>();
        for (int i = 0; i < CookieList.size(); i++) {
            result.add(getCookie(i));
        }
        return result;
    }

    /**
     * Get the total number of cookies.
     *
     * @return Number of available cookies
     */
    public int getTotalCookies() {
        return CookieList.size();
    }

    /**
     * Get cookie statistics summary.
     *
     * @return String with cookie count and tier distribution
     */
    public String getStatistics() {
        int sCount = getCookiesByTier("S").size();
        int aCount = getCookiesByTier("A").size();
        int bCount = getCookiesByTier("B").size();
        int cCount = getCookiesByTier("C").size();

        return String.format(
                "Total Cookies: %d | S-Tier: %d | A-Tier: %d | B-Tier: %d | C-Tier: %d",
                CookieList.size(), sCount, aCount, bCount, cCount
        );
    }

    /**
     * Clear the cookie cache.
     * Use this when you need fresh instances for all cookies.
     */
    public void clearCache() {
        for (int i = 0; i < cookieCache.length; i++) {
            cookieCache[i] = null;
        }
    }

    // =========================
    // GAMEPLAY LOGIC (New)
    // =========================

    /**
     * All gameplay state is managed by CookieManager.
     * Cookie class is pure data.
     */

    // Physics and state for loaded cookie
    private Physics cookiePhysics = null;
    private CookieState cookieState = CookieState.RUNNING;
    private Image[] runFrames = new Image[4];
    private Image[] jumpFrames = new Image[4];
    private Image[] slideFrames = new Image[4];
    private Image[] deadFrames = new Image[4];
    private Image[] currentFrames = null;
    private int currentFrame = 0;
    private double frameTimer = 0;
    private static final double FRAME_DUR = 0.06;

    // Gameplay timers
    private double healEffectTimer = 0;
    private double invincibilityTimer = 0;
    private static final double INVINCIBILITY_DURATION = 0.5;

    /**
     * Cookie state enum.
     */
    public enum CookieState {
        RUNNING, JUMPING, SLIDING, DEAD
    }

    /**
     * Initialize gameplay state for loaded cookie.
     * Call this after loading a cookie for gameplay.
     */
    public void initializeGameplayState() {
        if (loadedCookie == null) return;

        cookiePhysics = new Physics(80, Physics.GROUND_Y - Physics.NORMAL_H, 70, Physics.NORMAL_H);
        cookieState = CookieState.RUNNING;
        loadedCookie.setHp(loadedCookie.getMaxHpValue());

        loadAnimationFrames();
        currentFrames = runFrames;
        currentFrame = 0;
        frameTimer = 0;
        healEffectTimer = 0;
        invincibilityTimer = 0;
    }

    /**
     * Load animation frames for the loaded cookie.
     */
    private void loadAnimationFrames() {
        if (loadedCookie == null) return;
        String name = loadedCookie.getCookieName();

        loadFrameSet(runFrames, name, "RUN");
        loadFrameSet(jumpFrames, name, "JUMP");
        loadFrameSet(slideFrames, name, "SLIDE");
        loadFrameSet(deadFrames, name, "DEAD");
    }

    /**
     * Load sprite frames for an animation.
     */
    private void loadFrameSet(Image[] frames, String cookieName, String state) {
        for (int i = 0; i < 4; i++) {
            String resourcePath = "/CookieSprite/" + cookieName + "/" + cookieName + (i + 1) + ".png";
            try {
                var stream = getClass().getResourceAsStream(resourcePath);
                if (stream == null) {
                    String adjustedPath = resourcePath.substring(1);
                    stream = getClass().getResourceAsStream(adjustedPath);
                }
                if (stream != null) {
                    Image img = new Image(stream);
                    if (img != null && !img.isError()) {
                        frames[i] = img;
                    }
                }
            } catch (Exception e) {
                System.err.println("Load failed: " + resourcePath);
            }
        }
    }

    /**
     * Update cookie gameplay state.
     */
    public void update(Cookie cookie, double delta) {
        if (cookieState == CookieState.DEAD || cookiePhysics == null) return;

        // Natural HP drain over time (~2 HP per second)
        double newHp = Math.max(0, cookie.getHp() - delta * 3.0);
        cookie.setHp(newHp);
        if (newHp <= 0) {
            die(cookie);
            return;
        }

        // Timers
        if (healEffectTimer > 0) healEffectTimer -= delta;
        if (invincibilityTimer > 0) invincibilityTimer -= delta;

        // Jump physics
        if (cookieState == CookieState.JUMPING) {
            cookiePhysics.update(delta);
            if (cookiePhysics.isOnGround()) {
                cookieState = CookieState.RUNNING;
                cookiePhysics.setHeight(Physics.NORMAL_H);
                currentFrames = runFrames;
                currentFrame = 0;
            }
        }

        // Sync position to cookie
        cookie.setX(cookiePhysics.getX());
        cookie.setY(cookiePhysics.getY());
        cookie.setWidth(cookiePhysics.getWidth());
        cookie.setHeight(cookiePhysics.getHeight());

        // Animation
        frameTimer += delta;
        if (frameTimer >= FRAME_DUR) {
            frameTimer = 0;
            currentFrame = (currentFrame + 1) % 4;
        }
    }

    /**
     * Render the loaded cookie.
     */
    public void render(Cookie cookie, GraphicsContext gc) {
        if (loadedCookie == null || currentFrames == null) return;

        // Ghost mode
        if (cookie.isGhost()) {
            gc.setGlobalAlpha(0.4);
        }

        // Low HP flash
        if (cookie.getHp() < 30) {
            double alpha = 0.7 + 0.3 * Math.sin(System.currentTimeMillis() / 50.0);
            gc.setGlobalAlpha(alpha);
        }

        // Heal glow
        if (healEffectTimer > 0) {
            gc.setStroke(Color.LIME);
            gc.strokeOval(cookie.getX() - 5, cookie.getY() - 5, cookie.getWidth() + 10, cookie.getHeight() + 10);
        }

        // Render current frame
        Image frame = currentFrames[currentFrame];
        if (frame != null && !frame.isError()) {
            gc.drawImage(frame, cookie.getX(), cookie.getY(), cookie.getWidth(), cookie.getHeight());
        } else {
            drawPlaceholder(cookie, gc);
        }

        gc.setGlobalAlpha(1.0);
    }

    /**
     * Draw placeholder if frames fail to load.
     */
    private void drawPlaceholder(Cookie cookie, GraphicsContext gc) {
        Color color = cookie.getPlaceholderColor();
        gc.setFill(color);
        gc.fillRoundRect(cookie.getX(), cookie.getY(), cookie.getWidth(), cookie.getHeight(), 20, 20);
        gc.setFill(color.darker());
        gc.fillOval(cookie.getX() + cookie.getWidth() / 2 - 10, cookie.getY() + 4, 20, 20);
        gc.setFill(Color.WHITE);
        gc.fillOval(cookie.getX() + cookie.getWidth() / 2 - 6, cookie.getY() + 8, 5, 5);
        gc.fillOval(cookie.getX() + cookie.getWidth() / 2 + 1, cookie.getY() + 8, 5, 5);
    }

    /**
     * Jump action.
     */
    public void jump(Cookie cookie) {
        if (cookieState == CookieState.RUNNING && cookiePhysics.isOnGround()) {
            cookieState = CookieState.JUMPING;

            double jumpVelocity = Physics.JUMP_VELOCITY;
            if (cookie.getAbility() instanceof JumpBoostAbility boost) {
                jumpVelocity *= boost.getJumpVelocityMultiplier();
            }

            cookiePhysics.setVelocityY(jumpVelocity);
            currentFrames = jumpFrames;
            currentFrame = 0;
            frameTimer = 0;

            SoundManager.getInstance().playJumpSound();
        }
    }

    /**
     * Slide action.
     */
    public void slideDown(Cookie cookie) {
        if (cookieState == CookieState.RUNNING) {
            cookieState = CookieState.SLIDING;
            cookiePhysics.setHeight(Physics.SLIDE_H);
            cookiePhysics.setY(Physics.GROUND_Y - Physics.SLIDE_H);

            currentFrames = slideFrames;
            currentFrame = 0;
            frameTimer = 0;

            SoundManager.getInstance().playSlideSound();
        }
    }

    /**
     * Release slide.
     */
    public void releaseSlide(Cookie cookie) {
        if (cookieState == CookieState.SLIDING) {
            cookieState = CookieState.RUNNING;
            cookiePhysics.setHeight(Physics.NORMAL_H);
            cookiePhysics.setY(Physics.GROUND_Y - Physics.NORMAL_H);

            currentFrames = runFrames;
            currentFrame = 0;
            frameTimer = 0;
        }
    }

    /**
     * Kill the cookie.
     */
    public void die(Cookie cookie) {
        cookieState = CookieState.DEAD;
        currentFrames = deadFrames;
        currentFrame = 0;
        cookie.setAlive(false);

        SoundManager.getInstance().playGameOverSound();
    }

    /**
     * Decrease cookie HP.
     */
    public void decreaseHp(Cookie cookie, double amount) {
        cookie.setHp(Math.max(0, cookie.getHp() - amount));
    }

    /**
     * Heal cookie.
     */
    public void heal(Cookie cookie, double amount) {
        cookie.setHp(Math.min(cookie.getMaxHpValue(), cookie.getHp() + amount));
        healEffectTimer = 0.4;
    }

    /**
     * Set invincibility.
     */
    public void setInvincible(boolean value) {
        invincibilityTimer = value ? INVINCIBILITY_DURATION : 0;
    }

    /**
     * Check if invincible.
     */
    public boolean isInvincible() {
        return invincibilityTimer > 0;
    }

    /**
     * Get current state.
     */
    public CookieState getState() {
        return cookieState;
    }

    /**
     * Reset for new game.
     */
    public void resetGameplayState(Cookie cookie) {
        if (loadedCookie == null) return;
        initializeGameplayState();
    }

    // =========================
    // ANIMATABLE INTERFACE
    // =========================

    /**
     * Advances the animation to the next frame.
     * Cycles through the current animation frame array (4 frames per animation).
     */
    @Override
    public void nextFrame() {
        if (currentFrames != null) {
            currentFrame = (currentFrame + 1) % 4;
        }
    }

    /**
     * Sets the current animation by name.
     * Valid animation names: "RUN", "JUMP", "SLIDE", "DEAD"
     *
     * @param animationName The name of the animation
     */
    @Override
    public void setAnimation(String animationName) {
        if (animationName == null || currentFrames == null) return;

        switch (animationName.toUpperCase()) {
            case "RUN":
                currentFrames = runFrames;
                currentFrame = 0;
                frameTimer = 0;
                break;
            case "JUMP":
                currentFrames = jumpFrames;
                currentFrame = 0;
                frameTimer = 0;
                break;
            case "SLIDE":
                currentFrames = slideFrames;
                currentFrame = 0;
                frameTimer = 0;
                break;
            case "DEAD":
                currentFrames = deadFrames;
                currentFrame = 0;
                frameTimer = 0;
                break;
            default:
                // Ignore unknown animation names
                break;
        }
    }

    /**
     * Returns the index of the currently active animation frame.
     *
     * @return the current frame index (0-3)
     */
    @Override
    public int getCurrentFrame() {
        return currentFrame;
    }
}
