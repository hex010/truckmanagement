package truck.truckmanagement.Service;

import truck.truckmanagement.Model.TruckStop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TruckStopService {
    EntityManager entityManager = null;
    EntityManagerFactory emf = null;

    public TruckStopService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<TruckStop> getAllTruckStopsByDestinationId(int id) {
        entityManager = emf.createEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<TruckStop> cq = cb.createQuery(TruckStop.class);
            Root<TruckStop> rootEntry = cq.from(TruckStop.class);
            CriteriaQuery<TruckStop> all = cq.select(rootEntry).where(cb.equal(rootEntry.get("destination"), id));

            TypedQuery<TruckStop> allQuery = entityManager.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
        return new ArrayList<>();
    }

    public void createTruckStop(TruckStop truckStop) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(truckStop);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}
