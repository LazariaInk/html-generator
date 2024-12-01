package com.pluriverse.htmlgenerator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


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

    private Pane workArea;

    @FXML
    public void initialize() {
        workArea = new Pane();
        workArea.setPrefSize(982, 775);
        scrollPane.setContent(workArea);

        configureDragAndDrop(titleButton, "Title");
        configureDragAndDrop(subTitleButton, "SubTitle");
        configureDragAndDrop(paragraphButton, "Paragraph");
        configureDragAndDrop(imageButton, "Image");
        configureDragAndDrop(enumerationButton, "Enumeration");
        configureDragAndDrop(codeButton, "Code");

        workArea.setOnDragOver(event -> {
            if (event.getGestureSource() != workArea && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        workArea.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                //addElementToWorkArea(db.getString(), event.getX(), event.getY());
                addBlockToWorkArea(event.getX(), event.getY());
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    private void configureDragAndDrop(Button button, String elementName) {
        button.setOnDragDetected(event -> {
            Dragboard db = button.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(elementName);
            db.setContent(content);
            event.consume();
        });
    }

    private void addElementToWorkArea(String elementType, double x, double y) {
        System.out.println("asta este elementType " + elementType);
        TextArea newElement = new TextArea();
        newElement.setPrefSize(700,150);
        newElement.setLayoutX(x);
        newElement.setLayoutY(y);
        newElement.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue;");
        workArea.getChildren().add(newElement);
    }

    private void addBlockToWorkArea(double x, double y) {
        TextArea button = new TextArea();
        button.setOnMouseClicked(event -> {
            System.out.println(button.getText());
        });
        button.setPrefSize(150, 150);
        HBox hBox = new HBox();
        hBox.getChildren().add(button);
        hBox.setPrefSize(700, 150);
        hBox.setLayoutX(x);
        hBox.setLayoutY(y);
        hBox.setStyle("-fx-background-color: black;");
        workArea.getChildren().add(hBox);
    }
}
