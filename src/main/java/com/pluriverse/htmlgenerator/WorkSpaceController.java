package com.pluriverse.htmlgenerator;

import com.pluriverse.htmlgenerator.util.Colors;
import com.pluriverse.htmlgenerator.util.I18nUtils;
import com.pluriverse.htmlgenerator.util.Styles;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class WorkSpaceController {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button titleButton;
    @FXML
    private Button subTitleButton;
    @FXML
    private Button paragraphButton;
    @FXML
    private Button imageButton;
    @FXML
    private Button enumerationButton;
    @FXML
    private Button codeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button exportButton;
    @FXML
    private VBox workAreaContainer;

    private VBox workArea;

    @FXML
    public void initialize() {
        workArea = new VBox(15);
        workArea.setPrefWidth(900);
        workArea.setPrefHeight(1500);
        workArea.setStyle(Styles.WORK_AREA);
        scrollPane.setContent(workArea);

        configureDragAndDrop(titleButton, "Title");
        configureDragAndDrop(subTitleButton, "SubTitle");
        configureDragAndDrop(paragraphButton, "Paragraph");
        configureDragAndDrop(imageButton, "Image");
        configureDragAndDrop(enumerationButton, "Enumeration");
        configureDragAndDrop(codeButton, "Code");

        configureWorkAreaForReordering();
        reloadTexts();
        colorElements();
    }

    private void colorElements() {
        boolean isDarkMode = Boolean.parseBoolean(Settings.get("darkMode"));
        String textColor = isDarkMode ? Colors.DARK_TEXT_COLOR : Colors.LIGHT_TEXT_COLOR;
        String bgColor = isDarkMode ? Colors.DARK_BG_COLOR : Colors.LIGHT_BG_COLOR;

        titleButton.setStyle("-fx-text-fill: " + textColor + ";");
        subTitleButton.setStyle("-fx-text-fill: " + textColor + ";");
        paragraphButton.setStyle("-fx-text-fill: " + textColor + ";");
        imageButton.setStyle("-fx-text-fill: " + textColor + ";");
        enumerationButton.setStyle("-fx-text-fill: " + textColor + ";");
        codeButton.setStyle("-fx-text-fill: " + textColor + ";");
        backButton.setStyle("-fx-text-fill: " + textColor + ";");
        exportButton.setStyle("-fx-text-fill: " + textColor + ";");
        workAreaContainer.setStyle("-fx-background-color: " + bgColor + ";");
        scrollPane.setStyle("-fx-background: " + bgColor + ";");
        workArea.setStyle("-fx-background: " + bgColor + ";");
    }

    private void reloadTexts() {
        titleButton.setText(I18nUtils.getText("enter_title"));
        subTitleButton.setText(I18nUtils.getText("enter_subtitle"));
        paragraphButton.setText(I18nUtils.getText("enter_paragraph"));
        imageButton.setText(I18nUtils.getText("select_image"));
        enumerationButton.setText(I18nUtils.getText("enter_item"));
        codeButton.setText(I18nUtils.getText("enter_code_snippet"));
        backButton.setText(I18nUtils.getText("back_button"));
        exportButton.setText(I18nUtils.getText("export_button"));
    }

    private void configureDragAndDrop(Button button, String elementType) {
        button.setOnDragDetected(event -> {
            Dragboard db = button.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(elementType);
            db.setContent(content);
            event.consume();
        });
    }

    private void configureWorkAreaForReordering() {
        workArea.setOnDragOver(event -> {
            if (event.getGestureSource() != workArea && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        workArea.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                String elementType = db.getString();
                Node draggedNode = (Node) event.getGestureSource();

                if (workArea.getChildren().contains(draggedNode)) {
                    workArea.getChildren().remove(draggedNode);
                    int insertIndex = calculateInsertIndex(event.getY());
                    workArea.getChildren().add(insertIndex, draggedNode);
                } else {
                    addElementToWorkArea(elementType, event.getY());
                }
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    private int calculateInsertIndex(double dropY) {
        double cumulativeHeight = 0;
        for (int i = 0; i < workArea.getChildren().size(); i++) {
            Node child = workArea.getChildren().get(i);
            cumulativeHeight += child.getBoundsInParent().getHeight() + workArea.getSpacing();
            if (dropY < cumulativeHeight) {
                return i;
            }
        }
        return workArea.getChildren().size();
    }

    private void addElementToWorkArea(String elementType, double dropY) {
        Node newElement = null;

        switch (elementType) {
            case "Title":
                newElement = createTitle();
                break;
            case "SubTitle":
                newElement = createSubTitle();
                break;
            case "Paragraph":
                newElement = createParagraph();
                break;
            case "Image":
                newElement = createImage();
                break;
            case "Enumeration":
                newElement = createEnumeration();
                break;
            case "Code":
                newElement = createCodeSnippet();
                break;
        }

        if (newElement != null) {
            HBox wrappedElement = wrapWithDeleteButton(newElement);
            addDraggableBehavior(wrappedElement);
            int insertIndex = calculateInsertIndex(dropY);
            workArea.getChildren().add(insertIndex, wrappedElement);
        }
    }

    private HBox wrapWithDeleteButton(Node element) {
        HBox container = new HBox(10);
        Button deleteButton = new Button("X");
        deleteButton.setStyle(Styles.DELETE_BUTTON);
        deleteButton.setOnAction(event -> workArea.getChildren().remove(container));
        container.getChildren().addAll(element, deleteButton);
        addDraggableBehavior(container);
        return container;
    }

    private Node createTitle() {
        boolean isDarkMode = Boolean.parseBoolean(Settings.get("darkMode"));
        String textColor = isDarkMode ? Colors.DARK_TEXT_COLOR : Colors.LIGHT_TEXT_COLOR;
        String bgColor = isDarkMode ? Colors.DARK_BG_COLOR : Colors.LIGHT_BG_COLOR;

        TextField titleField = new TextField();
        titleField.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + ";");
        titleField.setPrefWidth(800);
        titleField.setPromptText(I18nUtils.getText("enter_title"));

        titleField.setStyle(titleField.getStyle() + Styles.TITLE_FIELD);

        return titleField;
    }

    private Node createSubTitle() {
        boolean isDarkMode = Boolean.parseBoolean(Settings.get("darkMode"));
        String textColor = isDarkMode ? Colors.DARK_TEXT_COLOR : Colors.LIGHT_TEXT_COLOR;
        String bgColor = isDarkMode ? Colors.DARK_BG_COLOR : Colors.LIGHT_BG_COLOR;

        TextField subTitleField = new TextField();
        String dynamicStyles = "-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + ";";
        subTitleField.setStyle(dynamicStyles + Styles.SUB_TITLE_FIELD);

        subTitleField.setPrefWidth(800);
        subTitleField.setPromptText(I18nUtils.getText("enter_subtitle"));

        return subTitleField;
    }

    private Node createParagraph() {
        boolean isDarkMode = Boolean.parseBoolean(Settings.get("darkMode"));
        String textColor = isDarkMode ? Colors.DARK_TEXT_COLOR : Colors.LIGHT_TEXT_COLOR;
        String bgColor = isDarkMode ? Colors.DARK_BG_COLOR : Colors.LIGHT_BG_COLOR;

        TextArea paragraphField = new TextArea();

        String dynamicStyles = "-fx-control-inner-background: " + bgColor + "; -fx-text-fill: " + textColor + ";";
        paragraphField.setStyle(dynamicStyles + Styles.PARAGRAPH_FIELD);

        paragraphField.setPromptText(I18nUtils.getText("enter_paragraph"));
        paragraphField.setPrefHeight(100);
        paragraphField.setPrefWidth(800);

        return paragraphField;
    }


    private Node createImage() {
        Button imagePlaceholder = new Button(I18nUtils.getText("select_image"));
        imagePlaceholder.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(I18nUtils.getText("select_image"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(scrollPane.getScene().getWindow());
            if (selectedFile != null) {
                ImageView imageView = new ImageView(new Image(selectedFile.toURI().toString()));
                imageView.setFitWidth(200);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                int index = workArea.getChildren().indexOf(imagePlaceholder.getParent());
                HBox wrappedElement = wrapWithDeleteButton(imageView);
                workArea.getChildren().set(index, wrappedElement);
            }
        });
        imagePlaceholder.setStyle(Styles.IMAGE_PLACEHOLDER);
        return imagePlaceholder;
    }

    private Node createEnumeration() {
        VBox enumerationBox = new VBox(5);
        enumerationBox.setStyle(Styles.ENUMERATION_BOX);

        addNewEnumerationItem(enumerationBox);

        return enumerationBox;
    }

    private void addNewEnumerationItem(VBox enumerationBox) {
        boolean isDarkMode = Boolean.parseBoolean(Settings.get("darkMode"));
        String textColor = isDarkMode ? Colors.DARK_TEXT_COLOR : Colors.LIGHT_TEXT_COLOR;
        String bgColor = isDarkMode ? Colors.DARK_BG_COLOR : Colors.LIGHT_BG_COLOR;

        HBox itemContainer = new HBox(10);
        Label dotLabel = new Label("●");
        dotLabel.setStyle(Styles.ENUMERATION_ITEM);
        TextField listItem = new TextField();
        listItem.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + ";");
        listItem.setPrefWidth(750);
        listItem.setPromptText(I18nUtils.getText("enter_item"));

        listItem.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                enumerationBox.getChildren().remove(itemContainer);
            } else if (enumerationBox.getChildren().indexOf(itemContainer) == enumerationBox.getChildren().size() - 1) {
                addNewEnumerationItem(enumerationBox);
            }
        });

        itemContainer.getChildren().addAll(dotLabel, listItem);

        enumerationBox.getChildren().add(itemContainer);
    }

    private Node createCodeSnippet() {
        boolean isDarkMode = Boolean.parseBoolean(Settings.get("darkMode"));
        String textColor = isDarkMode ? Colors.DARK_TEXT_COLOR : Colors.LIGHT_TEXT_COLOR;
        String bgColor = isDarkMode ? Colors.DARK_BG_COLOR : Colors.LIGHT_BG_COLOR;

        TextArea codeField = new TextArea();
        codeField.setStyle("-fx-control-inner-background: " + bgColor + "; -fx-text-fill: " + textColor + ";" + Styles.CODE_SNIPPET);

        codeField.setPromptText(I18nUtils.getText("enter_code_snippet"));
        codeField.setPrefHeight(100);

        return codeField;
    }



    private void addDraggableBehavior(Node element) {
        element.setOnDragDetected(event -> {
            Dragboard db = element.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("");
            db.setContent(content);
            event.consume();
        });

        element.setOnDragOver(event -> {
            if (event.getGestureSource() != element && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        element.setOnDragDropped(event -> {
            Node draggedNode = (Node) event.getGestureSource();
            if (workArea.getChildren().contains(draggedNode)) {
                workArea.getChildren().remove(draggedNode);

                int insertIndex = calculateInsertIndex(event.getY());
                workArea.getChildren().add(insertIndex, draggedNode);
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    @FXML
    public void handleStartController(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/start-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Html generator");
        stage.setHeight(506.0);
        stage.setWidth(685.0);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
        stage.show();
    }
}
