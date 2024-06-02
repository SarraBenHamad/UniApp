package com.example.demo3;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CalculermoyenneController {

    @FXML
    private GridPane container;

    @FXML
    private TextField moyen;

    private Student student;

    public void initdata(Student student) {
        this.student = student;
    }

    @FXML
    public void calculate() {
        student.initGrades();
        if (collectData()) {
            student.calculateAverage();
            moyen.setText(String.valueOf(student.getAverage()));
        }
    }

    private boolean collectData() {
        for (Node node : container.getChildren()) {
            if (node instanceof AnchorPane) {
                AnchorPane subjectPane = (AnchorPane) node;
                String subjectName = subjectPane.getId();
                int coef = 2;  // Default coefficient
                if (subjectName.equals("java")) {
                    coef = 1;
                } else if (subjectName.equals("mathematics")) {
                    coef = 2;
                } else if (subjectName.equals("architecture")) {
                    coef = 1;
                }

                TextField examTextField = (TextField) subjectPane.lookup("#exam");
                TextField dsTextField = (TextField) subjectPane.lookup("#ds");
                TextField tpTextField = (TextField) subjectPane.lookup("#tp");

                try {
                    double exam = examTextField != null ? validateGrade(examTextField.getText(), subjectName, "Exam") : 0;
                    double ds = dsTextField != null ? validateGrade(dsTextField.getText(), subjectName, "DS") : 0;
                    double tp = tpTextField != null ? validateGrade(tpTextField.getText(), subjectName, "TP") : 0;

                    Note note;
                    if (examTextField != null && dsTextField != null && tpTextField != null) {
                        note = new Note(exam, ds, tp);
                    } else if (examTextField != null && dsTextField != null) {
                        note = new Note(exam, ds);
                    } else if (examTextField != null) {
                        note = new Note(exam);
                    } else {
                        continue; // Skip this subject if no grades are provided
                    }

                    Subject subject = new Subject(subjectName, coef, note);
                    student.addGrade(subject);

                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input for " + subjectName + ": " + e.getMessage());
                    return false;
                } catch (IllegalArgumentException e) {
                    showErrorAlert(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    private double validateGrade(String gradeInput, String subjectName, String gradeType) {
        double grade;
        try {
            grade = Double.parseDouble(gradeInput);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(gradeType + " grade for " + subjectName + " is not a valid number.");
        }
        if (grade < 0 || grade > 20) {
            throw new IllegalArgumentException(gradeType + " grade for " + subjectName + " must be between 0 and 20.");
        }
        return grade;
    }

    @FXML
    public void save() {
        MongoDBManager manager = new MongoDBManager();
        if (collectData()) { // Ensure the data is valid before saving
            calculate(); // In case the user didn't click calculate first and saved directly
            if (manager.exists(student.getId())) {
                manager.updateStudent(student);
            } else {
                manager.insertStudent(student);
            }
            manager.close();
        }
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene StudentsSpace = new Scene(root);
        stage.setScene(StudentsSpace);
        stage.show();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

