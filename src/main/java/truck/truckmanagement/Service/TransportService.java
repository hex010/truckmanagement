package truck.truckmanagement.Service;

import truck.truckmanagement.Model.Transport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class TransportService {
    EntityManager entityManager = null;
    EntityManagerFactory emf = null;

    public TransportService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Transport> getAllTransports() {
        entityManager = emf.createEntityManager();

        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Transport.class));
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

    public Transport getTransportById(int id) {
        entityManager = emf.createEntityManager();
        Transport transport = null;
        try {
            entityManager.getTransaction().begin();
            transport = entityManager.find(Transport.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such Transport by given Id");
        }
        return transport;
    }

    public void updateTransport(Transport transport) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(transport);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void removeTransportById(int id){
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("Delete from Transport c where c.id = :id");
            query.setParameter("id", id);
            int rows = query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void createTransport(Transport transport) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(transport);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}
