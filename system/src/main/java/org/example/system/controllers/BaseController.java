package org.example.system.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import java.io.IOException;

public abstract class BaseController {

    protected void navigateTo(String fxmlPath, String title, Control control) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) control.getScene().getWindow();
            scene.getStylesheets().addAll(control.getScene().getStylesheets());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected <T> T loadController(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void closeWindow(Control control) {
        Stage stage = (Stage) control.getScene().getWindow();
        stage.close();
    }

    protected abstract void initialize() throws Exception;

    protected abstract void cleanup() throws Exception;

    protected abstract void refreshView() throws Exception;

    protected abstract void resetForm();

    protected abstract boolean validateForm();
}