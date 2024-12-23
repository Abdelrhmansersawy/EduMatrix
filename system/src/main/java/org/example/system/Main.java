package org.example.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/org/example/system/views/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        scene.getStylesheets().add(getClass().getResource("/org/example/system/views/styles/application.css").toExternalForm());
        stage.setTitle("University System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}