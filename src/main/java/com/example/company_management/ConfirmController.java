package com.example.company_management;


import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ConfirmController {
    private boolean confirmed = false;

    @FXML
    private void confirmDelete() {
        confirmed = true;
        closeStage();
    }

    @FXML
    private void cancelDelete() {
        confirmed = false;
        closeStage();
    }

    private void closeStage() {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
}
