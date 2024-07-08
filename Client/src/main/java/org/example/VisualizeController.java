package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.models.Movie;
import org.example.models.MovieGenre;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class VisualizeController {
    private Client client;

    @FXML
    private Button BackToMenuButton;

    @FXML
    private Canvas movieCanvas;
    @FXML
    private Canvas overlayCanvas; // New canvas for handling mouse events

    private static final double SCALE = 0.01; // 1:200 scale (10000/200 = 50)
    private static final double BASE_IMAGE_SIZE = 25;
    private Random random = new Random();
    private ObservableList<Movie> movies;

    public VisualizeController(Client client) {
        this.client = client;
    }

    @FXML
    private void initialize() {
        drawMovies();
        startAnimation();
    }

    private ObservableList<Movie> getMovies() {
        LinkedList<Movie> movieList = (LinkedList<Movie>) client.sendCommand("getLinkedList", "").getObject();
        return FXCollections.observableArrayList(movieList);
    }

    @FXML
    private void handleRefreshCanvasButtonAction(ActionEvent event) {
        drawMovies();
    }

    private void drawMovies() {
        GraphicsContext gc = movieCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, movieCanvas.getWidth(), movieCanvas.getHeight());

        // Draw grid for better visualization
        drawGrid(gc);

        movies = getMovies();
        for (Movie movie : movies) {
            drawMovie(gc, movie, 0, 0); // Initial draw without animation offset
        }

        // Setup click handler
        setupClickHandlers();
    }

    private void drawGrid(GraphicsContext gc) {
        double width = movieCanvas.getWidth();
        double height = movieCanvas.getHeight();
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        // Draw vertical lines
        for (int i = 0; i < width; i += 10) {
            gc.strokeLine(i, 0, i, height);
        }

        // Draw horizontal lines
        for (int i = 0; i < height; i += 10) {
            gc.strokeLine(0, i, width, i);
        }

        // Draw X and Y axes
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(0, height / 2, width, height / 2); // X axis
        gc.strokeLine(width / 2, 0, width / 2, height); // Y axis

        // Draw axis labels
        gc.setFill(Color.BLACK);
        gc.fillText("X", width - 10, height / 2 - 10);
        gc.fillText("Y", width / 2 + 10, 10);
    }

    private void drawMovie(GraphicsContext gc, Movie movie, double offsetX, double offsetY) {
        double x = movie.getCoordinates().getX() * SCALE + movieCanvas.getWidth() / 2 + offsetX;
        double y = movie.getCoordinates().getY() * SCALE + movieCanvas.getHeight() / 2 + offsetY;

        // Check if coordinates are within the canvas
        if (x < 0 || x > movieCanvas.getWidth() || y < 0 || y > movieCanvas.getHeight()) {
            System.err.println("Coordinates out of bounds: (" + x + ", " + y + ")");
            return;
        }

        Image image = getImageForGenre(movie.getGenre());
        if (image != null) {
            double imageSize = BASE_IMAGE_SIZE; // Base size
            int oscarsCount = movie.getOscarsCount();
            if (oscarsCount > 1 && oscarsCount <= 5) {
                imageSize *= 1.5;
            } else if (oscarsCount > 5) {
                imageSize *= 2;
            }

            gc.drawImage(image, x - imageSize / 2, y - imageSize / 2, imageSize, imageSize); // Draw image centered at coordinates (x, y)
        } else {
            System.err.println("Image for genre " + movie.getGenre() + " not found.");
        }
    }

    private Image getImageForGenre(MovieGenre genre) {
        String imagePath = switch (genre) {
            case MUSICAL -> "/images/Musical.png";
            case ADVENTURE -> "/images/Adventure.png";
            case TRAGEDY -> "/images/Tragedy.png";
            case THRILLER -> "/images/Thriller.png";
            case FANTASY -> "/images/Fantasy.png";
        };

        Image image = null;
        try {
            image = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath);
        }
        return image;
    }

    private void setupClickHandlers() {
        overlayCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            for (Movie movie : movies) {
                double x = movie.getCoordinates().getX() * SCALE + movieCanvas.getWidth() / 2;
                double y = movie.getCoordinates().getY() * SCALE + movieCanvas.getHeight() / 2;

                if (mouseX >= x - 12.5 && mouseX <= x + 12.5 && mouseY >= y - 12.5 && mouseY <= y + 12.5) {
                    showConfirmationDialog(movie);
                    break;
                }
            }
        });
    }

    private void showConfirmationDialog(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("confirmation-dialog.fxml"), client.getResourceBundle());
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            ConfirmationDialogController controller = loader.getController();
            controller.setSelectedMovie(movie);
            controller.setVisualizeController(this);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEdit(Movie movie) {
        // Open the edit window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new-movie.fxml"), client.getResourceBundle());
            loader.setControllerFactory(param -> new NewMovieController(client, "update", String.valueOf(movie.getId())));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Edit Movie");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDelete(Movie movie) {
        // Send delete command to server
        client.sendCommand("remove_by_id " + movie.getId(), "");

        // Remove movie from the local list and redraw
        movies.remove(movie);
        drawMovies();
    }

    private void startAnimation() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            GraphicsContext gc = movieCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, movieCanvas.getWidth(), movieCanvas.getHeight());
            drawGrid(gc);
            for (Movie movie : movies) {
                double offsetX = random.nextDouble() * 4 - 2; // Random offset between -2 and 2
                double offsetY = random.nextDouble() * 4 - 2; // Random offset between -2 and 2
                drawMovie(gc, movie, offsetX, offsetY);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void switchToMain(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"), client.getResourceBundle());
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
