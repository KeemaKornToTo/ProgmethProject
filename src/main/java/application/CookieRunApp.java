package application;

import core.entities.base.Cookie;
import game.GameController;
import game.managers.CookieManager;
import game.config.GameConfig;
import audio.SoundManager;

import gui.pages.CookieSelectView;
import gui.pages.GameStage;
import gui.pages.HomePageView;
import gui.pages.StageSelectView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class.
 *
 * Responsibilities:
 * - Start JavaFX application
 * - Switch between pages/scenes
 * - Start the game
 */
public class CookieRunApp extends Application {
    // Window size (from GameConfig)
    public static final int WIDTH  = GameConfig.WINDOW_WIDTH;
    public static final int HEIGHT = GameConfig.WINDOW_HEIGHT;

    // Main window
    private Stage primaryStage;

    // Selected game data
    private int selectedCookieIndex = 0;
    private int selectedStageIndex  = 0;

    /**
     * Called automatically when the application starts.
     */
    @Override
    public void start(Stage stage) {

        primaryStage = stage;

        stage.setTitle("VinZaynt's Cookie Run");

        stage.setResizable(GameConfig.RESIZABLE);

        // Initialize all game sounds
        SoundManager.getInstance().initializeSounds();

        showHomePage();

        stage.show();
    }

    /**
     * Show home page.
     */
    public void showHomePage() {

        setScene(
                new HomePageView(this)
        );
    }

    /**
     * Show cookie selection page.
     */
    public void showCookieSelect() {

        setScene(
                new CookieSelectView(this)
        );
    }

    /**
     * Show stage selection page.
     */
    public void showStageSelection() {

        setScene(
                new StageSelectView(this)
        );
    }

    /**
     * Start the game with selected cookie and stage.
     */
    public void startGame(
            int cookieIndex,
            int stageIndex
    ) {

        selectedStageIndex = stageIndex;

        Cookie cookie =
                CookieManager.getInstance().loadCookie(cookieIndex);

        GameStage gameStage =
                new GameStage(
                        this,
                        cookieIndex,
                        cookie
                );

        setScene(gameStage);

        // Start game loop
        gameStage.startGame();
    }

    /**
     * Start game using previously selected stage.
     */
    public void startGame(int cookieIndex) {

        startGame(
                cookieIndex,
                selectedStageIndex
        );
    }

    /**
     * Helper method for switching scenes.
     */
    private void setScene(javafx.scene.Parent root) {

        Scene scene =
                new Scene(
                        root,
                        WIDTH,
                        HEIGHT
                );

        primaryStage.setScene(scene);
    }

    // =========================
    // GETTERS / SETTERS
    // =========================

    public int getSelectedCookieIndex() {

        return selectedCookieIndex;
    }

    public void setSelectedCookieIndex(int index) {

        selectedCookieIndex = index;
    }

    public int getSelectedStageIndex() {

        return selectedStageIndex;
    }

    public void setSelectedStageIndex(int index) {

        selectedStageIndex = index;
    }

    /**
     * Program entry point.
     */
    public static void main(String[] args) {

        launch(args);
    }
}
