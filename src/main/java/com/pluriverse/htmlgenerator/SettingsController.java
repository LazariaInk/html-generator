package com.pluriverse.htmlgenerator;

import com.pluriverse.htmlgenerator.util.Colors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {
    @FXML
    private Text settingsText;
    @FXML
    private Text darkModeText;
    @FXML
    private Text stylesForTextText;
    @FXML
    private Text stylesForImageText;
    @FXML
    private Text stylesForTitleText;
    @FXML
    private Text stylesForSubTitle;
    @FXML
    private Text htmlGeneratorByLazariaInkText;
    @FXML
    private Text mailText;


    @FXML
    private CheckBox darkModeCheckBox;

    @FXML
    private VBox settingsWindow;

    @FXML
    public void initialize() {
        initDarkModeCheckBox();
        colorElements();
    }

    @FXML
    private void handleDarkModeChange() {
        boolean isDarkModeSelected = darkModeCheckBox.isSelected();
        System.out.println(isDarkModeSelected);
        Settings.set("darkMode", Boolean.toString(isDarkModeSelected));
        Settings.save();
        colorElements();
    }

    @FXML
    public void handleStartController(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/start-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Html generator");
        stage.show();
    }

    public void colorElements() {
        boolean isDarkMode = Boolean.parseBoolean(Settings.get("darkMode"));
        String textColor = isDarkMode ? Colors.DARK_TEXT_COLOR : Colors.LIGHT_TEXT_COLOR;
        String bgColor = isDarkMode ? Colors.DARK_BG_COLOR : Colors.LIGHT_BG_COLOR;
        settingsText.setStyle("-fx-fill:" + textColor + ";");
        darkModeText.setStyle("-fx-fill:" + textColor + ";");
        stylesForTextText.setStyle("-fx-fill:" + textColor + ";");
        stylesForImageText.setStyle("-fx-fill:" + textColor + ";");
        stylesForTitleText.setStyle("-fx-fill:" + textColor + ";");
        stylesForSubTitle.setStyle("-fx-fill:" + textColor + ";");
        htmlGeneratorByLazariaInkText.setStyle("-fx-fill:" + textColor + ";");
        mailText.setStyle("-fx-fill:" + textColor + ";");
        settingsWindow.setStyle("-fx-background-color:" +  bgColor + ";");
    }

    public void initDarkModeCheckBox() {
        String darkModeValue = Settings.get("darkMode");
        if (darkModeValue == null) {
            darkModeValue = "false";
            Settings.set("darkMode", darkModeValue);
            Settings.save();
        }
        darkModeCheckBox.setSelected(Boolean.parseBoolean(darkModeValue));
    }
}

