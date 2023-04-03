package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.Test;
import truck.truckmanagement.Model.Destination;
import truck.truckmanagement.Model.User;
import truck.truckmanagement.Service.DestinationService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

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
    public void setData(Destination selectedItem, User loggedInUser) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");
        this.destinationService = new DestinationService(entityManagerFactory);
        this.loggedInUser = loggedInUser;
        this.selectedDestination = selectedItem;

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
