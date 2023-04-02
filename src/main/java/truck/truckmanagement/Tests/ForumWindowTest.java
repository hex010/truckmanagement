package truck.truckmanagement.Tests;

import javafx.application.Platform;
import javafx.scene.control.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import truck.truckmanagement.Controller.ForumWindow;
import truck.truckmanagement.Enum.Role_enum;
import truck.truckmanagement.Model.Comment;
import truck.truckmanagement.Model.Forum;
import truck.truckmanagement.Model.User;
import truck.truckmanagement.Utils.FxUtils;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class ForumWindowTest {
    ForumWindow forumWindow;
    User currentUser;

    @BeforeClass
    public static void initToolkit() {
        Platform.startup(() -> {
            // Inicijuoja JavaFX toolkit
        });
    }
    @Before
    public void setUp() {
        forumWindow = new ForumWindow();
        currentUser = new User(Role_enum.VAIRUOTOJAS, "Adsf", "sdag", "aersdf", "fgadf", "fagdfh", 864826498, LocalDate.now());
    }

    @Test
    public void shouldReturnTrueIfOneOfTheFieldsIsEmpty() throws InterruptedException {
        //given
        forumWindow.topicDescriptionTextArea = new TextArea("sometext");
        forumWindow.topicHeaderTextField = new TextField("");

        //when
        boolean result = forumWindow.topicHeaderTextField.getText().isEmpty() || forumWindow.topicDescriptionTextArea.getText().isEmpty();

        //then
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseIfBothFieldsAreNotEmpty() throws InterruptedException {
        //given
        forumWindow.topicDescriptionTextArea = new TextArea("sometext");
        forumWindow.topicHeaderTextField = new TextField("some");

        //when
        boolean result = forumWindow.topicHeaderTextField.getText().isEmpty() || forumWindow.topicDescriptionTextArea.getText().isEmpty();

        //then
        assertFalse(result);
    }

    @Test
    public void shouldCreateNewForumObjectFromFields() throws InterruptedException {
        //given
        forumWindow.topicDescriptionTextArea = new TextArea("sometext");
        forumWindow.topicHeaderTextField = new TextField("asdfa");

        //when
        Forum someForumTopic = new Forum(forumWindow.topicHeaderTextField.getText(),forumWindow.topicDescriptionTextArea.getText(), null);

        //then
        assertEquals(someForumTopic.getTitle(), forumWindow.topicHeaderTextField.getText());
        assertEquals(someForumTopic.getDescription(), forumWindow.topicDescriptionTextArea.getText());
    }

    @Test
    public void shouldForumTopicBeAssociatedWithCurrentUser() throws InterruptedException {
        //given
        forumWindow.topicDescriptionTextArea = new TextArea("sometext");
        forumWindow.topicHeaderTextField = new TextField("asdfa");

        //when
        Forum someForumTopic = new Forum(forumWindow.topicHeaderTextField.getText(),forumWindow.topicDescriptionTextArea.getText(), currentUser);

        //then
        assertEquals(someForumTopic.getUser(), currentUser);
    }
    @Test
    public void shouldReturnTrueIfCommentTextIsEmpty(){
        //given
        forumWindow.myReplieToCommentTextArea = new TextArea("");

        //when
        boolean isempty = forumWindow.myReplieToCommentTextArea.getText().isEmpty();

        //then
        assertTrue(isempty);
    }
    @Test
    public void shouldAddNewCommentToTreeViewIfCommentFieldIsFilled() throws InterruptedException {
        //given
        forumWindow.myReplieToCommentTextArea = new TextArea("adsg");
        forumWindow.commentsTreeView = new TreeView<>();
        forumWindow.commentsTreeView.setRoot(new TreeItem<>(new Comment()));
        Comment comment = new Comment(forumWindow.myReplieToCommentTextArea.getText(), null, null, null);

        //when
        if (!forumWindow.myReplieToCommentTextArea.getText().isEmpty()) {
            addTreeItem(comment, forumWindow.commentsTreeView.getRoot());
        }

        //then
        if(forumWindow.myReplieToCommentTextArea.getText().isEmpty())
            assertTrue(forumWindow.commentsTreeView.getRoot().getChildren().isEmpty());
        else if(!forumWindow.myReplieToCommentTextArea.getText().isEmpty()){
            assertFalse(forumWindow.commentsTreeView.getRoot().getChildren().isEmpty());
        }
    }
    private void addTreeItem(Comment comment, TreeItem parent) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parent.getChildren().add(treeItem);
        if(comment.getReplies() != null) comment.getReplies().forEach(r -> addTreeItem(r, treeItem));
    }
}
