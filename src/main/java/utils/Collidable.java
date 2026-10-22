package utils;

import javafx.geometry.Rectangle2D;

/**
 * Interface for objects that participate in collision detection.
 */
public interface Collidable {
    /**
     * @return the bounding rectangle of the object for collision detection.
     */
    Rectangle2D getBounds();

    /**
     * Checks if this object intersects with another collidable object.
     * @param other The other collidable object.
     * @return true if they intersect, false otherwise.
     */
    boolean intersects(Collidable other);
}
