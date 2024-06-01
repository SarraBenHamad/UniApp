module com.example.demo3 {
    requires javafx.controls;
    requires javafx.fxml;

    /*requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;*/
    requires java.desktop;
    requires mongo.java.driver;

    opens com.example.demo3 to javafx.fxml;
    exports com.example.demo3;
}