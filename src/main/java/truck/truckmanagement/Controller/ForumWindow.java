package truck.truckmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import truck.truckmanagement.Enum.CRUD_enum;
import truck.truckmanagement.Model.Forum;
import truck.truckmanagement.Model.User;

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

    private Forum selectedForumTopic;
    private CRUD_enum selectedAction;
    private User loggedInUser;

    public void setData(Forum selectedItem, CRUD_enum selectedAction, User loggedInUser) {
        this.selectedForumTopic = selectedItem;
        this.selectedAction = selectedAction;
        this.loggedInUser = loggedInUser;
    }

    @FXML
    public void buttonSave(ActionEvent actionEvent) {

    }
}
