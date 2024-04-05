package cat.uvic.fetchtypecachedemo;

import cat.uvic.fetchtypecachedemo.entity.Publisher;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class LevelOneCacheTest {

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

    // Hibernate level 1 cache test, querying the same object produces no additional
    // trips to the db
    @Test
    void multipleQueriesForSameObjectShouldProduceOneQueryToDB(){
        SQLStatementCountValidator.reset();
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Publisher publisher = entityManager1.find(Publisher.class,1L);
        Publisher publisher2 = entityManager1.find(Publisher.class,1L);
        Publisher publisher3 = entityManager1.find(Publisher.class,1L);
        Publisher publisher4 = entityManager1.find(Publisher.class,1L);
        Publisher publisher5 = entityManager1.find(Publisher.class,1L);

        transaction.commit();
        SQLStatementCountValidator.assertSelectCount(1);
    }

    // Hibernate level 1 cache test, querying the same object produces no additional
    // trips to the db, after removing the object from the session cache, another
    // trip to the db is needed, resulting in 2 db accesses.
    @Test
    void multipleQueriesForSameObjectShouldProduceTwoQueriesToDbAfterClearing(){
        SQLStatementCountValidator.reset();
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Publisher publisher = entityManager1.find(Publisher.class,1L);

        Session session = entityManager1.unwrap(Session.class);
        session.evict(publisher);
        entityManager1.clear();

        Publisher publisher2 = entityManager1.find(Publisher.class,1L);

        transaction.commit();
        SQLStatementCountValidator.assertSelectCount(2);
    }
}
