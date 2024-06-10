package com.example.company_management;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UpdateClientController {

    @FXML
    private TextField nameField;

    @FXML
    private Button refuseButton;

    private boolean confirmed = false;

    private Client clientToUpdate;

    public void setClientToUpdate(Client client) {
        this.clientToUpdate = client;
        nameField.setText(client.getName());
    }

    @FXML
    private void confirmUpdate() {
        confirmed = true;
        String new_name = this.nameField.getText();
        createClient(new_name);
        closeStage();
    }

    @FXML
    private void cancelUpdate() {
        confirmed = false;
        closeStage();
    }

    private void createClient(String name) {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                this.clientToUpdate.setName(name);
                session.update(this.clientToUpdate);
                transaction.commit();
                this.confirmed = true;
            } catch (Exception e) {
                System.out.println("Error deleting client");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        }

    }

    private void closeStage() {
        ((Stage) refuseButton.getScene().getWindow()).close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
