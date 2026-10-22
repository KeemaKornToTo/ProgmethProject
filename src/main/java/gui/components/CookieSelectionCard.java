package gui.components;

import core.entities.base.Cookie;
import gui.baseElements.BaseCard;
import gui.graphics.FontLoader;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Cookie selection card - uses BaseCard's left/right pane structure.
 * Left pane: Cookie icon
 * Right pane: Description and stats
 * Size: 300x200
 */
public class CookieSelectionCard extends BaseCard {

    private final Cookie cookie;
    private final int index;
    private final int originalIndex;  // Original index from CookieList
    private StackPane iconPane;

    public CookieSelectionCard(Cookie cookie, int index, int originalIndex, boolean selected) {
        super(240, 200);
        this.cookie = cookie;
        this.index = index;
        this.originalIndex = originalIndex;
        this.selected = selected;
        buildCard();
    }

    /**
     * Build the card content - populate up and down panes.
     */
    private void buildCard() {
        // Up pane: Cookie name
        Label nameLabel = new Label(cookie.getDisplayName());
        nameLabel.setFont(FontLoader.getCookieRunBold(14));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setStyle("""
            -fx-text-alignment: center;
            -fx-wrap-text: true;
            -fx-effect: dropshadow(gaussian, #000000, 4, 0.6, 1, 1);
            """);
        nameLabel.setMaxWidth(240);
        upPane.getChildren().add(nameLabel);

        // Down pane: Cookie icon on left + HP/ability info on right
        StackPane iconPane = buildIconPane();

        // Right side: HP, ability, description
        javafx.scene.layout.VBox rightContent = new javafx.scene.layout.VBox(6);
        rightContent.setAlignment(Pos.TOP_LEFT);
        rightContent.setPrefWidth(160);

        Label tierLabel = new Label("Tier: " + cookie.getTier());
        tierLabel.setFont(FontLoader.getCookieRunBold(11));
        // Color code tiers
        String tierColor = switch(cookie.getTier()) {
            case "S" -> "#FF6B6B";  // Red for S-tier
            case "A" -> "#FFD93D";  // Gold for A-tier
            case "B" -> "#2ECC71";  // Green for B-tier
            default -> "#A8DADC";   // Gray for C-tier and below
        };
        tierLabel.setTextFill(Color.web(tierColor));
        tierLabel.setAlignment(Pos.CENTER_LEFT);
        tierLabel.setStyle("""
            -fx-text-alignment: left;
            -fx-effect: dropshadow(gaussian, #000000, 3, 0.5, 0.5, 0.5);
            """);

        Label hpLabel = new Label("HP: " + cookie.getMaxHpValue());
        hpLabel.setFont(FontLoader.getCookieRunBold(11));
        hpLabel.setTextFill(Color.web("#9AD815"));
        hpLabel.setAlignment(Pos.CENTER_LEFT);
        hpLabel.setStyle("""
            -fx-text-alignment: left;
            -fx-effect: dropshadow(gaussian, #000000, 3, 0.5, 0.5, 0.5);
            """);

        Label abilityLabel = new Label("Ability:");
        abilityLabel.setFont(FontLoader.getCookieRunBold(10));
        abilityLabel.setTextFill(Color.WHITE);
        abilityLabel.setAlignment(Pos.CENTER_LEFT);
        abilityLabel.setStyle("""
            -fx-text-alignment: left;
            -fx-effect: dropshadow(gaussian, #000000, 3, 0.5, 0.5, 0.5);
            """);

        Label descLabel = new Label(cookie.getCookieAbilityDescription());
        descLabel.setFont(FontLoader.getCookieRunBold(9));
        descLabel.setTextFill(Color.WHITE);
        descLabel.setAlignment(Pos.TOP_LEFT);
        descLabel.setStyle("""
            -fx-text-alignment: left;
            -fx-wrap-text: true;
            -fx-effect: dropshadow(gaussian, #000000, 2, 0.4, 0.5, 0.5);
            """);
        descLabel.setMaxWidth(150);
        descLabel.setWrapText(true);

        // Add small spacer for modest spacing
        Region smallSpacer = new Region();
        smallSpacer.setPrefHeight(4);
        rightContent.getChildren().addAll(smallSpacer, tierLabel, hpLabel, abilityLabel, descLabel);

        // Add icon and content to down pane
        downPane.getChildren().addAll(iconPane, rightContent);
    }

    /**
     * Build the icon pane (100x100 with fallback).
     */
    private StackPane buildIconPane() {
        iconPane = new StackPane();
        iconPane.setPrefSize(70, 70);

        // Fallback canvas
        Canvas fallback = new Canvas(70, 70);
        GraphicsContext gc = fallback.getGraphicsContext2D();
        gc.setFill(Color.web(cookie.getHex()));
        gc.fillRoundRect(2, 2, 66, 66, 10, 10);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Impact", 24));
        gc.fillText(cookie.getDisplayName().substring(0, 1), 24, 45);

        iconPane.getChildren().add(fallback);

        // Try to load the icon image
        try {
            String iconPath = cookie.getIconPath();
            if (iconPath.startsWith("/")) {
                iconPath = iconPath.substring(1);
            }
            var stream = getClass().getResourceAsStream("/" + iconPath);
            if (stream == null) {
                stream = getClass().getResourceAsStream(iconPath);
            }

            if (stream != null) {
                Image icon = new Image(stream);
                if (icon != null && !icon.isError()) {
                    ImageView iv = new ImageView(icon);
                    iv.setFitWidth(70);
                    iv.setFitHeight(70);
                    iv.setPreserveRatio(true);
                    iconPane.getChildren().setAll(iv);
                }
            }
        } catch (Exception ignored) {}

        return iconPane;
    }

    public Cookie getCookie() {return cookie;}

    public int getOriginalIndex() {return originalIndex;}
}
