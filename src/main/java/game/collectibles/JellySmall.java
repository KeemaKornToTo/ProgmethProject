package game.collectibles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class JellySmall extends Collectible {

    private static final double SIZE = 20;

    private static final int SCORE = 50;

    public JellySmall(
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
                Math.sin(time * 1.8) * 1.5;

        double renderY =
                y + floatOffset;

        Color base =
                Color.web("#33CCFF");

        Color light =
                Color.web("#99EEFF");

        // Outer glow
        gc.setFill(
                Color.color(
                        0.3,
                        0.8,
                        1,
                        0.14 * glow
                )
        );

        gc.fillOval(
                x - 3,
                renderY - 3,
                width + 6,
                height + 6
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
                width * 0.28,
                height * 0.18
        );

        // Eyes
        gc.setFill(Color.web("#2A2A2A"));

        gc.fillOval(
                x + width * 0.28,
                renderY + height * 0.42,
                3,
                3
        );

        gc.fillOval(
                x + width * 0.56,
                renderY + height * 0.42,
                3,
                3
        );

        // Smile
        gc.setStroke(Color.web("#2A2A2A"));

        gc.strokeArc(
                x + width * 0.4,
                renderY + height * 0.52,
                4,
                3,
                180,
                180,
                ArcType.OPEN
        );
    }
}