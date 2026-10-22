package utils;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for any object that can be drawn on a JavaFX Canvas.
 */
public interface Renderable {
    /**
     * Draw the object on the canvas.
     * @param gc The GraphicsContext used for drawing.
     */
    void render(GraphicsContext gc);
}
