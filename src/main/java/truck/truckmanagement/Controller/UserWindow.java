package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import truck.truckmanagement.Enum.CRUD_enum;
import truck.truckmanagement.Enum.Role_enum;
import truck.truckmanagement.Model.User;
import truck.truckmanagement.Service.UserService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;


public class UserWindow {
    @FXML
    public TextField loginField;
    @FXML
    public Label headerText;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField firstnameField;
    @FXML
    public TextField lastnameField;
    @FXML
    public DatePicker dateField;
    @FXML
    public Button saveButton;
    @FXML
    public ComboBox<Role_enum> roleComboBox;

    private User selectedUser;
    private User loggedInUser;
    private CRUD_enum selectedAction;
    private UserService userService;
    public void setData(User selectedUser, CRUD_enum selectedAction, User loggedInUser){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");

        this.selectedUser = selectedUser;
        this.selectedAction = selectedAction;
        this.loggedInUser = loggedInUser;
        this.userService = new UserService(entityManagerFactory);

        fillFields();
    }
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    private void fillFields() {
        if(selectedAction != CRUD_enum.VIEW) {
            if(loggedInUser.getRole() == Role_enum.ADMINISTRATORIUS)
                roleComboBox.getItems().setAll(Role_enum.values());
            else{
                roleComboBox.getItems().setAll(Role_enum.VAIRUOTOJAS);
            }
        }

        if(selectedAction == CRUD_enum.CREATE){
            headerText.setText("Vartotojo kūrimas");
            return;
        }

        loginField.setText(selectedUser.getLogin());
        passwordField.setText(selectedUser.getPassword());
        emailField.setText(selectedUser.getEmail());
        phoneField.setText(String.valueOf(selectedUser.getPhoneNumber()));
        firstnameField.setText(selectedUser.getFirstname());
        lastnameField.setText(selectedUser.getLastname());
        dateField.setValue(selectedUser.getBirthday());
        roleComboBox.getSelectionModel().select(selectedUser.getRole());

        if(selectedAction == CRUD_enum.EDIT){
            headerText.setText("Vartotojo redagavimas");
        }

        if(selectedAction == CRUD_enum.VIEW){
            headerText.setText("Vartotojo peržiūra");
            loginField.setEditable(false);
            passwordField.setEditable(false);
            emailField.setEditable(false);
            phoneField.setEditable(false);
            firstnameField.setEditable(false);
            lastnameField.setEditable(false);
            dateField.setEditable(false);
            saveButton.setDisable(true);
        }

        if(!loggedInUser.getRole().equals(Role_enum.ADMINISTRATORIUS)){
            if(selectedAction == CRUD_enum.EDIT || selectedAction == CRUD_enum.VIEW){
                loginField.setText("Nerodomas");
                passwordField.setText("Nerodomas");
                loginField.setDisable(true);
                passwordField.setDisable(true);
            }
        }
    }

    public void confirmUserEdit() {
        if(fieldIsEmpty()) return;
        if(!isNumeric(phoneField.getText()) || phoneField.getText().length() != 9){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Netinkamas telefono numerio formatas.");
            return;
        }
        setObject();
        if(selectedAction == CRUD_enum.EDIT){
            userService.updateUser(selectedUser);
            alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Vartotojas atnaujintas", "Vartotojo duomenys buvo sėkmingai atnaujinti.");
        }else if(selectedAction == CRUD_enum.CREATE){
            userService.createUser(selectedUser);
            alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Vartotojas sukurtas", "Vartotojas buvo sėkmingai sukurtas.");
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    private void setObject() {
        if(selectedAction == CRUD_enum.CREATE){
            selectedUser = new User(roleComboBox.getValue(), loginField.getText(), passwordField.getText(), firstnameField.getText(), lastnameField.getText(), emailField.getText(), Integer.parseInt(phoneField.getText()), dateField.getValue());
            return;
        }

        selectedUser.setRole(roleComboBox.getValue());

        if(loggedInUser.getRole().equals(Role_enum.ADMINISTRATORIUS)){
            selectedUser.setLogin(loginField.getText());
            selectedUser.setPassword(passwordField.getText());
        }

        selectedUser.setFirstname(firstnameField.getText());
        selectedUser.setLastname(lastnameField.getText());
        selectedUser.setEmail(emailField.getText());
        selectedUser.setPhoneNumber(Integer.parseInt(phoneField.getText()));
        selectedUser.setBirthday(dateField.getValue());
    }

    private boolean fieldIsEmpty() {
        if(roleComboBox.getSelectionModel().isEmpty() || loginField.getText().isEmpty() || passwordField.getText().isEmpty() || firstnameField.getText().isEmpty() || lastnameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Prašome įvesti visus duomenis.");
            return true;
        }
        return false;
    }

}
