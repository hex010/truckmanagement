package truck.truckmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
    public void buttonSave(ActionEvent actionEvent) {

    }
}
