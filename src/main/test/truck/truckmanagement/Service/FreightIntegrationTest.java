package truck.truckmanagement.Service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import truck.truckmanagement.Model.Forum;
import truck.truckmanagement.Model.Freight;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FreightIntegrationTest {
    private static EntityManagerFactory entityManagerFactory;
    private static FreightService freightService;

    private Freight freightFromDB;

    @Before
    public void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");
        freightService = new FreightService(entityManagerFactory);
    }

    @After
    public void cleanup() {
        if(freightFromDB != null) {
            freightService.removeFreight(freightFromDB.getId());
            freightFromDB = null;
        }
        entityManagerFactory.close();
    }
    @Test
    public void testCreateFreight() {
        Freight freight = new Freight();
        freight.setDescription("Plytos");
        freight.setProvider("Girteka");
        freight.setWeight(12.0);
        freight.setHeader("Plytos");
        freight.setQuantity(2);
        //freight.setId(100);
        freightService.createFreight(freight);

        freightFromDB = freightService.getFreightById(freight.getId());

        assertNotNull(freightFromDB);
        assertEquals(freight.getHeader(), freightFromDB.getHeader());
        assertEquals(freight.getDescription(), freightFromDB.getDescription());
        assertEquals(freight.getWeight(), freightFromDB.getWeight(),2);
        assertEquals(freight.getProvider(), freightFromDB.getProvider());
        assertEquals(freight.getQuantity(), freightFromDB.getQuantity());
    }
    @Test
    public void testDeleteFreight() {
        Freight freight = new Freight();
        freight.setDescription("Plytos");
        freight.setProvider("Girteka");
        freight.setWeight(12.0);
        freight.setHeader("Plytos");
        freight.setQuantity(2);
        //freight.setId(100);
        freightService.createFreight(freight);
        freightFromDB = freightService.getFreightById(freight.getId());
        freightService.removeFreight(freightFromDB.getId());
        freightFromDB = freightService.getFreightById(freight.getId());

        assertNull(freightFromDB);
    }
    @Test
    public void testUpdateFreight() {
        Freight freight = new Freight();
        freight.setDescription("Plytos");
        freight.setProvider("Girteka");
        freight.setWeight(12.0);
        freight.setHeader("Plytos");
        freight.setQuantity(2);
        //freight.setId(100);
        freightService.createFreight(freight);
        freightFromDB = freightService.getFreightById(freight.getId());
        freightFromDB.setProvider("Logistika");
        freightService.updateFreight(freightFromDB);
        freightFromDB = freightService.getFreightById(freightFromDB.getId());

        assertEquals("Logistika",freightFromDB.getProvider());
    }
}

