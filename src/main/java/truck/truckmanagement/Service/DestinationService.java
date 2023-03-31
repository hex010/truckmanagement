package truck.truckmanagement.Service;

import truck.truckmanagement.Enum.Destination_filters_enum;
import truck.truckmanagement.Model.Destination;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DestinationService {
    EntityManager entityManager = null;
    EntityManagerFactory emf = null;

    public DestinationService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Destination> getAllDestinations() {
        entityManager = emf.createEntityManager();

        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Destination.class));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(entityManager != null){
                entityManager.close();
            }
        }
        return new ArrayList<>();
    }

    public List<Destination> getAllDestinationsByDriverId(int id, Destination_filters_enum filter, String filterData) {
        entityManager = emf.createEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Destination> cq = cb.createQuery(Destination.class);
            Root<Destination> rootEntry = cq.from(Destination.class);
            CriteriaQuery<Destination> all = null;

            if(filter.equals(Destination_filters_enum.NONE)) {
                all = cq.select(rootEntry).where(cb.equal(rootEntry.get("driver"), id));
            } else if(filter.equals(Destination_filters_enum.COUNTRY)){
                all = cq.select(rootEntry).where(cb.and(cb.equal(rootEntry.get("driver"), id), cb.equal(rootEntry.get("destinationPoint").get("country"), filterData)));
            } else if(filter.equals(Destination_filters_enum.TRUCK_MODEL)){
                all = cq.select(rootEntry).where(cb.and(cb.equal(rootEntry.get("driver"), id), cb.equal(rootEntry.get("transport").get("model"), filterData)));
            }

            TypedQuery<Destination> allQuery = entityManager.createQuery(all);
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

    public List<Destination> getAllDestinationsByManagerId(int id) {
        entityManager = emf.createEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Destination> cq = cb.createQuery(Destination.class);
            Root<Destination> rootEntry = cq.from(Destination.class);
            CriteriaQuery<Destination> all = cq.select(rootEntry).where(cb.equal(rootEntry.get("manager"), id));

            TypedQuery<Destination> allQuery = entityManager.createQuery(all);
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

    public void removeDestination(Destination destination) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("Delete from Destination c where c.id = :id");
            query.setParameter("id", destination.getId());
            int rows = query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void updateDestination(Destination destination) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(destination);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void createDestination(Destination destination) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(destination);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}
