package com.SnapSeek;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController {

    @FXML private Button selectImageButton;
    @FXML private Label selectImageLabel;
    @FXML private ComboBox<String> captionTypeDropDown;
    @FXML private Button generateCaptionButton;
    @FXML private TextArea captionOutputArea;

    private String selectedImagePath;
    // UI components
    @FXML
    private Button folderButton;    // "Select Folder" button
    @FXML
    private Button searchButton;    // Search button
    @FXML
    private TextField searchField;  // Search query input
    @FXML
    private FlowPane resultsPane;   //to display image results

    // Data
    private File selectedFolder;

    @FXML
    public void handleSelectFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Image Folder");

        // Start from user's home directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Open dialog
        File folder = directoryChooser.showDialog(new Stage());
        if (folder != null && folder.isDirectory()) {
            selectedFolder = folder;
            System.out.println("Selected Folder: " + selectedFolder.getAbsolutePath());
        } else {
            System.out.println("No folder selected");}
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        if (selectedFolder == null) {
            System.out.println("Please select a folder first!");
            return;
        }

        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            System.out.println("Please enter a search query");
            return;
        }

        // Clear previous results
        resultsPane.getChildren().clear();

        // Add dummy results
        for (int i = 1; i <= 6; i++) {
            Region placeholder = new Region();
            placeholder.setPrefSize(100, 100);
            placeholder.setStyle("-fx-background-color: lightgray;");

            Label caption = new Label("Image " + i);

            VBox card = new VBox(5);
            card.setAlignment(Pos.CENTER);
            card.getChildren().addAll(placeholder, caption);

            resultsPane.getChildren().add(card);
        }

        System.out.println("Dummy search performed for query: " + query);
    }
    @FXML
    public void handleSelectImage(ActionEvent actionEvent) {
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files","*.png", "*.jpg", "*.jpeg"));
        File file= fileChooser.showOpenDialog(new Stage());
        if(file!=null){
            selectedImagePath= file.getAbsolutePath();
            selectImageLabel.setText(file.getName());
        }else {
            selectImageLabel.setText("No Image Selected");
            selectedImagePath=null;
        }
    }

    public void handleGenerateCaption(ActionEvent actionEvent) {
        if(selectedImagePath==null){
            captionOutputArea.setText("⚠ Please select an image first.");
            return;
        }
        String captionType=captionTypeDropDown.getValue();
        if(captionType==null)captionType="Funny";
        captionOutputArea.setText("Generated " + captionType + " caption for: " + selectedImagePath);
    }
}
