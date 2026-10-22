package gui.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.Renderable;
import utils.Updatable;

/**
 * Small particle effect used when collecting items.
 * Gives the game more feedback and feel.
 *
 * Optimized for object pooling to reduce garbage collection pressure.
 */
public class Particle implements Renderable, Updatable {

    // Position
    private double x;
    private double y;

    // Velocity
    private double vx;
    private double vy;

    // Lifetime (seconds)
    private double life = 1;

    // Particle size
    private double size;

    public Particle(double x, double y) {
        reset(x, y);
    }

    /**
     * Reset particle for pooling/reuse.
     * Called by particle pool or when creating new particles.
     */
    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.life = 1.0;

        // Random movement
        vx = (Math.random() - 0.5) * 90;
        vy = (Math.random() - 0.5) * 90;

        // Random size
        size = 2 + Math.random() * 3;
    }

    /**
     * Update particle physics.
     */
    @Override
    public void update(double delta) {

        // Move particle
        x += vx * delta;
        y += vy * delta;

        // Gravity
        vy += 200 * delta;

        // Fade over time
        life -= delta;
    }

    /**
     * Render glowing particle.
     *
     * Note: This method sets/resets alpha. For batch rendering optimization,
     * the calling code should set alpha once before rendering all particles,
     * and reset it once after. See CollectibleManager.renderParticles().
     */
    @Override
    public void render(GraphicsContext gc) {

        // Fade effect
        gc.setGlobalAlpha(life);

        // Outer glow
        gc.setFill(Color.WHITE);
        gc.fillOval(x, y, size, size);

        // Inner gold
        gc.setFill(Color.GOLD);
        gc.fillOval(
                x + 1,
                y + 1,
                size - 2,
                size - 2
        );

        gc.setGlobalAlpha(1);
    }

    /**
     * Remove particle when life ends.
     */
    public boolean isDead() {

        return life <= 0;
    }
}