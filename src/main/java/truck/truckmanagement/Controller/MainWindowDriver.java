package truck.truckmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import truck.truckmanagement.Enum.CRUD_enum;
import truck.truckmanagement.Enum.Destination_filters_enum;
import truck.truckmanagement.HelloApplication;
import truck.truckmanagement.Model.*;
import truck.truckmanagement.Service.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.*;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

public class MainWindowDriver {
    @FXML
    public ListView<Destination> listViewTrips;
    @FXML
    public ListView<Forum> listViewForum;
    @FXML
    public TextField fieldFirstname;
    @FXML
    public TextField fieldLastname;
    @FXML
    public TextField fieldEmail;
    @FXML
    public TextField fieldPassword;
    @FXML
    public TextField fieldPhone;
    @FXML
    public DatePicker dateBirthday;

    private User loggedInUser;
    private DestinationService destinationService;
    private ForumService forumService;
    private UserService userService;

    public void setData(User user) {
        this.loggedInUser = user;

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");
        this.destinationService = new DestinationService(entityManagerFactory);
        this.forumService = new ForumService(entityManagerFactory);
        this.userService = new UserService(entityManagerFactory);

        fillFields();
    }

    private void fillFields() {
        fillTripsList();
        fillForumList();
        fillProfileFields();
    }

    private void fillTripsList() {
        List<Destination> destinations = destinationService.getAllDestinationsByDriverId(loggedInUser.getId(), Destination_filters_enum.NONE, "");
        for (Destination destination : destinations) {
            if (destination.getEndDate() == null) {
                listViewTrips.getItems().add(destination);
                break;
            }
        }
    }

    private boolean checkIfTripIsSelected() {
        return listViewTrips.getSelectionModel().getSelectedItems().isEmpty();
    }

    private void callTripViewPage() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Trip-view.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            TripWindow tripWindow = fxmlLoader.getController();
            tripWindow.setData(listViewTrips.getSelectionModel().getSelectedItem(), loggedInUser);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(listViewTrips.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Reiso valdymas");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewTrip() {
        if (checkIfTripIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinktas reisas", "Prašome pasirinkti reisą iš sąrašo.");
            return;
        }
        callTripViewPage();
        fillTripsList();
    }

    private void callForumTopicViewPage(CRUD_enum selectedAction) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Forum-view.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            ForumWindow forumWindow = fxmlLoader.getController();
            forumWindow.setData(listViewForum.getSelectionModel().getSelectedItem(), selectedAction, loggedInUser);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initOwner(listViewForum.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Forumo valdymas");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void readForumTopic() {
        if (checkIfForumTopicIsSelected()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Nepasirinkta forumo tema", "Prašome pasirinkti forumo temą iš sąrašo.");
            return;
        }
        callForumTopicViewPage(CRUD_enum.VIEW);

        //kad atsinaujintu nauji komentarai
        listViewForum.getItems().clear();
        fillForumList();
    }

    private boolean checkIfForumTopicIsSelected() {
        return listViewForum.getSelectionModel().getSelectedItems().isEmpty();
    }

    @FXML
    public void createForumTopic() {
        callForumTopicViewPage(CRUD_enum.CREATE);

        listViewForum.getItems().clear();
        fillForumList();
    }

    private void fillForumList() {
        List<Forum> forums = forumService.getAllForums();
        forums.forEach(f -> listViewForum.getItems().add(f));
    }

    private void fillProfileFields() {
        fieldFirstname.setText(loggedInUser.getFirstname());
        fieldLastname.setText(loggedInUser.getLastname());
        fieldPassword.setText(loggedInUser.getPassword());
        fieldEmail.setText(loggedInUser.getEmail());
        fieldPhone.setText(String.valueOf(loggedInUser.getPhoneNumber()));
        dateBirthday.setValue(loggedInUser.getBirthday());
    }

    private boolean myInfofieldsAreEmpty() {
        if (fieldPassword.getText().isEmpty() || fieldFirstname.getText().isEmpty() || fieldLastname.getText().isEmpty() || fieldEmail.getText().isEmpty() || fieldPhone.getText().isEmpty()) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Prašome įvesti visus duomenis.");
            return true;
        }
        if (!isNumeric(fieldPhone.getText()) || fieldPhone.getText().length() != 9) {
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Netinkamas telefono numerio formatas.");
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    @FXML
    public void saveMyInfo() {
        if(myInfofieldsAreEmpty()) return;
        alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Vartotojas atnaujintas", "Jūsų duomenys buvo sėkmingai atnaujinti.");
    }
}
