module org.example.system {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    // Open packages to JavaFX
    opens org.example.system.controllers to javafx.fxml;

    // Export all packages
    exports org.example.system;
    exports org.example.system.controllers;
    exports org.example.system.courses;
    exports org.example.system.enums;
    exports org.example.system.models;
    exports org.example.system.services;
    exports org.example.system.users;
    exports org.example.system.utils;
}