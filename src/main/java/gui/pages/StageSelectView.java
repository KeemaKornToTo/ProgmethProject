package gui.pages;

import application.CookieRunApp;
import gui.graphics.BaseBackgroundPane;
import gui.baseElements.BaseImageButton;
import gui.components.StagesCardRowPane;
import gui.graphics.FontLoader;
import audio.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Stage Selection view - pick a stage, then start the game.
 * Extends StackPane to manage background and content layers.
 * Displays all available stages with animated background.
 * Navigation flow: Home → Cookie Selection → Stage Selection → Game
 */
public class StageSelectView extends StackPane {

    private final StagesCardRowPane stagesRowPane;
    private final BaseBackgroundPane backgroundPane;

    /**
     * Create the stage selection view.
     * @param app The main application instance for navigation
     */
    public StageSelectView(CookieRunApp app) {
        this.backgroundPane = new BaseBackgroundPane("/CookieSelectionPane/MagicalBg.png", 0.5);
        this.getChildren().add(backgroundPane);

        // Page title with background image
        StackPane headingPane = new StackPane();
        headingPane.setPrefHeight(80);
        headingPane.setAlignment(Pos.CENTER);

        // Background image
        try {
            var bgStream = getClass().getResourceAsStream("/CookieSelectionPane/anotherbutton.png");
            if (bgStream != null) {
                Image bgImage = new Image(bgStream);
                ImageView bgImageView = new ImageView(bgImage);
                bgImageView.setFitHeight(60);
                bgImageView.setPreserveRatio(true);
                headingPane.getChildren().add(bgImageView);
            }
        } catch (Exception ignored) {}

        // Text with black outline
        Text heading = new Text("SELECT YOUR STAGE");
        heading.setFont(FontLoader.getCookieRunBold(25));
        heading.setFill(Color.WHITE);
        heading.setStroke(Color.BLACK);
        heading.setStrokeWidth(1.7);
        heading.setStyle("-fx-effect: dropshadow(gaussian, #000000, 0, 0.6, 1.3, 1.4);");
        headingPane.getChildren().add(heading);

        // Stage cards row pane (initialize with previously selected stage)
        stagesRowPane = new StagesCardRowPane(app.getSelectedStageIndex());

        // Play button (gold glow)
        BaseImageButton play = new BaseImageButton(
            "/CookieSelectionPane/PlayButton.png",
            "#FFD700"
        );
        play.setOnMouseClicked(e -> {
            SoundManager.getInstance().playClickSound();
            int selectedStage = stagesRowPane.getSelectedIndex();
            app.setSelectedStageIndex(selectedStage);
            app.startGame(app.getSelectedCookieIndex(), selectedStage);
        });

        // Back button (blue glow)
        BaseImageButton back = new BaseImageButton(
            "/CookieSelectionPane/BackButton.png",
            "#C3CBF4"
        );
        back.setOnMouseClicked(e -> {
            SoundManager.getInstance().playClickSound();
            app.showCookieSelect();
        });

        HBox buttons = new HBox(16, back, play);
        buttons.setAlignment(Pos.CENTER);
        VBox content = new VBox(18, headingPane, stagesRowPane, buttons);
        content.setAlignment(Pos.CENTER);

        // Add content to this StackPane
        this.getChildren().add(content);

        // Play selection screen music (continues if already playing)
        SoundManager.getInstance().playBackgroundMusicIfNotPlaying("selection");
    }
}
