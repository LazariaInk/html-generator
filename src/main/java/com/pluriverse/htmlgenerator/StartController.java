package com.pluriverse.htmlgenerator;

import com.pluriverse.htmlgenerator.util.Colors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


public class StartController {

    @FXML
    private VBox startWindow;
    @FXML
    private Text htmlGeneratorByLazariaInkText;
    @FXML
    private Text wellComeText;
    @FXML
    private Text mailText;

    @FXML
    public void initialize() {
        colorElements();
    }

    @FXML
    public void handleSettingsButton(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/settings-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.show();
    }

    @FXML
    public void handleWorkspaceButton(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/workspace-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);

        stage.setWidth(1200);
        stage.setHeight(820);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);

        stage.setTitle("Workspace");
        stage.show();
    }


    public void colorElements() {
        boolean isDarkMode = Boolean.parseBoolean(Settings.get("darkMode"));
        String textColor = isDarkMode ? Colors.DARK_TEXT_COLOR : Colors.LIGHT_TEXT_COLOR;
        String bgColor = isDarkMode ? Colors.DARK_BG_COLOR : Colors.LIGHT_BG_COLOR;
        wellComeText.setStyle("-fx-fill:" + textColor + ";");
        mailText.setStyle("-fx-fill:" + textColor + ";");
        htmlGeneratorByLazariaInkText.setStyle("-fx-fill:" + textColor + ";");
        startWindow.setStyle("-fx-background-color:" + bgColor + ";");
    }
}
