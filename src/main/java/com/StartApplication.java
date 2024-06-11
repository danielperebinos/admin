package com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("controllers/start.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CMS");
        stage.setScene(scene);
        stage.show();
    }
}
