package com.example.demo3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        collectData();
        student.calculateAverage();
        moyen.setText(String.valueOf(student.getAverage()));
    }

    private void collectData() {
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
                    double exam = examTextField != null ? Double.parseDouble(examTextField.getText()) : 0;
                    double ds = dsTextField != null ? Double.parseDouble(dsTextField.getText()) : 0;
                    double tp = tpTextField != null ? Double.parseDouble(tpTextField.getText()) : 0;

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
                    System.out.println("Invalid input for " + subjectName + ": " + e.getMessage());
                }
            }
        }
    }

    @FXML
    public void save() {
        MongoDBManager manager = new MongoDBManager();
        manager.insertStudent(student);
        manager.close();
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene StudentsSpace = new Scene(root);
        stage.setScene(StudentsSpace);

        stage.show();
    }
}
