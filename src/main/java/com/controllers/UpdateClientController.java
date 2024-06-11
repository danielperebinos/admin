package com.controllers;

import com.entities.Client;
import com.services.ClientService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateClientController {
    @FXML
    private TextField nameField;

    @FXML
    private Button refuseButton;

    private ClientService service = new ClientService();

    private boolean confirmed = false;

    private Client clientToUpdate;

    public void setClientToUpdate(Client client) {
        this.clientToUpdate = client;
        nameField.setText(client.getName());
    }

    @FXML
    private void confirmUpdate() {
        String clientName = this.nameField.getText();
        this.confirmed = this.service.update(this.clientToUpdate, clientName);
        closeStage();
    }

    @FXML
    private void cancelUpdate() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) refuseButton.getScene().getWindow()).close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
