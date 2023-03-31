package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import truck.truckmanagement.Enum.CRUD_enum;
import truck.truckmanagement.Model.Freight;
import truck.truckmanagement.Service.FreightService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

public class FreightWindow {
    @FXML
    public TextField headerField;
    @FXML
    public TextField providerField;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public Spinner<Double> weightSpinner;
    @FXML
    public Spinner<Integer> quantitySpinner;
    @FXML
    public Button saveButton;
    @FXML
    public Label headerText;
    private Freight selectedFreight;
    private CRUD_enum selectedAction;
    private FreightService freightService;
    @FXML
    public void makeSave() {
        if(fieldIsEmpty()) return;
        setObject();
        if(selectedAction == CRUD_enum.EDIT){
            freightService.updateFreight(selectedFreight);
            alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Krovinys atnaujintas", "Krovinio duomenys buvo sėkmingai atnaujinti.");
        }else if(selectedAction == CRUD_enum.CREATE){
            freightService.createFreight(selectedFreight);
            alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Krovinys sukurtas", "Krovinys buvo sėkmingai sukurtas.");
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    public void setData(Freight selectedFreight, CRUD_enum selectedAction) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");

        this.selectedFreight = selectedFreight;
        this.selectedAction = selectedAction;
        this.freightService = new FreightService(entityManagerFactory);

        fillFields();
    }

    private void setObject() {
        if(selectedAction == CRUD_enum.CREATE){
            selectedFreight = new Freight(headerField.getText(), descriptionTextArea.getText(), weightSpinner.getValue(), quantitySpinner.getValue(), providerField.getText());
            return;
        }
        selectedFreight.setHeader(headerField.getText());
        selectedFreight.setDescription(descriptionTextArea.getText());
        selectedFreight.setWeight(weightSpinner.getValue());
        selectedFreight.setQuantity(quantitySpinner.getValue());
        selectedFreight.setProvider(providerField.getText());
    }
    private void fillFields() {
        if(selectedAction == CRUD_enum.CREATE){
            headerText.setText("Krovinio kūrimas");
            return;
        }
        headerField.setText(selectedFreight.getHeader());
        providerField.setText(selectedFreight.getProvider());
        descriptionTextArea.setText(selectedFreight.getDescription());

        quantitySpinner.getValueFactory().setValue(selectedFreight.getQuantity());
        weightSpinner.getValueFactory().setValue(selectedFreight.getWeight());

        if(selectedAction == CRUD_enum.VIEW){
            headerText.setText("Krovinio peržiūra");
            providerField.setEditable(false);
            headerField.setEditable(false);
            descriptionTextArea.setEditable(false);
            weightSpinner.setEditable(false);
            quantitySpinner.setEditable(false);
            saveButton.setDisable(true);
        }else if(selectedAction == CRUD_enum.EDIT){
            headerText.setText("Krovinio redagavimas");
        }
    }

    private boolean fieldIsEmpty() {
        if(headerField.getText().isEmpty() || descriptionTextArea.getText().isEmpty() || providerField.getText().isEmpty() || (quantitySpinner.getValue() == 0) || (weightSpinner.getValue() == 0.0)){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Prašome įvesti visus duomenis.");
            return true;
        }
        return false;
    }
}
