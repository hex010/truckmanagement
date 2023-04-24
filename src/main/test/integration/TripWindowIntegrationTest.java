package integration;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import truck.truckmanagement.HelloApplication;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;


@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TripWindowIntegrationTest{

    @BeforeEach
    void setUp() throws Exception {
        // Code in this method will be executed before each test method.
    }

    @Start
    public void start(Stage stage) throws Exception {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.start(stage);
    }

    @Test
    public void loginUser(FxRobot robot) {
        // Type username and password
        robot.clickOn("#loginTextField");
        robot.write("hi");
        robot.clickOn("#passwordTextField");
        robot.write("123");

        // Click on the login button
        robot.clickOn("Prisijungti");

        //Wait for the alert to be shown
        waitForCondition(Duration.ofSeconds(5), () -> robot.lookup(".alert").tryQuery().isPresent());

        robot.clickOn("OK");

        //waitForCondition(Duration.ofSeconds(5), () -> robot.lookup("#listViewTrips").tryQuery().isPresent());

        FxAssert.verifyThat("#listViewTrips", NodeMatchers.isVisible());

        robot.lookup("#listViewTrips").queryAs(ListView.class);
    }

    private void waitForCondition(Duration duration, BooleanSupplier condition) {
        CountDownLatch latch = new CountDownLatch(1);
        long endTime = System.currentTimeMillis() + duration.toMillis();

        Runnable checkCondition = new Runnable() {
            @Override
            public void run() {
                if (condition.getAsBoolean() || System.currentTimeMillis() > endTime) {
                    latch.countDown();
                } else {
                    Platform.runLater(this);
                }
            }
        };

        Platform.runLater(checkCondition);

        try {
            if (!latch.await(duration.toMillis(), TimeUnit.MILLISECONDS)) {
                throw new AssertionError("Timeout waiting for condition");
            }
        } catch (InterruptedException e) {
            throw new AssertionError("Interrupted while waiting for condition", e);
        }
    }
}

