package truck.truckmanagement.Service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import truck.truckmanagement.Model.Forum;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

//integration tests
public class ForumIntegrationTest {
    private static EntityManagerFactory entityManagerFactory;
    private static ForumService forumService;

    private Forum forumFromDB;

    @Before
    public void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("TruckManagement");
        forumService = new ForumService(entityManagerFactory);
    }
    @After
    public void cleanup() {
        if(forumFromDB != null) {
           forumService.removeForumTopic(forumFromDB);
           forumFromDB = null;
        }
        entityManagerFactory.close();
    }

    @Test
    public void testCreateForumTopic() {
        Forum forum = new Forum();
        forum.setTitle("My title");
        forum.setDescription("My description");

        forumService.createForumTopic(forum);

        forumFromDB = forumService.getForumTopicById(forum.getId());

        assertNotNull(forumFromDB);
        assertEquals(forum.getTitle(), forumFromDB.getTitle());
        assertEquals(forum.getDescription(), forumFromDB.getDescription());
    }

    @Test
    public void testDeleteForumTopic() {
        Forum forum = new Forum();
        forum.setTitle("My title");
        forum.setDescription("My description");

        forumService.createForumTopic(forum);
        forumFromDB = forumService.getForumTopicById(forum.getId());
        forumService.removeForumTopic(forumFromDB);
        forumFromDB = forumService.getForumTopicById(forum.getId());

        assertNull(forumFromDB);
    }
}