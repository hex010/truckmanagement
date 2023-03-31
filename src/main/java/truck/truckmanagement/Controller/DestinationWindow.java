package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import truck.truckmanagement.Enum.CRUD_enum;
import truck.truckmanagement.Enum.Destination_filters_enum;
import truck.truckmanagement.Model.*;
import truck.truckmanagement.Service.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

public class DestinationWindow {
    @FXML
    public ComboBox<User> driverComboBox;
    @FXML
    public Label headerText;
    @FXML
    public ComboBox<User> managerComboBox;
    @FXML
    public ComboBox<Transport> transportComboBox;
    @FXML
    public ComboBox<Freight> freightComboBox;
    @FXML
    public ComboBox<DestinationPoint> locationComboBox;

    @FXML
    public Button saveButton;

    private Destination selectedDestination;
    private CRUD_enum selectedAction;
    private DestinationService destinationService;
    private TransportService transportService;
    private FreightService freightService;
    private DestinationPointService destinationPointService;
    private UserService userService;

    public void setData(Destination selectedItem, CRUD_enum selectedAction) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");

        this.selectedDestination = selectedItem;
        this.selectedAction = selectedAction;
        this.destinationService = new DestinationService(entityManagerFactory);
        this.transportService = new TransportService(entityManagerFactory);
        this.freightService = new FreightService(entityManagerFactory);
        this.destinationPointService = new DestinationPointService(entityManagerFactory);
        this.userService = new UserService(entityManagerFactory);

        fillFields();
    }

    private void fillFields() {
        if(selectedAction != CRUD_enum.VIEW) {
            List<Transport> transports = transportService.getAllTransports();
            transports.forEach(t->transportComboBox.getItems().add(t));

            List<Freight> freights = freightService.getAllFreights();
            freights.forEach(f->freightComboBox.getItems().add(f));

            List<DestinationPoint> destinationPoints = destinationPointService.getAllDestinationPoints();
            destinationPoints.forEach(c->locationComboBox.getItems().add(c));

            List<User> drivers = userService.getAllDrivers();
            drivers.forEach(d->driverComboBox.getItems().add(d));

            List<User> managers = userService.getAllManagers();
            managers.forEach(m->managerComboBox.getItems().add(m));
        }

        if(selectedAction == CRUD_enum.VIEW){
            headerText.setText("Reisų peržiūra");
            saveButton.setDisable(true);
            comboBoxSelectFrontItem();
            return;
        }

        if(selectedAction == CRUD_enum.CREATE){
            headerText.setText("Reisų kūrimas");
            return;
        }
        if(selectedAction == CRUD_enum.EDIT){
            headerText.setText("Reisų redagavimas");
            comboBoxSelectFrontItem();
        }
    }

    private void comboBoxSelectFrontItem() {
        driverComboBox.getSelectionModel().select(selectedDestination.getDriver());
        managerComboBox.getSelectionModel().select(selectedDestination.getManager());
        freightComboBox.getSelectionModel().select(selectedDestination.getFreight());
        transportComboBox.getSelectionModel().select(selectedDestination.getTransport());
        locationComboBox.getSelectionModel().select(selectedDestination.getDestinationPoint());
    }

    @FXML
    public void buttonSave() {
        if(!checkComboBoxIsSelected()) return;
        if(selectedDriverHasTripAlready()){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Netinkamai pasirinktas vairuotojas", "Negalima pasirinkti šio vairuotojo, kadangi jis jau turi aktyvų reisą.");
            return;
        }
        setObject();

        if(selectedAction == CRUD_enum.EDIT){
            destinationService.updateDestination(selectedDestination);
            alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Reisas atnaujintas", "Reiso duomenys buvo sėkmingai atnaujinti.");
        }else if(selectedAction == CRUD_enum.CREATE){
            destinationService.createDestination(selectedDestination);
            alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Reisas sukurtas", "Reisas buvo sėkmingai sukurtas.");
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    private boolean selectedDriverHasTripAlready() {
        List<Destination> destinations = destinationService.getAllDestinationsByDriverId(driverComboBox.getValue().getId(), Destination_filters_enum.NONE, "");
        for(Destination destination : destinations){
            if(destination.getEndDate() == null) return true;
        }
        return false;
    }

    private void setObject() {
        DestinationPoint destinationPoint = locationComboBox.getValue();
        Transport transport = transportComboBox.getValue();
        Freight freight = freightComboBox.getValue();
        User driver = driverComboBox.getValue();
        User manager = managerComboBox.getValue();

        if(selectedAction == CRUD_enum.CREATE){
            selectedDestination = new Destination(LocalDate.now(),destinationPoint,transport,freight,driver,manager);
        }else{
            selectedDestination.setManager(manager);
            selectedDestination.setDriver(driver);
            selectedDestination.setTransport(transport);
            selectedDestination.setFreight(freight);
            selectedDestination.setDestinationPoint(destinationPoint);
        }
    }

    private boolean checkComboBoxIsSelected() {
        if(managerComboBox.getSelectionModel().isEmpty() || driverComboBox.getSelectionModel().isEmpty() || freightComboBox.getSelectionModel().isEmpty() || transportComboBox.getSelectionModel().isEmpty() || locationComboBox.getSelectionModel().isEmpty()){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Pasirinkimo klaida", "Prašome pasirinkti visus laukus.");
            return false;
        }

        return true;
    }
}
