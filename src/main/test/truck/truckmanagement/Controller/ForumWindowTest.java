package truck.truckmanagement.Controller;

import javafx.application.Platform;
import javafx.scene.control.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import truck.truckmanagement.Controller.ForumWindow;
import truck.truckmanagement.Enum.Role_enum;
import truck.truckmanagement.Model.Comment;
import truck.truckmanagement.Model.Forum;
import truck.truckmanagement.Model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ForumWindowTest extends JavaFXTestManage {
    ForumWindow forumWindow;
    User currentUser;

    @Before
    public void setUp() {
        forumWindow = new ForumWindow();
        currentUser = new User(Role_enum.VAIRUOTOJAS, "Adsf", "sdag", "aersdf", "fgadf", "fagdfh", 864826498, LocalDate.now());
        currentUser.setId(8);
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

    @Test
    public void replyCommentTextAreaShouldBeEditibleIfCommentIsMine(){
        //given
        forumWindow.commentTextByLabel = new TextArea();
        User userThatWroteComment = new User(Role_enum.VAIRUOTOJAS, "random456", "cxbxcb", "qewrq", "werw", "gdfg", 868826498, LocalDate.now());
        userThatWroteComment.setId(8);
        Comment comment = new Comment("some comment text", null, null, userThatWroteComment);

        //when
        if(comment.getUser().getId() != currentUser.getId()){
            forumWindow.commentTextByLabel.setEditable(false);
        }else{
            forumWindow.commentTextByLabel.setEditable(true);
        }

        //then
        assertTrue(forumWindow.commentTextByLabel.isEditable());
    }
    
    @Test
    public void replyCommentsShouldBeAddedToParentComment(){
        //given
        Comment comment = new Comment("some comment text", null, null, null);
        Comment commentReply1 = new Comment("some comment text2", comment, null, null);
        Comment commentReply2 = new Comment("some comment text3", comment, null, null);
        List<Comment> allReplies = new ArrayList<>();
        allReplies.add(commentReply1);
        allReplies.add(commentReply2);

        //when
        comment.setReplies(allReplies);

        //then
        assertEquals(2, comment.getReplies().size());
    }

    @Test
    public void replyCommentsShouldBeAddedToParentCommentInTreeView(){
        //given
        forumWindow.commentsTreeView = new TreeView<>();
        forumWindow.commentsTreeView.setRoot(new TreeItem<>(new Comment()));
        Comment comment = new Comment("some", null, null, null);
        Comment commentReply1 = new Comment("some comment text2", comment, null, null);
        Comment commentReply2 = new Comment("some comment text3", comment, null, null);
        Comment commentReply3 = new Comment("some comment text3", comment, null, null);

        //when
        addTreeItem(comment, forumWindow.commentsTreeView.getRoot());
        addTreeItem(commentReply1, forumWindow.commentsTreeView.getRoot());
        addTreeItem(commentReply2, forumWindow.commentsTreeView.getRoot());
        addTreeItem(commentReply3, forumWindow.commentsTreeView.getRoot());
        //then
        assertEquals(3, forumWindow.commentsTreeView.getRoot().getChildren().size()-1);
    }
    @Test
    public void selectedCommentShouldBeDeletedFromTreeViewAndAllTheirChildToo(){
        //given
        forumWindow.commentsTreeView = new TreeView<>();
        forumWindow.commentsTreeView.setRoot(new TreeItem<>(new Comment()));

        Comment comment = new Comment("some", null, null, null);
        Comment commentReply1 = new Comment("some comment text2", comment, null, null);
        Comment commentReply2 = new Comment("some comment text3", comment, null, null);
        Comment commentReply3 = new Comment("some comment text4", comment, null, null);

        addTreeItem(comment, forumWindow.commentsTreeView.getRoot());
        addTreeItem(commentReply1, forumWindow.commentsTreeView.getRoot());
        addTreeItem(commentReply2, forumWindow.commentsTreeView.getRoot());
        addTreeItem(commentReply3, forumWindow.commentsTreeView.getRoot());

        TreeItem selectedTreeItem = new TreeItem<>();
        selectedTreeItem = forumWindow.commentsTreeView.getRoot().getChildren().get(1);
        Comment newReply = new Comment("some comment text 561", null, null, null);
        addTreeItem(newReply, selectedTreeItem);

        String selectedTreeItemCommentText = forumWindow.commentsTreeView.getRoot().getChildren().get(1).getValue().getCommentText();

        //when
        selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);

        //then
        assertNotEquals(selectedTreeItemCommentText, forumWindow.commentsTreeView.getRoot().getChildren().get(1).getValue().getCommentText());
        assertEquals(2, forumWindow.commentsTreeView.getRoot().getChildren().size()-1);
    }

    @Test
    public void commentShouldNotBeDeletedIfCommentIsNotSelected(){
        //given
        forumWindow.commentsTreeView = new TreeView<>();
        forumWindow.commentsTreeView.setRoot(new TreeItem<>(new Comment()));

        Comment comment = new Comment("some", null, null, null);
        Comment commentReply1 = new Comment("some comment text2", comment, null, null);
        Comment commentReply2 = new Comment("some comment text3", comment, null, null);
        Comment commentReply3 = new Comment("some comment text4", comment, null, null);

        addTreeItem(comment, forumWindow.commentsTreeView.getRoot());
        addTreeItem(commentReply1, forumWindow.commentsTreeView.getRoot());
        addTreeItem(commentReply2, forumWindow.commentsTreeView.getRoot());
        addTreeItem(commentReply3, forumWindow.commentsTreeView.getRoot());

        TreeItem selectedTreeItem = new TreeItem();

        if(selectedTreeItem.getValue() != null){
            selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);
        }
        assertEquals(3, forumWindow.commentsTreeView.getRoot().getChildren().size()-1);
    }
}
