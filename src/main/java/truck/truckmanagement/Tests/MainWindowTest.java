package truck.truckmanagement.Tests;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import truck.truckmanagement.Controller.ForumWindow;
import truck.truckmanagement.Controller.MainWindowDriver;
import truck.truckmanagement.Enum.Role_enum;
import truck.truckmanagement.Model.Forum;
import truck.truckmanagement.Model.User;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static truck.truckmanagement.Enum.Role_enum.VAIRUOTOJAS;

public class MainWindowTest {
    MainWindowDriver mainWindowDriver;
    @BeforeClass
    public static void initToolkit() {
        Platform.startup(() -> {
            // Inicijuoja JavaFX toolkit
        });
    }
    @Before
    public void setUp() {
        mainWindowDriver = new MainWindowDriver();
    }

    @Test
    public void shouldAddNewForumTopicsToTheListView() throws InterruptedException {
        //Given
        Forum forum1 = new Forum("header1", "some desc1", null);
        Forum forum2 = new Forum("heade2", "some desc2", null);
        Forum forum3 = new Forum("heade3", "some desc3", null);
        mainWindowDriver.listViewForum = new ListView<Forum>();

        //when
        mainWindowDriver.listViewForum.getItems().addAll(forum1, forum2, forum3);

        //then
        assertEquals(3, mainWindowDriver.listViewForum.getItems().size());
        assertEquals(mainWindowDriver.listViewForum.getItems().get(0), forum1);
        assertEquals(mainWindowDriver.listViewForum.getItems().get(1), forum2);
        assertEquals(mainWindowDriver.listViewForum.getItems().get(2), forum3);
    }

    @Test
    public void shouldReturnTrueIfForumTopicIsNotSelectedInListView(){
        //Given
        Forum forum1 = new Forum("header1", "some desc1", null);
        Forum forum2 = new Forum("heade2", "some desc2", null);
        Forum forum3 = new Forum("heade3", "some desc3", null);
        mainWindowDriver.listViewForum = new ListView<Forum>();
        mainWindowDriver.listViewForum.getItems().addAll(forum1, forum2, forum3);

        //when
            //nothing select

        //then
        assertTrue(mainWindowDriver.listViewForum.getSelectionModel().getSelectedItems().isEmpty());
    }

    @Test
    public void shouldReturnFalseIfForumTopicIsSelectedInListView(){
        //Given
        Forum forum1 = new Forum("header1", "some desc1", null);
        Forum forum2 = new Forum("heade2", "some desc2", null);
        Forum forum3 = new Forum("heade3", "some desc3", null);
        mainWindowDriver.listViewForum = new ListView<Forum>();
        mainWindowDriver.listViewForum.getItems().addAll(forum1, forum2, forum3);

        //when
        mainWindowDriver.listViewForum.getSelectionModel().select(1);

        //then
        assertFalse(mainWindowDriver.listViewForum.getSelectionModel().getSelectedItems().isEmpty());
    }

    @Test
    public void shouldReturnFalseIfFilledViewWithData(){
        //Given
        User loggedInUser = new User(VAIRUOTOJAS,"Vardas","Pass","Vardenis","Pavardenis","Elpastas@gmai.co",867898887,LocalDate.of(2000,12,22) );
        mainWindowDriver.fieldFirstname.setText(loggedInUser.getFirstname());
        mainWindowDriver.fieldLastname.setText(loggedInUser.getLastname());
        mainWindowDriver.fieldPassword.setText(loggedInUser.getPassword());
        mainWindowDriver.fieldEmail.setText(loggedInUser.getEmail());
        mainWindowDriver.fieldPhone.setText(String.valueOf(loggedInUser.getPhoneNumber()));
        mainWindowDriver.dateBirthday.setValue(loggedInUser.getBirthday());

        //When


        //Then
        assertFalse(mainWindowDriver.fieldFirstname.getText().isEmpty() && mainWindowDriver.fieldLastname.getText().isEmpty() && mainWindowDriver.fieldPassword.getText().isEmpty() && mainWindowDriver.fieldEmail.getText().isEmpty() && mainWindowDriver.fieldPhone.getText().isEmpty() && mainWindowDriver.dateBirthday.equals(null) );

    }

    @Test
    public void shouldReturnTrueIfUserDataIsUpdated(){
        User loggedInUser = new User(VAIRUOTOJAS,"Vardas","Pass","Vardenis","Pavardenis","Elpastas@gmai.co",867898887,LocalDate.of(2000,12,22) );
        mainWindowDriver.fieldFirstname.setText(loggedInUser.getFirstname()+"NEW");
        mainWindowDriver.fieldLastname.setText(loggedInUser.getLastname()+"NEW");
        mainWindowDriver.fieldPassword.setText(loggedInUser.getPassword()+"NEW");
        mainWindowDriver.fieldEmail.setText(loggedInUser.getEmail()+"NEW");
        mainWindowDriver.fieldPhone.setText(loggedInUser.getPhoneNumber() +"8");
        mainWindowDriver.dateBirthday.setValue(loggedInUser.getBirthday());

        loggedInUser.setPassword(mainWindowDriver.fieldPassword.getText());
        loggedInUser.setFirstname(mainWindowDriver.fieldFirstname.getText());
        loggedInUser.setLastname(mainWindowDriver.fieldLastname.getText());
        loggedInUser.setEmail(mainWindowDriver.fieldEmail.getText());
        loggedInUser.setPhoneNumber(Integer.parseInt(mainWindowDriver.fieldPhone.getText()));
        loggedInUser.setBirthday(mainWindowDriver.dateBirthday.getValue());

        assertEquals("VardasNEW",loggedInUser.getFirstname());
        assertEquals("VardenisNEW",loggedInUser.getLastname());
        assertEquals("PassNEW",loggedInUser.getPassword());
        assertEquals("Elpastas@gmai.coNEW",loggedInUser.getEmail());
        assertEquals("8678988878",String.valueOf(loggedInUser.getPhoneNumber()));
    }


}
