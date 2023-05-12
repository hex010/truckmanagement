package E2E;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;
import truck.truckmanagement.HelloApplication;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import static org.hamcrest.MatcherAssert.assertThat;


@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FreightWindowE2ETest extends ApplicationTest {

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
    @Order(1)
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

    @Test
    @Order(2)
    public void testViewFreight(FxRobot robot) {
        loginUser(robot);

        WaitForAsyncUtils.sleep(5000, TimeUnit.MILLISECONDS);

        //best to use fx id instead of text value
        robot.clickOn("#Freights");

        Node firstCell = lookup("#freightList .list-cell").queryAll().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("No cell found in the ListView"));

        robot.clickOn(firstCell);
        WaitForAsyncUtils.sleep(5000, TimeUnit.MILLISECONDS);

        robot.clickOn("#showFreightBtn");

        WaitForAsyncUtils.sleep(5000, TimeUnit.MILLISECONDS);

        FxAssert.verifyThat("#freightViewWindow", NodeMatchers.isVisible());
    }

    @Test
    @Order(3)
    void testDeleteFreight(FxRobot robot) {

        loginUser(robot);

        WaitForAsyncUtils.sleep(5000, TimeUnit.MILLISECONDS);

        //best to use fx id instead of text value
        robot.clickOn("#Freights");

        Node firstCell = lookup("#freightList .list-cell").queryAll().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("No cell found in the ListView"));

        robot.clickOn(firstCell);

        robot.clickOn("#deleteBtn");

        WaitForAsyncUtils.sleep(5000, TimeUnit.MILLISECONDS);


        waitForCondition(Duration.ofSeconds(5), () -> robot.lookup(".alert").tryQuery().isPresent());

        robot.clickOn("OK");

        WaitForAsyncUtils.sleep(5000, TimeUnit.MILLISECONDS);

        assertThat("The first cell removed ", firstCell.isVisible());
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

