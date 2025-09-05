package com.SnapSeek;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = Main.class.getResource("/com.SnapSeek/View.fxml");
        System.out.println(fxmlUrl);  // Should print something like: file:/C:/.../target/classes/com/SnapSeek/View.fxml
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

         loader = new FXMLLoader(Main.class.getResource("/com.SnapSeek/View.fxml"));
        root = loader.load();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("SnapSeek - AI Image Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
