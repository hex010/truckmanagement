package truck.truckmanagement.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import truck.truckmanagement.Enum.CRUD_enum;
import truck.truckmanagement.HelloApplication;
import truck.truckmanagement.Model.*;
import truck.truckmanagement.Service.*;
import truck.truckmanagement.TableParameters.ActionButtonTableCell;
import truck.truckmanagement.TableParameters.TransportTableParameters;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

public class MainWindowAdmin {
    @FXML
    public ListView<Destination> listViewTrips;
    @FXML
    public TableView truckTableView;
    @FXML
    public TableColumn<TransportTableParameters, String> columnTruckId;
    @FXML
    public TableColumn<TransportTableParameters, String> columnTruckModel;
    @FXML
    public TableColumn<TransportTableParameters, String> columnTruckMaxSpeed;
    @FXML
    public TableColumn<TransportTableParameters, String> columnTruckMileage;
    @FXML
    public TableColumn<TransportTableParameters, String> columnTruckManufacturedDate;
    @FXML
    public TableColumn<TransportTableParameters, String> columnTruckColor;
    @FXML
    public TableColumn<TransportTableParameters, String> columnTruckFuelType;
    @FXML
    public TableColumn<TransportTableParameters, String> columnTruckTransimissionType;
    @FXML
    public TableColumn<TransportTableParameters, Button> columnTruckDelete;
    @FXML
    public TextField truckModelField;
    @FXML
    public TextField truckMaxSpeedField;
    @FXML
    public TextField truckMileageField;
    @FXML
    public DatePicker truckManufacturedDatePicker;
    @FXML
    public TextField truckFuelTypeField;
    @FXML
    public TextField truckTransmissionTypeField;
    @FXML
    public TextField colorField;
    @FXML
    public ListView<Freight> freightList;
    @FXML
    public ListView<DestinationPoint> listViewCheckpoint;
    @FXML
    public ListView<User> userList;

    private User loggedInUser;
    private ObservableList<TransportTableParameters> truckData = FXCollections.observableArrayList();
    private UserService userService;
    private FreightService freightService;
    private TransportService transportService;
    private DestinationPointService destinationPointService;
    private DestinationService destinationService;

    public void setData(User user) {
        this.loggedInUser = user;

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");
        userService = new UserService(entityManagerFactory);
        freightService = new FreightService(entityManagerFactory);
        transportService = new TransportService(entityManagerFactory);
        destinationPointService = new DestinationPointService(entityManagerFactory);
        destinationService = new DestinationService(entityManagerFactory);

        setTransprotTableParameters();

        fillUserList();
        fillFreightList();
        fillcheckpointList();
        fillTripList();

    }

    private void fillTripList() {
        List<Destination> destinations = destinationService.getAllDestinations();

        destinations.forEach(d->listViewTrips.getItems().add(d));
    }

    private void fillcheckpointList() {
        List<DestinationPoint> destinationPoints = destinationPointService.getAllDestinationPoints();

        destinationPoints.forEach(c->listViewCheckpoint.getItems().add(c));
    }



    private void fillFreightList() {
        List<Freight> freights = freightService.getAllFreights();

        freights.forEach(f->freightList.getItems().add(f));
    }

    private void fillUserList() {
        List<User> users = userService.getAllUsers();

        users.forEach(u->userList.getItems().add(u));
    }

    private void setTransprotTableParameters() {
        truckTableView.setEditable(true);

        columnTruckId.setCellValueFactory(new PropertyValueFactory<>("truckId"));

        columnTruckModel.setCellValueFactory(new PropertyValueFactory<>("truckModel"));
        columnTruckModel.setCellFactory(TextFieldTableCell.forTableColumn()); //redagavimui
        columnTruckModel.setOnEditCommit(t-> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setTruckModel(t.getNewValue());
            Transport transport = transportService.getTransportById(Integer.parseInt(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckId()));
            transport.setModel(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckModel());
            transportService.updateTransport(transport);
        });

