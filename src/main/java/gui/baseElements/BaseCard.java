package gui.baseElements;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Base card class - HBox container with left and right panes.
 * Provides common card styling, selection state, and hover effects.
 * Subclasses populate leftPane and rightPane with their content.
 */
public class BaseCard extends VBox {

    protected boolean selected = false;
    protected VBox upPane;
    protected HBox downPane;
    protected String selectedStyle;
    protected String normalStyle;
    protected String hoverStyle;
    protected double hoverScale = 1.1;

    /**
     * Create a base card with custom dimensions.
     * @param width card width
     * @param height card height
     */
    public BaseCard(double width, double height) {
        super(15);
        this.setPrefSize(width, height);
        initializePanes(width, height);
    }

    /**
     * Initialize the left and right panes.
     */
    protected void initializePanes(double width, double height) {
        // Left pane
        upPane = new VBox(8);
        upPane.setAlignment(Pos.CENTER);
        upPane.setPrefWidth(width * 0.20);
        upPane.setStyle("-fx-background-color: transparent;");

        // Right pane
        downPane = new HBox(10);
        downPane.setAlignment(Pos.TOP_LEFT);
        downPane.setPrefWidth(width * 0.80);
        downPane.setStyle("-fx-background-color: transparent;");

        // Add panes to this HBox
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-padding: 12; -fx-background-color: transparent;");
        this.getChildren().addAll(upPane, downPane);

        // Initialize styling
        initializeDefaults();
    }

    /**
     * Initialize default styling.
     * Uses black glass-like effect with translucent dark background.
     */
    protected void initializeDefaults() {
        // Default style - Black glass effect
        this.normalStyle = """
            -fx-background-color: rgba(15, 15, 20, 0.7);
            -fx-background-radius: 12;
            -fx-border-color: rgba(150, 150, 200, 0.3);
            -fx-border-width: 1.5;
            -fx-border-radius: 12;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 8, 0.5, 2, 2);
            """;

        // Selected style - Black glass with subtle golden border (no glow)
        this.selectedStyle = """
            -fx-background-color: rgba(20, 20, 25, 0.8);
            -fx-background-radius: 12;
            -fx-border-color: rgba(200, 180, 100, 0.7);
            -fx-border-width: 2;
            -fx-border-radius: 12;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 8, 0.5, 1, 1);
            """;

        // Hover style - Slightly brighter with subtle blue tint (no yellow)
        this.hoverStyle = """
            -fx-background-color: rgba(25, 25, 35, 0.8);
            -fx-background-radius: 12;
            -fx-border-color: rgba(150, 170, 220, 0.6);
            -fx-border-width: 1.5;
            -fx-border-radius: 12;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 6, 0.5, 1, 1);
            """;

        applyNormalStyle();
        setupHoverEffects();
    }

    /**
     * Setup hover effects.
     */
    protected void setupHoverEffects() {
        this.setOnMouseEntered(e -> onHoverEnter());
        this.setOnMouseExited(e -> onHoverExit());
    }

    /**
     * Called when mouse enters card.
     */
    protected void onHoverEnter() {
        this.setScaleX(hoverScale);
        this.setScaleY(hoverScale);
        // Only show hover style if NOT selected
        if (!selected) {
            this.setStyle(hoverStyle);
        }
    }

    /**
     * Called when mouse exits card.
     */
    protected void onHoverExit() {
        this.setScaleX(1.0);
        this.setScaleY(1.0);
        // Always return to current style (selected or normal)
        applyCurrentStyle();
    }

    /**
     * Set selection state.
     * @param selected whether card is selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        applyCurrentStyle();
    }
    /**
     * Apply normal style.
     */
    protected void applyNormalStyle() {
        this.setStyle(normalStyle);
    }

    /**
     * Apply selected style.
     */
    protected void applySelectedStyle() {
        this.setStyle(selectedStyle);
    }

    /**
     * Apply the appropriate style based on selection state.
     */
    protected void applyCurrentStyle() {
        if (selected) {
            applySelectedStyle();
        } else {
            applyNormalStyle();
        }
    }

    /**
     * Apply hover style without changing selection state.
     * Useful for initial UI state setup.
     */
    public void applyHoverStyle() {
        this.setStyle(hoverStyle);
        this.setScaleX(hoverScale);
        this.setScaleY(hoverScale);
    }

    /**
     * Reset to normal style and scale.
     */
    public void resetHoverStyle() {
        this.setScaleX(1.0);
        this.setScaleY(1.0);
        applyCurrentStyle();
    }
}
