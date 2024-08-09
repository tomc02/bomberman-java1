package Bomberman;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public class Door {
    private final Point2D position;
    private final World world;
    private final double size;
    private boolean open = false;

    public Door(Point2D position, World world) {
        this.position = position;
        this.world = world;
        this.size = 3*16;
    }

    public Rectangle2D getBoundingBox() {
        if (open) return new Rectangle2D(position.getX() + 2, position.getY() + 2, size - 16, size - 16);
        else return new Rectangle2D(0, 0, 0, 0);
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
