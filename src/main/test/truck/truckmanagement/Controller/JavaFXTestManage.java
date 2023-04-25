package truck.truckmanagement.Controller;

import javafx.application.Platform;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class JavaFXTestManage {
    private static boolean initialized = false;

    @BeforeClass
    public static void setUpClass() {
        if (!initialized) {
            Platform.startup(() -> {});
            initialized = true;
        }
    }

    @AfterClass
    public static void tearDownClass() {
        //Platform.exit();
        initialized = false;
    }
}
