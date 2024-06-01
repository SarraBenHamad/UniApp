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
import java.util.ArrayList;
import java.util.List;
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
                        setStyle(item.getAverage() > 10 ? "-fx-background-color: lightgreen;" : "-fx-background-color: indianred;");
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

    /*@FXML
    private void updateSelectedStudent(ActionEvent event) {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            showUpdateDialog(selectedStudent);
        } else {
            showErrorAlert("No student selected for update");
        }
    }*/







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

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
