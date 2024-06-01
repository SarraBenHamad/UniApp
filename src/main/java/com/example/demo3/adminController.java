package com.example.demo3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class adminController implements Initializable {

    private String txt;
    private double nb;
    private int order;

    @FXML
    private TableView<Student> table;
    @FXML
    private TableColumn<Student, Double> avg;
    @FXML
    private TableColumn<Student, String> birthdate;
    @FXML
    private TableColumn<Student, Integer> id;
    @FXML
    private TableColumn<Student, String> name;

    private MongoDBManager manager = new MongoDBManager();
    private ObservableList<Student> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            list = FXCollections.observableArrayList(manager.getStudents(nb, -1));

            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            birthdate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
            avg.setCellValueFactory(new PropertyValueFactory<>("average"));

            table.setItems(list);

            table.setRowFactory(tv -> new TableRow<>() {
                @Override
                protected void updateItem(Student item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else {
                        setStyle(item.getAverage() >= 10 ? "-fx-background-color: lightgreen;" : "-fx-background-color: #d57171;");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Initialization error: " + e.getMessage());
        }
    }

    @FXML
    private void deleteSelectedStudent(ActionEvent event) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            manager.deleteStudent(selectedStudent.getId());
            table.getItems().remove(selectedStudent);
        } else {
            showErrorAlert("No student selected for deletion");
        }
    }

    @FXML
    private void updateSelectedStudent(ActionEvent event) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            showUpdateDialog(selectedStudent);
        } else {
            showErrorAlert("No student selected for update");
        }
    }
