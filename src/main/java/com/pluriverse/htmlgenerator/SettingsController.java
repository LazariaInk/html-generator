package com.pluriverse.htmlgenerator;

import com.pluriverse.htmlgenerator.util.Colors;
import com.pluriverse.htmlgenerator.util.I18nUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
    private MenuButton languageDropdown;
    @FXML
    private Text languageText;

    @FXML
    public void initialize() {
        handleLanguageChange(Settings.get("language"));
        initDarkModeCheckBox();
        colorElements();
        setTextToElements();
        initLanguageDropdown();
        reloadTexts();
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
        settingsWindow.setStyle("-fx-background-color:" + bgColor + ";");
        languageText.setStyle("-fx-fill:" + textColor + ";");
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

    private void initLanguageDropdown() {
        languageDropdown.setText(Settings.get("language"));
        for (MenuItem item : languageDropdown.getItems()) {
            item.setOnAction(event -> handleLanguageChange(item.getText()));
        }
    }

    private void handleLanguageChange(String languageCode) {
        languageDropdown.setText(languageCode);
        Settings.set("language", languageCode);
        Settings.save();
        I18nUtils.loadLanguage();
        reloadTexts();
    }

    private void reloadTexts() {
        settingsText.setText(I18nUtils.getText("settings"));
        darkModeText.setText(I18nUtils.getText("dark_mode"));
        stylesForImageText.setText(I18nUtils.getText("styles_for_image"));
        stylesForTextText.setText(I18nUtils.getText("styles_for_text"));
        stylesForTitleText.setText(I18nUtils.getText("styles_for_title"));
        stylesForSubTitle.setText(I18nUtils.getText("styles_tor_sub_title"));
    }

    public void setTextToElements() {
        settingsText.setText("Settings");
        darkModeText.setText("Dark mode");
        stylesForImageText.setText("Styles for image");
        stylesForTextText.setText("Styles for text");
        stylesForTitleText.setText("Styles for title");
        stylesForSubTitle.setText("Styles for sub title");
    }
}

