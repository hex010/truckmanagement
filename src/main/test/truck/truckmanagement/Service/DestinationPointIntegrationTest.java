package truck.truckmanagement.Service;

import org.junit.jupiter.api.*;
import truck.truckmanagement.Model.DestinationPoint;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DestinationPointIntegrationTest {

    private static EntityManagerFactory emf;
    private static DestinationPointService testedDestinationPointService;

    private DestinationPoint dp1, dp2;

    @BeforeEach
    public void setup() {
        emf = Persistence.createEntityManagerFactory("TruckManagement");
        testedDestinationPointService = new DestinationPointService(emf);
        dp1 = new DestinationPoint("Lietuva", "Vilnius", "Laisves pr. 123", "Kazkur Vilniuje");
        dp2 = new DestinationPoint("Italija", "Roma", "Vatican str. 25", "Kazkur Romoje");
    }

    @AfterEach
    public void cleanup() {
        if(dp1 != null) {
            testedDestinationPointService.removeDestinationPoint(dp1);
            dp1 = null;
        }
        if(dp2 != null) {
            testedDestinationPointService.removeDestinationPoint(dp2);
            dp2 = null;
        }
        emf.close();
    }

    @Test
    public void testGetAllDestinationPoints() {

        testedDestinationPointService.createDestinationPoint(dp1);
        testedDestinationPointService.createDestinationPoint(dp2);

        testedDestinationPointService.createDestinationPoint(dp1);
        testedDestinationPointService.createDestinationPoint(dp2);

        List<DestinationPoint> destinationPoints = testedDestinationPointService.getAllDestinationPoints();

        boolean isDp1Present = false;
        boolean isDp2Present = false;

        for (DestinationPoint destinationPoint : destinationPoints) {
            if (destinationPoint.getCountry().equals(dp1.getCountry()) &&
                    destinationPoint.getCity().equals(dp1.getCity()) &&
                    destinationPoint.getAddress().equals(dp1.getAddress()) &&
                    destinationPoint.getAdditionalInformation().equals(dp1.getAdditionalInformation())) {
                isDp1Present = true;
            } else if (destinationPoint.getCountry().equals(dp2.getCountry()) &&
                    destinationPoint.getCity().equals(dp2.getCity()) &&
                    destinationPoint.getAddress().equals(dp2.getAddress()) &&
                    destinationPoint.getAdditionalInformation().equals(dp2.getAdditionalInformation())) {
                isDp2Present = true;
            }

            if (isDp1Present && isDp2Present) {
                break;
            }
        }

        Assertions.assertTrue(isDp1Present);
        Assertions.assertTrue(isDp2Present);

    }

    @Test
    public void testUpdateDestinationPoint() {
        testedDestinationPointService.createDestinationPoint(dp1);

        dp1.setCity("Kaunas");
        testedDestinationPointService.updateDestinationPoint(dp1);

        List<DestinationPoint> destinationPoints = testedDestinationPointService.getAllDestinationPoints();
        boolean isUpdated = false;

        for (DestinationPoint destinationPoint : destinationPoints) {
            if (destinationPoint.getCountry().equals(dp1.getCountry()) &&
                    destinationPoint.getCity().equals(dp1.getCity()) &&
                    destinationPoint.getAddress().equals(dp1.getAddress()) &&
                    destinationPoint.getAdditionalInformation().equals(dp1.getAdditionalInformation())) {
                isUpdated = true;
                break;
            }
        }

        Assertions.assertTrue(isUpdated);
    }

    @Test
    public void testCreateDestinationPoint() {
        testedDestinationPointService.createDestinationPoint(dp2);
        List<DestinationPoint> destinationPoints = testedDestinationPointService.getAllDestinationPoints();

        boolean isDestinationPointPresent = false;
        for (DestinationPoint destinationPoint : destinationPoints) {
            if (destinationPoint.getCountry().equals(dp2.getCountry()) &&
                    destinationPoint.getCity().equals(dp2.getCity()) &&
                    destinationPoint.getAddress().equals(dp2.getAddress()) &&
                    destinationPoint.getAdditionalInformation().equals(dp2.getAdditionalInformation())) {
                isDestinationPointPresent = true;
                break;
            }
        }
        Assertions.assertTrue(isDestinationPointPresent);
    }

    @Test
    public void testCreateDestinationPointWithNull() {
        assertThrows(Exception.class, () -> {
            testedDestinationPointService.createDestinationPoint(null);
        });
    }
    @Test
    public void testRemoveDestinationPoint() {
        testedDestinationPointService.createDestinationPoint(dp1);

        List<DestinationPoint> destinationPointsBeforeRemoval = testedDestinationPointService.getAllDestinationPoints();
        boolean isDestinationPointPresent = false;
        for (DestinationPoint destinationPoint : destinationPointsBeforeRemoval) {
            if (destinationPoint.getCountry().equals(dp1.getCountry()) &&
                    destinationPoint.getCity().equals(dp1.getCity()) &&
                    destinationPoint.getAddress().equals(dp1.getAddress()) &&
                    destinationPoint.getAdditionalInformation().equals(dp1.getAdditionalInformation())) {
                isDestinationPointPresent = true;
                break;
            }
        }
        Assertions.assertTrue(isDestinationPointPresent);

        testedDestinationPointService.removeDestinationPoint(dp1);

        List<DestinationPoint> destinationPointsAfterRemoval = testedDestinationPointService.getAllDestinationPoints();
        isDestinationPointPresent = false;
        for (DestinationPoint destinationPoint : destinationPointsAfterRemoval) {
            if (destinationPoint.getCountry().equals(dp1.getCountry()) &&
                    destinationPoint.getCity().equals(dp1.getCity()) &&
                    destinationPoint.getAddress().equals(dp1.getAddress()) &&
                    destinationPoint.getAdditionalInformation().equals(dp1.getAdditionalInformation())) {
                isDestinationPointPresent = true;
                break;
            }
        }
        Assertions.assertFalse(isDestinationPointPresent);
    }
}
