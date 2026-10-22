package game.obstacles.types.groundobstacles;

import game.obstacles.GroundObstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Candy Wall obstacle - ground obstacle requiring jump to avoid.
 */
public class CandyWall extends GroundObstacle {

    public CandyWall(double x, double speed) {
        super(x, 30, 80, speed);
        this.baseDamage = 25.0;  // CandyWall does 25 base damage
    }

    @Override
    public void render(GraphicsContext gc) {
        Color primary = Color.web("#E63B6F");
        Color accent = Color.WHITE;

        gc.setFill(primary);
        gc.fillRoundRect(x, y, width, height, 8, 8);
        gc.setFill(accent);
        for (int i = 0; i < 4; i++) gc.fillRect(x + 4, y + 8 + i * 18, width - 8, 7);
        gc.setFill(Color.web("#FFD700"));
        gc.fillOval(x + width / 2 - 10, y - 12, 20, 20);
        gc.setFill(primary);
        gc.fillOval(x + width / 2 - 6, y - 8, 12, 12);
    }
}
