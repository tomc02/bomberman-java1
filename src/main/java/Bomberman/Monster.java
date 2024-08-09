package Bomberman;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Monster implements DrawableSimulable {
    private final World world;
    private final double speed;
    private final double size;
    private final Image monster;
    private Point2D position;
    private int direction = 0;
    private boolean moving, alive = true;

    public Monster(World world, double speed) {
        this.size = world.getTileSize();
        this.position = new Point2D(this.size,this.size);
        this.world = world;
        this.speed = speed;
        monster = new Image(getClass().getResourceAsStream("monster.gif"), size, size, true, true);
    }

    public void placeMonster() {
        int x, y;
        do {
            x = new Random().nextInt((int) world.getWidth());
            y = new Random().nextInt((int) world.getHeight());
            if (world.isFree(x, y) && (x > 2 * size && y > 2 * size)) {
                position = new Point2D(x, y);
                break;
            }
        } while (true);
    }

    public void moveMonester() {
        moving = false;
        if (direction == 0 && world.isFree((int) (position.getX() + speed), (int) position.getY())) {
            position = position.add(speed, 0);
            moving = true;
        }
        if (direction == 1 && world.isFree((int) (position.getX() - speed), (int) position.getY())) {
            position = position.add(-speed, 0);
            moving = true;
        }
        if (direction == 2 && world.isFree((int) position.getX(), (int) (position.getY() - speed))) {
            position = position.add(0, -speed);
            moving = true;
        }
        if (direction == 3 && world.isFree((int) position.getX(), (int) (position.getY() + speed))) {
            position = position.add(0, speed);
            moving = true;
        }
        if (!moving) {
            direction = ThreadLocalRandom.current().nextInt(4);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (alive) gc.drawImage(monster, position.getX(), position.getY(), size, size);
    }

    @Override
    public void simulate(double deltaT) {
        if (alive) moveMonester();
        if (new Random().nextInt(200) == 5) {
            direction = ThreadLocalRandom.current().nextInt(4);
        }
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX() + 8, position.getY() + 8, size - 16, size - 16);
    }

    public void killMonster() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }
}
