package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ResourceBundle;

public class AuthorizationController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private final Client client;

    @FXML
    private TextField usernameFieldL;
    @FXML
    private PasswordField passwordFieldL;
    @FXML
    private TextField usernameFieldR;
    @FXML
    private PasswordField passwordFieldR;
    @FXML
    private PasswordField acceptPasswordFieldR;
    @FXML
    private Label errorLabel;

    public AuthorizationController(Client client) {
        this.client = client;
    }

    public void switchToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"), client.getResourceBundle());
        loader.setControllerFactory(param -> new AuthorizationController(client));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"), client.getResourceBundle());
        loader.setControllerFactory(param -> new AuthorizationController(client));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleLogin(ActionEvent event) throws IOException, ClassNotFoundException {
        String username = usernameFieldL.getText();
        String password = passwordFieldL.getText();
        if (client.login(username, password)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"), client.getResourceBundle());
            loader.setControllerFactory(param -> new HelloViewController(client));
            root = loader.load();
            HelloViewController helloViewController = loader.getController();
            helloViewController.setUsername(username);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            errorLabel.setText(client.getResourceBundle().getString("error_invalid_credentials"));
            errorLabel.setVisible(true);
        }
    }

    public void handleRegister(ActionEvent event) throws IOException, ClassNotFoundException {
        String userName = usernameFieldR.getText();
        String password1 = passwordFieldR.getText();
        String password2 = acceptPasswordFieldR.getText();
        if (client.register(userName, password1, password2)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"), client.getResourceBundle());
            loader.setControllerFactory(param -> new HelloViewController(client));
            root = loader.load();
            HelloViewController helloViewController = loader.getController();
            helloViewController.setUsername(userName);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println(client.getResourceBundle().getString("register_error"));
        }
    }
}
