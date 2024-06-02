package com.example.demo3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class studentController {
    private Student student;
    @FXML
    private TextField fullName;
    @FXML
    private TextField id;
    @FXML
    private DatePicker birthDate;

    public void signUp(ActionEvent event) {
        MongoDBManager manager = new MongoDBManager();
        String studentName = fullName.getText();
        int studentId;
        try {
            studentId = Integer.parseInt(id.getText());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid ID number entered!");
            return;
        }

        String birthDateString;
        if (birthDate.getValue() == null) {
            showErrorAlert("Invalid date entered!");
            return;
        } else {
            birthDateString = birthDate.getValue().toString();
        }

        if (manager.exists(studentId)) {
            showErrorAlert("This id already exists");
        } else if (!studentName.isEmpty() && studentId != 0) {
            student = new Student(studentId, studentName, birthDateString);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo3/calculmoy.fxml"));
                Parent calculerMoyen = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene allScene = new Scene(calculerMoyen);
                stage.setScene(allScene);

                CalculermoyenneController controller = loader.getController();
                controller.initdata(student);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorAlert("Failed to load the next scene");
            }
        } else {
            showErrorAlert("Please fill in all fields");
        }
    }

    public void backMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo3/hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to go back to the menu");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
