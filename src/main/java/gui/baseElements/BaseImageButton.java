package gui.baseElements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * Base image button class - reusable button component with hover effects.
 *
 * Features:
 * - Loads button image from resources
 * - Scaling on hover
 * - Customizable glow effect
 * - Standard button dimensions
 * - Hand cursor styling
 * - Opacity changes
 *
 * Can be used in two ways:
 * 1. Direct instantiation: new BaseImageButton("/path/to/image.png", "#FFD700")
 * 2. Subclassing: Override getImagePath() and getGlowColor() in subclass
 *
 * Subclasses can customize:
 * - Resource path (image location)
 * - Glow color
 * - Scale factor
 * - Dimensions
 */
public class BaseImageButton extends ImageView {

    protected static final double DEFAULT_SCALE = 1.1;
    protected static final double DEFAULT_WIDTH = 174;
    protected static final double DEFAULT_HEIGHT = 49;
    protected static final double DEFAULT_HOVER_OPACITY = 1.0;
    protected static final double DEFAULT_NORMAL_OPACITY = 0.9;

    protected DropShadow glowEffect;
    protected double hoverScale = DEFAULT_SCALE;
    protected String imagePath;
    protected String glowColor;

    /**
     * Create a base image button with specified image path and glow color.
     * This constructor allows direct instantiation without subclassing.
     *
     * @param imagePath Resource path to the button image (e.g., "/CookieSelectionPane/PlayButton.png")
     * @param glowColor Hex color for the glow effect (e.g., "#FFD700" for gold)
     */
    public BaseImageButton(String imagePath, String glowColor) {
        this.imagePath = imagePath;
        this.glowColor = glowColor;
        initializeButton();
    }

    /**
     * Create a base image button for subclassing.
     * Subclasses must override getImagePath() and getGlowColor()
     */
    protected BaseImageButton() {
        initializeButton();
    }

    /**
     * Initialize button with image and effects.
     */
    private void initializeButton() {
        try {
            // Load image from subclass-defined path
            String imagePath = getImagePath();
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            this.setImage(image);

            // Set default dimensions
            this.setFitWidth(getButtonWidth());
            this.setFitHeight(getButtonHeight());
            this.setPreserveRatio(true);
            this.setStyle("-fx-cursor: hand;");

            // Setup glow effect with subclass-defined color
            setupGlowEffect();

            // Setup hover effects
            setupHoverEffects();
        } catch (Exception e) {
            System.err.println("Failed to load button image: " + e.getMessage());
        }
    }

    /**
     * Setup the glow effect with the button's color.
     */
    protected void setupGlowEffect() {
        glowEffect = new DropShadow();
        glowEffect.setColor(Color.web(getGlowColor(), getGlowOpacity()));
        glowEffect.setRadius(15);
        glowEffect.setSpread(0.5);
    }

    /**
     * Setup hover effects.
     */
    protected void setupHoverEffects() {
        this.setOnMouseEntered(e -> onHoverEnter());
        this.setOnMouseExited(e -> onHoverExit());
    }

    /**
     * Called when mouse enters button.
     */
    protected void onHoverEnter() {
        this.setScaleX(hoverScale);
        this.setScaleY(hoverScale);
        this.setEffect(glowEffect);
        this.setOpacity(DEFAULT_HOVER_OPACITY);
    }

    /**
     * Called when mouse exits button.
     */
    protected void onHoverExit() {
        this.setScaleX(1.0);
        this.setScaleY(1.0);
        this.setEffect(null);
        this.setOpacity(DEFAULT_NORMAL_OPACITY);
    }

    // ===========================
    // CUSTOMIZATION METHODS - Can override in subclasses
    // ===========================

    /**
     * Get the resource path to the button image.
     * Example: "/CookieSelectionPane/PlayButton.png"
     *
     * If created with constructor(imagePath, glowColor), returns the provided imagePath.
     * If subclassing, override this method to provide custom path.
     */
    protected String getImagePath() {
        if (imagePath != null) {
            return imagePath;
        }
        throw new IllegalStateException(
            "imagePath not set. Either use constructor(imagePath, glowColor) or override getImagePath()"
        );
    }

    /**
     * Get the hex color for the glow effect.
     * Example: "#FFD700" for gold
     *
     * If created with constructor(imagePath, glowColor), returns the provided glowColor.
     * If subclassing, override this method to provide custom color.
     */
    protected String getGlowColor() {
        if (glowColor != null) {
            return glowColor;
        }
        throw new IllegalStateException(
            "glowColor not set. Either use constructor(imagePath, glowColor) or override getGlowColor()"
        );
    }

    // ===========================
    // SIZE & EFFECT CUSTOMIZATION - Subclasses can override
    // ===========================

    /**
     * Get button width. Override to customize.
     */
    protected double getButtonWidth() {
        return DEFAULT_WIDTH;
    }

    /**
     * Get button height. Override to customize.
     */
    protected double getButtonHeight() {
        return DEFAULT_HEIGHT;
    }

    /**
     * Get hover scale factor. Override to customize.
     */
    protected double getHoverScale() {
        return DEFAULT_SCALE;
    }

    /**
     * Get glow effect opacity. Override to customize.
     */
    protected double getGlowOpacity() {
        return 0.8;
    }
}
