package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import truck.truckmanagement.HelloApplication;
import truck.truckmanagement.Model.Destination;
import truck.truckmanagement.Model.TruckStop;
import truck.truckmanagement.Model.User;
import truck.truckmanagement.Service.DestinationService;
import truck.truckmanagement.Service.TruckStopService;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.List;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

import javax.persistence.Persistence;
import java.time.LocalDate;

public class TripWindow extends Parent {
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
    @FXML
    public Button finishTripButtonId;
    private User loggedInUser;
    Destination selectedDestination;
    DestinationService destinationService;
    public ListView truckStopsListView;
    private TruckStopService truckStopService;
    private EntityManagerFactory entityManagerFactory;

    public void setData(Destination selectedItem, User loggedInUser) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");
        this.destinationService = new DestinationService(entityManagerFactory);
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

        isShowButton(selectedDestination.getEndDate());

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

    public void isShowButton(LocalDate localDate) {
        if(localDate != null) finishTripButtonId.setVisible(false);
    }

    @FXML
    public void finishTrip() {
        updateDestinationEndDate();
        showInformationAlert();
        closeWindow();
    }
    void updateDestinationEndDate() {
        selectedDestination.setEndDate(LocalDate.now());
        destinationService.updateDestination(selectedDestination);
    }

    void showInformationAlert() {
        alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Reisas atnaujintas", "Reisas sėkmingai užbaigtas.");
    }

    void closeWindow() {
        Stage stage = (Stage) finishTripButtonId.getScene().getWindow();
        stage.close();
    }

}
