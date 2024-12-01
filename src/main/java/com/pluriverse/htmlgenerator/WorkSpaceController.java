package com.pluriverse.htmlgenerator;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    private VBox workArea;

    private Rectangle hintBar; // Blue bar for hinting the drop position

    @FXML
    public void initialize() {
        workArea = new VBox(15);
        workArea.setPrefWidth(900);
        workArea.setPrefHeight(1500);
        workArea.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #cccccc;");
        scrollPane.setContent(workArea);

        configureDragAndDrop(titleButton, "Title");
        configureDragAndDrop(subTitleButton, "SubTitle");
        configureDragAndDrop(paragraphButton, "Paragraph");
        configureDragAndDrop(imageButton, "Image");
        configureDragAndDrop(enumerationButton, "Enumeration");
        configureDragAndDrop(codeButton, "Code");

        configureWorkAreaForReordering();

        // Initialize the blue bar for visual hinting
        hintBar = new Rectangle(900, 3, Color.BLUE); // A thin blue rectangle
        hintBar.setVisible(false); // Hidden by default
        workArea.getChildren().add(hintBar); // Add it to the workArea (at the end for layering)
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
                updateHintBar(event.getY());
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

                hideHintBar();
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });

        workArea.setOnDragExited(event -> hideHintBar());
    }

    private int calculateInsertIndex(double dropY) {
        double cumulativeHeight = 0;
        for (int i = 0; i < workArea.getChildren().size(); i++) {
            Node child = workArea.getChildren().get(i);
            if (child == hintBar) continue;
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
            int insertIndex = calculateInsertIndex(dropY);
            workArea.getChildren().add(insertIndex, wrappedElement);
        }
    }

    private HBox wrapWithDeleteButton(Node element) {
        HBox container = new HBox(10);
        Button deleteButton = new Button("X");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setOnAction(event -> workArea.getChildren().remove(container));
        container.getChildren().addAll(element, deleteButton);
        return container;
    }

    private Node createTitle() {
        TextField titleField = new TextField();
        titleField.setPromptText("Enter title...");
        titleField.setStyle("-fx-border-color: lightblue; -fx-padding: 5;");
        return titleField;
    }

    private Node createSubTitle() {
        TextField subTitleField = new TextField();
        subTitleField.setPromptText("Enter subtitle...");
        subTitleField.setStyle("-fx-border-color: lightblue; -fx-padding: 5;");
        return subTitleField;
    }

    private Node createParagraph() {
        TextArea paragraphField = new TextArea();
        paragraphField.setPromptText("Enter paragraph...");
        paragraphField.setPrefHeight(100);
        paragraphField.setStyle("-fx-border-color: lightblue; -fx-padding: 5;");
        return paragraphField;
    }

    private Node createImage() {
        Button imagePlaceholder = new Button("Select Image");
        imagePlaceholder.setOnAction(event -> {
            System.out.println("Image selection logic here.");
        });
        imagePlaceholder.setStyle("-fx-border-color: lightblue; -fx-padding: 5;");
        return imagePlaceholder;
    }

    private Node createEnumeration() {
        VBox enumerationBox = new VBox(5);
        enumerationBox.setStyle("-fx-border-color: lightblue; -fx-padding: 5;");

        Button addButton = new Button("+ Add Item");
        addButton.setOnAction(event -> {
            TextField listItem = new TextField();
            listItem.setPromptText("Enter item...");
            enumerationBox.getChildren().add(listItem);
        });

        enumerationBox.getChildren().add(addButton);
        return enumerationBox;
    }

    private Node createCodeSnippet() {
        TextArea codeField = new TextArea();
        codeField.setPromptText("Enter code snippet...");
        codeField.setPrefHeight(100);
        codeField.setStyle("-fx-font-family: monospace; -fx-border-color: lightblue; -fx-padding: 5;");
        return codeField;
    }

    private void updateHintBar(double mouseY) {
        hintBar.setVisible(true);
        int insertIndex = calculateInsertIndex(mouseY);
        if (insertIndex < workArea.getChildren().size()) {
            Node targetNode = workArea.getChildren().get(insertIndex);
            hintBar.setLayoutY(targetNode.getBoundsInParent().getMinY() - 2);
        } else {
            hintBar.setLayoutY(workArea.getHeight() - 3);
        }
    }

    private void hideHintBar() {
        hintBar.setVisible(false);
    }
}
