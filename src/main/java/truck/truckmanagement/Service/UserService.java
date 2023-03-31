package truck.truckmanagement.Service;

import truck.truckmanagement.Enum.Role_enum;
import truck.truckmanagement.Model.User;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    EntityManager entityManager = null;
    EntityManagerFactory emf = null;

    public UserService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<User> getAllUsers() {
        entityManager = emf.createEntityManager();

        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(User.class));
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

    public List<User> getAllDrivers() {
        entityManager = emf.createEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> rootEntry = cq.from(User.class);
            CriteriaQuery<User> all = cq.select(rootEntry).where(cb.equal(rootEntry.get("role"), Role_enum.VAIRUOTOJAS));

            TypedQuery<User> allQuery = entityManager.createQuery(all);
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

    public List<User> getAllManagers() {
        entityManager = emf.createEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> rootEntry = cq.from(User.class);
            CriteriaQuery<User> all = cq.select(rootEntry).where(cb.equal(rootEntry.get("role"), Role_enum.VADYBININKAS));

            TypedQuery<User> allQuery = entityManager.createQuery(all);
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

    public void removeUser(User user) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("Delete from User c where c.id = :id");
            query.setParameter("id", user.getId());
            int rows = query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void updateUser(User user) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void createUser(User user) {
        entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public User getUserByLoginData(String login, String psw) {
        entityManager = emf.createEntityManager();
        Query q = null;

        CriteriaQuery<User> queryUser = null;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        queryUser = cb.createQuery(User.class);
        Root<User> root = queryUser.from(User.class);
        queryUser.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), psw)));

        try {
            q = entityManager.createQuery(queryUser);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
