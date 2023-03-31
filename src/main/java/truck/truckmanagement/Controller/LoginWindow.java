package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import truck.truckmanagement.Enum.Role_enum;
import truck.truckmanagement.HelloApplication;
import truck.truckmanagement.Model.User;
import truck.truckmanagement.Service.UserService;
import truck.truckmanagement.Utils.FxUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class LoginWindow {
    @FXML
    public TextField loginTextField;
    @FXML
    public PasswordField passwordTextField;

    private final UserService userService;

    public LoginWindow() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");
        this.userService = new UserService(entityManagerFactory);
    }

    @FXML
    public void authenticateUser() {
        if(areTheFieldsEmpty()){
            FxUtils.alertMessage(Alert.AlertType.ERROR, "Klaida", "Ne visi laukai užpildyti", "Prašome užpildyti visus laukus");
            return;
        }
        User user = userService.getUserByLoginData(loginTextField.getText(), passwordTextField.getText());
        if(user == null){
            FxUtils.alertMessage(Alert.AlertType.ERROR, "Klaida", "Autentifikacijos klaida", "Toks vartotojas nerastas. Bandykite dar kartą.");
            return;
        }
        FxUtils.alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Prisijungimas pavyko", "Prisijungimas sėkmingai pavyko");
        openMainWindow(user);
    }

    private void openMainWindow(User user) {
        FXMLLoader fxmlLoader;
        if(user.getRole() == Role_enum.ADMINISTRATORIUS){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainWindowAdmin-view.fxml"));
        } else if(user.getRole() == Role_enum.VADYBININKAS){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainWindowManager-view.fxml"));
        } else{
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainWindowDriver-view.fxml"));
        }

        try {
            Parent parent = fxmlLoader.load();
            if(user.getRole() == Role_enum.ADMINISTRATORIUS){
                MainWindowAdmin mainWindow = fxmlLoader.getController();
                mainWindow.setData(user);
            } else if(user.getRole() == Role_enum.VADYBININKAS){
                MainWindowManager mainWindow = fxmlLoader.getController();
                mainWindow.setData(user);
            } else{
                MainWindowDriver mainWindow = fxmlLoader.getController();
                mainWindow.setData(user);
            }
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Krovinių valdymo sistema");
            stage.setScene(scene);

            Stage stageCurrent = (Stage) loginTextField.getScene().getWindow();
            stageCurrent.close();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean areTheFieldsEmpty() {
        return loginTextField.getText().isEmpty() || passwordTextField.getText().isEmpty();
    }


}
