package utils;

/**
 * Interface for objects that are spawned into the game world
 * and scroll off-screen (obstacles and collectibles).
 */
public interface Spawnable {
    /**
     * @return true if the object is still active in the game world.
     */
    boolean isAlive();

    /**
     * Set the active state of the object.
     * @param alive true to keep it active, false to mark for removal.
     */
    void    setAlive(boolean alive);

    /**
     * @return the current x-coordinate of the object.
     */
    double  getX();
}
