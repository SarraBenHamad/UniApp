package com.example.demo3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML

    private Label welcomeText;
    private Stage stage;
    private Scene scene;
    private Parent root;
   /* @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
*/
    @FXML
    public void Admin(ActionEvent event)throws IOException {
try{
    Parent root = FXMLLoader.load(getClass().getResource("adminview.fxml"));

        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }


    }

    public void Student(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("studentview.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            /*stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}