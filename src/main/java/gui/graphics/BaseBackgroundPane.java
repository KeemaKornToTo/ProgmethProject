package gui.graphics;

import application.CookieRunApp;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

/**
 * Base background pane with seamless looping animation.
 * Draws the image multiple times to create infinite scrolling.
 */
public class BaseBackgroundPane extends StackPane {

    private Canvas canvas;
    private Image backgroundImage;
    private double offsetX = 0;
    private double velocity;

    /**
     * Create a seamless looping background.
     * @param imagePath path to the background image
     * @param velocity scroll speed (pixels per frame)
     */
    public BaseBackgroundPane(String imagePath, double velocity) {
        this.velocity = velocity;
        this.setStyle("-fx-background-color: #000000;");
        this.setPrefSize(CookieRunApp.WIDTH, CookieRunApp.HEIGHT);

        // Create canvas
        canvas = new Canvas(CookieRunApp.WIDTH, CookieRunApp.HEIGHT);
        this.getChildren().add(canvas);

        // Load image
        try {
            backgroundImage = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + imagePath + " - " + e.getMessage());
        }

        // Start animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
            }
        };
        timer.start();
    }

    /**
     * Update the offset for scrolling.
     */
    public void update() {
        offsetX += velocity;

        // Wrap offset when it exceeds image width
        if (backgroundImage != null) {
            double imageWidth = backgroundImage.getWidth();
            if (offsetX >= imageWidth) {
                offsetX = offsetX - imageWidth;
            }
        }
    }

    /**
     * Draw the background with seamless looping.
     */
    private void draw() {
        if (backgroundImage == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, CookieRunApp.WIDTH, CookieRunApp.HEIGHT);

        double imageWidth = backgroundImage.getWidth();
        double imageHeight = backgroundImage.getHeight();

        // Scale image to fill height
        double scaleRatio = CookieRunApp.HEIGHT / imageHeight;
        double scaledWidth = imageWidth * scaleRatio;
        double scaledHeight = CookieRunApp.HEIGHT;

        // Draw image at current offset
        gc.drawImage(backgroundImage, -offsetX, 0, scaledWidth, scaledHeight);

        // Draw second copy for seamless looping
        gc.drawImage(backgroundImage, scaledWidth - offsetX, 0, scaledWidth, scaledHeight);
    }
}
