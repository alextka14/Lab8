package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.example.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.function.UnaryOperator;

public class NewMovieController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField oscarsCountField;
    @FXML
    private TextField budgetField;
    @FXML
    private TextField taglineField;
    @FXML
    private ChoiceBox<String> genreChoiceBox;
    @FXML
    private TextField personNameField;
    @FXML
    private DatePicker birthdayPicker;
    @FXML
    private TextField locationXField;
    @FXML
    private TextField locationYField;
    @FXML
    private TextField locationZField;
    @FXML
    private TextField locationNameField;

    private HelloViewController helloViewController;

    @FXML
    public void initialize() {
        configureValidation();
        genreChoiceBox.getItems().addAll("MUSICAL", "ADVENTURE", "TRAGEDY", "THRILLER", "FANTASY");
        birthdayPicker.getEditor().setDisable(true);
    }
    private Client client;
    private String commandName = "update";
    private String args;
    public NewMovieController(Client client, String commandName){
        this.client = client;
        this.commandName = commandName;
        this.args = "";
    }
    public NewMovieController(Client client, String commandName, String args){
        this.client = client;
        this.commandName = commandName;
        this.args = args;
    }
    public NewMovieController(Client client){
        this.client = client;
        this.args = "";
    }
    public void setHelloViewController(HelloViewController helloViewController) {
        this.helloViewController = helloViewController;
    }

    private void configureValidation() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        };

        UnaryOperator<TextFormatter.Change> longFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        };

        coordinatesXField.setTextFormatter(new TextFormatter<>(integerFilter));
        coordinatesYField.setTextFormatter(new TextFormatter<>(longFilter));
        oscarsCountField.setTextFormatter(new TextFormatter<>(integerFilter));
        budgetField.setTextFormatter(new TextFormatter<>(integerFilter));
        locationXField.setTextFormatter(new TextFormatter<>(longFilter));
        locationYField.setTextFormatter(new TextFormatter<>(longFilter));
        locationZField.setTextFormatter(new TextFormatter<>(longFilter));
    }



    private boolean validateIntegerField(TextField field) {
        return validateIntegerField(field, false);
    }

    private boolean validateIntegerField(TextField field, boolean allowNull) {
        try {
            String text = field.getText();
            if (allowNull && text.isEmpty()) {
                return true;
            }
            int value = Integer.parseInt(text);
            if (value > 0) {
                highlightField(field, false);
                return true;
            }
        } catch (NumberFormatException e) {
            // Ignore exception
        }
        highlightField(field, true);
        return false;
    }

    private boolean validateLongField(TextField field, Long max) {
        try {
            long value = Long.parseLong(field.getText());
            if (max == null || value <= max) {
                highlightField(field, false);
                return true;
            }
        } catch (NumberFormatException e) {
            // Ignore exception
        }
        highlightField(field, true);
        return false;
    }

//    private boolean validatePersonFields() {
//        boolean isValid = true;
//
//        if (personNameField.getText().isEmpty()) {
//            highlightField(personNameField, true);
//            isValid = false;
//        } else {
//            highlightField(personNameField, false);
//        }
//
//        if (birthdayPicker.getValue() == null || birthdayPicker.getValue().isAfter(LocalDate.now())) {
//            highlightField(birthdayPicker, true);
//            isValid = false;
//        } else {
//            highlightField(birthdayPicker, false);
//        }
//
//        if (!validateLongField(locationXField, null)) {
//            isValid = false;
//        }
//
//        if (!validateLongField(locationYField, null)) {
//            isValid = false;
//        }
//
//        if (!validateLongField(locationZField, null)) {
//            isValid = false;
//        }
//
//        if (locationNameField.getText().isEmpty()) {
//            highlightField(locationNameField, true);
//            isValid = false;
//        } else {
//            highlightField(locationNameField, false);
//        }
//
//        return isValid;
//    }
private boolean validatePersonFields() {
    boolean isValid = true;

//    if (personNameField.getText().isEmpty()) {
//        highlightField(personNameField, true);
//        isValid = false;
//    } else {
//        highlightField(personNameField, false);
//    }
//
//    LocalDate localDate = birthdayPicker.getValue();
//    if (localDate == null || localDate.isAfter(LocalDate.now())) {
//        highlightField(birthdayPicker, true);
//        isValid = false;
//    } else {
//        highlightField(birthdayPicker, false);
//    }

    if (!validateLongField(locationXField, null)) {
        isValid = false;
    }

    if (!validateLongField(locationYField, null)) {
        isValid = false;
    }

    if (!validateLongField(locationZField, null)) {
        isValid = false;
    }

    if (locationNameField.getText().isEmpty()) {
        highlightField(locationNameField, true);
        isValid = false;
    } else {
        highlightField(locationNameField, false);
    }

    return isValid;
}

    @FXML
    private void handleSubmit() {
        boolean isValid = true;

        if (nameField.getText().isEmpty()) {
            highlightField(nameField, true);
            isValid = false;
        } else {
            highlightField(nameField, false);
        }

        if (!validateIntegerField(coordinatesXField)) {
            isValid = false;
        }

        if (!validateLongField(coordinatesYField, 628L)) {
            isValid = false;
        }

        if (!validateIntegerField(oscarsCountField, true)) {
            isValid = false;
        }

        if (!validateIntegerField(budgetField)) {
            isValid = false;
        }

        if (taglineField.getText().isEmpty()) {
            highlightField(taglineField, true);
            isValid = false;
        } else {
            highlightField(taglineField, false);
        }

        if (!validatePersonFields()) {
            isValid = false;
        }

        if (genreChoiceBox.getValue() == null) {
            highlightField(genreChoiceBox, true);
            isValid = false;
        } else {
            highlightField(genreChoiceBox, false);
        }

        if (isValid) {
            String name = nameField.getText();
            long x = Long.parseLong(coordinatesXField.getText());
            int y = Integer.parseInt(coordinatesYField.getText());
            int oscarsCount = Integer.parseInt(oscarsCountField.getText());
            double budget = Double.parseDouble(budgetField.getText());
            String tagline = taglineField.getText();
            MovieGenre movieGenre = MovieGenre.valueOf(genreChoiceBox.getValue());
            String personName = personNameField.getText();
            LocalDate localDate = birthdayPicker.getValue();
            LocalDateTime personBirthday = null;
            if (localDate != null) {
                personBirthday = localDate.atStartOfDay();
            }

            float personLocationX = Float.parseFloat(locationXField.getText());
            Float personLocationY = Float.parseFloat(locationYField.getText());
            float personLocationZ = Float.parseFloat(locationZField.getText());
            String personLocationName = locationNameField.getText();
            Location location = new Location(personLocationX, personLocationY, personLocationZ, personLocationName);
            Person screenwriter = new Person(personName, personBirthday, location);
            Coordinates coordinates = new Coordinates(x, y);

            Movie newMovie = new Movie(9999, name, coordinates, new Date(), oscarsCount, budget, tagline, movieGenre, screenwriter);
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
            showAlert(commandName, client.sendCommand(commandName + " " + args, newMovie).getMessage());
        }
    }


    private void highlightField(Control field, boolean highlight) {
        if (highlight) {
            field.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else {
            field.setStyle(null);
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
