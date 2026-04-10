package com.SnapSeek;

import com.SnapSeek.integration.ApiService;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
// private File selectedFolder;

public class MainController {

    @FXML private Button selectImageButton;
    @FXML private Label selectImageLabel;
    @FXML private ComboBox<String> captionTypeDropDown;
    @FXML private Button generateCaptionButton;
    @FXML private TextArea captionOutputArea;

    @FXML private Button folderButton;
    @FXML private Button searchButton;
    @FXML private TextField searchField;
    @FXML private FlowPane resultsPane;

    private String selectedImagePath;
    private File selectedFolder;

    private final ApiService apiService = new ApiService();
    private final Gson gson = new Gson();

    @FXML
    public void initialize() {
        captionTypeDropDown.getItems().addAll(
                "funny",
                "playful",
                "professional",
                "romantic"
        );
        captionTypeDropDown.setValue("funny");
    }

    @FXML
    public void handleSelectFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Image Folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // File folder = directoryChooser.showDialog(new Stage());
        File folder = directoryChooser.showDialog(folderButton.getScene().getWindow());
        if (folder != null && folder.isDirectory()) {
            selectedFolder = folder;
            captionOutputArea.setText("Selected folder: " + selectedFolder.getAbsolutePath());
        } else {
            captionOutputArea.setText("No folder selected.");
        }
    }

    @FXML
    public void handleSelectImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            selectImageLabel.setText(file.getName());
        } else {
            selectImageLabel.setText("No Image Selected");
            selectedImagePath = null;
        }
    }

    // @FXML
    // public void handleSearch(ActionEvent event) {
    //     String query = searchField.getText().trim();

    //     if (query.isEmpty()) {
    //         captionOutputArea.setText("Please enter a search query.");
    //         return;
    //     }

    //     if (selectedFolder == null) {
    //         captionOutputArea.setText("Please select a folder first.");
    //         return;
    //     }

    //     captionOutputArea.setText("Searching...");
    //     resultsPane.getChildren().clear();

    //     new Thread(() -> {
    //         try {
    //             String responseJson = apiService.searchImages(query);
    //             SearchResponse response = gson.fromJson(responseJson, SearchResponse.class);

    //             Platform.runLater(() -> {
    //                 resultsPane.getChildren().clear();

    //                 if (response == null || response.results == null || response.results.isEmpty()) {
    //                     captionOutputArea.setText("No images found.");
    //                     return;
    //                 }

    //                 for (String imagePath : response.results) {
    //                     File imageFile = new File(imagePath);

    //                     Image image = new Image(imageFile.toURI().toString(), 120, 120, true, true);
    //                     ImageView imageView = new ImageView(image);
    //                     imageView.setFitWidth(120);
    //                     imageView.setFitHeight(120);
    //                     imageView.setPreserveRatio(true);

    //                     Label imageLabel = new Label(imageFile.getName());

    //                     VBox card = new VBox(5, imageView, imageLabel);
    //                     card.setAlignment(Pos.CENTER);

    //                     card.setOnMouseClicked(e -> {
    //                         selectedImagePath = imagePath;
    //                         selectImageLabel.setText(imageFile.getName());
    //                         captionOutputArea.setText("Selected image: " + imageFile.getName());
    //                     });

    //                     resultsPane.getChildren().add(card);
    //                 }

    //                 captionOutputArea.setText("Search completed.");
    //             });
    //         } catch (Exception e) {
    //             Platform.runLater(() ->
    //                     captionOutputArea.setText("Search failed: " + e.getMessage())
    //             );
    //         }
    //     }).start();
    // }

    @FXML
public void handleSearch(ActionEvent event) {
    String query = searchField.getText().trim();

    if (query.isEmpty()) {
        captionOutputArea.setText("Please enter a search query.");
        return;
    }

    if (selectedFolder == null) {
        captionOutputArea.setText("Please select a folder first.");
        return;
    }

    captionOutputArea.setText("Searching...");
    resultsPane.getChildren().clear();

    new Thread(() -> {
        try {
            String responseJson = apiService.searchImages(query, selectedFolder.getAbsolutePath());
            System.out.println("Raw search response: " + responseJson);
            SearchResponse response = gson.fromJson(responseJson, SearchResponse.class);

            Platform.runLater(() -> {
                resultsPane.getChildren().clear();

                if (response == null || response.results == null || response.results.isEmpty()) {
                    captionOutputArea.setText("No images found.");
                    return;
                }

                for (String imageName : response.results) {
                    File imageFile = new File(selectedFolder, imageName);
                    String imagePath = imageFile.getAbsolutePath();

                    if (!imageFile.exists()) {
                        System.out.println("File not found: " + imagePath);
                        continue;
                    }

                    Image image = new Image(imageFile.toURI().toString(), 120, 120, true, true);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(120);
                    imageView.setFitHeight(120);
                    imageView.setPreserveRatio(true);

                    Label imageLabel = new Label(imageFile.getName());

                    VBox card = new VBox(5, imageView, imageLabel);
                    card.setAlignment(Pos.CENTER);

                    card.setOnMouseClicked(e -> {
                        selectedImagePath = imagePath;
                        selectImageLabel.setText(imageFile.getName());
                        captionOutputArea.setText("Selected image: " + imageFile.getName());
                    });

                    resultsPane.getChildren().add(card);
                }

                captionOutputArea.setText("Search completed.");
            });

        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() ->
                    captionOutputArea.setText("Search failed: " + e.toString())
            );
        }
    }).start();
}

    @FXML
    private void handleGenerateCaption() {
        if (selectedImagePath == null) {
            captionOutputArea.setText("No image selected!");
            return;
        }

        if (selectedFolder == null) {
            captionOutputArea.setText("Please select a folder first!");
            return;
        }

        String tone = captionTypeDropDown.getValue();

        captionOutputArea.setText("Generating caption...");

        new Thread(() -> {
            try {
                // String responseJson = apiService.generateCaption(selectedImagePath, tone);
                String responseJson = apiService.generateCaption(selectedImagePath, tone, selectedFolder.getAbsolutePath());
                CaptionResponse response = gson.fromJson(responseJson, CaptionResponse.class);

                Platform.runLater(() -> {
                    if (response != null && response.caption != null) {
                        captionOutputArea.setText(response.caption);
                    } else {
                        captionOutputArea.setText("No caption returned from backend.");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() ->
                        captionOutputArea.setText("Caption failed: " + e.getMessage())
                );
            }
        }).start();
    }

    static class SearchResponse {
        List<String> results;
    }

    static class CaptionResponse {
        String caption;
    }
}

