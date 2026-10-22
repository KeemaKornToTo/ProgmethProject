package gui.components;

import gui.graphics.FontLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Game Over pane - displays score, coins, and action buttons.
 * Extends Pane to manage background image and content layout.
 */
public class GameOverPane extends Pane {

    private Label scoreLbl;
    private Label coinsLbl;
    private ImageView retryImg;
    private ImageView homeImg;

    /**
     * Create a game over pane with background image and content.
     */
    public GameOverPane() {
        this.setMaxWidth(420);
        this.setMaxHeight(234);
        buildPane();
    }

    /**
     * Build the game over pane with all components.
     */
    private void buildPane() {
        // Background image
        try {
            var bgStream = getClass().getResourceAsStream("/Stages/GameOverPane.png");
            if (bgStream != null) {
                ImageView bgImage = new ImageView(new Image(bgStream));
                bgImage.setFitWidth(420);
                bgImage.setFitHeight(234);
                this.getChildren().add(bgImage);
            }
        } catch (Exception ignored) {}

        // Content VBox (385x177)
        VBox contentBox = new VBox(12);
        contentBox.setPrefWidth(385);
        contentBox.setPrefHeight(177);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

        // Position content at center (adjust these values later if needed)
        double x = (420 - 385) / 2;  // 17.5
        double y = (234 - 177) / 2;  // 28.5
        contentBox.setLayoutX(x);
        contentBox.setLayoutY(y);

        // Score label with custom font, stroke, and drop shadow
        scoreLbl = new Label("Score: 0");
        scoreLbl.setId("goScore");
        scoreLbl.setFont(FontLoader.getCookieRunBold(20));
        scoreLbl.setTextFill(Color.WHITE);
        scoreLbl.setStyle("-fx-effect:dropshadow(gaussian,#000000,4,0.6,1,1);");

        // Coins label with custom font, stroke, and drop shadow
        coinsLbl = new Label("Coins: 0");
        coinsLbl.setId("goCoins");
        coinsLbl.setFont(FontLoader.getCookieRunBold(20));
        coinsLbl.setTextFill(Color.WHITE);
        coinsLbl.setStyle("-fx-effect:dropshadow(gaussian,#000000,4,0.6,1,1);");

        // Try Again button (image) - 174x49
        retryImg = new ImageView();
        try {
            var retryStream = getClass().getResourceAsStream("/Stages/TryAgainButtonpng.png");
            if (retryStream != null) {
                retryImg = new ImageView(new Image(retryStream));
                retryImg.setFitWidth(174);
                retryImg.setFitHeight(49);
                retryImg.setPreserveRatio(true);
                retryImg.setStyle("-fx-cursor: hand;");
                setupButtonHoverEffects(retryImg);
            }
        } catch (Exception ignored) {}

        // Home button (image) - 174x49
        homeImg = new ImageView();
        try {
            var homeStream = getClass().getResourceAsStream("/Stages/HomeButton.png");
            if (homeStream != null) {
                homeImg = new ImageView(new Image(homeStream));
                homeImg.setFitWidth(174);
                homeImg.setFitHeight(49);
                homeImg.setPreserveRatio(true);
                homeImg.setStyle("-fx-cursor: hand;");
                setupButtonHoverEffects(homeImg);
            }
        } catch (Exception ignored) {}

        HBox buttonRow = new HBox(12, retryImg, homeImg);
        buttonRow.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        spacer.setPrefHeight(20);

        contentBox.getChildren().addAll(spacer, scoreLbl, coinsLbl, buttonRow);
        this.getChildren().add(contentBox);
    }

    /**
     * Setup hover effects for button images.
     */
    private void setupButtonHoverEffects(ImageView button) {
        button.setOnMouseEntered(e -> {
            button.setScaleX(1.1);
            button.setScaleY(1.1);
            button.setOpacity(0.9);
        });
        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
            button.setOpacity(1.0);
        });
    }

    /**
     * Set the action for the Try Again button.
     */
    public void setOnRetry(Runnable action) {
        retryImg.setOnMouseClicked(e -> action.run());
    }

    /**
     * Set the action for the Home button.
     */
    public void setOnHome(Runnable action) {
        homeImg.setOnMouseClicked(e -> action.run());
    }

    /**
     * Update the score label.
     */
    public void setScore(int score) {
        scoreLbl.setText("Score: " + score);
    }

    /**
     * Update the coins label.
     */
    public void setCoins(int coins) {
        coinsLbl.setText("Coins: " + coins);
    }
}
