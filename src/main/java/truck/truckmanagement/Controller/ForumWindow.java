package truck.truckmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import truck.truckmanagement.Enum.CRUD_enum;
import truck.truckmanagement.Model.Comment;
import truck.truckmanagement.Model.Forum;
import truck.truckmanagement.Model.User;
import truck.truckmanagement.Service.CommentService;
import truck.truckmanagement.Service.ForumService;
import truck.truckmanagement.Utils.FxUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static truck.truckmanagement.Utils.FxUtils.alertMessage;
import static truck.truckmanagement.Utils.FxUtils.alertMessageYesAndNo;

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
    @FXML
    public TreeView<Comment> commentsTreeView;
    @FXML
    public AnchorPane anchorPaneComments;
    @FXML
    public Label commentByLabel;
    @FXML
    public TextArea commentTextByLabel;
    @FXML
    public Text commentReplyLabel;
    @FXML
    public TextArea myReplieToCommentTextArea;
    @FXML
    public Button applyReplyButton;
    @FXML
    public Button deleteMyCommentButton;

    private Forum selectedForumTopic;
    private CRUD_enum selectedAction;
    private User loggedInUser;
    private ForumService forumService;
    private CommentService commentService;

    private Comment selectedComment;
    private TreeItem selectedTreeItem;

    public void setData(Forum selectedItem, CRUD_enum selectedAction, User loggedInUser) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");

        this.selectedForumTopic = selectedItem;
        this.selectedAction = selectedAction;
        this.loggedInUser = loggedInUser;
        this.forumService = new ForumService(entityManagerFactory);
        this.commentService = new CommentService(entityManagerFactory);

        fillFields();
    }

    private void fillFields() {
        deleteMyCommentButton.setVisible(false);
        if(selectedAction == CRUD_enum.CREATE) {
            topicAuthorLabel.setText("Temos autorius: " + loggedInUser);
            commentByLabel.setVisible(false);
            commentReplyLabel.setVisible(false);
            applyReplyButton.setVisible(false);
            commentTextByLabel.setVisible(false);
            myReplieToCommentTextArea.setVisible(false);
            commentsTreeView.setVisible(false);
            labelComments.setVisible(false);
            anchorPaneComments.setVisible(false);
            scrollPane.setPrefHeight(398);
        } else if(selectedAction == CRUD_enum.VIEW){
            newCommentUI();
            loadComments();
            topicAuthorLabel.setText("Temos autorius: "+ selectedForumTopic.getUser());
            topicHeaderTextField.setText(selectedForumTopic.getTitle());
            topicDescriptionTextArea.setText(selectedForumTopic.getDescription());
            topicHeaderTextField.setEditable(false);
            topicDescriptionTextArea.setEditable(false);
            saveButton.setDisable(true);
        }
    }
    public void loadComments() {
        List<Comment> comments = selectedForumTopic.getCommets();

        commentsTreeView.setRoot(new TreeItem<>(new Comment()));
        commentsTreeView.setShowRoot(false);
        commentsTreeView.getRoot().setExpanded(true);

        for(Comment comment : comments){
            if(comment.getParentComment() == null){
                addTreeItem(comment, commentsTreeView.getRoot());
            }
        }
    }
    private void newCommentUI() {
        deleteMyCommentButton.setVisible(false);
        commentTextByLabel.setEditable(false);
        commentTextByLabel.clear();
        myReplieToCommentTextArea.clear();
        commentByLabel.setVisible(false);
        commentReplyLabel.setText("Rašyti naują komentarą:");
        applyReplyButton.setText("Pateikti komentarą");
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

    @FXML
    public void showFullCommentWithReplyOptionOnMouseClick(MouseEvent mouseEvent) {
        Node node = mouseEvent.getPickResult().getIntersectedNode();
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            selectedTreeItem = commentsTreeView.getSelectionModel().getSelectedItem();
            selectedComment = (Comment) selectedTreeItem.getValue();

            commentByLabel.setVisible(true);
            commentByLabel.setText("Komentaras nuo " +selectedComment.getUser());
            commentTextByLabel.setText(selectedComment.getCommentText());
            commentReplyLabel.setText("Atsakyti į komentarą:");
            applyReplyButton.setText("Pateikti atsakymą");

            if(selectedComment.getUser().getId() != loggedInUser.getId()){
                commentTextByLabel.setEditable(false);
                deleteMyCommentButton.setVisible(false);
            }else{
                commentTextByLabel.setEditable(true);
                deleteMyCommentButton.setVisible(true);
            }
        }else{
            selectedTreeItem = null;
            selectedComment = null;
            commentsTreeView.getSelectionModel().clearSelection();
            newCommentUI();
        }
    }
    @FXML
    public void applyReply() {
        if (myReplieToCommentTextArea.getText().isEmpty()) {
            FxUtils.alertMessage(Alert.AlertType.ERROR, "Klaida", "Neužpildytas komentaras", "Užpildyti jūsų komentarą, jis negali būti tuščias.");
            return;
        }

        //naujas komentaras
        if (selectedTreeItem == null || selectedComment == null) {
            Comment comment = new Comment(
                    myReplieToCommentTextArea.getText(),
                    null,
                    selectedForumTopic,
                    loggedInUser
            );
            commentService.createComment(comment);

            addTreeItem(comment, commentsTreeView.getRoot());
            myReplieToCommentTextArea.clear();

            return;
        }

        //atsakymas i komentara
        Comment comment = new Comment(
                myReplieToCommentTextArea.getText(),
                selectedComment,
                selectedForumTopic,
                loggedInUser
        );
        commentService.createComment(comment);

        addTreeItem(comment, selectedTreeItem);
        myReplieToCommentTextArea.clear();
        selectedTreeItem.setExpanded(true);
    }
    private void addTreeItem(Comment comment, TreeItem parent) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parent.getChildren().add(treeItem);
        if(comment.getReplies() != null) comment.getReplies().forEach(r -> addTreeItem(r, treeItem));
    }
    @FXML
    public void deleteMyComment() {
        if(selectedComment != null) {
            if (alertMessageYesAndNo("Patvirtinkite", "Ar Jūs įsitikinę, kad norite ištrinti šį komentarą?", "Paspauskite 'Taip' norint ištrinti, arba 'Ne' norint atšaukti.")) {
                commentService.removeComment(selectedComment);
                selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
                alertMessage(Alert.AlertType.INFORMATION, "Pavyko", "Komentaras ištrintas", "Komentaras buvo sėkmingai ištrintas.");
            }
        }
    }
}
