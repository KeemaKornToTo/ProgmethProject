package core.entities.base;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import utils.Collidable;
import utils.Renderable;
import utils.Spawnable;
import utils.Updatable;

/**
 * Base class for every object in the game.
 *
 * Used for:
 * - Player
 * - Obstacles
 * - Collectibles
 *
 * Provides:
 * - Position
 * - Size
 * - Collision detection
 * - Basic object lifecycle
 */
public abstract class GameObject implements Renderable, Updatable, Collidable, Spawnable {
    /**
     * GameObject Position
     */
    protected double x;
    protected double y;

    protected double width;
    protected double height;

    protected boolean alive = true;

    public GameObject(
            double x,
            double y,
            double width,
            double height
    ) {

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
    }

    /**
     * Updates the object's state based on elapsed time.
     * @param delta time elapsed since last frame in seconds.
     */
    public abstract void update(double delta);

    /**
     * Renders the object on the given graphics context.
     * @param gc the graphics context to draw on.
     */
    public abstract void render(GraphicsContext gc);

    /**
     * Simple AABB collision detection with another GameObject.
     * @param other the other game object to check collision with.
     * @return true if this object's hitbox overlaps with the other object's hitbox.
     */
    public boolean collides(GameObject other) {

        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }

    /**
     * Returns the bounding box of this object.
     */
    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    /**
     * Checks for intersection with another Collidable.
     * Uses optimized GameObject-to-GameObject collision if possible.
     */
    @Override
    public boolean intersects(Collidable other) {
        if (other instanceof GameObject) {
            return collides((GameObject) other);
        }
        return getBounds().intersects(other.getBounds());
    }

    /**
     * @return true if the object is still alive/active.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets the alive status of the object.
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Marks the object as dead/inactive so it can be removed from the game.
     */
    public void destroy() {
        alive = false;
    }

    /**
     * @return true if the object has moved completely past the left edge of the screen.
     */
    public boolean isOutOfScreen() {
        return x + width < 0;
    }

    // GETTERS

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
