package game.obstacles.types.groundobstacles;

import game.obstacles.GroundObstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Spike obstacle - ground obstacle requiring jump to avoid.
 */
public class Spike extends GroundObstacle {

    public Spike(double x, double speed) {
        super(x, 44, 56, speed);
        this.baseDamage = 20.0;  // Spike does 20 base damage
    }

    @Override
    public void render(GraphicsContext gc) {
        Color primary = Color.web("#FFDE00");
        Color accent = Color.web("#A26D07");

        double cx = x + width / 2;
        gc.setFill(primary);
        gc.fillPolygon(new double[]{cx, cx - 14, cx + 14},
                       new double[]{y, y + height, y + height}, 3);
        gc.fillPolygon(new double[]{cx - 10, cx - 22, cx - 2},
                       new double[]{y + 20, y + height, y + height}, 3);
        gc.fillPolygon(new double[]{cx + 10, cx + 2, cx + 22},
                       new double[]{y + 20, y + height, y + height}, 3);
        gc.setFill(accent);
        gc.fillOval(cx - 3, y + 5, 6, 10);
    }
}
