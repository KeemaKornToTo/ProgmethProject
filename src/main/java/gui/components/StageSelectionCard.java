package gui.components;

import core.stages.Stage;
import gui.baseElements.BaseCard;
import gui.graphics.FontLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Stage selection card - displays stage information.
 * Extends BaseCard with up/down pane structure.
 */
public class StageSelectionCard extends BaseCard {

    private final Stage stage;
    private StackPane logoPane;

    public StageSelectionCard(Stage stage, int index) {
        super(240, 200);
        this.stage = stage;
        buildCard();
    }

    /**
     * Build the card content.
     */
    private void buildCard() {
        // Up pane: Stage name
        Label nameLabel = new Label(stage.getDisplayName());
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

        // Down pane: Logo on left + World info on right
        StackPane logoPane = buildLogoPane();

        // Right side: World info
        javafx.scene.layout.VBox rightContent = new javafx.scene.layout.VBox(2);
        rightContent.setAlignment(Pos.TOP_LEFT);
        rightContent.setPrefWidth(160);

        // Difficulty label header
        Label diffHeaderLabel = new Label("Difficulty:");
        diffHeaderLabel.setFont(FontLoader.getCookieRunBold(10));
        diffHeaderLabel.setTextFill(Color.WHITE);
        diffHeaderLabel.setAlignment(Pos.CENTER_LEFT);
        diffHeaderLabel.setStyle("""
            -fx-text-alignment: left;
            -fx-effect: dropshadow(gaussian, #000000, 3, 0.5, 0.5, 0.5);
            """);

        // Difficulty value with color gradient (green to red)
        Label diffValueLabel = new Label(stage.getDifficulty());
        diffValueLabel.setFont(FontLoader.getCookieRunBold(11));
        Color diffColor = getDifficultyColor(stage.getDifficulty());
        diffValueLabel.setTextFill(diffColor);
        diffValueLabel.setAlignment(Pos.CENTER_LEFT);
        diffValueLabel.setStyle("""
            -fx-text-alignment: left;
            -fx-effect: dropshadow(gaussian, #000000, 3, 0.5, 0.5, 0.5);
            """);

        // Description label
        Label descLabel = new Label(stage.getDescription());
        descLabel.setFont(FontLoader.getCookieRunBold(8));
        descLabel.setTextFill(Color.WHITE);
        descLabel.setAlignment(Pos.TOP_LEFT);
        descLabel.setStyle("""
            -fx-text-alignment: left;
            -fx-wrap-text: true;
            -fx-effect: dropshadow(gaussian, #000000, 2, 0.4, 0.5, 0.5);
            """);
        descLabel.setMaxWidth(150);
        descLabel.setWrapText(true);

        rightContent.getChildren().addAll(diffHeaderLabel, diffValueLabel, descLabel);

        // Add logo and content to down pane
        downPane.getChildren().addAll(logoPane, rightContent);
    }

    /**
     * Build the logo pane (70x70).
     */
    private StackPane buildLogoPane() {
        logoPane = new StackPane();
        logoPane.setPrefSize(70, 70);

        // Placeholder - will be replaced with actual jelly logo
        javafx.scene.shape.Rectangle placeholder = new javafx.scene.shape.Rectangle(70, 70);
        placeholder.setFill(Color.web("#FFD700"));
        logoPane.getChildren().add(placeholder);

        // Try to load the jelly logo image
        try {
            var stream = getClass().getResourceAsStream(stage.getJellyLogoPath());
            if (stream != null) {
                Image logo = new Image(stream);
                if (logo != null && !logo.isError()) {
                    ImageView iv = new ImageView(logo);
                    iv.setFitWidth(70);
                    iv.setFitHeight(70);
                    iv.setPreserveRatio(true);
                    logoPane.getChildren().setAll(iv);
                }
            }
        } catch (Exception ignored) {}

        return logoPane;
    }
    /**
     * Get color for difficulty level (green to red gradient).
     */
    private Color getDifficultyColor(String difficulty) {
        return switch (difficulty) {
            case "Easy" -> Color.web("#2ECC71");          // Green
            case "Decent" -> Color.web("#F1C40F");        // Yellow
            case "Normal" -> Color.web("#E67E22");        // Orange
            case "Hard" -> Color.web("#E74C3C");          // Light Red
            case "CP" -> Color.web("#C0392B");  // Dark Red
            default -> Color.WHITE;
        };
    }
}