        columnTruckMaxSpeed.setCellValueFactory(new PropertyValueFactory<>("truckMaxSpeed"));
        columnTruckMaxSpeed.setCellFactory(TextFieldTableCell.forTableColumn()); //redagavimui
        columnTruckMaxSpeed.setOnEditCommit(t-> {
            if(!isNumeric(t.getNewValue())) {
                alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Įvesti galima tik skaičių.");
                return;
            }
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setTruckMaxSpeed(t.getNewValue());
            Transport transport = transportService.getTransportById(Integer.parseInt(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckId()));
            transport.setMaxSpeed(Integer.parseInt(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckMaxSpeed()));
            transportService.updateTransport(transport);
        });

        columnTruckMileage.setCellValueFactory(new PropertyValueFactory<>("truckMileage"));
        columnTruckMileage.setCellFactory(TextFieldTableCell.forTableColumn()); //redagavimui
        columnTruckMileage.setOnEditCommit(t-> {
            if(!isNumeric(t.getNewValue())) {
                alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Įvesti galima tik skaičių.");
                return;
            }
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setTruckMileage(t.getNewValue());
            Transport transport = transportService.getTransportById(Integer.parseInt(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckId()));
            transport.setMileage(Integer.parseInt(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckMileage()));
            transportService.updateTransport(transport);
        });

