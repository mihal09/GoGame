package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        final MainController mainController = new MainController();

        Button button = new Button("Wyczysc plansze");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("odwiezam");
                mainController.clearBoard();
            }
        });
        HBox hBox = new HBox(20, mainController.board, button);
        mainController.drawBoard();
        primaryStage.setTitle("Go");
        primaryStage.setScene(new Scene(hBox));
        primaryStage.setMaximized(false);
        primaryStage.setResizable(true);
        primaryStage.setMinHeight(768);
        primaryStage.setMinWidth(1024);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}