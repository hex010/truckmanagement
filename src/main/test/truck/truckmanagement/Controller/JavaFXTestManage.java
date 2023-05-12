package truck.truckmanagement.Controller;

import javafx.application.Platform;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class JavaFXTestManage {
    @BeforeClass
    public static void setUpClass() {
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException e) {
            // Platform already started; ignore
        }
    }
}
