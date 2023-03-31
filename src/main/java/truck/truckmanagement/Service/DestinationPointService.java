package truck.truckmanagement.Service;

import truck.truckmanagement.Model.DestinationPoint;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class DestinationPointService {
    EntityManager entityManager = null;
    EntityManagerFactory emf = null;

    public DestinationPointService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<DestinationPoint> getAllDestinationPoints() {
        entityManager = emf.createEntityManager();

        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(DestinationPoint.class));
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

    public void updateDestinationPoint(DestinationPoint selectedDestinationPoint) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(selectedDestinationPoint);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void createDestinationPoint(DestinationPoint selectedDestinationPoint) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(selectedDestinationPoint);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void removeDestinationPoint(DestinationPoint destinationPoint) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("Delete from DestinationPoint c where c.id = :id");
            query.setParameter("id", destinationPoint.getId());
            int rows = query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}
