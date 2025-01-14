module com.hunghq.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires transitive javafx.graphics;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires jbcrypt;
    requires java.mail;
    requires java.net.http;
    requires com.google.gson;
    requires org.json;
    requires java.desktop;
    requires com.google.common;
    requires kernel;
    requires layout;

    opens com.hunghq.librarymanagement.Model.Entity to javafx.base;
    opens com.hunghq.librarymanagement.Controller to javafx.fxml;
    exports com.hunghq.librarymanagement;
}