        columnTruckManufacturedDate.setCellValueFactory(new PropertyValueFactory<>("truckManufacturedDate"));
        columnTruckManufacturedDate.setCellFactory(TextFieldTableCell.forTableColumn()); //redagavimui
        columnTruckManufacturedDate.setOnEditCommit(t-> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setTruckManufacturedDate(t.getNewValue());
            Transport transport = transportService.getTransportById(Integer.parseInt(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckId()));
            transport.setDateOfManufacture(LocalDate.parse(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckManufacturedDate()));
            transportService.updateTransport(transport);
        });

        columnTruckColor.setCellValueFactory(new PropertyValueFactory<>("truckColor"));
        columnTruckColor.setCellFactory(TextFieldTableCell.forTableColumn()); //redagavimui
        columnTruckColor.setOnEditCommit(t-> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setTruckColor(t.getNewValue());
            Transport transport = transportService.getTransportById(Integer.parseInt(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckId()));
            transport.setColor(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckColor());
            transportService.updateTransport(transport);
        });

        columnTruckFuelType.setCellValueFactory(new PropertyValueFactory<>("truckFuelType"));
        columnTruckFuelType.setCellFactory(TextFieldTableCell.forTableColumn()); //redagavimui
        columnTruckFuelType.setOnEditCommit(t-> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setTruckFuelType(t.getNewValue());
            Transport transport = transportService.getTransportById(Integer.parseInt(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckId()));
            transport.setFuelType(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckFuelType());
            transportService.updateTransport(transport);
        });

        columnTruckTransimissionType.setCellValueFactory(new PropertyValueFactory<>("truckTransmissionType"));
        columnTruckTransimissionType.setCellFactory(TextFieldTableCell.forTableColumn()); //redagavimui
        columnTruckTransimissionType.setOnEditCommit(t-> {
            t.getTableView().getItems().get(
                    t.getTablePosition().getRow()).setTruckTransmissionType(t.getNewValue());
            Transport transport = transportService.getTransportById(Integer.parseInt(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckId()));
            transport.setTransmissionType(t.getTableView().getItems().get(t.getTablePosition().getRow()).getTruckTransmissionType());
            transportService.updateTransport(transport);
        });

        columnTruckDelete.setCellFactory(ActionButtonTableCell.forTableColumn("Trinti", (TransportTableParameters p) -> {
            transportService.removeTransportById(Integer.parseInt(p.getTruckId()));
            truckTableView.getItems().remove(p);
            return p;
        }));

        fillTransportTable();
    }
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    private void fillTransportTable() {
        List<Transport> transports = transportService.getAllTransports();
        for(Transport transport : transports){
            TransportTableParameters transportTableParameters = new TransportTableParameters();
            transportTableParameters.setTruckId(String.valueOf(transport.getId()));
            transportTableParameters.setTruckModel(transport.getModel());
            transportTableParameters.setTruckMaxSpeed(String.valueOf(transport.getMaxSpeed()));
            transportTableParameters.setTruckMileage(String.valueOf(transport.getMileage()));
            transportTableParameters.setTruckManufacturedDate(String.valueOf(transport.getDateOfManufacture()));
            transportTableParameters.setTruckColor(transport.getColor());
            transportTableParameters.setTruckFuelType(transport.getFuelType());
            transportTableParameters.setTruckTransmissionType(transport.getTransmissionType());
            truckData.add(transportTableParameters);
        }
        truckTableView.setItems(truckData);
    }
    private void resetTruckFields() {
        truckModelField.clear();
        truckMaxSpeedField.clear();
        truckMileageField.clear();
        truckManufacturedDatePicker.getEditor().clear();
        colorField.clear();
        truckFuelTypeField.clear();
        truckTransmissionTypeField.clear();
    }
    private Boolean checkIfUserIsSelected(){
        return userList.getSelectionModel().getSelectedItems().isEmpty();
    }
    private boolean checkIfTripIsSelected() {
        return listViewTrips.getSelectionModel().getSelectedItems().isEmpty();
    }

    private boolean checkIfCheckpointIsSelected() {
        return listViewCheckpoint.getSelectionModel().getSelectedItems().isEmpty();
    }
    private boolean checkIfFreightIsSelected() {
        return freightList.getSelectionModel().getSelectedItems().isEmpty();
    }
    @FXML
    public void viewTrip(ActionEvent actionEvent) {
        if(checkIfTripIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas reisas", "Prašome pasirinkti reisą iš sąrašo.");
            return;
        }
        callTripViewPage(CRUD_enum.VIEW);
    }

    @FXML
    public void editTrip(ActionEvent actionEvent) {
        if(checkIfTripIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas reisas", "Prašome pasirinkti reisą iš sąrašo.");
            return;
        }
        callTripViewPage(CRUD_enum.EDIT);
        listViewTrips.getItems().clear();
        fillTripList();
    }

    @FXML
    public void createTrip(ActionEvent actionEvent) {
        callTripViewPage(CRUD_enum.CREATE);
        listViewTrips.getItems().clear();
        fillTripList();
    }

    @FXML
    public void deleteTrip(ActionEvent actionEvent) {
        if(checkIfTripIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas reisas", "Prašome pasirinkti reisą iš sąrašo.");
            return;
        }
        destinationService.removeDestination(listViewTrips.getSelectionModel().getSelectedItem());
        listViewTrips.getItems().remove(listViewTrips.getSelectionModel().getSelectedIndex());
        alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Reisas ištrintas", "Reisas buvo sėkmingai ištrintas.");
    }
    private void callTripViewPage(CRUD_enum selectedAction) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Destination-view.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            DestinationWindow destinationWindow = fxmlLoader.getController();
            destinationWindow.setData(listViewTrips.getSelectionModel().getSelectedItem(), selectedAction);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(listViewTrips.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Reisų valdymas");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addTruckButton(ActionEvent actionEvent) {
        if(checkTruckFieldIsEmpty()) return;
        if(!isNumeric(truckMaxSpeedField.getText()) || !isNumeric(truckMileageField.getText())){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Ne visi įvesti duomenys yra korektiški.");
            return;
        }
        Transport transport = new Transport(truckModelField.getText(), Integer.parseInt(truckMaxSpeedField.getText()), Integer.parseInt(truckMileageField.getText()), truckManufacturedDatePicker.getValue(), colorField.getText(), truckFuelTypeField.getText(), truckTransmissionTypeField.getText());
        transportService.createTransport(transport);
        alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Transportas sukurtas", "Transport buvo sėkmingai sukurtas.");
        resetTruckFields();
        //update table
        truckData.clear();
        fillTransportTable();
    }
    private boolean checkTruckFieldIsEmpty() {
        if(truckFuelTypeField.getText().isEmpty() || colorField.getText().isEmpty() || truckMileageField.getText().isEmpty() || truckModelField.getText().isEmpty() || truckTransmissionTypeField.getText().isEmpty() || truckMaxSpeedField.getText().isEmpty()){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Prašome įvesti visus duomenis.");
            return true;
        }
        return false;
    }

    @FXML
    public void showFreight(ActionEvent actionEvent) {
        if(checkIfFreightIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas krovinys", "Prašome pasirinkti krovinį iš sąrašo.");
            return;
        }
        callFreightViewPage(CRUD_enum.VIEW);
    }

    @FXML
    public void editFreight(ActionEvent actionEvent) {
        if(checkIfFreightIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas krovinys", "Prašome pasirinkti krovinį iš sąrašo.");
            return;
        }
        callFreightViewPage(CRUD_enum.EDIT);
        freightList.getItems().clear();
        fillFreightList();
    }

    @FXML
    public void addFreight(ActionEvent actionEvent) {
        callFreightViewPage(CRUD_enum.CREATE);
        freightList.getItems().clear();
        fillFreightList();
    }

    @FXML
    public void deleteFreight(ActionEvent actionEvent) {
        if(checkIfFreightIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas krovinys", "Prašome pasirinkti krovinį iš sąrašo.");
            return;
        }
        freightService.removeFreight(freightList.getSelectionModel().getSelectedItem().getId());
        freightList.getItems().remove(freightList.getSelectionModel().getSelectedIndex());
        alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Krovinys ištrintas", "Krovinys buvo sėkmingai ištrintas.");
    }

    void callFreightViewPage(CRUD_enum selectedAction){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Freight-view.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            FreightWindow freightWindow = fxmlLoader.getController();
            freightWindow.setData(freightList.getSelectionModel().getSelectedItem(), selectedAction);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(freightList.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Krovinių valdymas");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showCheckpoint(ActionEvent actionEvent) {
        if(checkIfCheckpointIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas maršrutas", "Prašome pasirinkti maršrutą iš sąrašo.");
            return;
        }
        callCheckpointViewPage(CRUD_enum.VIEW);
    }

    @FXML
    public void editCheckpoint(ActionEvent actionEvent) {
        if(checkIfCheckpointIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas maršrutas", "Prašome pasirinkti maršrutą iš sąrašo.");
            return;
        }
        callCheckpointViewPage(CRUD_enum.EDIT);
        listViewCheckpoint.getItems().clear();
        fillcheckpointList();
    }

    @FXML
    public void createCheckpoint(ActionEvent actionEvent) {
        callCheckpointViewPage(CRUD_enum.CREATE);
        listViewCheckpoint.getItems().clear();
        fillcheckpointList();
    }

    @FXML
    public void deleteCheckpoint(ActionEvent actionEvent) {
        if(checkIfCheckpointIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas maršrutas", "Prašome pasirinkti maršrutą iš sąrašo.");
            return;
        }
        destinationPointService.removeDestinationPoint(listViewCheckpoint.getSelectionModel().getSelectedItem());
        listViewCheckpoint.getItems().remove(listViewCheckpoint.getSelectionModel().getSelectedIndex());
        alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Maršrutas ištrintas", "Maršrutas buvo sėkmingai ištrintas.");
    }

    private void callCheckpointViewPage(CRUD_enum selectedAction) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("DestinationPoint-view.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            DestinationPointWindow destinationPointWindow = fxmlLoader.getController();
            destinationPointWindow.setData(listViewCheckpoint.getSelectionModel().getSelectedItem(), selectedAction);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(listViewCheckpoint.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Maršrutų valdymas");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showUser(ActionEvent actionEvent) {
        if(checkIfUserIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas vartotojas", "Prašome pasirinkti vartotoją iš sąrašo.");
            return;
        }
        callUserViewPage(CRUD_enum.VIEW);
    }

    @FXML
    public void editUser(ActionEvent actionEvent) {
        if(checkIfUserIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas vartotojas", "Prašome pasirinkti vartotoją iš sąrašo.");
            return;
        }
        callUserViewPage(CRUD_enum.EDIT);
        userList.getItems().clear();
        fillUserList();
    }

    @FXML
    public void addUser(ActionEvent actionEvent) {
        callUserViewPage(CRUD_enum.CREATE);
        userList.getItems().clear();
        fillUserList();
    }

    @FXML
    public void deleteUser(ActionEvent actionEvent) {
        if(checkIfUserIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas vartotojas", "Prašome pasirinkti vartotoją iš sąrašo.");
            return;
        }
        userService.removeUser(userList.getSelectionModel().getSelectedItem());
        userList.getItems().remove(userList.getSelectionModel().getSelectedIndex());
        alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Vartotojas ištrintas", "Vartotojas buvo sėkmingai ištrintas.");
    }
    void callUserViewPage(CRUD_enum selectedAction){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("User-view.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            UserWindow userWindow = fxmlLoader.getController();
            userWindow.setData(userList.getSelectionModel().getSelectedItem(), selectedAction, loggedInUser);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(userList.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Vartotojų valdymas");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
