package game.collectibles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class JellyBig extends Collectible {

    private static final double SIZE = 32;

    private static final int SCORE = 200;

    public JellyBig(
            double x,
            double y,
            double speed
    ) {

        super(x, y, SIZE, speed, SCORE);
    }

    @Override
    protected boolean isMagnetic() {

        return false;
    }

    @Override
    public void render(GraphicsContext gc) {

        double time = System.currentTimeMillis() / 250.0;

        double glow =
                0.75 + 0.25 * Math.sin(time);

        double floatOffset =
                Math.sin(time * 1.5) * 2;

        double renderY =
                y + floatOffset;

        Color base =
                Color.web("#FF3399");

        Color light =
                Color.web("#FF99CC");

        // Outer glow
        gc.setFill(
                Color.color(
                        1,
                        0.4,
                        0.7,
                        0.15 * glow
                )
        );

        gc.fillOval(
                x - 4,
                renderY - 4,
                width + 8,
                height + 8
        );

        // Body
        gc.setFill(
                Color.color(
                        base.getRed(),
                        base.getGreen(),
                        base.getBlue(),
                        0.95
                )
        );

        gc.fillOval(
                x,
                renderY + height * 0.2,
                width,
                height * 0.8
        );

        gc.fillArc(
                x,
                renderY,
                width,
                height * 0.6,
                0,
                180,
                ArcType.CHORD
        );

        // Highlight
        gc.setFill(light);

        gc.fillOval(
                x + width * 0.18,
                renderY + height * 0.08,
                width * 0.32,
                height * 0.18
        );

        // Eyes
        gc.setFill(Color.web("#2A2A2A"));

        gc.fillOval(
                x + width * 0.28,
                renderY + height * 0.42,
                4,
                4
        );

        gc.fillOval(
                x + width * 0.55,
                renderY + height * 0.42,
                4,
                4
        );

        // Tiny mouth
        gc.setStroke(Color.web("#2A2A2A"));

        gc.strokeArc(
                x + width * 0.4,
                renderY + height * 0.5,
                6,
                4,
                180,
                180,
                ArcType.OPEN
        );
    }
}