package gui.pages;

import application.CookieRunApp;

import core.abilities.CookieAbility;
import core.stages.StageList;

import game.GameController;
import game.config.GameConfig;

import gui.graphics.ScrollingLayer;

import gui.components.GameOverPane;
import gui.graphics.FontLoader;

import gamelogic.events.EventBus;
import gamelogic.events.CoinCollectedEvent;
import gamelogic.events.HealthChangedEvent;
import gamelogic.events.ObstacleHitEvent;

import utils.Renderable;
import utils.Updatable;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;

import javafx.geometry.Pos;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Button;

import javafx.scene.image.Image;

import javafx.scene.input.KeyCode;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Main gameplay scene.
 * Handles rendering, input, HUD, and overlays.
 */
public class GameStage extends StackPane {

    // =========================
    // CONSTANTS
    // =========================

    private static final double W =
            CookieRunApp.WIDTH;

    private static final double H =
            CookieRunApp.HEIGHT;

    private static final double ABILITY_SIZE = 90;

    private static final double ABILITY_MARGIN = 120;

    // =========================
    // GAME STATE
    // =========================

    private final CookieRunApp app;
    private final GameController ctrl;
    private final Canvas canvas;
    private final int cookieIndex;
    private final ScrollingLayer bgLayer;
    private final Button menuButton;
    private long lastNano;
    private boolean gameOverShown = false;

    // ===== FPS COUNTER =====
    private int frameCounter = 0;
    private long lastSecond = System.currentTimeMillis();
    private double displayedFps = GameConfig.TARGET_FPS;
    // =======================

    // ===== INPUT BUFFERING (Reduce Input Lag) =====
    private volatile boolean jumpPressed = false;
    private volatile boolean slidePressed = false;
    private volatile boolean slideReleased = false;
    private volatile boolean abilityPressed = false;
    // ==============================================

    private final List<Updatable> updatables = new ArrayList<>();
    private final List<Renderable> renderables = new ArrayList<>();

    // =========================
    // CONSTRUCTOR
    // =========================

    public GameStage(
            CookieRunApp app,
            int cookieIndex,
            core.entities.base.Cookie cookie
    ) {

        this.app = app;

        this.cookieIndex = cookieIndex;

        core.stages.Stage stage =
                StageList.getStage(
                        app.getSelectedStageIndex()
                );

        ctrl = new GameController(cookie, stage);

        canvas = new Canvas(W, H);

        bgLayer =
                new ScrollingLayer(
                        W,
                        0,
                        H,
                        80
                );

        updatables.add(ctrl);
        updatables.add(bgLayer);
        renderables.add(bgLayer);
        renderables.add(ctrl);
        menuButton = createMenuButton();
        getChildren().addAll(
                canvas,
                menuButton
        );
        setFocusTraversable(true);
        loadImages();
        bindInput();
        setupEventListeners();
    }

    // =========================
    // INITIALIZATION
    // =========================

    private Button createMenuButton() {

        Button button =
                new Button("Main Menu");

        button.setVisible(false);

        button.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(#FFD95A,#F4B400);" +
                        "-fx-text-fill: #2B1B00;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: #FFF3B0;" +
                        "-fx-border-width: 2;" +
                        "-fx-padding: 12 30 12 30;"
        );

        button.setOnAction(
                e -> app.showHomePage()
        );

        StackPane.setAlignment(
                button,
                Pos.CENTER
        );

        button.setTranslateY(80);

        return button;
    }

