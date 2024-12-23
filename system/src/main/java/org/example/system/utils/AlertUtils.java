package org.example.system.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

public class AlertUtils {
    public static void showError(String title, String content) {
        showAlert(AlertType.ERROR, title, content);
    }

    public static void showInfo(String title, String content) {
        showAlert(AlertType.INFORMATION, title, content);
    }

    private static void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}