package game.obstacles.types.groundobstacles;

import game.obstacles.GroundObstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Block obstacle - ground obstacle requiring jump to avoid.
 */
public class Block extends GroundObstacle {

    public Block(double x, double speed) {
        super(x, 52, 46, speed);
        this.baseDamage = 25.0;  // Block does 25 base damage
    }

    @Override
    public void render(GraphicsContext gc) {
        Color primary = Color.web("#1E90C8");
        Color accent = Color.web("#A0D8F8");

        gc.setFill(primary);
        gc.fillRoundRect(x, y, width, height, 10, 10);
        gc.setFill(accent);
        gc.fillRoundRect(x + 5, y + 5, width - 10, height - 10, 6, 6);
        gc.setFill(Color.WHITE);
        gc.fillOval(x + width / 2 - 3, y + height / 2 - 3, 6, 6);
    }
}
