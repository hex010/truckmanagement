package truck.truckmanagement.Service;

import truck.truckmanagement.Model.Forum;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class ForumService {
    EntityManager entityManager = null;
    EntityManagerFactory emf = null;

    public ForumService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Forum> getAllForums() {
        entityManager = emf.createEntityManager();

        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Forum.class));
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

    public Forum getForumTopicById(int id) {
        entityManager = emf.createEntityManager();
        Forum forum = null;
        try {
            entityManager.getTransaction().begin();
            forum = entityManager.find(Forum.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such Forum by given Id");
        }
        return forum;
    }

    public void updateForumTopic(Forum forum) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(forum);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void createForumTopic(Forum forum) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(forum);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void removeForumTopic(Forum forum) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("Delete from Forum c where c.id = :id");
            query.setParameter("id", forum.getId());
            int rows = query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}
