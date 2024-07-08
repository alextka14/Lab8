package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageSelectionController {
    private Stage stage;
    private Client client;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private void setRussian() {
        setLanguage(new Locale("ru", "RU"));
    }

    @FXML
    private void setLithuanian() {
        setLanguage(new Locale("lt", "LT"));
    }

    @FXML
    private void setSpanish() {
        setLanguage(new Locale("es", "MX"));
    }

    @FXML
    private void setTurkish() {
        setLanguage(new Locale("tr", "TR"));
    }

    private void setLanguage(Locale locale) {
        Locale.setDefault(locale);
        ResourceBundle bundle = ResourceBundle.getBundle("org.example.messages", locale);
        client.setResourceBundle(bundle);
        loadLoginScene();
    }

    private void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"), client.getResourceBundle());
            loader.setControllerFactory(type -> new AuthorizationController(client));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
