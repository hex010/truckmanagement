package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import truck.truckmanagement.HelloApplication;
import truck.truckmanagement.Model.Destination;
import truck.truckmanagement.Model.User;
import truck.truckmanagement.Service.TruckStopService;

import java.io.IOException;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

public class TripWindow {
    @FXML
    public Label countryField;
    @FXML
    public Label cityField;
    @FXML
    public Label addressField;
    @FXML
    public Label addInfoField;
    @FXML
    public Label freightTitleField;
    @FXML
    public Label freightDescField;
    @FXML
    public Label freightQuantityField;
    @FXML
    public Label freightWeight;
    @FXML
    public Label modelField;
    @FXML
    public Label fuelTypeField;
    @FXML
    public Label transmissionTypeField;
    @FXML
    public Label mileageField;
    @FXML
    public Label colorField;
    public ListView truckStopsListView;
    private User loggedInUser;
    private Destination selectedDestination;
    private TruckStopService truckStopService;

    public void setData(Destination selectedItem, User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.selectedDestination = selectedItem;
        this.truckStopService = new TruckStopService(entityManagerFactory);

        fillFields();
    }

    private void fillFields() {
        countryField.setText(countryField.getText() + " " + selectedDestination.getDestinationPoint().getCountry());
        cityField.setText(cityField.getText() + " " + selectedDestination.getDestinationPoint().getCity());
        addressField.setText(addressField.getText() + " " + selectedDestination.getDestinationPoint().getAddress());
        addInfoField.setText(addInfoField.getText() + " " + selectedDestination.getDestinationPoint().getAdditionalInformation());

        freightTitleField.setText(freightTitleField.getText() + " " + selectedDestination.getFreight().getHeader());
        freightDescField.setText(freightDescField.getText() + " " + selectedDestination.getFreight().getDescription());
        freightQuantityField.setText(freightQuantityField.getText() + " " + selectedDestination.getFreight().getQuantity());
        freightWeight.setText(freightWeight.getText() + " " + selectedDestination.getFreight().getWeight());

        modelField.setText(modelField.getText() + " " + selectedDestination.getTransport().getModel());
        fuelTypeField.setText(fuelTypeField.getText() + " " + selectedDestination.getTransport().getFuelType());
        transmissionTypeField.setText(transmissionTypeField.getText() + " " + selectedDestination.getTransport().getTransmissionType());
        mileageField.setText(mileageField.getText() + " " + selectedDestination.getTransport().getMileage());
        colorField.setText(colorField.getText() + " " + selectedDestination.getTransport().getColor());
        fillStopsList();
    }
    @FXML
    public void addTruckStop() {
        if(selectedDestination.getEndDate() != null){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Pridėti negalima", "Šis reisas jau yra pasibaigęs, pridėti naujo sustojimo negalima.");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("TruckStop-view.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            TruckStopWindow truckStopWindow = fxmlLoader.getController();
            truckStopWindow.setData(selectedDestination);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(truckStopsListView.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Reiso sustojimas");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        truckStopsListView.getItems().clear();
        fillStopsList();
    }
    private void fillStopsList() {
        List<TruckStop> truckStops = truckStopService.getAllTruckStopsByDestinationId(selectedDestination.getId());
        truckStops.forEach(f->truckStopsListView.getItems().add(f));
    }
}