    private void loadImages() {

        try {

            var stage =
                    StageList.getStage(
                            app.getSelectedStageIndex()
                    );

            var bgStream =
                    getClass().getResourceAsStream(
                            stage.getPlayBackgroundPath()
                    );

            if (bgStream != null) {

                bgLayer.setImage(
                        new Image(bgStream)
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Background load failed: "
                            + e.getMessage()
            );
        }
    }

    // =========================
    // INPUT
    // =========================

    private void bindInput() {

        requestFocus();

        setOnKeyPressed(
                e -> handleKeyPressed(e.getCode())
        );

        setOnKeyReleased(
                e -> handleKeyReleased(e.getCode())
        );

        canvas.setOnMouseClicked(e -> {

            if (ctrl.isGameOver()) {
                return;
            }

            if (e.getY() < H / 2) {
                ctrl.onJump();
            }

            else {
                ctrl.onSlide();
            }
        });

        canvas.setOnMouseReleased(
                e -> ctrl.onReleaseSlide()
        );
    }

    private void handleKeyPressed(KeyCode key) {

        switch (key) {

            case SPACE, UP -> {
                jumpPressed = true;
            }

            case DOWN, S -> {
                slidePressed = true;
            }

            case E -> {
                abilityPressed = true;
            }

            case P ->
                    ctrl.togglePause();
        }
    }

    private void handleKeyReleased(KeyCode key) {

        if (key == KeyCode.DOWN
                || key == KeyCode.S) {

            slideReleased = true;
        }
    }

    // =========================
    // EVENT LISTENERS
    // =========================

    /**
     * Setup EventBus listeners for game events.
     * Events are published by game managers and handled here for UI updates.
     */
    private void setupEventListeners() {
        EventBus eventBus = EventBus.getInstance();

        // Listen for coin collection
        eventBus.subscribe("COIN_COLLECTED", event -> {
            CoinCollectedEvent e = (CoinCollectedEvent) event;
            // Event already triggered sound effect in CollectibleManager
            // This listener can be used for visual effects or logging
            System.out.println("Coin collected! Value: " + e.getCoinValue());
        });

        // Listen for health changes
        eventBus.subscribe("HEALTH_CHANGED", event -> {
            HealthChangedEvent e = (HealthChangedEvent) event;
            // Event already triggered sound effect in HealthManager
            // HUD updates happen in drawHPBar() which reads from GameController
            System.out.println("Health changed from " + e.getOldHealth() + " to " + e.getNewHealth() +
                    " (" + e.getReason() + ")");
        });

        // Listen for obstacle hits
        eventBus.subscribe("OBSTACLE_HIT", event -> {
            ObstacleHitEvent e = (ObstacleHitEvent) event;
            // Event already triggered camera shake and sound in ObstacleManager
            // Red overlay animation happens in drawRedOverlay()
            System.out.println("Hit by " + e.getObstacleType() + "! Damage: " + e.getDamageAmount());
        });
    }

    // =========================
    // GAME LOOP
    // =========================

    public void startGame() {

        new AnimationTimer() {

            @Override
            public void handle(long now) {

                if (lastNano == 0) {

                    lastNano = now;

                    return;
                }

                double delta = (now - lastNano) / 1_000_000_000.0;

                // Cap delta to avoid huge jumps during lag spikes (e.g., max 0.1s or 10fps)
                if (delta > 0.1) delta = 0.1;

                lastNano = now;

                update(delta);

                render();

                // ===== FPS COUNTER UPDATE =====
                frameCounter++;
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastSecond >= 1000) {
                    displayedFps = frameCounter;
                    frameCounter = 0;
                    lastSecond = currentTime;
                    System.out.println("FPS: " + String.format("%.1f", displayedFps));
                }
                // ===============================

                if (ctrl.isGameOver()
                        && !gameOverShown) {

                    gameOverShown = true;

                    showGameOver();

                    stop();
                }
            }

        }.start();

        requestFocus();
    }

    private void update(double delta) {
        // ===== PROCESS INPUT BUFFER (Reduce Input Lag) =====
        if (jumpPressed) {
            ctrl.onJump();
            jumpPressed = false;
        }
        if (slidePressed) {
            ctrl.onSlide();
            slidePressed = false;
        }
        if (slideReleased) {
            ctrl.onReleaseSlide();
            slideReleased = false;
        }
        if (abilityPressed) {
            CookieAbility ability = ctrl.getCookie().getAbility();
            if (ability != null && !ability.isPassive()) {
                ctrl.onAbility();
            }
            abilityPressed = false;
        }
        // ====================================================

        for (Updatable u : updatables) {
            u.update(delta);
        }
    }

    // =========================
    // RENDER
    // =========================

    private void render() {

        GraphicsContext gc =
                canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, W, H);

        applyCameraShake(gc);

        for (Renderable r : renderables) {
            r.render(gc);
        }

        drawHUD(gc);

        drawAbilityHUD(gc);

        if (ctrl.isPaused()) {

            drawPauseOverlay(gc);

            menuButton.setVisible(true);
        }

        else {

            menuButton.setVisible(false);
        }

        drawRedOverlay(gc);

        // ===== DRAW FPS COUNTER =====
        if (GameConfig.SHOW_FPS) {
            gc.setFont(Font.font("Arial", 11));
            gc.setFill(Color.web("#FFFFFF", 0.7));  // Semi-transparent white
            gc.fillText("FPS: " + String.format("%.1f", displayedFps), 15, 435);  // Bottom-left corner
        }
        // =============================

