module com.example.company_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.controllers to javafx.fxml, org.hibernate.orm.core;
    opens com.entities to javafx.fxml, org.hibernate.orm.core;
    opens com to javafx.fxml, org.hibernate.orm.core;

    exports com.controllers;
    exports com.entities;
    exports com;
}