/* hedhaa yeemchiiiii
    private void showUpdateDialog(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Update Student");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create text fields for each field to update
        TextField nameField = new TextField(student.getName());
        TextField birthDateField = new TextField(student.getBirthDate());
        TextField averageField = new TextField(String.valueOf(student.getAverage()));

        // Create text fields for each subject's grade
        TextField javaExamField = new TextField();
        TextField mathematicsExamField = new TextField();
        TextField mathematicsDsField = new TextField();
        TextField architectureExamField = new TextField();
        TextField architectureDsField = new TextField();
        TextField architectureTpField = new TextField();
        TextField computersExamField = new TextField();
        TextField computersDsField = new TextField();
        TextField computersTpField = new TextField();

        GridPane grid = new GridPane();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Birth Date:"), 0, 1);
        grid.add(birthDateField, 1, 1);
        grid.add(new Label("Average:"), 0, 2);
        grid.add(averageField, 1, 2);

        // Add fields for each subject's grade
        grid.add(new Label("Java Exam:"), 0, 3);
        grid.add(javaExamField, 1, 3);
        grid.add(new Label("Mathematics Exam:"), 0, 4);
        grid.add(mathematicsExamField, 1, 4);
        grid.add(new Label("Mathematics DS:"), 0, 5);
        grid.add(mathematicsDsField, 1, 5);
        grid.add(new Label("Architecture Exam:"), 0, 6);
        grid.add(architectureExamField, 1, 6);
        grid.add(new Label("Architecture DS:"), 0, 7);
        grid.add(architectureDsField, 1, 7);
        grid.add(new Label("Architecture TP:"), 0, 8);
        grid.add(architectureTpField, 1, 8);
        grid.add(new Label("Computers Exam:"), 0, 9);
        grid.add(computersExamField, 1, 9);
        grid.add(new Label("Computers DS:"), 0, 10);
        grid.add(computersDsField, 1, 10);
        grid.add(new Label("Computers TP:"), 0, 11);
        grid.add(computersTpField, 1, 11);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                // Update the student object with the new values from the text fields
                student.setFullname(nameField.getText());
                student.setBirthDate(birthDateField.getText());

                // Clear existing grades before adding new ones
                student.initGrades();

                // Add grades for each subject
                addGradeIfNotEmpty(javaExamField.getText(), "java", student, 1, true, false, false);
                addGradeIfNotEmpty(mathematicsExamField.getText(), "mathematics", student, 2, true, true, false);
                addGradeIfNotEmpty(mathematicsDsField.getText(), "mathematics", student, 2, true, true, false);
                addGradeIfNotEmpty(architectureExamField.getText(), "architecture", student, 1, true, true, true);
                addGradeIfNotEmpty(architectureDsField.getText(), "architecture", student, 1, true, true, true);
                addGradeIfNotEmpty(architectureTpField.getText(), "architecture", student, 1, true, true, true);
                addGradeIfNotEmpty(computersExamField.getText(), "computers", student, 1, true, true, true);
                addGradeIfNotEmpty(computersDsField.getText(), "computers", student, 1, true, true, true);
                addGradeIfNotEmpty(computersTpField.getText(), "computers", student, 1, true, true, true);

                // Recalculate the average
                student.calculateAverage();
                // Update the average field
                averageField.setText(String.valueOf(student.getAverage()));
                return student;
            }
            return null;
        });

        // Show the dialog
        Optional<Student> result = dialog.showAndWait();
        // Handle the result if needed
        result.ifPresent(updatedStudent -> {
            // Perform actions with the updated student if needed
            // Refresh the table view to show updated student data
            table.refresh();
        });
    }

    // Utility method to add grade if the input is not empty
    private void addGradeIfNotEmpty(String gradeInput, String subjectName, Student student, int coef, boolean hasExam, boolean hasDs, boolean hasTp) {
        if (!gradeInput.isEmpty()) {
            double grade = Double.parseDouble(gradeInput);
            Note note = null;
            if (hasExam && hasDs && hasTp) {
                note = new Note(grade, grade, grade); // You can adjust this as per the actual input fields
            } else if (hasExam && hasDs) {
                note = new Note(grade, grade);
            } else if (hasExam) {
                note = new Note(grade);
            }
            Subject subject = new Subject(subjectName, coef, note);
            student.addGrade(subject);
        }
    }
*/

    // rajja partie hedhi
    private void showUpdateDialog(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Update Student");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create text fields for each field to update
        TextField nameField = new TextField(student.getName());
        TextField birthDateField = new TextField(student.getBirthDate());
        TextField averageField = new TextField(String.valueOf(student.getAverage()));

        // Create text fields for each subject's grade
        TextField javaExamField = new TextField();
        TextField mathematicsExamField = new TextField();
        TextField mathematicsDsField = new TextField();
        TextField architectureExamField = new TextField();
        TextField architectureDsField = new TextField();
        TextField architectureTpField = new TextField();
        TextField computersExamField = new TextField();
        TextField computersDsField = new TextField();
        TextField computersTpField = new TextField();



        GridPane grid = new GridPane();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Birth Date:"), 0, 1);
        grid.add(birthDateField, 1, 1);
        grid.add(new Label("Average:"), 0, 2);
        grid.add(averageField, 1, 2);

        // Add fields for each subject's grade
        grid.add(new Label("Java Exam:"), 0, 3);
        grid.add(javaExamField, 1, 3);
        grid.add(new Label("Mathematics Exam:"), 0, 4);
        grid.add(mathematicsExamField, 1, 4);
        grid.add(new Label("Mathematics DS:"), 0, 5);
        grid.add(mathematicsDsField, 1, 5);
        grid.add(new Label("Architecture Exam:"), 0, 6);
        grid.add(architectureExamField, 1, 6);
        grid.add(new Label("Architecture DS:"), 0, 7);
        grid.add(architectureDsField, 1, 7);
        grid.add(new Label("Architecture TP:"), 0, 8);
        grid.add(architectureTpField, 1, 8);
        grid.add(new Label("Computers Exam:"), 0, 9);
        grid.add(computersExamField, 1, 9);
        grid.add(new Label("Computers DS:"), 0, 10);
        grid.add(computersDsField, 1, 10);
        grid.add(new Label("Computers TP:"), 0, 11);
        grid.add(computersTpField, 1, 11);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                // Update the student object with the new values from the text fields
                student.setFullname(nameField.getText());
                student.setBirthDate(birthDateField.getText());

                // Clear existing grades before adding new ones
                student.initGrades();

                // Add grades for each subject
                addGradeIfNotEmpty(javaExamField.getText(), "java", student, 1, true, false, false);
                addGradeIfNotEmpty(mathematicsExamField.getText(), "mathematics", student, 2, true, true, false);
                addGradeIfNotEmpty(mathematicsDsField.getText(), "mathematics", student, 2, true, true, false);
                addGradeIfNotEmpty(architectureExamField.getText(), "architecture", student, 1, true, true, true);
                addGradeIfNotEmpty(architectureDsField.getText(), "architecture", student, 1, true, true, true);
                addGradeIfNotEmpty(architectureTpField.getText(), "architecture", student, 1, true, true, true);
                addGradeIfNotEmpty(computersExamField.getText(), "computers", student, 1, true, true, true);
                addGradeIfNotEmpty(computersDsField.getText(), "computers", student, 1, true, true, true);
                addGradeIfNotEmpty(computersTpField.getText(), "computers", student, 1, true, true, true);

                // Recalculate the average
                student.calculateAverage();
                // Update the average field
                averageField.setText(String.valueOf(student.getAverage()));
                return student;
            }
            return null;
        });

        // Show the dialog
        Optional<Student> result = dialog.showAndWait();
        // Handle the result if needed
        result.ifPresent(updatedStudent -> {
            // Perform actions with the updated student if needed
            // Refresh the table view to show updated student data
            table.refresh();

            // Save the updated student to the database
            saveStudent(updatedStudent);
        });
    }




    // Method to save the updated student to the database
    private void saveStudent(Student student) {
        MongoDBManager manager = new MongoDBManager();
        manager.updateStudent(student); // Implement the updateStudent method in your MongoDBManager class
        manager.close();
    }

    private void addGradeIfNotEmpty(String gradeInput, String subjectName, Student student, int coef, boolean hasExam, boolean hasDs, boolean hasTp) {
        if (!gradeInput.isEmpty()) {
            double grade = Double.parseDouble(gradeInput);
            Note note = null;
            if (hasExam && hasDs && hasTp) {
                note = new Note(grade, grade, grade); // Adjust this to use the correct inputs
            } else if (hasExam && hasDs) {
                note = new Note(grade, grade);
            } else if (hasExam) {
                note = new Note(grade);
            }
            Subject subject = new Subject(subjectName, coef, note);
            student.addGrade(subject);
        }
    }
