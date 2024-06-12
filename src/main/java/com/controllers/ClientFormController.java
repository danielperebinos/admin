package com.controllers;

import com.entities.Client;
import com.services.ClientService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientFormController {
    @FXML
    private Button confirmButton, cancelButton;

    @FXML
    private Label title;

    @FXML
    private TextField nameField, surnameField, phoneField, adressField;

    private ClientService service = new ClientService();

    private Client client;

    private boolean confirmed = false, success = false, updateAction = false;

    private void closeStage() {
        ((Stage) confirmButton.getScene().getWindow()).close();
    }

    @FXML
    private void confirmAction() {
        this.confirmed = true;
        if (this.updateAction) {
            this.success = this.service.update(
                    this.client,
                    this.nameField.getText(),
                    this.surnameField.getText(),
                    this.phoneField.getText(),
                    this.adressField.getText()
            );
        } else {
            this.success = this.service.create(
                    this.nameField.getText(),
                    this.surnameField.getText(),
                    this.phoneField.getText(),
                    this.adressField.getText()
            );
        }
        this.closeStage();
    }

    @FXML
    private void cancelAction() {
        this.closeStage();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setClient(Client client) {
        this.client = client;
        this.updateAction = true;
        this.confirmButton.setText("Update");
        this.title.setText("Update Client");
        this.nameField.setText(client.getName());
        this.surnameField.setText(client.getSurname());
        this.phoneField.setText(client.getPhone());
        this.adressField.setText(client.getAdress());
    }
}
