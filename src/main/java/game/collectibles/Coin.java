package game.collectibles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Coin collectible.
 *
 * Features:
 * - Gives score points
 * - Can be attracted by MagneticAbility
 * - Has glow animation effect
 */
public class Coin extends Collectible {

    private static final double SIZE = 22;
    private static final int SCORE = 10;

    /**
     * Create a coin collectible.
     *
     * @param x     spawn x position
     * @param y     spawn y position
     * @param speed movement speed
     */
    public Coin(
            double x,
            double y,
            double speed
    ) {
        super(x, y, SIZE, speed, SCORE);
    }

    /**
     * Coins can be pulled by
     * MagneticAbility.
     */
    @Override
    protected boolean isMagnetic() {
        return true;  // Coins are magnetic
    }

    /**
     * Render coin graphics.
     *
     * Includes:
     * - Glow effect
     * - Gold inner layer
     * - Star icon
     */
    @Override
    public void render(GraphicsContext gc) {
        double glow = 0.7 + 0.3 * Math.sin(System.currentTimeMillis() / 250.0);

        gc.setFill(Color.color(glow, glow * 0.78, 0));
        gc.fillOval(x, y, width, height);

        gc.setFill(Color.web("#FFD700"));
        gc.fillOval(x + 3, y + 3, width - 6, height - 6);

        gc.setFill(Color.web("#CC8800"));
        gc.setFont(javafx.scene.text.Font.font(10));
        gc.fillText("★", x + 4, y + width - 5);
    }
}
