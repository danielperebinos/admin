package com.controllers;

import com.services.ClientService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateClientController {
    @FXML
    private Button createClientButton;
    @FXML
    private TextField nameField;

    private ClientService service = new ClientService();
    private boolean clientCreated = false;

    public boolean isClientCreated() {
        return clientCreated;
    }

    private void closeStage() {
        ((Stage) createClientButton.getScene().getWindow()).close();
    }

    @FXML
    private void createClient() {
        String clientName = this.nameField.getText();
        this.clientCreated = this.service.create(clientName);
        this.closeStage();
    }

    @FXML
    private void cancelCreate() {
        this.closeStage();
    }

}
