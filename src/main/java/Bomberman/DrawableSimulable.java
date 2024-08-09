package Bomberman;

import javafx.scene.canvas.GraphicsContext;

public interface DrawableSimulable {
    void simulate(double deltaT);

    void draw(GraphicsContext gc);
}
