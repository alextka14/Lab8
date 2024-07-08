package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMain extends Application {
    private Client client;

    @Override
    public void init() {
        // Создание объекта Client при запуске приложения
        client = new Client("localhost", 8000);
    }

    @Override
    public void start(Stage stage) throws IOException {
        showLanguageSelection(stage);
    }

    private void showLanguageSelection(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("language-selection.fxml"), client.getResourceBundle());
        Scene scene = new Scene(loader.load());
        LanguageSelectionController controller = loader.getController();
        controller.setStage(stage);
        controller.setClient(client);
        stage.setScene(scene);
        stage.setTitle(null);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
