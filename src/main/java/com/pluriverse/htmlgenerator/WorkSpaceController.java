package com.pluriverse.htmlgenerator;

import com.pluriverse.htmlgenerator.util.Colors;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkSpaceController {

    @FXML
    private VBox contentArea;
    @FXML
    private VBox workSpaceWindow;
    @FXML
    private ScrollPane scrollPane;
    private final List<String> htmlElements = new ArrayList<>();

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            // Apply styles directly to the ScrollPane
            scrollPane.setStyle("-fx-background-color: transparent;");

            // Ensure the viewport inside the ScrollPane is transparent
            Node viewport = scrollPane.lookup(".viewport");
            if (viewport != null) {
                viewport.setStyle("-fx-background-color: transparent;");
            }
        });
        colorElements();
    }

    public void addParagraph() {
        HBox paragraphBox = new HBox();
        paragraphBox.setSpacing(10.0);

        Label tagLabel = new Label("<p/>");
        tagLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5px;");

        TextField paragraphField = createDynamicField("Enter paragraph text...", paragraphBox, "Paragraph");

        htmlElements.add("<p></p>");
        paragraphBox.getChildren().addAll(tagLabel, paragraphField);
        contentArea.getChildren().add(paragraphBox);
    }

    public void addTitle() {
        HBox titleBox = new HBox();
        titleBox.setSpacing(10.0);

        Label tagLabel = new Label("<h1/>");
        tagLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5px;");

        TextField titleField = createDynamicField("Enter title text...", titleBox, "Title");

        htmlElements.add("<h1></h1>");
        titleBox.getChildren().addAll(tagLabel, titleField);
        contentArea.getChildren().add(titleBox);
    }

    public void addSubtitle() {
        HBox subtitleBox = new HBox();
        subtitleBox.setSpacing(10.0);

        Label tagLabel = new Label("<h2/>");
        tagLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5px;");

        TextField subtitleField = createDynamicField("Enter subtitle text...", subtitleBox, "Subtitle");

        htmlElements.add("<h2></h2>");
        subtitleBox.getChildren().addAll(tagLabel, subtitleField);
        contentArea.getChildren().add(subtitleBox);
    }

    public void addImage() {
        HBox imageBox = new HBox();
        imageBox.setSpacing(10.0);

        Label tagLabel = new Label("<img/>");
        tagLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5px;");

        TextField imageField = createDynamicField("Enter image URL...", imageBox, "Image");

        htmlElements.add("<img src=\"\" alt=\"Image\" style=\"max-width:100%;height:auto;\"/>");
        imageBox.getChildren().addAll(tagLabel, imageField);
        contentArea.getChildren().add(imageBox);
    }

    public void addEnumeration() {
        HBox enumerationBox = new HBox();
        enumerationBox.setSpacing(10.0);

        Label tagLabel = new Label("<ul/>");
        tagLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5px;");

        VBox itemsContainer = new VBox();
        itemsContainer.setSpacing(5.0);

        TextField firstItemField = createDynamicInput(itemsContainer);
        itemsContainer.getChildren().add(firstItemField);

        Button deleteEnumerationButton = new Button("Delete");
        deleteEnumerationButton.setOnAction(e -> {
            contentArea.getChildren().remove(enumerationBox);
            htmlElements.removeIf(html -> html.startsWith("<ul>") && html.endsWith("</ul>"));
        });

        enumerationBox.getChildren().addAll(tagLabel, itemsContainer, deleteEnumerationButton);
        contentArea.getChildren().add(enumerationBox);

        htmlElements.add("<ul></ul>");
    }

    private TextField createDynamicInput(VBox itemsContainer) {
        TextField textField = new TextField();
        textField.setPromptText("Enter item...");

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            int currentIndex = itemsContainer.getChildren().indexOf(textField);

            if (!newValue.isEmpty()) {
                if (currentIndex == itemsContainer.getChildren().size() - 1) {
                    TextField newInput = createDynamicInput(itemsContainer);
                    itemsContainer.getChildren().add(newInput);
                }
            } else {
                if (currentIndex < itemsContainer.getChildren().size() - 1) {
                    itemsContainer.getChildren().remove(currentIndex + 1);
                }
            }
            updateEnumerationHtml(itemsContainer);
        });
        return textField;
    }

    private TextField createDynamicField(String placeholder, HBox elementBox, String elementType) {
        TextField textField = new TextField();
        textField.setPromptText(placeholder);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                deleteElement(elementBox);
            } else {
                String updatedHtml = "";

                switch (elementType) {
                    case "Paragraph":
                        updatedHtml = "<p>" + newValue + "</p>";
                        break;
                    case "Title":
                        updatedHtml = "<h1>" + newValue + "</h1>";
                        break;
                    case "Subtitle":
                        updatedHtml = "<h2>" + newValue + "</h2>";
                        break;
                    case "Image":
                        updatedHtml = "<img src=\"" + newValue + "\" alt=\"Image\" style=\"max-width:100%;height:auto;\"/>";
                        break;
                }
                updateHtmlElement(elementBox, updatedHtml);
            }
        });

        return textField;
    }


    private void updateHtmlElement(HBox elementBox, String updatedHtml) {
        int index = contentArea.getChildren().indexOf(elementBox);
        if (index >= 0 && index < htmlElements.size()) {
            htmlElements.set(index, updatedHtml);
        }
    }

    private void updateEnumerationHtml(VBox itemsContainer) {
        StringBuilder html = new StringBuilder("<ul>");
        for (Node node : itemsContainer.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                String itemText = textField.getText().trim();
                if (!itemText.isEmpty()) {
                    html.append("<li>").append(itemText).append("</li>");
                }
            }
        }
        html.append("</ul>");

        for (int i = 0; i < htmlElements.size(); i++) {
            if (htmlElements.get(i).startsWith("<ul>") && htmlElements.get(i).endsWith("</ul>")) {
                htmlElements.set(i, html.toString());
                return; // Update and exit loop
            }
        }
    }


    public void generateHtml() {
        StringBuilder htmlPage = new StringBuilder();
        htmlPage.append("<!DOCTYPE html>\n<html>\n<head>\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n<title>Generated Page</title>\n</head>\n<body>\n");
        for (String element : htmlElements) {
            htmlPage.append(element).append("\n");
        }
        htmlPage.append("</body>\n</html>");
        showHtmlAlert("Generated HTML", htmlPage.toString());
    }

    private void addElementToUI(String tag, String visibleText, String htmlContent, String elementType) {
        HBox elementBox = new HBox();
        elementBox.setSpacing(10.0);
        Label tagLabel = new Label(tag);
        tagLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 5px;");
        Label elementLabel = new Label(visibleText);
        elementLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> editElement(elementBox, htmlContent, elementType));
        Button deleteButton = new Button("Delete");
        elementBox.getChildren().addAll(tagLabel, elementLabel, editButton, deleteButton);
        contentArea.getChildren().add(elementBox);
    }

    private void editElement(HBox elementBox, String oldContent, String elementType) {
        String newContent = showInputDialog("Edit " + elementType, "Update content:");
        if (newContent != null) {
            String updatedHtml = "";

            switch (elementType) {
                case "Paragraph":
                    updatedHtml = "<p>" + newContent + "</p>";
                    break;
                case "Image":
                    updatedHtml = "<img src=\"" + newContent + "\" alt=\"Image\" style=\"max-width:100%;height:auto;\"/>";
                    break;
                case "Title":
                    updatedHtml = "<h1>" + newContent + "</h1>";
                    break;
                case "Subtitle":
                    updatedHtml = "<h2>" + newContent + "</h2>";
                    break;
                case "Enumeration":
                    String[] items = newContent.split(",");
                    StringBuilder listHtml = new StringBuilder("<ul>");
                    for (String item : items) {
                        listHtml.append("<li>").append(item.trim()).append("</li>");
                    }
                    listHtml.append("</ul>");
                    updatedHtml = listHtml.toString();
                    break;
                default:
                    updatedHtml = oldContent;
            }

            htmlElements.set(htmlElements.indexOf(oldContent), updatedHtml);
            contentArea.getChildren().remove(elementBox);
            addElementToUI(tagFromType(elementType), newContent, updatedHtml, elementType);
        }
    }

    private void deleteElement(HBox elementBox) {
        int index = contentArea.getChildren().indexOf(elementBox);
        if (index >= 0) {
            contentArea.getChildren().remove(index);
            htmlElements.remove(index);
        }
    }


    private String showInputDialog(String title, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(title);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void showHtmlAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String tagFromType(String elementType) {
        switch (elementType) {
            case "Paragraph":
                return "<p/>";
            case "Image":
                return "<img/>";
            case "Title":
                return "<h1/>";
            case "Subtitle":
                return "<h2/>";
            case "Enumeration":
                return "<ul/>";
            default:
                return "";
        }
    }

    public void colorElements() {
        boolean isDarkMode = Boolean.parseBoolean(Settings.get("darkMode"));
        String textColor = isDarkMode ? Colors.DARK_TEXT_COLOR : Colors.LIGHT_TEXT_COLOR;
        String bgColor = isDarkMode ? Colors.DARK_BG_COLOR : Colors.LIGHT_BG_COLOR;
        String inputBgColor = isDarkMode ? Colors.DARK_INPUT_BG_COLOR : Colors.LIGHT_INPUT_BG_COLOR;

        // Set the background color for the main workspace
        workSpaceWindow.setStyle("-fx-background-color:" + bgColor + ";");

        // Ensure the ScrollPane itself and its content area have the correct background
        scrollPane.setStyle("-fx-background-color:" + bgColor + ";");
        if (scrollPane.getContent() != null) {
            scrollPane.getContent().setStyle("-fx-background-color:" + bgColor + ";");
        }

        // Update the background color of the content area
        contentArea.setStyle("-fx-background-color:" + bgColor + ";");

        // Update styles for all child elements in the content area
        for (Node node : contentArea.getChildren()) {
            if (node instanceof HBox) {
                HBox elementBox = (HBox) node;
                for (Node child : elementBox.getChildren()) {
                    if (child instanceof Label) {
                        Label label = (Label) child;
                        label.setStyle("-fx-text-fill:" + textColor + "; -fx-font-weight: bold;");
                    } else if (child instanceof TextField) {
                        TextField textField = (TextField) child;
                        textField.setStyle("-fx-text-fill:" + textColor + "; -fx-background-color:" + inputBgColor + ";");
                    } else if (child instanceof VBox) {
                        VBox itemsContainer = (VBox) child;
                        for (Node itemNode : itemsContainer.getChildren()) {
                            if (itemNode instanceof TextField) {
                                TextField textField = (TextField) itemNode;
                                textField.setStyle("-fx-text-fill:" + textColor + "; -fx-background-color:" + inputBgColor + ";");
                            }
                        }
                    }
                }
            }
        }
    }



}
