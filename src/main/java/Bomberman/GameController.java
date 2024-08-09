package Bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {

    private final List<Score> scoreList;
    private World world;
    private AnimationTimer animationTimer;
    @FXML
    private Canvas canvas;
    private Scene scene;
    @FXML
    private Button startBtn, pauseBtn, scoresBtn, exitBtn, backButton, loadBtn, saveBtn;
    @FXML
    private Text pausedText, nickText;
    @FXML
    private TextField nickTextBox;
    @FXML
    private ListView scoresListView;

    public GameController() {
        scoreList = new ArrayList<Score>();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void startGame() {
        this.world = new World(canvas.getWidth(), canvas.getHeight());
        //Draw scene on a separate thread to avoid blocking UI
        animationTimer = new DrawingThread(canvas, world, scene);
        animationTimer.start();
        world.pause();
    }

    public void stopGame() {
        animationTimer.stop();
    }

    @FXML
    public void startPressed() {
        this.startGame();
        canvas.setVisible(true);
        showMainMenu(false);
        world.pause();
    }

    @FXML
    public void pausePressed() {
        if (!world.isGameOver()) {
            canvas.setVisible(!canvas.isVisible());
            world.pause();
            pausedText.setVisible(!pausedText.isVisible());
        } else {
            scoreList.add(new Score(world.getScore(), nickTextBox.getText()));
            showMainMenu(true);
            animationTimer.stop();
        }
    }

    private void showMainMenu(boolean show) {
        startBtn.setDisable(!show);
        startBtn.setVisible(show);
        pauseBtn.setVisible(!show);
        canvas.setVisible(!show);
        exitBtn.setDisable(!show);
        scoresBtn.setDisable(!show);
        exitBtn.setVisible(show);
        scoresBtn.setVisible(show);
        nickText.setVisible(show);
        nickTextBox.setVisible(show);
        nickTextBox.setDisable(!show);
    }

    @FXML
    public void exitPressed() {
        Platform.exit();
    }

    @FXML
    public void scoresPressed() {
        if (scoreList.isEmpty()) {
            if (!this.load()) return;
        } else {
            Collections.sort(scoreList, (o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));
            this.scoresListView.setItems(FXCollections.observableList(scoreList));
        }
        scoresListView.setVisible(true);
        showMainMenu(false);
        pauseBtn.setVisible(false);
        backButton.setVisible(true);
        backButton.setDisable(false);
        canvas.setVisible(false);
        loadBtn.setVisible(true);
        saveBtn.setVisible(true);
        loadBtn.setDisable(false);
        saveBtn.setDisable(false);
    }

    @FXML
    public void backPressed() {
        scoresListView.setVisible(false);
        backButton.setVisible(false);
        backButton.setDisable(true);
        showMainMenu(true);
        loadBtn.setVisible(false);
        saveBtn.setVisible(false);
        loadBtn.setDisable(true);
        saveBtn.setDisable(true);
    }

    @FXML
    private boolean load() {
        scoreList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            while (null != (line = br.readLine())) {
                String[] token = line.split(";");
                Score score = new Score(Integer.parseInt(token[1]), token[0]);
                scoreList.add(score);
            }
        } catch (IOException e) {
            return false;
        }
        Collections.sort(scoreList, (o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));
        scoresListView.setItems(FXCollections.observableList(scoreList));
        return true;
    }

    @FXML
    private void save() {
        Collections.sort(scoreList, (o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));
        try (PrintWriter pw = new PrintWriter(new FileWriter("data.csv"))) {
            for (Score score : scoreList) {
                pw.printf("%s;%d\n", score.getName(), score.getScore());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
