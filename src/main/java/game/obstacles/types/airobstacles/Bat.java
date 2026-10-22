package game.obstacles.types.airobstacles;

import game.obstacles.AirObstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Bat obstacle - air obstacle with bobbing animation, requiring slide to avoid.
 */
public class Bat extends AirObstacle {

    public Bat(double x, double speed) {
        super(x, 170, 56, 36, speed);
        this.baseDamage = 25.0;  // Bat does 25 base damage
    }

    @Override
    public void render(GraphicsContext gc) {
        Color primary = Color.web("#4A1060");
        Color accent = Color.web("#CC66FF");

        double cx = x + width / 2, cy = y + height / 2;
        gc.setFill(primary);
        gc.fillPolygon(new double[]{cx, cx - 28, cx - 10, cx - 18, cx - 5},
                       new double[]{cy, cy - 15, cy + 5, cy + 18, cy + 10}, 5);
        gc.fillPolygon(new double[]{cx, cx + 28, cx + 10, cx + 18, cx + 5},
                       new double[]{cy, cy - 15, cy + 5, cy + 18, cy + 10}, 5);
        gc.fillOval(cx - 10, cy - 12, 20, 22);
        gc.setFill(accent);
        gc.fillOval(cx - 7, cy - 8, 6, 6);
        gc.fillOval(cx + 1, cy - 8, 6, 6);
        gc.setFill(Color.RED);
        gc.fillOval(cx - 5, cy - 6, 3, 3);
        gc.fillOval(cx + 3, cy - 6, 3, 3);
    }
}
