package gui.pages;

import application.CookieRunApp;
import gui.graphics.BaseBackgroundPane;
import gui.graphics.FontLoader;
import audio.SoundManager;
import gui.baseElements.BaseImageButton;
import gui.components.CookieCardsRowPane;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Text;


/**
 * Cookie Selection view - pick a cookie, then proceed to stage selection.
 * Extends StackPane to manage background and content layers.
 * Displays all available cookies with animated background.
 * Navigation flow: Home → Cookie Selection → Stage Selection → Game
 */
public class CookieSelectView extends StackPane {

    private final CookieCardsRowPane cookieCardsRowPane;
    private final BaseBackgroundPane backgroundPane;

    /**
     * Create the cookie selection view.
     * @param app The main application instance for navigation
     */
    public CookieSelectView(CookieRunApp app) {
        this.backgroundPane = new BaseBackgroundPane("/CookieSelectionPane/MagicalBg.png", 0.5);
        this.getChildren().add(backgroundPane);

        // Page title with background image
        StackPane headingPane = new StackPane();
        headingPane.setPrefHeight(80);
        headingPane.setAlignment(Pos.CENTER);

        // Background image
        var bgStream = getClass().getResourceAsStream("/CookieSelectionPane/anotherbutton.png");
        Image bgImage = new Image(bgStream);
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitHeight(60);
        bgImageView.setPreserveRatio(true);
        headingPane.getChildren().add(bgImageView);

        // Text with black outline
        Text heading = new Text("SELECT YOUR COOKIE");
        heading.setFont(FontLoader.getCookieRunBold(25));
        heading.setFill(Color.WHITE);
        heading.setStroke(Color.BLACK);
        heading.setStrokeWidth(1.7);
        heading.setStyle("-fx-effect: dropshadow(gaussian, #000000, 0, 0.6, 1.3, 1.4);");
        headingPane.getChildren().add(heading);

        // Cookie cards row pane (initialize with previously selected cookie)
        cookieCardsRowPane = new CookieCardsRowPane(app.getSelectedCookieIndex());

        // Play button (gold glow)
        BaseImageButton play = new BaseImageButton(
            "/CookieSelectionPane/PlayButton.png",
            "#FFD700"
        );
        play.setOnMouseClicked(e  -> {
            SoundManager.getInstance().playClickSound();
            int selectedIndex = cookieCardsRowPane.getSelectedIndex();
            app.setSelectedCookieIndex(selectedIndex);
            app.showStageSelection();
        });

        // Back button (blue glow)
        BaseImageButton back = new BaseImageButton(
            "/CookieSelectionPane/BackButton.png",
            "#C3CBF4"
        );
        back.setOnMouseClicked(e -> {
            SoundManager.getInstance().playClickSound();
            app.showHomePage();
        });

        HBox buttons = new HBox(16, back, play);
        buttons.setAlignment(Pos.CENTER);
        VBox content = new VBox(18, headingPane, cookieCardsRowPane, buttons);
        content.setAlignment(Pos.CENTER);

        this.getChildren().add(content);

        // Play selection screen music (continues if already playing)
        SoundManager.getInstance().playBackgroundMusicIfNotPlaying("selection");
    }
}
