package Bomberman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private GameController controller;
	@Override
	public void start(Stage primaryStage) {
		try {
			//Construct a main window with a canvas.

			FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("GameView.fxml"));
			AnchorPane root = fxmlLoader.load();

			Scene scene = new Scene(root);
			controller = fxmlLoader.getController();
			controller.setScene(scene);
			controller.startGame();

			primaryStage.setScene(scene);
			primaryStage.resizableProperty().set(false);
			primaryStage.setTitle("Bomberman");
			primaryStage.show();
			primaryStage.setOnCloseRequest(this::exitProgram);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void exitProgram(WindowEvent evt) {
		controller.stopGame();
		System.exit(0);
	}
}