        gc.restore();
    }

    private void applyCameraShake(
            GraphicsContext gc
    ) {

        double shake =
                ctrl.getCameraShake();

        double offsetX = 0;
        double offsetY = 0;

        if (shake > 0) {

            offsetX =
                    (Math.random() - 0.5)
                            * shake;

            offsetY =
                    (Math.random() - 0.5)
                            * shake;
        }

        gc.save();

        gc.translate(offsetX, offsetY);
    }

    // =========================
    // HUD
    // =========================

    private void drawHUD(GraphicsContext gc) {

        gc.setFont(
                FontLoader.getCookieRunBold(18)
        );

        drawOutlinedText(
                gc,
                "Score: " + ctrl.getScore(),
                20,
                30
        );

        gc.setFont(
                FontLoader.getCookieRunBold(16)
        );

        drawOutlinedText(
                gc,
                "Coins: " + ctrl.getCoins(),
                20,
                55
        );

        drawHPBar(gc);
    }

    private void drawOutlinedText(
            GraphicsContext gc,
            String text,
            double x,
            double y
    ) {

        gc.setStroke(Color.BLACK);

        gc.setLineWidth(2);

        gc.strokeText(text, x, y);

        gc.setFill(Color.WHITE);

        gc.fillText(text, x, y);
    }

    private void drawHPBar(
            GraphicsContext gc
    ) {

        double hp =
                ctrl.getCookie().getHp();

        double maxHp =
                ctrl.getCookie().getMaxHpValue();

        double barWidth = 150;

        double hpWidth =
                (hp / maxHp) * barWidth;

        Color hpColor =
                hp > maxHp / 2
                        ? Color.web("#2ECC71")
                        : hp > maxHp / 4
                        ? Color.web("#F39C12")
                        : Color.web("#E74C3C");

        gc.setFill(hpColor);

        gc.fillRoundRect(
                20,
                70,
                hpWidth,
                20,
                10,
                10
        );

        gc.setStroke(Color.WHITE);

        gc.strokeRoundRect(
                20,
                70,
                barWidth,
                20,
                10,
                10
        );

        gc.setFill(Color.WHITE);

        gc.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12
                )
        );

        gc.fillText(
                "HP " + (int) hp
                        + " / "
                        + (int) maxHp,
                90,
                84
        );
    }

    private void drawAbilityHUD(
            GraphicsContext gc
    ) {

        CookieAbility ability =
                ctrl.getCookie().getAbility();

        if (ability == null
                || ability.isPassive()) {

            return;
        }

        double x =
                W - ABILITY_MARGIN;

        double y =
                H - ABILITY_MARGIN;

        double size =
                ABILITY_SIZE;

        gc.setFill(
                Color.rgb(
                        255,
                        215,
                        0,
                        0.25
                )
        );

        gc.fillOval(
                x - 6,
                y - 6,
                size + 12,
                size + 12
        );

        gc.setFill(
                Color.rgb(20,20,20,0.88)
        );

        gc.fillOval(x, y, size, size);

        gc.setFill(Color.web("#2C3E50"));

        gc.fillOval(
                x + 6,
                y + 6,
                size - 12,
                size - 12
        );

        if (ability.getCooldownRemaining() > 0) {

            double percent =
                    ability.getCooldownPercent();

            gc.setFill(
                    Color.rgb(0,0,0,0.68)
            );

            gc.fillArc(
                    x,
                    y,
                    size,
                    size,
                    90,
                    -360 * percent,
                    javafx.scene.shape.ArcType.ROUND
            );

            gc.setFill(Color.WHITE);

            gc.setFont(
                    Font.font(
                            "Arial",
                            FontWeight.EXTRA_BOLD,
                            20
                    )
            );

            gc.setTextAlign(
                    TextAlignment.CENTER
            );

            gc.fillText(
                    String.format(
                            "%.0f",
                            Math.ceil(
                                    ability.getCooldownRemaining()
                            )
                    ),
                    x + size / 2,
                    y + size / 2 + 8
            );
        }

        else {

            gc.setFill(Color.LIME);

            gc.setFont(
                    Font.font(
                            "Arial",
                            FontWeight.BOLD,
                            16
                    )
            );

            gc.setTextAlign(
                    TextAlignment.CENTER
            );

            gc.fillText(
                    "READY",
                    x + size / 2,
                    y + size / 2 + 6
            );
        }

        gc.setFill(
                Color.rgb(0,0,0,0.8)
        );

        gc.fillOval(
                x + size / 2 - 14,
                y + size + 5,
                28,
                28
        );

        gc.setFill(Color.WHITE);

        gc.fillText(
                "E",
                x + size / 2,
                y + size + 24
        );

        gc.setTextAlign(
                TextAlignment.LEFT
        );
    }

    // =========================
    // OVERLAYS
    // =========================

    private void drawPauseOverlay(
            GraphicsContext gc
    ) {

        gc.setFill(
                Color.rgb(0,0,0,0.58)
        );

        gc.fillRect(0,0,W,H);

        gc.setTextAlign(
                TextAlignment.CENTER
        );

        gc.setFont(
                FontLoader.getCookieRunBold(58)
        );

        gc.setFill(
                Color.web("#FFD700")
        );

        gc.fillText(
                "PAUSED",
                W / 2,
                H / 2 - 20
        );

        gc.setFill(Color.WHITE);

        gc.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        20
                )
        );

        gc.fillText(
                "Press P to resume",
                W / 2,
                H / 2 + 30
        );

        gc.setTextAlign(
                TextAlignment.LEFT
        );
    }

    private void drawRedOverlay(
            GraphicsContext gc
    ) {

        double opacity =
                ctrl.getRedOverlayOpacity();

        if (opacity <= 0) {
            return;
        }

        gc.setFill(
                Color.web(
                        "#FF0000",
                        opacity * 0.3
                )
        );

        gc.fillRect(0, 0, W, H);
    }

    // =========================
    // GAME OVER
    // =========================

    private void showGameOver() {
        GameOverPane pane =
                new GameOverPane();
        pane.setScore(
                ctrl.getScore()
        );
        pane.setCoins(
                ctrl.getCoins()
        );
        pane.setOnRetry(
                () -> app.startGame(cookieIndex)
        );
        pane.setOnHome(
                app::showHomePage
        );
        VBox overlay = new VBox(pane);
        overlay.setAlignment(Pos.CENTER);
        getChildren().add(overlay);
    }
}