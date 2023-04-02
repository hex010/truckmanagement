package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import truck.truckmanagement.Model.Destination;
import truck.truckmanagement.Model.User;
import truck.truckmanagement.Service.DestinationService;

import java.time.LocalDate;

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
    @FXML
    public Button finishTripButtonId;
    private User loggedInUser;
    private Destination selectedDestination;
    private DestinationService destinationService;
    public void setData(Destination selectedItem, User loggedInUser) {
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
    public void finishTrip() {}
}
