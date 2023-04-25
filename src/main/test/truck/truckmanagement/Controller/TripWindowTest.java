package truck.truckmanagement.Controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import truck.truckmanagement.Model.Destination;
import truck.truckmanagement.Service.DestinationService;
import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class TripWindowTest {

    private TripWindow tripWindow;
    private Stage stage;
    @Mock
    private DestinationService destinationService;

    @BeforeClass
    public static void setUpClass() {
        // initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @Before
    public void setUp() throws Exception {
        tripWindow = new TripWindow();
        tripWindow.finishTripButtonId = new Button();

        Platform.runLater(() -> {
            stage = new Stage();
            Scene scene = new Scene(tripWindow);
            stage.setScene(scene);
            stage.show();
        });
    }

    @AfterClass
    public static void tearDownClass() {
        Platform.exit();
    }

    @Test
    public void testIsShowButtonWhenEndDateIsNotNull() {
        tripWindow.isShowButton(LocalDate.now());
        assertFalse(tripWindow.finishTripButtonId.isVisible());
    }

    @Test
    public void testIsShowButtonWhenEndDateIsNull() {
        tripWindow.isShowButton(null);
        assertTrue(tripWindow.finishTripButtonId.isVisible());
    }

    @Test
    public void testUpdateDestinationEndDate() {
        Destination destination = new Destination();
        DestinationService destinationService = mock(DestinationService.class);

        tripWindow.selectedDestination = destination;
        tripWindow.destinationService = destinationService;

        tripWindow.updateDestinationEndDate();

        verify(destinationService).updateDestination(destination);
        assertEquals(LocalDate.now(), destination.getEndDate());
    }

    @Test
    public void testShowInformationAlert() {
        Platform.runLater(() -> {
            Alert alert = mock(Alert.class);
            when(new Alert(Alert.AlertType.INFORMATION, "Reisas sėkmingai užbaigtas.", ButtonType.OK)).thenReturn(alert);

            tripWindow.showInformationAlert();

            verify(new Alert(Alert.AlertType.INFORMATION, "Reisas sėkmingai užbaigtas.", ButtonType.OK)).show();
        });
    }

    @Test
    public void testCloseWindow() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            Scene scene = new Scene(tripWindow);
            stage.setScene(scene);
            tripWindow.finishTripButtonId.getScene().getWindow().setOnCloseRequest(event -> {
                // it should be closed if successful
            });

            tripWindow.closeWindow();

            verify(stage).close();
        });
    }

    @After
    public void tearDown() {
        Platform.runLater(() -> {
            if (stage != null) {
                stage.close();
            }
        });
    }
}