package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.models.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

public class TableController {
    private Client client;

    @FXML
    Button BackToMenuButton;

    @FXML
    private TableView<Movie> movieTable;

    @FXML
    private TableColumn<Movie, Integer> idColumn;

    @FXML
    private TableColumn<Movie, String> nameColumn;

    @FXML
    private TableColumn<Movie, String> coordinatesX;

    @FXML
    private TableColumn<Movie, String> coordinatesY;

    @FXML
    private TableColumn<Movie, Integer> oscarsCountColumn;

    @FXML
    private TableColumn<Movie, Integer> budgetColumn;

    @FXML
    private TableColumn<Movie, String> taglineColumn;

    @FXML
    private TableColumn<Movie, String> genreColumn;

    @FXML
    private TableColumn<Movie, String> screenWriterNameColumn;

    @FXML
    private TableColumn<Movie, String> screenWriterBirth;

    @FXML
    private TableColumn<Movie, String> locationX;

    @FXML
    private TableColumn<Movie, String> locationY;

    @FXML
    private TableColumn<Movie, String> locationZ;

    @FXML
    private TableColumn<Movie, String> locationName;

    public TableController(Client client) {
        this.client = client;
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        oscarsCountColumn.setCellValueFactory(new PropertyValueFactory<>("oscarsCount"));
        budgetColumn.setCellValueFactory(new PropertyValueFactory<>("budget"));
        taglineColumn.setCellValueFactory(new PropertyValueFactory<>("tagline"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        coordinatesX.setCellValueFactory(cellData -> {
            long x = cellData.getValue().getCoordinates().getX();
            return new SimpleStringProperty(Long.toString(x));
        });

        coordinatesY.setCellValueFactory(cellData -> {
            long y = cellData.getValue().getCoordinates().getY();
            return new SimpleStringProperty(Long.toString(y));
        });

        screenWriterNameColumn.setCellValueFactory(cellData -> {
            String name = cellData.getValue().getScreenWriter().getName();
            return new SimpleStringProperty(name != null ? name : "");
        });

        screenWriterBirth.setCellValueFactory(cellData -> {
            LocalDate birth = cellData.getValue().getScreenWriter().getBirthday();
            return new SimpleStringProperty(birth != null ? birth.toString() : "");
        });

        locationX.setCellValueFactory(cellData -> {
            Person screenwriter = cellData.getValue().getScreenWriter();
            if (screenwriter != null && screenwriter.getLocation() != null) {
                float x = screenwriter.getLocation().getX();
                return new SimpleStringProperty(Float.toString(x));
            } else {
                return new SimpleStringProperty("");
            }
        });

        locationY.setCellValueFactory(cellData -> {
            Person screenwriter = cellData.getValue().getScreenWriter();
            if (screenwriter != null && screenwriter.getLocation() != null) {
                Float y = screenwriter.getLocation().getY();
                return new SimpleStringProperty(y != null ? y.toString() : "");
            } else {
                return new SimpleStringProperty("");
            }
        });

        locationZ.setCellValueFactory(cellData -> {
            Person screenwriter = cellData.getValue().getScreenWriter();
            if (screenwriter != null && screenwriter.getLocation() != null) {
                float z = screenwriter.getLocation().getZ();
                return new SimpleStringProperty(Float.toString(z));
            } else {
                return new SimpleStringProperty("");
            }
        });

        locationName.setCellValueFactory(cellData -> {
            Person screenwriter = cellData.getValue().getScreenWriter();
            if (screenwriter != null && screenwriter.getLocation() != null) {
                String name = screenwriter.getLocation().getName();
                return new SimpleStringProperty(name != null ? name : "");
            } else {
                return new SimpleStringProperty("");
            }
        });

        movieTable.setItems(getMovies());

        movieTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && movieTable.getSelectionModel().getSelectedItem() != null) {
                Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
                openConfirmationDialog(selectedMovie);
            }
        });
    }

    private ObservableList<Movie> getMovies() {
        LinkedList<Movie> movieList = new LinkedList<>();
        movieList = (LinkedList<Movie>) client.sendCommand("getLinkedList", "").getObject();
        return FXCollections.observableArrayList(movieList);
    }

    @FXML
    private void handleRefreshTableButtonAction(ActionEvent event) {
        // Обновление данных таблицы
        movieTable.setItems(getMovies());
    }

    private void openConfirmationDialog(Movie selectedMovie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("confirmation-dialog.fxml"), client.getResourceBundle());
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            ConfirmationDialogController controller = loader.getController();
            controller.setSelectedMovie(selectedMovie);
            controller.setTableController(this);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openEditMovieWindow(Movie selectedMovie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new-movie.fxml"));
            loader.setControllerFactory(param -> new NewMovieController(client, "update", "" + selectedMovie.getId()));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Изменить фильм");
            NewMovieController controller = loader.getController();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(Movie movie) {
        client.sendCommand("remove_by_id " + movie.getId(), "");
        movieTable.setItems(getMovies()); // Обновляем таблицу
    }

    @FXML
    private void switchToMain(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            fxmlLoader.setControllerFactory(param -> new HelloViewController(client));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) BackToMenuButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
