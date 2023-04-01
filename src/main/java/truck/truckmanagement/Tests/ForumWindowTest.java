package truck.truckmanagement.Tests;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import truck.truckmanagement.Controller.ForumWindow;
import truck.truckmanagement.Enum.Role_enum;
import truck.truckmanagement.Model.Forum;
import truck.truckmanagement.Model.User;

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
        Platform.runLater(() -> {
            forumWindow.topicDescriptionTextArea = new TextArea("sometext");
            forumWindow.topicHeaderTextField = new TextField("");
        });
        Thread.sleep(500); // palaukia, kad runLater suveiktu

        //when
        boolean result = forumWindow.topicHeaderTextField.getText().isEmpty() || forumWindow.topicDescriptionTextArea.getText().isEmpty();

        //then
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseIfBothFieldsAreNotEmpty() throws InterruptedException {
        //given
        Platform.runLater(() -> {
            forumWindow.topicDescriptionTextArea = new TextArea("sometext");
            forumWindow.topicHeaderTextField = new TextField("asdfa");
        });
        Thread.sleep(500); // palaukia, kad runLater suveiktu

        //when
        boolean result = forumWindow.topicHeaderTextField.getText().isEmpty() || forumWindow.topicDescriptionTextArea.getText().isEmpty();

        //then
        assertFalse(result);
    }

    @Test
    public void shouldCreateNewForumObjectFromFields() throws InterruptedException {
        //given
        Platform.runLater(() -> {
            forumWindow.topicDescriptionTextArea = new TextArea("sometext");
            forumWindow.topicHeaderTextField = new TextField("asdfa");
        });
        Thread.sleep(500); // palaukia, kad runLater suveiktu

        //when
        Forum someForumTopic = new Forum(forumWindow.topicHeaderTextField.getText(),forumWindow.topicDescriptionTextArea.getText(), null);

        //then
        assertEquals(someForumTopic.getTitle(), forumWindow.topicHeaderTextField.getText());
        assertEquals(someForumTopic.getDescription(), forumWindow.topicDescriptionTextArea.getText());
    }

    @Test
    public void shouldForumTopicBeAssociatedWithCurrentUser() throws InterruptedException {
        //given
        Platform.runLater(() -> {
            forumWindow.topicDescriptionTextArea = new TextArea("sometext");
            forumWindow.topicHeaderTextField = new TextField("asdfa");
        });
        Thread.sleep(500); // palaukia, kad runLater suveiktu

        //when
        Forum someForumTopic = new Forum(forumWindow.topicHeaderTextField.getText(),forumWindow.topicDescriptionTextArea.getText(), currentUser);

        assertEquals(someForumTopic.getUser(), currentUser);
    }
}
