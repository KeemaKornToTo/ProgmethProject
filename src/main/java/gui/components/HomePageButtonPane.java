package gui.components;

import application.CookieRunApp;
import audio.SoundManager;
import gui.baseElements.BaseImageButton;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Button pane for home page.
 * Displays a single "Let's Go" button that leads to cookie selection.
 */
public class HomePageButtonPane extends VBox {

    private final CookieRunApp app;

    public HomePageButtonPane(CookieRunApp app) {
        this.app = app;
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Create the Let's Go button.
     * Note: letsGoButtonImg parameter is no longer used.
     */
    public void createButtons(Image letsGoButtonImg, Image unused) {
        // Let's Go button (gold glow)
        BaseImageButton letsGoButton = new BaseImageButton(
            "/HomePage/letsgoButton.png",
            "#FFD700"
        );
        letsGoButton.setOnMouseClicked(e -> {
            SoundManager.getInstance().playClickSound();
            app.showCookieSelect();
        });

        // Add button to this pane
        this.getChildren().add(letsGoButton);
    }
}
