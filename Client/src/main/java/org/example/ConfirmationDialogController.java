package org.example;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import org.example.models.Movie;

public class ConfirmationDialogController {
    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private Movie selectedMovie;
    private TableController tableController;
    private VisualizeController visualizeController;

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public void setTableController(TableController tableController) {
        this.tableController = tableController;
    }

    public void setVisualizeController(VisualizeController visualizeController) {
        this.visualizeController = visualizeController;
    }

    @FXML
    private void handleEdit() {
        if (tableController != null) {
            tableController.openEditMovieWindow(selectedMovie);
        } else if (visualizeController != null) {
            visualizeController.handleEdit(selectedMovie);
        }
        closeWindow();
    }

    @FXML
    private void handleDelete() {
        if (tableController != null) {
            tableController.deleteMovie(selectedMovie);
        } else if (visualizeController != null) {
            visualizeController.handleDelete(selectedMovie);
        }
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }
}

//package org.example;
//
//import javafx.fxml.FXML;
//import javafx.stage.Stage;
//import javafx.scene.control.Button;
//import org.example.models.Movie;
//
//public class ConfirmationDialogController {
//    @FXML
//    private Button editButton;
//
//    @FXML
//    private Button deleteButton;
//
//    private Movie selectedMovie;
//    private TableController tableController;
//
//    public void setSelectedMovie(Movie selectedMovie) {
//        this.selectedMovie = selectedMovie;
//    }
//
//    public void setTableController(TableController tableController) {
//        this.tableController = tableController;
//    }
//
//    @FXML
//    private void handleEdit() {
//        tableController.openEditMovieWindow(selectedMovie);
//        closeWindow();
//    }
//
//    @FXML
//    private void handleDelete() {
//        tableController.deleteMovie(selectedMovie);
//        closeWindow();
//    }
//
//    private void closeWindow() {
//        Stage stage = (Stage) editButton.getScene().getWindow();
//        stage.close();
//    }
//}
////package org.example;
////
////import javafx.fxml.FXML;
////import javafx.scene.control.Button;
////import javafx.stage.Stage;
////import org.example.models.Movie;
////
////public class ConfirmationDialogController {
////    @FXML
////    private Button editButton;
////
////    @FXML
////    private Button deleteButton;
////    private TableController tableController;
////    private Movie selectedMovie;
////    private VisualizeController visualizeController;
////
////    public void setSelectedMovie(Movie selectedMovie) {
////        this.selectedMovie = selectedMovie;
////    }
////    public void setTableController(TableController tableController) {
////        this.tableController = tableController;
////    }
////    public void setVisualizeController(VisualizeController visualizeController) {
////        this.visualizeController = visualizeController;
////    }
////
////    @FXML
////    private void handleEdit() {
////        visualizeController.handleEdit(selectedMovie);
////        closeWindow();
////    }
////
////    @FXML
////    private void handleDelete() {
////        visualizeController.handleDelete(selectedMovie);
////        closeWindow();
////    }
////
////    private void closeWindow() {
////        Stage stage = (Stage) editButton.getScene().getWindow();
////        stage.close();
////    }
////}
//
