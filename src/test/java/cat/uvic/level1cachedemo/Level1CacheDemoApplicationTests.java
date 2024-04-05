package cat.uvic.level1cachedemo;

import cat.uvic.level1cachedemo.entity.Author;
import cat.uvic.level1cachedemo.entity.Book;
import cat.uvic.level1cachedemo.repository.AuthorRepository;
import cat.uvic.level1cachedemo.repository.BookRepository;
import cat.uvic.level1cachedemo.service.AuthorService;
import com.jayway.jsonpath.Criteria;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class Level1CacheDemoApplicationTests {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    AuthorService authorService;

    @Autowired
    CacheManager cacheManager;


    @BeforeEach
    void evictAllCaches() {
        for(var name : cacheManager.getCacheNames()){
            Objects.requireNonNull(cacheManager.getCache(name)).clear();
        }
    }


    @Test
    void shouldThrowLazyLazyInitializationException() {
        List<Book> books = bookRepository.findAll();
        assertThrows(LazyInitializationException.class, () -> System.out.println(books));
    }


    // An open session is needed for a lazy collection to be loaded on demand.

    @Test
    @Transactional
    void shouldLoadLazyCollectionAndNotThrowLazyInitializationException() {
        List<Book> books = bookRepository.findAll();
        assertDoesNotThrow(()-> System.out.println(books));
    }

    @Test
    public void shouldPassWhenCallTransactionalMethodWithPropertyOff(){
        SQLStatementCountValidator.reset();

        long bookCount = authorService.countAllBookTitlesTransactional();

        assertEquals(bookCount,9);

        SQLStatementCountValidator.assertSelectCount(21);
    }

    @Test
    public void whenCallNonTransactionalMethodWithPropertyOff_thenThrowException() {
        Exception exception =
                assertThrows(LazyInitializationException.class,
                        ()->authorService.countAllBookTitlesNonTransactional());

        String message = "failed to lazily initialize a collection ";

        assertTrue(exception.getMessage().contains(message));
    }


    // if spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true property is set

}
