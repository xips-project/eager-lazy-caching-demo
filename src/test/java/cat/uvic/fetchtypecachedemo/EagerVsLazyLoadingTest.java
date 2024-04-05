package cat.uvic.fetchtypecachedemo;

import cat.uvic.fetchtypecachedemo.entity.Book;
import cat.uvic.fetchtypecachedemo.entity.Publisher;
import cat.uvic.fetchtypecachedemo.service.PublisherService;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class EagerVsLazyLoadingTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    PublisherService publisherService;

    private EntityManager entityManager;

    @PostConstruct
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @PreDestroy
    void closeEntityManager(){
        entityManager.close();
    }


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

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        String hql = "SELECT DISTINCT p FROM Publisher p " +
                "JOIN FETCH p.books";
        List<Publisher> publishers = entityManager.createQuery(hql, Publisher.class).getResultList();

        publishers.stream().map(Publisher::getBooks).map(Collection::stream).forEach(System.out::println);

        transaction.commit();


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

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        String hql = "SELECT DISTINCT p FROM Publisher p " +
                "JOIN FETCH p.books";
        List<Publisher> publishers = entityManager.createQuery(hql, Publisher.class).getResultList();

        // Accessing authors within the transactional context
        publishers.stream().map(Publisher::getBooks)
                .flatMap(Collection::stream)
                .forEach(System.out::println);

        transaction.commit();

        // Only 2 queries are run and both Books and Authors are loaded
        SQLStatementCountValidator.assertSelectCount(2);

    }

}
