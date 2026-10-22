package game.obstacles.types.airobstacles;

import game.obstacles.AirObstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Fireball obstacle - air obstacle with bobbing animation, requiring slide to avoid.
 */
public class Fireball extends AirObstacle {

    public Fireball(double x, double speed) {
        super(x, 195, 46, 46, speed);
        this.baseDamage = 30.0;  // Fireball does 30 base damage
    }

    @Override
    public void render(GraphicsContext gc) {
        Color primary = Color.web("#FF5500");
        Color accent = Color.web("#FFD700");

        gc.setFill(accent);
        gc.fillOval(x - 4, y - 4, width + 8, height + 8);
        gc.setFill(primary);
        gc.fillOval(x + 4, y + 4, width - 8, height - 8);
        gc.setFill(Color.WHITE);
        gc.fillOval(x + 14, y + 12, 10, 10);
        gc.setFill(Color.web("#FF8800", 0.7));
        gc.fillOval(x + width - 5, y + 10, 18, 14);
    }
}
