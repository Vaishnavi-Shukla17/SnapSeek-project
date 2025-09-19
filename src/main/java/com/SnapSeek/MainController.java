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

    @FXML
    private void handleGenerateCaption() {
        try {
            String imagePath = selectedImagePath; // set when "Select Image" is clicked
            String captionType = selectedCaptionType; // e.g. from a ComboBox or toggle

            // Safety checks
            if (imagePath == null) {
                captionOutputArea.setText("⚠️ No image selected!");
                return;
            }
            if (captionType == null) {
                captionOutputArea.setText("⚠️ No caption type selected!");
                return;
            }

            // Run Python script with TWO arguments
            ProcessBuilder pb = new ProcessBuilder(
                    "python", "caption_cli.py", imagePath, captionType
            );
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Capture output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line, caption = "";
            while ((line = reader.readLine()) != null) {
                caption += line + "\n";
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                captionOutputArea.setText(caption.trim());
            } else {
                captionOutputArea.setText("❌ Failed to generate caption.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            captionOutputArea.setText("Error: " + e.getMessage());
        }
    }

}