/*
    private void showUpdateDialog(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Update Student");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Create text fields for each field to update
        TextField nameField = new TextField(student.getName());
        TextField birthDateField = new TextField(student.getBirthDate());
        TextField averageField = new TextField(String.valueOf(student.getAverage()));

        // Create text fields for each subject's grade
        TextField javaExamField = new TextField();
        TextField mathematicsExamField = new TextField();
        TextField mathematicsDsField = new TextField();
        TextField architectureExamField = new TextField();
        TextField architectureDsField = new TextField();
        TextField computersExamField = new TextField();
        TextField computersDsField = new TextField();

        GridPane grid = new GridPane();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Birth Date:"), 0, 1);
        grid.add(birthDateField, 1, 1);
        grid.add(new Label("Average:"), 0, 2);
        grid.add(averageField, 1, 2);

        // Add fields for each subject's grade
        grid.add(new Label("Java Exam:"), 0, 3);
        grid.add(javaExamField, 1, 3);
        grid.add(new Label("Mathematics Exam:"), 0, 4);
        grid.add(mathematicsExamField, 1, 4);
        grid.add(new Label("Mathematics DS:"), 0, 5);
        grid.add(mathematicsDsField, 1, 5);
        grid.add(new Label("Architecture Exam:"), 0, 6);
        grid.add(architectureExamField, 1, 6);
        grid.add(new Label("Architecture DS:"), 0, 7);
        grid.add(architectureDsField, 1, 7);
        grid.add(new Label("Computers Exam:"), 0, 8);
        grid.add(computersExamField, 1, 8);
        grid.add(new Label("Computers DS:"), 0, 9);
        grid.add(computersDsField, 1, 9);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                // Update the student object with the new values from the text fields
                student.setFullname(nameField.getText());
                student.setBirthDate(birthDateField.getText());

                // Clear existing grades before adding new ones
                //student.clearGrades();

                // Add grades for each subject
                addGradeIfNotEmpty(javaExamField.getText(), "java", student);
                addGradeIfNotEmpty(mathematicsExamField.getText(), "mathematics", student);
                addGradeIfNotEmpty(mathematicsDsField.getText(), "mathematics", student);
                addGradeIfNotEmpty(architectureExamField.getText(), "architecture", student);
                addGradeIfNotEmpty(architectureDsField.getText(), "architecture", student);
                addGradeIfNotEmpty(computersExamField.getText(), "computers", student);
                addGradeIfNotEmpty(computersDsField.getText(), "computers", student);

                // Recalculate the average
                student.calculateAverage();
                // Update the average field
                averageField.setText(String.valueOf(student.getAverage()));
                return student;
            }
            return null;
        });

        // Show the dialog
        Optional<Student> result = dialog.showAndWait();
        // Handle the result if needed
        result.ifPresent(updatedStudent -> {
            // Perform actions with the updated student if needed
        });
    }

    // Utility method to add grade if the input is not empty
    private void addGradeIfNotEmpty(String gradeInput, String subjectName, Student student) {
        if (!gradeInput.isEmpty()) {
            double grade = Double.parseDouble(gradeInput);
            int coef = getSubjectCoefficient(subjectName);
            Note note = new Note(grade);
            Subject subject = new Subject(subjectName, coef, note);
            student.addGrade(subject);
        }
    }

    // Utility method to get the coefficient for a subject
    private int getSubjectCoefficient(String subjectName) {
        switch (subjectName) {
            case "java":
                return 1;
            case "mathematics":
                return 2;
            case "architecture":
                return 1;
            case "computers":
                return 1;
            default:
                return 0; // Default coefficient
        }
    }
*/
/* siimple
    private void showUpdateDialog(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Update Student");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(student.getName());
        TextField birthDateField = new TextField(student.getBirthDate());
        TextField averageField = new TextField(String.valueOf(student.getAverage()));

        GridPane grid = new GridPane();
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Birth Date:"), 0, 1);
        grid.add(birthDateField, 1, 1);
        grid.add(new Label("Average:"), 0, 2);
        grid.add(averageField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                student.setFullname(nameField.getText());
                student.setBirthDate(birthDateField.getText());
                student.setAverage(Double.parseDouble(averageField.getText()));
                return student;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedStudent -> {
            manager.updateStudent(updatedStudent);
            table.refresh();
        });
    }

*/


    void initData(double nb, int order, String txt) {
        this.nb = nb;
        this.order = order;
        this.txt = txt;
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
    public void AddStu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo3/studentview.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to go to Student view");
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
