package gui.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import utils.Renderable;
import utils.Updatable;

/**
 * Handles seamless scrolling background rendering.
 */
public class ScrollingLayer implements Renderable, Updatable {

    private Image image;

    private double offset = 0;

    private final double speed;
    private final double yPos;
    private final double height;
    private final double canvasWidth;

    public ScrollingLayer(
            double canvasWidth,
            double yPos,
            double height,
            double speed
    ) {

        this.canvasWidth = canvasWidth;
        this.yPos = yPos;
        this.height = height;
        this.speed = speed;
    }

    /**
     * Sets the image to be scrolled.
     * @param image the background image.
     */
    public void setImage(Image image) {

        this.image = image;
    }

    /**
     * Update scrolling offset based on default speed.
     * @param delta time elapsed since last frame.
     */
    @Override
    public void update(
            double delta
    ) {
        update(delta, 0);
    }

    /**
     * Update scrolling offset with additional speed factor.
     * @param delta time elapsed since last frame.
     * @param extraSpeed additional speed to add to the base speed.
     */
    public void update(
            double delta,
            double extraSpeed
    ) {

        if (!isLoaded()) {
            return;
        }

        double scaledWidth =
                image.getWidth()
                        * (height / image.getHeight());

        offset += delta * (speed + extraSpeed);

        // Seamless loop
        if (offset >= scaledWidth) {
            offset -= scaledWidth;
        }
    }

    /**
     * Render scrolling background with default fallback color.
     */
    @Override
    public void render(GraphicsContext gc) {
        render(gc, Color.web("#FFB6FF"));
    }

    /**
     * Render scrolling background.
     * @param gc the graphics context to draw on.
     * @param fallbackColor color to show if image is not loaded.
     */
    public void render(
            GraphicsContext gc,
            Color fallbackColor
    ) {

        if (!isLoaded()) {

            if (fallbackColor != null) {

                gc.setFill(fallbackColor);

                gc.fillRect(
                        0,
                        yPos,
                        canvasWidth,
                        height
                );
            }

            return;
        }

        double scaleRatio =
                height / image.getHeight();

        double scaledWidth =
                image.getWidth() * scaleRatio;

        // First image
        gc.drawImage(
                image,
                -offset,
                yPos,
                scaledWidth,
                height
        );

        // Second image
        gc.drawImage(
                image,
                scaledWidth - offset,
                yPos,
                scaledWidth,
                height
        );
    }

    /**
     * @return true if the image is successfully loaded and ready for rendering.
     */
    public boolean isLoaded() {

        return image != null
                && !image.isError();
    }

    // GETTERS / SETTERS
    public Image getImage() {
        return image;
    }
}