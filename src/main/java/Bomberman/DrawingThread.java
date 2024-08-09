package Bomberman;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrawingThread extends AnimationTimer {

    private final Canvas canvas;

    private final GraphicsContext gc;

    private final World world;

    private long lasttime = -1;
    private final Scene scene;
    private int totalFrames = 0;
    private long lastFpsCheck = 0;
    private int currentFps;

    public DrawingThread(Canvas canvas, World world, Scene scene) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.world = world;
        this.scene = scene;
    }

    /**
     * Draws objects into the canvas. Put you code here.
     */
    @Override
    public void handle(long now) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        world.draw(gc);
        totalFrames++;
        if (System.nanoTime() > lastFpsCheck + 1000000000) {
            lastFpsCheck = System.nanoTime();
            currentFps = totalFrames;
            totalFrames = 0;
            System.out.println("FPS: " + currentFps);
        }
        if (lasttime > 0) {
            //time are in nanoseconds and method simulate expects seconds
            world.simulate((now - lasttime) / 1e9);
            world.addKeyHandler(scene);
        }
        lasttime = now;
    }
}
