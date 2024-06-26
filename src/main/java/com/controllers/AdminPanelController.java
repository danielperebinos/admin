package com.controllers;

import com.entities.Client;
import com.services.ClientService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class AdminPanelController {
    private final ObservableList<Client> clientsRows = observableArrayList();
    private final int clientsPerPage = 19;
    private final ClientService service = new ClientService();
    @FXML
    private Button clientMenuButton, contractMenuButton, clientDeleteButton, clientCreateButton;
    @FXML
    private Pane clientPanel, contractPanel, clientFakePanel;
    @FXML
    private TableColumn selectClientColumn, idClientColumn, nameClientColumn, surnameClientColumn, phoneClientColumn,
            adressClientColumn, updateClientColumn;
    @FXML
    private Pagination clientPagination;
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
        this.refreshClientRows(
                this.service.filterByName(
                        name,
                        this.clientPagination.getCurrentPageIndex() * clientsPerPage,
                        clientsPerPage
                )
        );
    }

    @FXML
    private void onClickClientCreateButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(AdminPanelController.class.getResource("clientForm.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Create new Client");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(clientCreateButton.getScene().getWindow());
        stage.showAndWait();
        this.clientPagination.setCurrentPageIndex(1);
        this.refreshClientRows(
                this.service.selectAll(
                        this.clientPagination.getCurrentPageIndex() * this.clientsPerPage,
                        this.clientsPerPage
                )
        );
    }

    @FXML
    private void onClickClientDeleteButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(AdminPanelController.class.getResource("confirm.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Confirmation");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(clientDeleteButton.getScene().getWindow());

        ConfirmController controller = loader.getController();
        stage.showAndWait();

        if (controller.isConfirmed()) {
            List<Client> clientsToDelete = new ArrayList<Client>();
            for (Client client : this.clientsRows) {
                if (client.isSelected()) {
                    clientsToDelete.add(client);
                }
            }
            this.service.deleteMany(clientsToDelete);
            this.refreshClientRows(
                    this.service.selectAll(
                            this.clientPagination.getCurrentPageIndex() * this.clientsPerPage,
                            this.clientsPerPage
                    )
            );
        }
    }


    public ObservableList<Client> getClientRows() {
        return this.clientsRows;
    }

    public void refreshClientRows(List<Client> clients) {
        this.clientsRows.clear();
        this.clientsRows.addAll(clients);
    }

    @FXML
    void initialize() {
        this.clientPagination.setPageFactory(this::clientSetPageOnTable);

        selectClientColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Client, CheckBox>, ObservableValue<CheckBox>>) arg0 -> {
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

        });
        idClientColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        nameClientColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        surnameClientColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("surname"));
        phoneClientColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("phone"));
        adressClientColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("adress"));
        updateClientColumn.setCellValueFactory(new PropertyValueFactory<>("update"));

        Callback<TableColumn<Client, String>, TableCell<Client, String>> cellFactory = //
                new Callback<TableColumn<Client, String>, TableCell<Client, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Client, String> param) {
                        final TableCell<Client, String> cell = new TableCell<Client, String>() {
                            Image img = new Image(getClass().getResourceAsStream("redact.png"));
                            ImageView imgView = new ImageView(img);
                            Button updateClientButton = new Button("Update");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    updateClientButton.setOnAction(event -> {
                                        Client Client = getTableView().getItems().get(getIndex());
                                        FXMLLoader loader = new FXMLLoader(AdminPanelController.class.getResource("clientForm.fxml"));
                                        Stage stage = new Stage();
                                        try {
                                            stage.setScene(new Scene(loader.load()));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        stage.setTitle("Update Client");
                                        stage.initModality(Modality.WINDOW_MODAL);
                                        stage.initOwner(updateClientButton.getScene().getWindow());
                                        ClientFormController controller = loader.getController();
                                        controller.setClient(Client);
                                        stage.showAndWait();
                                        refreshClientRows(
                                                service.selectAll(
                                                        clientPagination.getCurrentPageIndex(),
                                                        clientsPerPage
                                                )
                                        );
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
        this.setClientPanel();
    }

    private Node clientSetPageOnTable(int pageIndex) {
        int clientsCount = (int) this.service.count();
        int fromIndex = pageIndex * this.clientsPerPage;
        int toIndex = Math.min(fromIndex + this.clientsPerPage, clientsCount);

        if (fromIndex <= toIndex) {
            this.onClickClientSearchButton();
        } else {
            this.clientsRows.clear();
        }

        return clientFakePanel;
    }


}
