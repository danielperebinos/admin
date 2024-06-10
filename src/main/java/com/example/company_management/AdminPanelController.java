package com.example.company_management;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class AdminPanelController {
    @FXML
    private TableView<Client> clientTable;

    @FXML
    private TableColumn selectClientColumn, idClientColumn, nameClientColumn, updateClientColumn;

    private final ObservableList<Client> clientsRows = FXCollections.observableArrayList();

    @FXML
    private Button clientMenuButton, contractMenuButton, clientDeleteButton;

    @FXML
    private Pane clientPanel, contractPanel;

    @FXML
    private TextField clientNameInput;


    private void setClientPanel() {
        clientMenuButton.setStyle("-fx-background-color: rgb(88, 88, 107);");
        clientPanel.setVisible(true);
        contractMenuButton.setStyle("-fx-background-color:  #282830;");
        contractPanel.setVisible(false);
    }

    private void setContractPanel() {
        contractMenuButton.setStyle("-fx-background-color: rgb(88, 88, 107);");
        contractPanel.setVisible(true);
        clientMenuButton.setStyle("-fx-background-color:  #282830;");
        clientPanel.setVisible(false);
    }

    @FXML
    private void onClickClientMenuButton() {
        this.setClientPanel();
    }

    @FXML
    private void onClickContractMenuButton() {
        this.setContractPanel();
    }

    @FXML
    private void onClickClientSearchButton() {
        String name = this.clientNameInput.getText();
        this.filterClients(name);
    }

    @FXML
    private void onClickClientDeleteButton() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(
                AdminPanelController.class.getResource("confirm.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("My modal window");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                (this.clientDeleteButton).getScene().getWindow());
        stage.show();

//        this.deleteClients();
//        this.loadClients();
    }

    private void loadClients() {
        this.filterClients("");
    }

    private void filterClients(String filter) {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                Query<Client> query;
                String queryString = "FROM Client";
                if (filter.length() > 0) {
                    queryString += " WHERE name LIKE :name";
                }

                query = session.createQuery(queryString, Client.class);

                if (filter.length() > 0) {
                    query.setParameter("name", "%" + filter + "%");
                }
                List<Client> clients = query.getResultList();
                this.clientsRows.clear();
                this.clientsRows.addAll(clients);
            } catch (Exception e) {
                System.out.println("Some Error");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        }
    }

    private void deleteClients() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();

                for (Client client : this.clientsRows) {
                    if (client.isSelected()) {
                        session.delete(client);
                    }
                }

                transaction.commit();
            } catch (Exception e) {
                System.out.println("Error deleting client");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        }

    }

    public ObservableList<Client> getClientRows() {
        return this.clientsRows;
    }

    @FXML
    void initialize() {
        selectClientColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Client, CheckBox>, ObservableValue<CheckBox>>() {

            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<Client, CheckBox> arg0) {
                Client user = arg0.getValue();

                CheckBox checkBox = new CheckBox();

                checkBox.selectedProperty().setValue(user.isSelected());

                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {

                        user.setSelected(new_val);

                    }
                });

                return new SimpleObjectProperty<CheckBox>(checkBox);

            }

        });
        idClientColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        nameClientColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        updateClientColumn.setCellValueFactory(new PropertyValueFactory<>("update"));
        Callback<TableColumn<Client, String>, TableCell<Client, String>> cellFactory = //
                new Callback<TableColumn<Client, String>, TableCell<Client, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Client, String> param) {
                        final TableCell<Client, String> cell = new TableCell<Client, String>() {
                            final Button updateClientButton = new Button("Update");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    updateClientButton.setOnAction(event -> {
                                        Client Client = getTableView().getItems().get(getIndex());
                                        System.out.println("Update " + Client.getId() + "   " + Client.getName());
                                    });
                                    setGraphic(updateClientButton);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        updateClientColumn.setCellFactory(cellFactory);
        Platform.runLater(this::loadClients);
        this.setClientPanel();
    }
}
