package Bomberman;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player implements DrawableSimulable {
    private final World world;
    private final double speed = 4;
    private final double size;
    private final Image playerR;
    private final Image playerL;
    private final Image playerD;
    private Point2D position;
    private boolean moving, right, left, up, down;
    private Image player;

    public Player(Point2D position, World world) {
        this.position = position;
        this.world = world;
        size = world.getTileSize();
        player = playerR = new Image(getClass().getResourceAsStream("bomberman2.gif"), size, size, true, true);
        playerL = new Image(getClass().getResourceAsStream("bombermanLeft.gif"), size, size, true, true);
        playerD = new Image(getClass().getResourceAsStream("front.gif"), size, size, true, true);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(player, position.getX(), position.getY(), size, size);
    }

    @Override
    public void simulate(double deltaT) {
        moving = false;
        if (right && world.isFree((int) (position.getX() + speed), (int) position.getY())) {
            position = position.add(speed, 0);
            player = playerR;
            moving = true;
        }
        if (left && world.isFree((int) (position.getX() - speed), (int) position.getY())) {
            position = position.add(-speed, 0);
            player = playerL;
            moving = true;
        }
        if (up && world.isFree((int) position.getX(), (int) (position.getY() - speed))) {
            position = position.add(0, -speed);
            moving = true;
        }
        if (down && world.isFree((int) position.getX(), (int) (position.getY() + speed))) {
            position = position.add(0, speed);
            moving = true;
        }
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX() + 8, position.getY() + 8, size - 16, size - 16);
    }

    public void kill() {
    }
}
