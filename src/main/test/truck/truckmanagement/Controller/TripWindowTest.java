package truck.truckmanagement.Controller;

import javafx.application.Platform;
import javafx.scene.control.Button;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TripWindowTest {

    private TripWindow tripWindow;

    @BeforeClass
    public static void setUpClass() {
        // initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @Before
    public void setUp() throws Exception {
        tripWindow = new TripWindow();
        tripWindow.finishTripButtonId = new Button();
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
    public void finishTrip() {
    }
}