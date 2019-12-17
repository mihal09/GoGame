package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {
    @FXML TextField textFieldIP;
    private Stage primaryStage;
    private static MainController mainController;
    private Button buttonAgree, buttonDisagree, buttonPass;

    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        mainController = new MainController(this);

        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));

        primaryStage.setTitle("Go");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(false);
        primaryStage.setResizable(true);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(700);
        primaryStage.show();
    }

    @FXML
    private void joinGame(){
        String ipAddress = textFieldIP.getText();
        mainController.startClient(ipAddress);
        System.out.println("JOIN GAME");
    }

    void setAgreementVisibility(boolean isVisible){
        if(isVisible){
            buttonPass.setVisible(false);
        }
        else
            buttonPass.setVisible(true);
        buttonAgree.setVisible(isVisible);
        buttonDisagree.setVisible(isVisible);
    }


    void endGame(int blackScore, int whiteScore){
        String message;
        if(blackScore > whiteScore)
            message="BLACK WON!";
        else if(blackScore < whiteScore)
            message="WHITE WON!";
        else
            message="DRAW!";
        message = "BLACK SCORE: "+blackScore+" WHITE SCORE: "+whiteScore+"\n"+ message;
        showEndMessage(message);
    }

    void endGameSurrender(){
        showEndMessage("Enemy surrendered, you win!");
    }

    private void showEndMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("KONIEC GRY");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        primaryStage.close();
    }

    void startGame(int size){
        System.out.println("START GAME");
        mainController.createBoard(size);

        Button buttonPass = new Button("Pass");
        this.buttonPass = buttonPass;
        buttonPass.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainController.pass();
            }
        });

        Button buttonSurrender = new Button("Surrender");
        buttonSurrender.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainController.surrender();
                showEndMessage("You surrender!");
            }
        });

        Button buttonAgree = new Button("territory agreement");
        this.buttonAgree = buttonAgree;
        buttonAgree.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainController.territoryAgree();
            }
        });

        Button buttonDisagree = new Button("territory disagreement");
        this.buttonDisagree = buttonDisagree;
        buttonDisagree.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainController.territoryDisagree();
            }
        });

        buttonPass.setMinWidth(250);
        buttonAgree.setMinWidth(250);
        buttonDisagree.setMinWidth(250);


        buttonAgree.setVisible(false);
        buttonDisagree.setVisible(false);

        VBox vBox = new VBox(15, buttonPass, buttonAgree, buttonDisagree);
        HBox hBox = new HBox(20, mainController.getBoard(), vBox);
        mainController.drawBoard();
        primaryStage.setScene(new Scene(hBox));
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1200);

    }


    public static void main(String[] args) {
        launch();
    }
}