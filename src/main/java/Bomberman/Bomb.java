package Bomberman;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class Bomb implements DrawableSimulable {
    private final Point2D position;
    private final Point2D mapPosition;
    private final World world;
    private final double size;
    private int counter, bombTimer = 60;

    public Bomb(Point2D position, Point2D mapPosition, World world) {
        this.position = position;
        this.world = world;
        this.mapPosition = mapPosition;
        this.size = world.getTileSize();
    }

    public Point2D getMapPosition() {
        return mapPosition;
    }

    @Override
    public void simulate(double deltaT) {
        if (counter > bombTimer) {
            world.explodeBomb();
            counter = 0;
        }
        counter++;
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    public Rectangle2D getHorizontalBoundingBox() {
        double tmpSize = size - 8;
        return new Rectangle2D(position.getX() - tmpSize, position.getY() + 8, 3 * tmpSize, tmpSize);
    }

    public Rectangle2D getVerticalBoundingBox() {
        double tmpSize = size - 8;
        return new Rectangle2D(position.getX() + 8, position.getY() - tmpSize, tmpSize, 3 * tmpSize);
    }
}

