module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.models to javafx.base;
    opens org.example to javafx.fxml;
    exports org.example;
}