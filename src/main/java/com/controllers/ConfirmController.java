package com.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConfirmController {
    private boolean confirmed = false;

    @FXML
    private Button refuseButton;

    @FXML
    private void confirmAction() {
        confirmed = true;
        closeStage();
    }

    @FXML
    private void cancelAction() {
        confirmed = false;
        closeStage();
    }

    private void closeStage() {
        ((Stage) refuseButton.getScene().getWindow()).close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

