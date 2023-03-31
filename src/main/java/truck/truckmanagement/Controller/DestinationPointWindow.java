package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import truck.truckmanagement.Enum.CRUD_enum;
import truck.truckmanagement.Model.DestinationPoint;
import truck.truckmanagement.Service.DestinationPointService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

public class DestinationPointWindow {
    @FXML
    public TextField countryTextField;
    @FXML
    public Label headerTextLabel;
    @FXML
    public TextField cityTextField;
    @FXML
    public TextField addressTextField;
    @FXML
    public TextArea additionalInfoTextArea;
    @FXML
    public Button saveButton;

    private DestinationPoint selectedDestinationPoint;
    private CRUD_enum selectedAction;
    private DestinationPointService destinationPointService;

    public void setData(DestinationPoint selectedItem, CRUD_enum selectedAction) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");

        this.selectedDestinationPoint = selectedItem;
        this.selectedAction = selectedAction;
        this.destinationPointService = new DestinationPointService(entityManagerFactory);

        fillFields();
    }

    private void fillFields() {
        if(selectedAction == CRUD_enum.CREATE){
            headerTextLabel.setText("Maršruto kūrimas");
            return;
        }
        countryTextField.setText(selectedDestinationPoint.getCountry());
        cityTextField.setText(selectedDestinationPoint.getCity());
        addressTextField.setText(selectedDestinationPoint.getAddress());
        additionalInfoTextArea.setText(selectedDestinationPoint.getAdditionalInformation());

        if(selectedAction == CRUD_enum.VIEW){
            headerTextLabel.setText("Maršruto peržiūra");
            countryTextField.setEditable(false);
            cityTextField.setEditable(false);
            addressTextField.setEditable(false);
            additionalInfoTextArea.setEditable(false);
            saveButton.setDisable(true);
        }else if(selectedAction == CRUD_enum.EDIT){
            headerTextLabel.setText("Maršruto redagavimas");
        }
    }

    private void setObject() {
        if(selectedAction == CRUD_enum.CREATE){
            selectedDestinationPoint = new DestinationPoint(countryTextField.getText(), cityTextField.getText(), addressTextField.getText(), additionalInfoTextArea.getText());
            return;
        }
        selectedDestinationPoint.setCountry(countryTextField.getText());
        selectedDestinationPoint.setCity(cityTextField.getText());
        selectedDestinationPoint.setAddress(addressTextField.getText());
        selectedDestinationPoint.setAdditionalInformation(additionalInfoTextArea.getText());
    }

    private boolean fieldIsEmpty() {
        if(countryTextField.getText().isEmpty() || cityTextField.getText().isEmpty() || addressTextField.getText().isEmpty() || additionalInfoTextArea.getText().isEmpty()){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Prašome įvesti visus duomenis.");
            return true;
        }
        return false;
    }

    @FXML
    public void buttonSave() {
        if(fieldIsEmpty()) return;
        setObject();
        if(selectedAction == CRUD_enum.EDIT){
            destinationPointService.updateDestinationPoint(selectedDestinationPoint);
            alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Krovinys atnaujintas", "Krovinio duomenys buvo sėkmingai atnaujinti.");
        }else if(selectedAction == CRUD_enum.CREATE){
            destinationPointService.createDestinationPoint(selectedDestinationPoint);
            alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Krovinys sukurtas", "Krovinys buvo sėkmingai sukurtas.");
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }
}
