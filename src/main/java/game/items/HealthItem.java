package game.items;

import core.entities.base.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HealthItem extends GameObject {

    private double speed = 300;

    public HealthItem(double x, double y) {
        super(x, y, 30, 30);
    }

    @Override
    public void update(double delta) {
        x -= speed * delta;
        if (x + width < -10) {
            alive = false;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(x, y, width, height, 8, 8);
        gc.setFill(Color.RED);
        gc.fillRect(x + 11, y + 5, 8, 20);
        gc.fillRect(x + 5, y + 11, 20, 8);
    }
}
