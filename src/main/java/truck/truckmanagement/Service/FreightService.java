package truck.truckmanagement.Service;

import truck.truckmanagement.Model.Freight;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class FreightService {
    EntityManager entityManager = null;
    EntityManagerFactory emf = null;

    public FreightService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Freight> getAllFreights() {
        entityManager = emf.createEntityManager();

        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Freight.class));
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

    public Freight getFreightById(int id) {
        entityManager = emf.createEntityManager();
        Freight freight = null;
        try {
            entityManager.getTransaction().begin();
            freight = entityManager.find(Freight.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such Transport by given Id");
        }
        return freight;
    }

    public void updateFreight(Freight freight) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(freight);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
    public void createFreight(Freight freight) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(freight);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void removeFreight(int id){
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("Delete from Freight c where c.id = :id");
            query.setParameter("id", id);
            int rows = query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}
