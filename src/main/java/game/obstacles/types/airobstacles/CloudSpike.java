package game.obstacles.types.airobstacles;

import game.obstacles.AirObstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Cloud Spike obstacle - air obstacle with bobbing animation, requiring slide to avoid.
 */
public class CloudSpike extends AirObstacle {

    public CloudSpike(double x, double speed) {
        super(x, 182, 62, 42, speed);
        this.baseDamage = 25.0;  // CloudSpike does 25 base damage
    }

    @Override
    public void render(GraphicsContext gc) {
        Color primary = Color.web("#AAAACC");
        Color accent = Color.WHITE;

        gc.setFill(accent);
        gc.fillOval(x, y + 15, 40, 22);
        gc.fillOval(x + 10, y + 8, 35, 25);
        gc.fillOval(x + 20, y + 15, 40, 22);
        gc.setFill(primary);
        double cx = x + width / 2;
        gc.fillPolygon(new double[]{cx - 8, cx + 8, cx},
                       new double[]{y + 28, y + 28, y + height + 10}, 3);
    }
}
