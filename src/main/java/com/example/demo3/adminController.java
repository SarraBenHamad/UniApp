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
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

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

    private void showUpdateDialog(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Update Student");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(student.getName());
        TextField birthDateField = new TextField(student.getBirthDate());
        TextField averageField = new TextField(String.valueOf(student.getAverage()));

        // Initialize TextField objects for each grade
        TextField javaExamField = new TextField();
        TextField mathematicsExamField = new TextField();
        TextField mathematicsDsField = new TextField();
        TextField architectureExamField = new TextField();
        TextField architectureDsField = new TextField();
        TextField architectureTpField = new TextField();
        TextField computersExamField = new TextField();
        TextField computersDsField = new TextField();
        TextField computersTpField = new TextField();

        // Fetch existing grades for the student
        Map<String, Double[]> grades = manager.getStudentGrades(student.getId());

        // Set existing grades to the TextField objects
        if (grades.containsKey("java")) {
            javaExamField.setText(grades.get("java")[0].toString());
        }
        if (grades.containsKey("mathematics")) {
            Double[] mathGrades = grades.get("mathematics");
            mathematicsExamField.setText(mathGrades[0].toString());
            if (mathGrades.length > 1 && mathGrades[1] != null) {
                mathematicsDsField.setText(mathGrades[1].toString());
            }
        }
        if (grades.containsKey("architecture")) {
            Double[] archGrades = grades.get("architecture");
            architectureExamField.setText(archGrades[0].toString());
            if (archGrades.length > 1 && archGrades[1] != null) {
                architectureDsField.setText(archGrades[1].toString());
                if (archGrades.length > 2 && archGrades[2] != null) {
                    architectureTpField.setText(archGrades[2].toString());
                }
            }
        }
        if (grades.containsKey("computers")) {
            Double[] compGrades = grades.get("computers");
            computersExamField.setText(compGrades[0].toString());
            if (compGrades.length > 1 && compGrades[1] != null) {
                computersDsField.setText(compGrades[1].toString());
                if (compGrades.length > 2 && compGrades[2] != null) {
                    computersTpField.setText(compGrades[2].toString());
                }
            }
        }

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
                student.setFullname(nameField.getText());
                student.setBirthDate(birthDateField.getText());
                student.setAverage(Double.parseDouble(averageField.getText()));

                // Clear existing grades before adding new ones
                student.initGrades();

                // Add grades for each subject


                if (!addGradeIfNotEmpty(javaExamField.getText(), "java", student, 1, true, false, false) ||
                        !addGradeIfNotEmpty(mathematicsExamField.getText(), mathematicsDsField.getText(), "mathematics", student, 2, true, true, false) ||
                        !addGradeIfNotEmpty(architectureExamField.getText(), architectureDsField.getText(), architectureTpField.getText(), "architecture", student, 1, true, true, true) ||
                        !addGradeIfNotEmpty(computersExamField.getText(), computersDsField.getText(), computersTpField.getText(), "computers", student, 2, true, true, true)) {
                    return null ; // Don't close the dialog if any input is invalid
                }

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
            manager.updateStudent(updatedStudent);
            table.refresh();
        });
    }

    private boolean addGradeIfNotEmpty(String gradeInputExam, String subjectName, Student student, int coef, boolean hasExam, boolean hasDs, boolean hasTp) {
        if (!gradeInputExam.isEmpty()) {
            double exam = Double.parseDouble(gradeInputExam);
            if (exam >= 0 && exam <= 20) { // Validate grade range
                Note note = null;
                if (hasExam && (!hasDs) && (!hasTp)) {
                    note = new Note(exam);
                }
                Subject subject = new Subject(subjectName, coef, note);
                student.addGrade(subject);
                return true;
            } else {
                // Handle invalid grade input
                showErrorAlert("Invalid grade input: Grade must be between 0 and 20");
                return false;
            }
        }
        return true;
    }

    private boolean addGradeIfNotEmpty(String gradeInputExam, String gradeInputDs, String subjectName, Student student, int coef, boolean hasExam, boolean hasDs, boolean hasTp) {
        if (!gradeInputExam.isEmpty() && !gradeInputDs.isEmpty()) {
            double exam = Double.parseDouble(gradeInputExam);
            double ds = Double.parseDouble(gradeInputDs);
            if (exam >= 0 && exam <= 20 && ds >= 0 && ds <= 20) { // Validate grade range
                Note note = null;
                if (hasExam && hasDs && (!hasTp)) {
                    note = new Note(exam, ds);
                }
                Subject subject = new Subject(subjectName, coef, note);
                student.addGrade(subject);
                return true;
            } else {
                // Handle invalid grade input
                showErrorAlert("Invalid grade input: Grades must be between 0 and 20");
                return false ;
            }
        }
        return true;
    }

    private boolean addGradeIfNotEmpty(String gradeInputExam, String gradeInputDs, String gradeInputTp, String subjectName, Student student, int coef, boolean hasExam, boolean hasDs, boolean hasTp) {
        if (!gradeInputExam.isEmpty() && !gradeInputDs.isEmpty() && !gradeInputTp.isEmpty()) {
            double exam = Double.parseDouble(gradeInputExam);
            double ds = Double.parseDouble(gradeInputDs);
            double tp = Double.parseDouble(gradeInputTp);
            if (exam >= 0 && exam <= 20 && ds >= 0 && ds <= 20 && tp >= 0 && tp <= 20) { // Validate grade range
                Note note = null;
                if (hasExam && hasDs && hasTp) {
                    note = new Note(exam, ds, tp);
                }
                Subject subject = new Subject(subjectName, coef, note);
                student.addGrade(subject);
                return true;
            } else {
                // Handle invalid grade input
                showErrorAlert("Invalid grade input: Grades must be between 0 and 20");
                return false;
            }
        }
        return true ;
    }

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
