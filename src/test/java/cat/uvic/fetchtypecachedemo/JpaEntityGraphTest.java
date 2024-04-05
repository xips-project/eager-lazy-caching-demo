package cat.uvic.fetchtypecachedemo;

import cat.uvic.fetchtypecachedemo.entity.Publisher;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class JpaEntityGraphTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @PostConstruct
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @PreDestroy
    void closeEntityManager(){
        entityManager.close();
    }

    // JPA ENTITY GRAPHS GO HERE
    @Test
    void entityGraph1() {
        SQLStatementCountValidator.reset();

        EntityGraph<?> entityGraph = entityManager.getEntityGraph("publish-entity-graph-with-books-and-authors");
        Map<String,Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.fetchgraph", entityGraph);
        Publisher publisher = entityManager.find(Publisher.class, 1L, properties);

        System.out.println(publisher.getBooks());

        SQLStatementCountValidator.assertSelectCount(4);
    }

    @Test
    void entityGraph2() {
        SQLStatementCountValidator.reset();

        EntityGraph<Publisher> entityGraph = (EntityGraph<Publisher>) entityManager.getEntityGraph("publish-entity-graph-with-books-and-authors");
        List<Publisher> publishers = entityManager.createQuery("select p from Publisher p", Publisher.class)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();

        SQLStatementCountValidator.assertSelectCount(1);
    }
}
