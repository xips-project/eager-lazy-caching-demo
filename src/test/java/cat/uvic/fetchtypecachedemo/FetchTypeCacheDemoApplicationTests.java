package cat.uvic.fetchtypecachedemo;

import cat.uvic.fetchtypecachedemo.entity.Author;
import cat.uvic.fetchtypecachedemo.entity.Book;
import cat.uvic.fetchtypecachedemo.entity.Publisher;
import cat.uvic.fetchtypecachedemo.service.AuthorService;
import cat.uvic.fetchtypecachedemo.service.BookService;
import cat.uvic.fetchtypecachedemo.service.PublisherService;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Sql(value = {"schema.sql", "data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class FetchTypeCacheDemoApplicationTests {

    @Autowired
    AuthorService authorService;

    @Autowired
    BookService bookService;

    @Autowired
    PublisherService publisherService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;


    // Can't access lazy loaded entity collection, only first publisher select query is run
    // before a LazyInitializationException is thrown
    @Test
    void accessPropertyOnLazyLoadShouldThrowLazyInitializationException() {

        SQLStatementCountValidator.reset();

        assertThatThrownBy(() -> publisherService.findAll()
                .stream()
                .map(Publisher::getBooks)
                .flatMap(Collection::stream)
                .map(Book::getTitle)
                .forEach(System.out::println))
                .isInstanceOf(LazyInitializationException.class);

        SQLStatementCountValidator.assertSelectCount(1);
    }

    // lazy loaded collection is loaded using Hibernate.initialize in a transactional method
    // mapped to book title since 2 lazy collections cant be loaded this way (authors from each book)
    // suffers from n +1 problem
    @Test
    void accessPropertyLoadingLazyPropertyWithTransactionalFindAllShouldNotThrow() {
        SQLStatementCountValidator.reset();

        assertDoesNotThrow(() -> publisherService.findAllTransactional()
                .stream()
                .map(Publisher::getBooks)
                .flatMap(Collection::stream)
                .map(Book::getTitle)
                .forEach(System.out::println));

        SQLStatementCountValidator.assertSelectCount(9);

    }

    // Manually running the query we want with hql, criteria api, etc. Removes the n+1
    // problem completely and gives better performance at the cost of adding more complexity to
    // our codebase.
    @Test
    void accessPropertyLoadingWithJoinFetch() {
        SQLStatementCountValidator.reset();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager1.getTransaction();
        transaction.begin();

        String hql = "SELECT DISTINCT p FROM Publisher p " +
                "JOIN FETCH p.books";
        List<Publisher> publishers = entityManager1.createQuery(hql, Publisher.class).getResultList();

        publishers.stream().map(Publisher::getBooks).map(Collection::stream).forEach(System.out::println);

        transaction.commit();
        entityManager1.close();

        // Only 1 query is run and both Books and Authors are loaded
        SQLStatementCountValidator.assertSelectCount(1);

    }


    // Can't eagerly load 2 collections so MultipleBagFetchException is thrown before any query
    // is run
    @Test
    void loadTwoLazyCollectionsUsingJoinFetchShouldFail() {
        SQLStatementCountValidator.reset();

        String hql = "SELECT DISTINCT p FROM Publisher p " +
                "JOIN FETCH p.books b " +
                "JOIN FETCH b.authors";

        assertThatThrownBy(() -> entityManager.createQuery(hql, Publisher.class).getResultList())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("MultipleBagFetchException");

        SQLStatementCountValidator.assertSelectCount(0);

    }

    // we can use join fetch with one collection and lazily load the other one, but we
    // need to be within a session so @Transactional or manual transaction management is needed
    @Test
    void loadLazyCollectionUsingJoinFetch() {
        SQLStatementCountValidator.reset();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager1.getTransaction();
        transaction.begin();

        String hql = "SELECT DISTINCT p FROM Publisher p " +
                "JOIN FETCH p.books";
        List<Publisher> publishers = entityManager1.createQuery(hql, Publisher.class).getResultList();

        // Accessing authors within the transactional context
        publishers.stream().map(Publisher::getBooks)
                .flatMap(Collection::stream)
                .forEach(System.out::println);

        transaction.commit();
        entityManager1.close();

        // Only 2 queries are run and both Books and Authors are loaded
        SQLStatementCountValidator.assertSelectCount(2);

    }

    // Hibernate level 1 cache tet, querying for an object present in the
    // persistence context doesn't produce additional queries to de db, and is
    // instead loaded from the level 1 cache
    @Test
    void multipleQueriesForSameObjectShouldProduceOneQueryToDB(){
        SQLStatementCountValidator.reset();
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager1.getTransaction();
        transaction.begin();

        Publisher publisher = entityManager1.find(Publisher.class,1L);
        Publisher publisher2 = entityManager1.find(Publisher.class,1L);
        Publisher publisher3 = entityManager1.find(Publisher.class,1L);
        Publisher publisher4 = entityManager1.find(Publisher.class,1L);
        Publisher publisher5 = entityManager1.find(Publisher.class,1L);

        transaction.commit();
        entityManager1.close();
        SQLStatementCountValidator.assertSelectCount(1);
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
