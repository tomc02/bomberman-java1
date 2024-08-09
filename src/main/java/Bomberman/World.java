package Bomberman;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class World {
    private final Player player;
    private final Map map;
    private final List<DrawableSimulable> objects;
    private ArrayList<Monster> monsters;
    private double width;
    private double height;
    private Bomb bomb;
    private Door door;
    private boolean pause, gameOver = false;
    private int monstersCount = 2, newLevel = 0, actualLevel = 2;
    private final int monsterSpeed = 3;

    public World(double width, double height) {
        this.width = width;
        this.height = height;
        map = new Map(this);
        player = new Player(new Point2D(map.getTileSize(), map.getTileSize()), this);
        bomb = null;
        monsters = new ArrayList<Monster>();
        for (int i = 0; i <= monstersCount; i++) {
            monsters.add(new Monster(this, monsterSpeed));
            monsters.get(i).placeMonster();
        }
        objects = new ArrayList<DrawableSimulable>();
        objects.add(map);
        objects.add(player);
        objects.addAll(monsters);
    }

    public void draw(GraphicsContext gc) {
        if (!gameOver) {
            gc.save();
            gc.setFill(GREEN);
            gc.fillRect(0, 0, width, height);
            for (DrawableSimulable ds : objects) {
                ds.draw(gc);
            }
        } else if (gameOver) {
            gameOverScreen(gc);
        }
        if (newLevel > 0){
            newLevelScreen(gc);
        }
    }

    public void simulate(double timeDelta) {
        if (!pause && !gameOver) {
            for (DrawableSimulable object : objects) {
                object.simulate(timeDelta);
            }
            if (bomb != null) {
                bomb.simulate(timeDelta);
                bombExplosion();
            }
            monstersColision();
            if (player.getBoundingBox().intersects(door.getBoundingBox())) {
                pause();
                newLevel();
                map.addScore(1000);
            }
        }
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void addKeyHandler(Scene scene) {
        scene.setOnKeyPressed(ke -> {
            KeyCode keyCode = ke.getCode();
            if (keyCode.equals(KeyCode.S)) {
                player.setDown(true);
            }
            if (keyCode.equals(KeyCode.W)) {
                player.setUp(true);
            }
            if (keyCode.equals(KeyCode.A)) {
                player.setLeft(true);
            }
            if (keyCode.equals(KeyCode.D)) {
                player.setRight(true);
            }
            if (keyCode.equals(KeyCode.SHIFT)) {
                if (bomb == null) {
                    double x = (player.getPosition().getX() + (map.getTileSize() / 2)) / (map.getTileSize());
                    double y = (player.getPosition().getY() + (map.getTileSize() / 2)) / (map.getTileSize());
                    if (map.isBombPlaceble(new Point2D(x, y))) {
                        bomb = new Bomb(player.getPosition(), new Point2D(x, y), this);
                        map.placeBomb(bomb.getMapPosition());
                    }
                }
            }
        });
        scene.setOnKeyReleased(ke -> {
            KeyCode keyCode = ke.getCode();
            if (keyCode.equals(KeyCode.S)) {
                player.setDown(false);
            }
            if (keyCode.equals(KeyCode.W)) {
                player.setUp(false);
            }
            if (keyCode.equals(KeyCode.A)) {
                player.setLeft(false);
            }
            if (keyCode.equals(KeyCode.D)) {
                player.setRight(false);
            }
        });
    }

    public void explodeBomb() {
        map.explosion(bomb.getMapPosition());
    }

    public boolean bombPlayerIntersects() {
        return player.getBoundingBox().intersects(bomb.getHorizontalBoundingBox()) || player.getBoundingBox().intersects(bomb.getVerticalBoundingBox());
    }
    public void killPlayer() {
        player.kill();
        gameOver = true;
    }

    public void deleteBomb() {
        bomb = null;
    }

    public void createDoor(Point2D pos) {
        door = new Door(pos, this);
    }

    public void openDoor() {
        door.setOpen(true);
    }

    public boolean isFree(int x, int y) {
        return map.isFree(x, y);
    }

    public void pause() {
        pause = !pause;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return map.getScore();
    }

    private void newLevel() {
        newLevel = 1;
        map.generateMap();
        objects.removeAll(monsters);
        monsters = new ArrayList<Monster>();
        monstersCount++;
        for (int i = 0; i <= monstersCount; i++) {
            monsters.add(new Monster(this, monsterSpeed));
            monsters.get(i).placeMonster();
        }
        player.setPosition(new Point2D(map.getTileSize(), map.getTileSize()));
        objects.addAll(monsters);
    }

    public double getTileSize() {
        return map.getTileSize();
    }

    private void bombExplosion() {
        if (map.isBombExploded()) {
            for (Monster m : monsters) {
                if (m.getBoundingBox().intersects(bomb.getHorizontalBoundingBox()) || m.getBoundingBox().intersects(bomb.getVerticalBoundingBox())) {
                    m.killMonster();
                    map.addScore(20);
                }
            }
            if (bombPlayerIntersects()) {
                killPlayer();
            }
        }
    }

    private void monstersColision() {
        int tmp = 0;
        for (Monster m : monsters) {
            if (m.getBoundingBox().intersects(player.getBoundingBox()) && m.isAlive()) {
                killPlayer();
            }
            if (!m.isAlive()) {
                tmp++;
            }
            if (tmp > monstersCount) {
                pause();
                newLevel();
            }
        }
    }
    private void gameOverScreen(GraphicsContext gc){
        gc.save();
        gc.setFill(BLACK);
        gc.fillRect(0, 0, width, height);
        String go = "GAME OVER";
        Font f = new Font("SansSerif", 40);
        gc.setFont(f);
        gc.setFill(WHITE);
        gc.fillText(go, (width / 2) - 100, (height / 2) - 40);
        go = "Press SPACE";
        gc.fillText(go, (width / 2) - 100, (height / 2) + 40);
        gc.restore();
    }
    private void newLevelScreen(GraphicsContext gc){
        if (newLevel > 50) {
            newLevel = 0;
            pause();
            actualLevel++;
        }
        else {
            gc.save();
            gc.setFill(BLACK);
            gc.fillRect(0, 0, width, height);
            String go = "LEVEL: "+actualLevel;
            Font f = new Font("SansSerif", 40);
            gc.setFont(f);
            gc.setFill(WHITE);
            gc.fillText(go, (width / 2) - 100, (height / 2) - 40);
            gc.restore();
            newLevel++;
        }
    }
}
