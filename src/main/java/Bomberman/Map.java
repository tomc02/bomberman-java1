package Bomberman;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Random;

import static javafx.scene.paint.Color.*;

public class Map implements DrawableSimulable {
    private final World world;
    private final Image hardWall;
    private final Image softWall;
    private final Image bomb;
    private final Image[] explosions;
    private final Image door;
    private final double tileSize = 3 * 16;
    private final double rows = 13;
    private final double columns = 15;
    private final double size = tileSize;
    private final double bombSize = 60;
    private int[][] map;
    private boolean exploded = false;
    private int counter = 0, score = 0, explosionTimer = 15;

    public Map(World world) {
        this.world = world;

        hardWall = new Image(getClass().getResourceAsStream("hardWall.png"), size, size, true, true);
        softWall = new Image(getClass().getResourceAsStream("softWall.png"), size, size, true, true);
        bomb = new Image(getClass().getResourceAsStream("bomb.gif"), bombSize, bombSize, true, true);
        door = new Image(getClass().getResourceAsStream("door.gif"), bombSize, bombSize, true, true);

        explosions = new Image[4];
        explosions[0] = new Image(getClass().getResourceAsStream("dExplosion.gif"), size, size, true, true);
        explosions[1] = new Image(getClass().getResourceAsStream("uExplosion.gif"), size, size, true, true);
        explosions[2] = new Image(getClass().getResourceAsStream("rExplosion.gif"), size, size, true, true);
        explosions[3] = new Image(getClass().getResourceAsStream("lExplosion.gif"), size, size, true, true);
        generateMap();
    }

    @Override
    public void simulate(double deltaT) {
        if (exploded) {
            counter++;
            if (counter > explosionTimer) {
                explosionDone();
                world.deleteBomb();
            }
        }
    }

    public boolean isBombPlaceble(Point2D position) {
        return map[(int) position.getY()][(int) position.getX()] == 0;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double size = tileSize;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (map[j][i] == 1) {
                    gc.drawImage(hardWall, i * size, j * size, size, size);
                } else if (map[j][i] == 2) {
                    gc.drawImage(softWall, i * size, j * size, size, size);
                } else if (map[j][i] == 3) {
                    gc.drawImage(bomb, i * size, j * size, bombSize, bombSize);
                } else if (map[j][i] == 4) {
                    gc.drawImage(softWall, i * size, j * size, size, size);
                } else if (map[j][i] == 5) {
                    gc.drawImage(explosions[1], i * size, j * size, size, size);
                } else if (map[j][i] == 6) {
                    gc.drawImage(explosions[0], i * size, j * size, size, size);
                } else if (map[j][i] == 7) {
                    gc.drawImage(explosions[2], i * size, j * size, size, size);
                } else if (map[j][i] == 8) {
                    gc.drawImage(explosions[3], i * size, j * size, size, size);
                } else if (map[j][i] == 9) {
                    gc.drawImage(door, i * size, j * size, size, size);
                }
            }
        }
        gc.save();
        gc.setFill(RED);
        Font f = new Font("SansSerif", 30);
        gc.setFont(f);
        String s = String.valueOf(score);
        gc.fillText(s, 10, 30);
        gc.restore();
    }

    public boolean isFree(int nextX, int nextY) {
        int size = (int) tileSize;

        int nextX1 = nextX / size;
        int nextY1 = nextY / size;

        int nextX2 = (nextX + size - 1) / size;
        int nextY2 = nextY / size;

        int nextX3 = nextX / size;
        int nextY3 = (nextY + size - 1) / size;

        int nextX4 = (nextX + size - 1) / size;
        int nextY4 = (nextY + size - 1) / size;

        return !((map[nextY1][nextX1] == 1 || map[nextY1][nextX1] == 2 || map[nextY1][nextX1] == 4) ||
                (map[nextY2][nextX2] == 1 || map[nextY2][nextX2] == 2 || map[nextY2][nextX2] == 4) ||
                (map[nextY3][nextX3] == 1 || map[nextY3][nextX3] == 2 || map[nextY3][nextX3] == 4) ||
                (map[nextY4][nextX4] == 1 || map[nextY4][nextX4] == 2 || map[nextY4][nextX4] == 4));
    }

    public double getTileSize() {
        return tileSize;
    }

    public void placeBomb(Point2D pos) {
        map[(int) pos.getY()][(int) pos.getX()] = 3;
    }

    public void explosion(Point2D pos) {
        int r = (int) pos.getY();
        int c = (int) pos.getX();
        if (map[r - 1][c] == 2) { //up
            map[r - 1][c] = 5;
            score += 10;
        } else if (map[r - 1][c] == 0) {
            map[r - 1][c] = 5;
        } else if (map[r - 1][c] == 4) {
            map[r - 1][c] = 9;
            world.openDoor();
            score += 10;
        }
        if (map[r + 1][c] == 2) { //down
            map[r + 1][c] = 6;
            score += 10;
        } else if (map[r + 1][c] == 0) {
            map[r + 1][c] = 6;
        } else if (map[r + 1][c] == 4) {
            map[r + 1][c] = 9;
            world.openDoor();
            score += 10;
        }
        if (map[r][c - 1] == 2) { //left
            map[r][c - 1] = 7;
            score += 10;
        } else if (map[r][c - 1] == 0) {
            map[r][c - 1] = 7;
        } else if (map[r][c - 1] == 4) {
            map[r][c - 1] = 9;
            world.openDoor();
            score += 10;
        }
        if (map[r][c + 1] == 2) { //right
            map[r][c + 1] = 8;
            score += 10;
        } else if (map[r][c + 1] == 0) {
            map[r][c + 1] = 8;
        } else if (map[r][c + 1] == 4) {
            map[r][c + 1] = 9;
            world.openDoor();
            score += 10;
        }
        exploded = true;
        counter = 0;
    }

    private void explosionDone() {
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (map[j][i] == 5) {
                    map[j][i] = 0;
                } else if (map[j][i] == 6) {
                    map[j][i] = 0;
                } else if (map[j][i] == 7) {
                    map[j][i] = 0;
                } else if (map[j][i] == 8) {
                    map[j][i] = 0;
                } else if (map[j][i] == 3) {
                    map[j][i] = 0;
                }
            }
        }
        exploded = false;
    }

    public void generateMap() {
        this.map = new int[][]{

                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}

        };

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (map[i][j] == 0) {
                    if (new Random().nextInt(40) < 5) {
                        map[i][j] = 2;
                    }
                }
            }
        }
        int i, j;
        do {
            i = new Random().nextInt((int) rows);
            j = new Random().nextInt((int) columns);
            if (map[i][j] == 2) map[i][j] = 4;
            world.createDoor(new Point2D(((j * getTileSize()) + (getTileSize() / 2)), (i * getTileSize()) + (getTileSize() / 2)));
        } while (map[i][j] != 4);

        map[1][1] = 0;
        map[2][1] = 0;
        map[1][2] = 0;
    }

    public boolean isBombExploded() {
        return exploded;
    }

    public void addScore(int s) {
        score += s;
    }


    public int getScore() {
        return score;
    }
}
