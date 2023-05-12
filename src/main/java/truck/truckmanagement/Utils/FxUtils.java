package truck.truckmanagement.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class FxUtils {
    public static void alertMessage(Alert.AlertType alertType, String title, String headerText, String alertMsg){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(alertMsg);
        alert.showAndWait();
    }

    public static boolean alertMessageYesAndNo(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ButtonType buttonTypeYes = new ButtonType("Taip");
        ButtonType buttonTypeNo = new ButtonType("Ne");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }
}
