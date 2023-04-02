package truck.truckmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import truck.truckmanagement.Enum.CRUD_enum;
import truck.truckmanagement.Model.Forum;
import truck.truckmanagement.Model.User;
import truck.truckmanagement.Service.ForumService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;

public class ForumWindow {
    @FXML
    public TextField topicHeaderTextField;
    @FXML
    public TextArea topicDescriptionTextArea;
    @FXML
    public Label topicAuthorLabel;
    @FXML
    public Text labelComments;
    @FXML
    public Button saveButton;
    @FXML
    public ScrollPane scrollPane;

    private Forum selectedForumTopic;
    private CRUD_enum selectedAction;
    private User loggedInUser;
    private ForumService forumService;

    public void setData(Forum selectedItem, CRUD_enum selectedAction, User loggedInUser) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");

        this.selectedForumTopic = selectedItem;
        this.selectedAction = selectedAction;
        this.loggedInUser = loggedInUser;
        this.forumService = new ForumService(entityManagerFactory);

        fillFields();
    }

    private void fillFields() {
        if(selectedAction == CRUD_enum.CREATE) {
            topicAuthorLabel.setText("Temos autorius: " + loggedInUser);
            labelComments.setVisible(false);
            scrollPane.setPrefHeight(398);
        } else if(selectedAction == CRUD_enum.VIEW){
            topicAuthorLabel.setText("Temos autorius: "+ selectedForumTopic.getUser());
            topicHeaderTextField.setText(selectedForumTopic.getTitle());
            topicDescriptionTextArea.setText(selectedForumTopic.getDescription());
            topicHeaderTextField.setEditable(false);
            topicDescriptionTextArea.setEditable(false);
            saveButton.setDisable(true);
        }
    }

    @FXML
    public void buttonSave() {
        if(fieldIsEmpty()) return;
        setObject();
        if(selectedAction == CRUD_enum.CREATE){
            forumService.createForumTopic(selectedForumTopic);
            alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Forumo tema sukurta", "Forumo tema buvo sėkmingai sukurta.");
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    private boolean fieldIsEmpty() {
        if(topicHeaderTextField.getText().isEmpty() || topicDescriptionTextArea.getText().isEmpty()){
            alertMessage(Alert.AlertType.ERROR, "Klaida", "Įvedimo klaida", "Prašome įvesti visus duomenis.");
            return true;
        }
        return false;
    }
    private void setObject() {
        if(selectedAction == CRUD_enum.CREATE){
            selectedForumTopic = new Forum(topicHeaderTextField.getText(), topicDescriptionTextArea.getText(), loggedInUser);
        }
    }
}
