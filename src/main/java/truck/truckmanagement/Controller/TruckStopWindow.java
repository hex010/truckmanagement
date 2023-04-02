package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import truck.truckmanagement.Model.Destination;
import truck.truckmanagement.Model.TruckStop;
import truck.truckmanagement.Service.TruckStopService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

public class TruckStopWindow {
    @FXML
    public TextField countryTextField;
    @FXML
    public TextField CityTextField;
    @FXML
    public TextField addressFTextField;
    @FXML
    public DatePicker startDate;
    @FXML
    public DatePicker endDate;
    @FXML
    public TextArea reasonTextArea;

    private Destination selectedDestination;
    private TruckStopService truckStopService;

    public void setData(Destination selectedDestination) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");

        this.truckStopService = new TruckStopService(entityManagerFactory);
        this.selectedDestination = selectedDestination;
    }

    @FXML
    public void saveTruckStop() {
        if(!correctFields()) return;
        TruckStop truckStop = new TruckStop(countryTextField.getText(), CityTextField.getText(), addressFTextField.getText(), reasonTextArea.getText(), startDate.getValue(), endDate.getValue(), selectedDestination);
        truckStopService.createTruckStop(truckStop);

        alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Reiso sustojimas sukurtas", "Reiso sustojimas buvo sėkmingai sukurtas.");
        Stage stage = (Stage) reasonTextArea.getScene().getWindow();
        stage.close();
    }

    private boolean correctFields() {
        if(addressFTextField.getText().isEmpty() || countryTextField.getText().isEmpty() || reasonTextArea.getText().isEmpty() || CityTextField.getText().isEmpty()){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Ne visi laukai pilnai užpildyti");
            return false;
        }
        return true;
    }
}
