package com.example.company_management;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class CreateClientController {

    public boolean isClientCreated() {
        return clientCreated;
    }

    private boolean clientCreated = false;

    @FXML
    private Button createClientButton;

    @FXML
    private TextField nameField;

    @FXML
    private void createClient() {
        System.out.println("Create Client");
        String clientName = this.nameField.getText();
        this.createClient(clientName);
        this.clientCreated = true;
        this.closeStage();
    }

    private void createClient(String name) {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();

                Client client = new Client();
                client.setName(name);
                session.save(client);
                transaction.commit();
                this.clientCreated = true;
            } catch (Exception e) {
                System.out.println("Error deleting client");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        }

    }

    @FXML
    private void cancelCreate() {
        System.out.println("Cancel");
        this.closeStage();
    }

    private void closeStage() {
        ((Stage) createClientButton.getScene().getWindow()).close();
    }

}
